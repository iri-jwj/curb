package jxpl.scnu.curb.immediateInformation;

import android.content.Context;

import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;


interface InformationContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        int getImageIdByType(String type);

        void showFilteringPopUpMenu(Context context);

        void showInformationDetailsUi(String id, Context context);

        void showInfo(List<ImmediateInformation> immediateInformations);

        void showNoInfo();

        void showLoadingError();

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {
        void getInformationFromRepository();

        String getFiltering();

        void setFiltering(String filtering);

        void openInformationDetails(ImmediateInformation immediateInformation, Context context);

        void loadInformation(boolean forceUpdate);
    }
}
