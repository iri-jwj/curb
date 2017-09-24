package jxpl.scnu.curb.homePage;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

/**
 * Created by irijw on 2017/9/18.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public interface homePageContract {
    interface View extends BaseView<Presenter>{
        int  getImageIdByType(String type);
    }
    interface Presenter extends BasePresenter{

    }
}
