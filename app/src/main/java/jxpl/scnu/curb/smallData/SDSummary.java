package jxpl.scnu.curb.smallData;


import java.util.UUID;

public class SDSummary {
    private UUID id;            //问卷的id号
    private String title;      //问卷标题
    private String creator;    //发起者
    private String create_time; //问卷发布的时间
    private String img;        //图标
    private String description;//问卷描述
    private boolean hasFinish;

    public SDSummary(UUID id, String title, String creator, String create_time, String img, String description, boolean para_hasFinish) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.create_time = create_time;
        this.img = img;
        this.description = description;
        hasFinish = para_hasFinish;
    }

    public boolean isHasFinish() {
        return hasFinish;
    }

    public void setHasFinish(boolean para_hasFinish) {
        hasFinish = para_hasFinish;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String creat_time) {
        this.create_time = creat_time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
