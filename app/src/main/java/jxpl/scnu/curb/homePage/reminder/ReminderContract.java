package jxpl.scnu.curb.homePage.reminder;

import java.util.List;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

/**
 * created on 2018/6/4
 *
 * @author iri-jwj
 * @version 1 init
 */
public interface ReminderContract {
    interface View extends BaseView<Presenter> {
        /**
         * 显示用户没有增加过提醒到系统日历中
         */
        void showNoReminder();

        /**
         * 显示用户添加到日历中的提醒/事件
         *
         * @param para_reminders 提醒的列表
         */
        void showReminders(List<Reminder> para_reminders);

        void showError(String msg);
    }

    interface Presenter extends BasePresenter {
        /**
         * 删除某个提醒
         *
         * @param title 目标的题目
         */
        void deleteReminder(String title);
    }
}
