package jxpl.scnu.curb.homePage.scholat;

import android.app.Activity;

import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

/**
 * Created by iri-jwj on 2018/3/27.
 *
 * @author iri-jwj
 * @version 1
 */

public interface ScholatContract {
    interface View extends BaseView<Presenter> {
        void showMessage(String message);

        void showScholats(List<ScholatHomework> para_homeworkList);

        void setLoadingIndicator(boolean active);

        void setActivity(final Activity para_activity);
    }

    interface Presenter extends BasePresenter {
        void loadScholats(boolean forceUpdate);

        void addToReminder(ScholatHomework para_scholatHomework);
    }
}
