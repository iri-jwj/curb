package jxpl.scnu.curb.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

/**
 * @author iri-jwj
 * @version 2
 * last update 3/25
 * 修改addToArrangement(ImmediateInformation)为addToReminder(ImmediateInformation)
 * 新增注释
 */

public interface InformationDataSource {
    /**
     * 获取单个Information
     *
     * @param callback 获取单个information的回调函数
     * @param id       目标information的id
     */
    void getInformation(@NonNull GetInformationCallback callback, UUID id);

    /**
     * 获取多个information用于显示
     *
     * @param callback  装载information的回调函数
     * @param userId    用户id
     * @param timestamp 用户执行查询的时间
     */
    void getInformations(@NonNull LoadInformationCallback callback,
                         String userId, String timestamp, Context para_context);

    /**
     * 将从网络中获取到的information保存到本地数据库
     *
     * @param immediateInformations 待保存的information
     */
    void saveInfoFromWeb(List<ImmediateInformation> immediateInformations);

    /**
     * 刷新repository中的map缓存
     *
     * @see InformationRepository#cachedInfo
     */
    void refreshInformation();

    /**
     * 将目标information添加到提醒中
     *
     * @param immediateInformation 目标information
     */
    void addToReminder(ImmediateInformation immediateInformation);

    /**
     * 将用户创建的information发送至服务器
     *
     * @param para_callback 回调函数，在发送成功和失败时调用不同的函数
     * @param information   待发送的information
     * @param userId        当前用户id
     */
    void postInformation(PostInformationCallback para_callback,
                         String information,
                         String userId, Context para_context);

    /**
     * 装载information信息的回调接口
     */
    interface LoadInformationCallback {
        void getInformationsLoaded(List<ImmediateInformation> immediateInformations);

        void onDataNotAvailable();
    }

    /**
     * 获取单个information的回调接口
     */
    interface GetInformationCallback {
        void onInformationLoaded(ImmediateInformation immediateInformation);

        void onDataNotAvailable();
    }


    interface PostInformationCallback {
        void onInformationPosted();

        void onPostFailed();
    }
}
