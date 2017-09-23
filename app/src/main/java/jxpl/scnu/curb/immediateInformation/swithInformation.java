package jxpl.scnu.curb.immediateInformation;

/**
 * Created by irijw on 2017/9/5.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class swithInformation {
    private String itemName;
    private String date;
    private String time;
    private int imageId;

    public swithInformation(String itemName, String date, String time, int imageId) {
        this.itemName = itemName;
        this.date = date;
        this.time = time;
        this.imageId = imageId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
