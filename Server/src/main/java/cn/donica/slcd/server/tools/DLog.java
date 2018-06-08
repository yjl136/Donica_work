package cn.donica.slcd.server.tools;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by liangmingjie on 2016/10/19.
 */
public class DLog {
    private final static boolean isDebug = true;
    private final static String TAG = "Donica_Server";

    /**
     * @param msg
     */
    public static void info(String msg) {
        if (isDebug) {
            if (TextUtils.isEmpty(msg)) {
                msg = "null";
            }
            Log.i(TAG, msg);
        }
    }

    /**
     * 错误log
     *
     * @param msg
     */
    public static void error(String msg) {
        if (isDebug) {
            if (TextUtils.isEmpty(msg)) {
                msg = "null";
            }
            Log.e(TAG, msg);
        }
    }

    /**
     * 警告log
     *
     * @param msg
     */
    public static void warn(String msg) {
        if (isDebug) {
            if (TextUtils.isEmpty(msg)) {
                msg = "null";
            }
            Log.w(TAG, msg);
        }
    }


}
