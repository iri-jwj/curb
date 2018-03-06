package jxpl.scnu.curb.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

import jxpl.scnu.curb.data.repository.AccountDataManage;

/**
 * Created by irijw on 2017/12/9.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class AccountDbManage implements AccountDataManage {
    private CurbDbHelper curbDbHelper;

    public AccountDbManage(@NonNull Context context) {
        curbDbHelper = new CurbDbHelper(context);
    }

    @Override
    public boolean saveAccountToDb(String account, String psw) {

        return false;
    }

    @Override
    public boolean changeAccountPsw(String id, String psw) {
        return false;
    }

    @Override
    public boolean checkExist(String type) {
        return false;
    }

    @Override
    public String[] getAccount(String id) {
        return new String[0];
    }

    @Override
    public void deleteAccount(String id) {

    }
}