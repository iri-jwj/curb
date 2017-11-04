package jxpl.scnu.curb.immediateInformation.informationDetails;

/**
 * Created by irijw on 2017/9/8.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */
import android.support.annotation.NonNull;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

public interface InformationDetailContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void showMissingInfo();
        boolean isActive();
        void showInfo(@NonNull ImmediateInformation immediateInformation);
    }
    interface Presenter extends BasePresenter {
        void openInfo();
    }
}
