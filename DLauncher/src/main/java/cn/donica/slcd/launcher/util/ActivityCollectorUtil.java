package cn.donica.slcd.launcher.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollectorUtil {
    private static final String TAG = "ActivityCollectorUtil";
    public static List<Activity> activities = new ArrayList<Activity>();

    /**
     * 每启动一个
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
                LogUtil.d(TAG, "finishAll()");
            }
        }
    }
}