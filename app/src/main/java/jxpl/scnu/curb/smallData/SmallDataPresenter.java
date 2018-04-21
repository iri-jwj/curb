package jxpl.scnu.curb.smallData;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Xml;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.repository.SmallDataDataSource;
import jxpl.scnu.curb.data.repository.SmallDataRepository;
import jxpl.scnu.curb.utils.SharedHelper;
import jxpl.scnu.curb.utils.XmlDataStorage;

public class SmallDataPresenter implements SmallDataInterface.Presenter {
    private final SmallDataInterface.View smallDataView;
    private final SmallDataRepository m_smallDataRepository;
    private final Context m_homePageActivity;

    private boolean firstLoad = true;
    private SDSummary m_sdSummaryAnswering;
    private int sumOfQuestions;
    private Map<Integer, Integer> answers = new LinkedHashMap<>();
    private List<SDAnswer> m_sdAnswers = new LinkedList<>();


    public SmallDataPresenter(SmallDataInterface.View para_smallDataView,
                              @NonNull SmallDataRepository para_smallDataRepository,
                              @NonNull Context para_context) {
        smallDataView = para_smallDataView;
        m_smallDataRepository = para_smallDataRepository;
        m_homePageActivity = para_context;
        smallDataView.setPresenter(this);
    }

    @Override
    public void start() {
        loadSummaries(false, 1);
    }



    @Override
    public void commitAnswer() {
        if (checkHaveFinished()) {
            Gson lc_gson = new Gson();
            String lc_uuid = UUID.randomUUID().toString();//for Test
            CreatedAnswer lc_createdAnswer = new
                    CreatedAnswer(lc_uuid,
                    m_sdSummaryAnswering.getId().toString(),
                    sumOfQuestions,
                    answers);
            String strEntry = lc_gson.toJson(lc_createdAnswer);
            Log.d("smallDataPresenter", "postAnswer:entity " + strEntry);
            m_smallDataRepository.commitAnswer(strEntry);
            saveAnswersToLocal();
        } else {
            smallDataView.showError("有题目未完成");
        }
    }

    private void saveAnswersToLocal() {
        m_smallDataRepository.saveAnswersToLocal(m_sdAnswers);
    }

    @Override
    public void setSummary(SDSummary para_summary) {
        m_sdSummaryAnswering = para_summary;
    }

    @Override
    public void markAnswered(String summaryId) {
        m_smallDataRepository.markAnswered(summaryId);
    }


    @Override
    public void saveAnswerToMap(int questionNum, int chosen) {
        SDAnswer lc_sdAnswer = new SDAnswer(m_sdSummaryAnswering.getId(), questionNum, chosen);
        m_sdAnswers.add(lc_sdAnswer);
        answers.put(questionNum, chosen);
    }

    @Override
    public int getChosenAnswer(int questionNum) {
        if (answers.containsKey(questionNum))
            return answers.get(questionNum);
        else
            return 0;
    }

    @Override
    public boolean getAnswerState() {
        return answers.size() > 0;
    }

    @Override
    public void clearAnswer() {
        answers.clear();
        m_sdAnswers.clear();
    }

    @Override
    public void loadSummaries(boolean forceUpdate, int direction) {
        if (!XmlDataStorage.isSharedHelperSet()) {
            SharedHelper lc_sharedHelper = SharedHelper.getInstance(m_homePageActivity);
            XmlDataStorage.setM_sharedHelper(lc_sharedHelper);
        }
        firstLoad = XmlDataStorage.getFirstRunState(XmlDataStorage.IS_SD_FIRST_RUN);
        loadSummariesFromRepository(forceUpdate || firstLoad, direction);
        if (firstLoad) {
            XmlDataStorage.setSDFirstRun(false);
            firstLoad = false;
        }
    }

    private void loadSummariesFromRepository(boolean forceUpdate, int direction) {
        if (forceUpdate)
            m_smallDataRepository.refreshSummaries();

        SimpleDateFormat lc_simpleDateFormat = new
                SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.CHINA);
        Date lc_date = new Date(System.currentTimeMillis());

        String time = lc_simpleDateFormat.format(lc_date);
        m_smallDataRepository.loadSummaries(new SmallDataDataSource.loadSummaryCallback() {
            @Override
            public void onSummaryLoaded(List<SDSummary> sdSummaries) {
                smallDataView.showSummaries(sdSummaries);
            }

            @Override
            public void onDataNotAvailable() {
                smallDataView.showError(m_homePageActivity
                        .getResources()
                        .getString(R.string.sd_loading_summaries_error));
            }
        }, time, direction);
    }
    @Override
    public void loadDetails(String summaryId) {
        m_smallDataRepository.loadDetails(new SmallDataDataSource.loadDetailCallback() {
            @Override
            public void onDetailLoaded(List<SDDetail> para_sdDetails) {
                sumOfQuestions = para_sdDetails.size();
                if (m_sdSummaryAnswering.isHasFinish()) {
                    m_smallDataRepository.loadAnswers(new SmallDataDataSource.loadAnswersCallback() {
                        @Override
                        public void onAnswerLoaded(List<SDAnswer> para_sdAnswers) {
                            for (SDAnswer answer :
                                    para_sdAnswers) {
                                answers.put(answer.getQuestionNum(), answer.getAnswer());
                            }
                        }

                        @Override
                        public void onDataNotAvailable() {
                            smallDataView.showError("加载答案错误");
                        }
                    }, m_sdSummaryAnswering.getId().toString());
                }
                smallDataView.showDetails(para_sdDetails);
            }

            @Override
            public void onDataNotAvailable() {
                sendErrorToView(R.string.sd_loading_details_error);
            }
        }, summaryId);
    }



    private boolean checkHaveFinished() {
        return sumOfQuestions == m_sdAnswers.size();
    }

    private void sendErrorToView(int messageId) {
        smallDataView.showError(m_homePageActivity
                .getResources()
                .getString(messageId));
    }


    /**
     * 内部类，用于将答案保存为服务器需要的格式
     */
    private class CreatedAnswer {
        private String user_id;
        private String st_id;
        private int question_num;
        private Map<Integer, Integer> result;

        CreatedAnswer(String para_user_id, String para_st_id, int para_question_num, Map<Integer, Integer> para_result) {
            user_id = para_user_id;
            st_id = para_st_id;
            question_num = para_question_num;
            result = para_result;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String para_user_id) {
            user_id = para_user_id;
        }

        public String getSt_id() {
            return st_id;
        }

        public void setSt_id(String para_st_id) {
            st_id = para_st_id;
        }

        public int getQuestion_num() {
            return question_num;
        }

        public void setQuestion_num(int para_question_num) {
            question_num = para_question_num;
        }

        public Map<Integer, Integer> getResult() {
            return result;
        }

        public void setResult(Map<Integer, Integer> para_result) {
            result = para_result;
        }
    }

    @Override
    public Context getContextInPresenter() {
        return m_homePageActivity;
    }
}
