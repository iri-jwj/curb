package jxpl.scnu.curb.immediateInformation;

/**
 * Created by irijw on 2017/9/5.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class immediateInformation {
    private String title;
    private String date;
    private String time;
    private String content;
    private String type;
    private String content_url;

    public immediateInformation(String title, String date, String time, String content, String type, String content_url) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.content = content;
        this.type = type;
        this.content_url = content_url;
    }

    public immediateInformation(String title, String date, String time, int imageId) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
