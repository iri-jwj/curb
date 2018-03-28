package jxpl.scnu.curb.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author iri-jwj
 * @version 1
 */
public class SharedHelper {
    private static final String fileName = "globalData";
    private static SharedHelper Instance = null;
    private static SharedPreferences stc_sharedPreferences;

    private SharedHelper(Context para_context) {
        stc_sharedPreferences = para_context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public SharedHelper getInstance(Context para_context) {
        if (Instance == null)
            Instance = new SharedHelper(para_context);
        return Instance;
    }

    public void saveData(String key, Object value) {
        SharedPreferences.Editor lc_editor = stc_sharedPreferences.edit();
        String type = value.getClass().getSimpleName();
        switch (type) {
            case "int":
                lc_editor.putInt(key, (int) value);
                break;
            case "String":
                lc_editor.putString(key, (String) value);
                break;
            case "boolean":
                lc_editor.putBoolean(key, (boolean) value);
                break;
            case "float":
                lc_editor.putFloat(key, (float) value);
                break;
            case "long":
                lc_editor.putLong(key, (long) value);
                break;
        }
        lc_editor.apply();
    }

    public Object getData(String key, Object defaultValue) {
        String type = defaultValue.getClass().getSimpleName();
        Object result = null;
        switch (type) {
            case "int":
                result = stc_sharedPreferences.getInt(key, (int) defaultValue);
                break;
            case "String":
                result = stc_sharedPreferences.getString(key, (String) defaultValue);
                break;
            case "boolean":
                result = stc_sharedPreferences.getBoolean(key, (boolean) defaultValue);
                break;
            case "float":
                result = stc_sharedPreferences.getFloat(key, (float) defaultValue);
                break;
            case "long":
                result = stc_sharedPreferences.getLong(key, (long) defaultValue);
                break;
        }
        return result;
    }

    public boolean isContainsKey(String key) {
        return stc_sharedPreferences.contains(key);
    }
}
