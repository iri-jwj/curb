package jxpl.scnu.curb.smallData;


import android.content.Context;

import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

public interface SmallDataInterface {
    interface View extends BaseView<Presenter> {
        boolean isActive();

/*        void changeToQuestionnaire();

        void changeBack();*/

        void markAnswered();

        void showError(String error);

        //void setLoadingIndicator(boolean active);//显示等待界面

        void showSummaries(List<SDSummary> para_sdSummaries);

        void showDetails(List<SDDetail> para_sdDetails);

        void showPopUpMenu(Context para_context);

        String getCurrentSummaryID();
    }

    interface Presenter extends BasePresenter {
        void saveAnswerToMap(String key, String value);

        void commitAnswer();

        void loadResult();

        void loadSummaries(boolean forceUpdate);//获取

        void loadDetails(String summaryId);

        void loadCreatedSummaries();

        void loadCreatedDetails();

        void saveCreatedSummary(SDSummaryCreate para_sdSummaryCreate);

        void saveCreatedDetail(SDDetail para_sdDetail);//前台保存时为单个单个保存
    }
}
