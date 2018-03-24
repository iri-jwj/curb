package jxpl.scnu.curb.smallData.result;

import android.content.Context;

import java.util.List;
import java.util.UUID;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;
import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDResult;


public interface ResultInterface {
    interface Presenter extends BasePresenter {
        void loadResult(String summary_id);

        Context getPresenterContext();
    }

    interface View extends BaseView<Presenter> {
        void showError(String error);

        void showResults(List<SDResult> para_sdResults);

        UUID getSummaryID();
    }
}
