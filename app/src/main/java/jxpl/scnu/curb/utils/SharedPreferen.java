package jxpl.scnu.curb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by irijw on 2017/12/1.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class SharedPreferen {
    public static final String IsFirstOpen = "ISFIRSTOPEN";
    public static final String UserName = "USERNAME";
    public static final String Password = "PASSWORD";
    private static PreferenceManager preferenceManager;
    private Context context;

    public static void setPreferenceManager(PreferenceManager preferenceManager) {
        SharedPreferen.preferenceManager = preferenceManager;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void CreatePreference() {
        SharedPreferences.Editor s = preferenceManager.getSharedPreferences().edit();
        s.putInt(IsFirstOpen, 1);
        s.putString(UserName, "root");
        s.putString(Password, "123");
        s.apply();
        SharedPreferences ss = preferenceManager.getSharedPreferences();
    }
}
