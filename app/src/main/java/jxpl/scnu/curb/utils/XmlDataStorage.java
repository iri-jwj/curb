package jxpl.scnu.curb.utils;

import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局的XML文件读写
 * Created by iri-jwj on 2018/3/28.
 *
 * @author iri-jwj
 * @version 1
 * <p>
 * update 3/29
 * 新增判断SharedHelper是否已经存在 {@link #isSharedHelperSet()}
 */
public class XmlDataStorage {
    public static final String USER_ACCOUNT = "user_account";
    public static final String USER_PSW = "user_psw";
    public static final String USER_ID = "user_id";
    public static final String SCHOLAT_ACCOUNT = "scholat_account";
    public static final String SCHOLAT_PSW = "scholat_psw";
    public static final String IS_INFO_FIRST_RUN = "is_info_first_run";
    public static final String IS_SD_FIRST_RUN = "is_sd_first_run";
    public static final String IS_RIVER_FIRST_RUN = "is_river_first_run";
    public static final String IS_SCHOLAT_FIRST_RUN = "is_scholat_first_run";

    public static final String AVATAR_PATH = "my_avatar_path";
    public static final String NICKNAME = "my_nickname";

    private static SharedHelper m_sharedHelper = null;

    /**
     * 判断 {@link #m_sharedHelper} 是否为null
     *
     * @return 不为null时返回true，否则为false
     */
    public static boolean isSharedHelperSet() {
        return m_sharedHelper != null;
    }

    /**
     * 设置sharedHelper实例
     *
     * @param para_sharedHelper
     */
    public static void setM_sharedHelper(SharedHelper para_sharedHelper) {
        m_sharedHelper = para_sharedHelper;
    }

    /**
     * 存储用户的主要信息
     *
     * @param userId
     * @param userPsw
     * @param userAccount
     */
    public static void saveUserInfo(@NonNull String userId,
                                    @NonNull String userPsw,
                                    @NonNull String userAccount) {
        m_sharedHelper.saveData(USER_ID, userId);
        m_sharedHelper.saveData(USER_ACCOUNT, userAccount);
        m_sharedHelper.saveData(USER_PSW, userPsw);
    }

    /**
     * 存储学者网账号信息
     *
     * @param scholatAccount
     * @param scholatPsw
     */
    public static void saveScholat(@NonNull String scholatAccount,
                                   @NonNull String scholatPsw) {
        m_sharedHelper.saveData(SCHOLAT_ACCOUNT, scholatAccount);
        m_sharedHelper.saveData(SCHOLAT_PSW, scholatPsw);
    }

    /**
     * 获取学者网账号信息
     *
     * @return Map保存
     */
    public static Map<String, String> getScholat() {
        Map<String, String> lc_stringMap = new LinkedHashMap<>();
        lc_stringMap.put(SCHOLAT_ACCOUNT, (String) m_sharedHelper.getData(SCHOLAT_ACCOUNT, ""));
        lc_stringMap.put(SCHOLAT_PSW, (String) m_sharedHelper.getData(SCHOLAT_PSW, ""));
        return lc_stringMap;
    }

    /**
     * 获取用户信息
     *
     * @return map保存
     */
    public static Map<String, String> getUserInfo() {
        Map<String, String> lc_stringMap = new LinkedHashMap<>();
        lc_stringMap.put(USER_ID, (String) m_sharedHelper.getData(USER_ID, ""));
        lc_stringMap.put(USER_ACCOUNT, (String) m_sharedHelper.getData(USER_ACCOUNT, ""));
        lc_stringMap.put(USER_PSW, (String) m_sharedHelper.getData(USER_PSW, ""));
        return lc_stringMap;
    }

    /**
     * 设置资讯是否是第一次运行
     *
     * @param isFirstRun
     */
    public static void setInfoFirstRun(boolean isFirstRun) {
        m_sharedHelper.saveData(IS_INFO_FIRST_RUN, isFirstRun);
    }

    /**
     * 设置SD是否是第一次运行
     *
     * @param isFirstRun
     */
    public static void setSDFirstRun(boolean isFirstRun) {
        m_sharedHelper.saveData(IS_SD_FIRST_RUN, isFirstRun);
    }

    /**
     * 设置River是否是第一次运行
     *
     * @param isFirstRun
     */
    public static void setRiverFirstRun(boolean isFirstRun) {
        m_sharedHelper.saveData(IS_RIVER_FIRST_RUN, isFirstRun);
    }

    /**
     * 设置学者网是否是第一次运行
     *
     * @param isFirstRun
     */
    public static void setScholatFirstRun(boolean isFirstRun) {
        m_sharedHelper.saveData(IS_SCHOLAT_FIRST_RUN, isFirstRun);
    }

    /**
     * 获取主要信息界面的运行信息
     *
     * @param target 要获取的对象
     * @return
     */
    public static boolean getFirstRunState(String target) {
        return (boolean) m_sharedHelper.getData(target, true);
    }

    public static void saveAvatarPath(String path) {
        m_sharedHelper.saveData(AVATAR_PATH, path);
    }

    public static String getAvatarPath() {
        return (String) m_sharedHelper.getData(AVATAR_PATH, "");
    }

    public static void saveNickname(String nickname) {
        m_sharedHelper.saveData(NICKNAME, nickname);
    }

    public static String getNickname() {
        return (String) m_sharedHelper.getData(NICKNAME, "");
    }
}
