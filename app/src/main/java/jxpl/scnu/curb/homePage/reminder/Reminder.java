package jxpl.scnu.curb.homePage.reminder;

/**
 * created on 2018/6/4
 *
 * @author iri-jwj
 * @version 1 init
 */
public class Reminder {
    private String id;
    private String time;
    private String address;
    private String content;
    private String title;

    public Reminder(String para_id, String para_time, String para_address, String para_content, String para_title) {
        id = para_id;
        time = para_time;
        address = para_address;
        content = para_content;
        title = para_title;
    }

    public String getId() {
        return id;
    }

    public void setId(String para_id) {
        id = para_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String para_time) {
        time = para_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String para_address) {
        address = para_address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String para_content) {
        content = para_content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String para_title) {
        title = para_title;
    }
}
