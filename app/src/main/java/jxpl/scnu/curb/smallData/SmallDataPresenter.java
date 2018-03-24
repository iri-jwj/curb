package jxpl.scnu.curb.smallData;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.repository.SmallDataDataSource;
import jxpl.scnu.curb.data.repository.SmallDataRepository;

public class SmallDataPresenter implements SmallDataInterface.Presenter {
    private final SmallDataInterface.View smallDataView;
    private final SmallDataRepository m_smallDataRepository;
    private final Context m_homePageActivity;

    private boolean isFirstLoad = true;
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


    /**
     * 用于提交用户填写的问卷
     * 通过 checkHaveFinished() 判断用户是否已经完成问卷
     *
     * @see this#checkHaveFinished
     * 若已完成则for循环中将 {@link this#answerTemp} 中的内容存至 {@see this#answer}
     * 否则调用 {@see this#smallDataView} 中的showError方法提示未完成所有题目
     */
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

    /**
     * 此处将用户正在填写问卷的信息记录到 answerTemp 中
     *
     * @param questionNum 题号
     * @param chosen      选择的值
     */
    @Override
    public void saveAnswerToMap(int questionNum, int chosen) {
        SDAnswer lc_sdAnswer = new SDAnswer(m_sdSummaryAnswering.getId(), questionNum, chosen);
        m_sdAnswers.add(lc_sdAnswer);
        answers.put(questionNum, chosen);
    }



    @Override
    public void loadSummaries(boolean forceUpdate, int direction) {
        loadSummariesFromRepository(forceUpdate || isFirstLoad, direction);
        isFirstLoad = false;
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
