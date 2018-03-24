package jxpl.scnu.curb.smallData.createSmallData;


import android.text.method.LinkMovementMethod;

import java.io.File;
import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDSummaryCreate;

public interface CreateInterface {
    interface View extends BaseView {
        void showError(String message);

        void showCreateSummaries(List<SDSummaryCreate> para_createList);

        void showCreateDetails(List<SDDetail> para_details);

    }

    interface Presenter extends BasePresenter {
        void loadCreatedSummaries();

        void loadCreatedDetails(String summaryId);

        void saveCreatedSummary(SDSummaryCreate para_sdSummaryCreate, final File para_imageFile);

        void saveCreatedDetail(SDDetail para_sdDetail);//前台保存时为单个单个保存

        void postCreatedSummary();
    }
}
