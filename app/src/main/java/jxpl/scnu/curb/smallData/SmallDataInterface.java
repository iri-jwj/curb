package jxpl.scnu.curb.smallData;


import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

public interface SmallDataInterface {
    interface View extends BaseView<Presenter> {
        void changeToQuestionnaire();

        void changeBack();

        void markAnswered();

        void showError(String error);
    }

    interface Presenter extends BasePresenter {
        void saveAnswerToMap(String key, String value);

        void saveAnswerTitle(String title);

        void commitAnswer();

        void getFinalResult();
    }
}
