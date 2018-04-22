package jxpl.scnu.curb.immediateInformation;

import android.content.Context;

import java.util.List;
import java.util.UUID;

import jxpl.scnu.curb.BasePresenter;
import jxpl.scnu.curb.BaseView;

/**
 * @author iri-jwj
 * @version 1
 */
interface InformationContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        /**
         * 根据目标information的type来选择drawable中的图片id
         *
         * @param type information的类型
         * @return 返回所需要获取的图片id
         */
        int getImageIdByType(String type);

        /**
         * @param id      information的id
         * @param context 应用上下文
         * @deprecated at 3.24
         */
        void showInformationDetailsUi(UUID id, Context context);

        /**
         * 调用adapter中的replaceInfo(List)方法替换显示的信息
         * {@link InformationFragment.InfoAdapter#replaceInfo(List)}
         *
         * @param immediateInformations 等待显示的资讯信息
         */
        void showInfo(List<ImmediateInformation> immediateInformations);

        /**
         * 显示没有信息的错误
         */
        void showNoInfo();

        /**
         * 显示没有新信息的错误
         */
        void showNoNewInfo();

        /**
         * 显示装载信息时发生错误
         */
        void showLoadingError();

        /**
         * 用于显示数据加载时的指示圈
         *
         * @param active 指示loading状态旋转小圈是否显示
         */
        void setLoadingIndicator(boolean active);

        /**
         * 获取当前adapter中显示着的List
         *
         * @return List<ImmediateInformation>
         */
        List<ImmediateInformation> getCurrentList();

        /**
         * 用于判断当前的adapter中是否已经存在信息
         *
         * @return 当前adapter中的list条目数量
         */
        boolean isListShowing();
    }

    interface Presenter extends BasePresenter {
        /**
         * {void getInformationFromRepository()}
         * 从Repository中获取资讯信息
         * 3.24更新：取消了分类机制，直接显示所有信息;
         * 将在View中Adapter中对新资讯与旧资讯的相同判断逻辑放到此处
         */
        void getInformationFromRepository();

        /**
         * @deprecated at 3/25
         */
        String getFiltering();

        /**
         * @param filtering 筛选的目的信息
         * @deprecated at 3/25
         */
        void setFiltering(String filtering);

        /**
         * @param immediateInformation 需要显示详细的information
         * @param context              应用上下文
         * @deprecated at 3/25
         */
        void openInformationDetails(ImmediateInformation immediateInformation, Context context);

        /**
         * 判断是否是第一次刷新，然后调用真正的装载信息的方法{@link #loadInformations(boolean)}
         *
         * @param forceUpdate 判断是否强制刷新，即强制从网络获取最新信息
         */
        void loadInformation(boolean forceUpdate);
    }
}
