/**
 * @author LiangMingJie
 */
package cn.donica.slcd.polling;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {
    public final static String ShellAppSuStr = "su";

    public final static String ShellAppRunStr = "/system/bin/mxc-v4l2-tvin -x 0 -ct 27 -cl 0 -cw 720 -ch 480 -ot 0 -ol 0 -ow 1280 -oh 800 -m 2 -tb 1";

    public final static String ShellKillAllStr = "busybox killall mxc-v4l2-tvin";
    public final static String ShellChmodStr = "chmod 777 /system/bin/mxc-v4l2-tvin";
    public static boolean appIsRun = false;
    public static boolean ntscIsOpen = false;

    private static Context context;
    public final static String URL = "http://192.168.2.99/CMT/index.php?module=dashboard&action=queryStatus";
    //public final static String URL = "http://192.168.4.72/WiFi-Trunk/CMT/index.php?module=dashboard&action=queryStatus";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
