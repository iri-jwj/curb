package jxpl.scnu.curb.immediateInformation;

import android.content.Context;

import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;


interface InformationContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        int getImageIdByType(String type);

        void showInformationDetailsUi(int id, Context context);

        void showInfo(List<ImmediateInformation> immediateInformations);

        void showNoInfo();

        void showNoNewInfo();

        void showLoadingError();

        void setLoadingIndicator(boolean active);

        List<ImmediateInformation> getCurrentList();

        boolean isListShowing();
    }

    interface Presenter extends BasePresenter {
        void getInformationFromRepository();

        String getFiltering();

        void setFiltering(String filtering);

        void openInformationDetails(ImmediateInformation immediateInformation, Context context);

        void loadInformation(boolean forceUpdate);
    }
}
