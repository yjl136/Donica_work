package cn.donica.slcd.settings.utils;

import android.content.SharedPreferences;

/**
 * Created by liangmingjie on 2015/12/8.
 */
public class Config {
    public static final String PREF_NAME = "Ip_And_Seat";
    public static char[] IP;
    private SharedPreferences prefercences;

    static String SSIDName = "Donica";
    static String SSIDPassword = "26983727";
    public static String LOCK_TIME = "60";
    static int inputGesturesPwdTime = 5;

    static int inputGesturesPwdTimess = 5;

    public static boolean isSuperUserTimerRun = false;

    public static boolean isAdminTimerRun = false;

    public static int lockCount = 3;
    /**
     * 手势密码输入框是否被锁死的状态值(boolean flag)
     */
    public static boolean isLock = false;

    public static String superUserPassword = "26983727";

    public static String getSSIDName() {
        return SSIDName;
    }

    public static String getSSIDPassword() {
        return SSIDPassword;
    }

    public static String getLockTime() {
        return LOCK_TIME;
    }
}


