package jxpl.scnu.curb.immediateInformation;

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

interface informationContract {
    interface View extends BaseView<Presenter>{
        int getImageIdByType(String type);
        void showFilteringPopUpMenu();
    }
    interface Presenter extends BasePresenter{
        List<immediateInformation> getInformationFromWeb();
        void setFiltering(informationFilter filtering);
    }
}
