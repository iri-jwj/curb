package jxpl.scnu.curb.immediateInformation;

import android.content.Context;

import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

/**
 * Created by irijw on 2017/9/25.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

interface InformationContract {
    interface View extends BaseView<Presenter>{
        boolean isActive();
        int getImageIdByType(String type);
        void showFilteringPopUpMenu(Context context);
        void showInformationDetailsUi();
        void showLoadingError();
        void setLoadingIndicator(boolean active);
    }
    interface Presenter extends BasePresenter{
        List<ImmediateInformation> getInformationFromRepository(boolean forceUpdate);
        void setFiltering(InformationFilter filtering);
        InformationFilter getFiltering();
        void openInformationDetails();
        void loadInformation(boolean forceUpdate);
    }
}
