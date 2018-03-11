package jxpl.scnu.curb.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

import jxpl.scnu.curb.data.repository.AccountDataManage;


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