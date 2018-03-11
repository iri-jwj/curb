package jxpl.scnu.curb.smallData;


import com.google.gson.Gson;

import java.util.HashMap;

public class SmallDataPresenter implements SmallDataInterface.Presenter {
    private final SmallDataInterface.View smallDataView;
    private HashMap<String, String> answer = new HashMap<>();
    private HashMap<String, String> answerTemp = new HashMap<>();

    public SmallDataPresenter(SmallDataInterface.View smallDataView) {
        this.smallDataView = smallDataView;
    }

    @Override
    public void start() {

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
            for (int i = 1; i <= answerTemp.size(); i++) {
                String key = i + "";
                answer.put(key, answerTemp.get(key));
            }
            Gson gson = new Gson();
            String strEntity = gson.toJson(answer);
        } else {
            smallDataView.showError("有题目未完成");
        }

    }

    /**
     * 此处将用户正在填写问卷的信息记录到 answerTemp 中
     *
     * @param key   题号
     * @param value 选择的值
     */
    @Override
    public void saveAnswerToMap(String key, String value) {
        answerTemp.put(key, value);
    }



/*    @Override
    public void saveAnswerTitle(String title) {
        answer.put("title", title);
        answer.put("userName", "");
    }*/

    /**
     * 这里获取测试的最终结果
     */
    @Override
    public void loadResult() {
        /*TODO 添加逻辑代码*/
    }

    @Override
    public void loadSummaries(boolean forceUpdate) {

    }

    @Override
    public void loadDetails(String summaryId) {

    }

    @Override
    public void loadCreatedSummaries() {

    }

    @Override
    public void loadCreatedDetails() {

    }

    @Override
    public void saveCreatedSummary(SDSummaryCreate para_sdSummaryCreate) {

    }

    @Override
    public void saveCreatedDetail(SDDetail para_sdDetail) {

    }

    private boolean checkHaveFinished() {
        return true;
    }
}
