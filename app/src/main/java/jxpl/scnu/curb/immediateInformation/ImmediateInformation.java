package jxpl.scnu.curb.immediateInformation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * @author iri-jwj
 * @version 1
 */

public final class ImmediateInformation {


    private final UUID id;
    @NonNull
    private final String title;//标题
    @NonNull
    private final String content;//正文内容
    @NonNull
    private final String belong;//类型 用于区分教务信息、招聘信息、兼职信息等
    @NonNull
    private final String createTime;
    @Nullable
    private final String time;
    @Nullable
    private String address;

    public ImmediateInformation(UUID para_id,
                                @NonNull String para_title,
                                @NonNull String para_content,
                                @NonNull String para_type,
                                @NonNull String para_createTime,
                                @Nullable String para_time,
                                @Nullable String para_address) {
        id = para_id;
        title = para_title;
        content = para_content;
        belong = para_type;
        createTime = para_createTime;
        time = para_time;
        address = para_address;
    }

    /* @Nullable
    private String content_url;//原文url*/


    @NonNull
    public String getCreateTime() {
        return createTime;
    }

    @Nullable
    public String getTime() {
        return time;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public UUID getId() {
        return id;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public String getBelong() {
        return belong;
    }


    @NonNull
    public String getTitle() {
        return title;
    }
}
