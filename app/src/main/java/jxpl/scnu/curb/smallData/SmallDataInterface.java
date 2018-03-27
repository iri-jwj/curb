package jxpl.scnu.curb.smallData;


import android.content.Context;

import java.io.File;
import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

public interface SmallDataInterface {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        /**
         * 提示用户出现某些错误
         *
         * @param error 错误的信息
         */
        void showError(String error);

        //void setLoadingIndicator(boolean active);//显示等待界面

        /**
         * 显示summaries
         * @param para_sdSummaries 待显示的内容
         */
        void showSummaries(List<SDSummary> para_sdSummaries);

        /**
         * 显示details
         * @param para_sdDetails 待显示的内容
         */
        void showDetails(List<SDDetail> para_sdDetails);

        /**
         * @deprecated at 3/26
         * @param para_context 应用上下文信息
         */
        void showPopUpMenu(Context para_context);

        /**
         * 获取当前的summary 的ID
         * @return 返回ID
         */
        String getCurrentSummaryID();
    }

    interface Presenter extends BasePresenter {
        /**
         * 将用户当前问题的答案保存至缓存map中
         * @param questionNum 问题题号
         * @param chosen 用户的选项，1为上，2为下
         */
        void saveAnswerToMap(int questionNum, int chosen);

        /**
         * 使用presenter保存用户点击的summary的实例
         * @param para_summary 保存的目标
         */
        void setSummary(SDSummary para_summary);

        /**
         * 用于提交用户填写的问卷
         * 通过 checkHaveFinished() 判断用户是否已经完成问卷
         *
         * @see SmallDataPresenter#checkHaveFinished
         * 若已完成则for循环中将 {@link SmallDataPresenter#answerTemp} 中的内容存至 {@see this#answer}
         * 否则调用 {@see this#smallDataView} 中的showError方法提示未完成所有题目
         */
        void commitAnswer();

        /**
         * 若用户已完成一个问卷，则将目标问卷标记成为已完成(answered)
         * @param summary_id 已完成问卷的id
         */
        void markAnswered(String summary_id);

        /**
         * view 从presenter中获取应用上下文信息，用于进行某些UI更新
         * @return
         */
        Context getContextInPresenter();

        /**
         * 装载Summaries的方法
         *
         * @param forceUpdate 是否强制获取最新信息
         * @param direction   刷新信息的方向
         */
        void loadSummaries(boolean forceUpdate, int direction);

        /**
         * 装载问卷详细信息的方法
         * @param summaryId 问卷的id，用于查找目的详细信息
         */
        void loadDetails(String summaryId);
    }
}
