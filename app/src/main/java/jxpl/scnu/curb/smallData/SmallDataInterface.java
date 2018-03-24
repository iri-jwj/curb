package jxpl.scnu.curb.smallData;


import android.content.Context;

import java.io.File;
import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

public interface SmallDataInterface {
    interface View extends BaseView<Presenter> {
        boolean isActive();

/*        void changeToQuestionnaire();

        void changeBack();*/


        void showError(String error);

        //void setLoadingIndicator(boolean active);//显示等待界面

        void showSummaries(List<SDSummary> para_sdSummaries);

        void showDetails(List<SDDetail> para_sdDetails);

        void showPopUpMenu(Context para_context);

        String getCurrentSummaryID();
    }

    interface Presenter extends BasePresenter {
        void saveAnswerToMap(int questionNum, int chosen);

        void setSummary(SDSummary para_summary);

        void commitAnswer();

        void markAnswered(String summary_id);

        Context getContextInPresenter();

        void loadSummaries(boolean forceUpdate, int direction);//获取

        void loadDetails(String summaryId);
    }
}
