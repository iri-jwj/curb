package jxpl.scnu.curb.immediateInformation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by irijw on 2017/9/5.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public final class ImmediateInformation {

    @NonNull
    private final String id;
    @NonNull
    private final String title;//标题
    @NonNull
    private final String date;//日期
    @NonNull
    private final String content;//正文内容
    @NonNull
    private final String type;//类型 用于区分教务信息、学者网信息、招聘信息、兼职信息等
    @Nullable
    private String content_url;//原文url

    public ImmediateInformation(@NonNull String id, @NonNull String title, @NonNull String date, @NonNull String content, @NonNull String type, String content_url) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.type = type;
        this.content_url = content_url;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @Nullable
    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(@Nullable String content_url) {
        this.content_url = content_url;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDate() {
        return date;
    }
}
