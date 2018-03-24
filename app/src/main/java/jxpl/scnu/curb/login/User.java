package jxpl.scnu.curb.login;


/**
 * Created by irijw on 2017/12/1.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class User {
    private int userId;
    private String name;

    public User(int para_userId, String para_name) {
        userId = para_userId;
        name = para_name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int para_userId) {
        userId = para_userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String para_name) {
        name = para_name;
    }
}
