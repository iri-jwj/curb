package jxpl.scnu.curb.smallData;

import java.util.UUID;

/**
 * Created by iri-jwj on 2018/3/4.
 */

public class SDSummaryCreate {
    private UUID id;            //问卷的id号
    private String title;      //问卷标题
    private String create_time; //问卷发布的时间
    private String img_url;     //图标
    private String description;//问卷描述

    public SDSummaryCreate(UUID para_id, String para_title, String para_create_time, String para_img_url, String para_description) {
        id = para_id;
        title = para_title;
        create_time = para_create_time;
        img_url = para_img_url;
        description = para_description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID para_id) {
        id = para_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String para_title) {
        title = para_title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String para_create_time) {
        create_time = para_create_time;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String para_img_url) {
        img_url = para_img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String para_description) {
        description = para_description;
    }
}
