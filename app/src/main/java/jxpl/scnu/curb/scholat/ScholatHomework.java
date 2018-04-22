package jxpl.scnu.curb.scholat;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by iri-jwj on 2018/3/27.
 *
 * @author iri-jwj
 * @version 1
 */

public final class ScholatHomework {
    @NonNull
    private final String homeworkId;
    @NonNull
    private final String title;
    @NonNull
    private final String content;
    @NonNull
    private final String createTime;
    @NonNull
    private final String endTime;

    public ScholatHomework(@NonNull String para_homeworkId,
                           @NonNull String para_title,
                           @NonNull String para_content,
                           @NonNull String para_createTime,
                           @NonNull String para_endTime) {
        homeworkId = para_homeworkId;
        title = para_title;
        content = para_content;
        createTime = para_createTime;
        endTime = para_endTime;
    }

    @NonNull
    public String getHomeworkId() {
        return homeworkId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public String getCreateTime() {
        return createTime;
    }

    @NonNull
    public String getEndTime() {
        return endTime;
    }
}
