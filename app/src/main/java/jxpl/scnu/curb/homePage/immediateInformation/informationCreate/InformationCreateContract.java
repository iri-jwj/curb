package jxpl.scnu.curb.homePage.immediateInformation.informationCreate;

import android.app.Activity;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;
import jxpl.scnu.curb.homePage.immediateInformation.ImmediateInformation;

/**
 * @author iri-jwj
 * @version 2
 * last update 3/25
 */
public interface InformationCreateContract {
    interface View extends BaseView<Presenter> {
        /**
         * 设置显示等待界面
         *
         * @param active true为显示等待界面，false为不显示
         */
        void setLoadingIndicator(boolean active);

        /**
         * 判断当前的view是否已经显示出来了
         *
         * @return true为已加入Fragment，false为否
         */
        boolean isActive();

        /**
         * 显示发送成功\失败的信息
         *
         * @param result true为发送成功，false为发送失败
         */
        void showPostResult(boolean result);

        /**
         * 设置Activity变量，方便调用UI变化
         *
         * @param para_activity 传入的参数
         */
        void setActivity(final Activity para_activity);
    }

    interface Presenter extends BasePresenter {
        /**
         * 将用户创建的information发送到服务端
         *
         * @param para_information 待发送的数据
         */
        void commitInformation(ImmediateInformation para_information);

    }
}
