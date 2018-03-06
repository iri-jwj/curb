package jxpl.scnu.curb.data.repository;

/**
 * Created by irijw on 2017/12/9.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public interface AccountDataManage {
    boolean saveAccountToDb(String account, String psw);

    boolean changeAccountPsw(String id, String psw);

    boolean checkExist(String type);

    void deleteAccount(String id);

    String[] getAccount(String id);
}
