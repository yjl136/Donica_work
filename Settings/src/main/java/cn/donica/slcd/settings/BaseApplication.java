/**
 * @author LiangMingJie
 */
package cn.donica.slcd.settings;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";

    private static Context context;
    public static final String LOCK = "lock";
    public static final String LOCK_KEY = "lock_key";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    public static Context getContext() {
        return context;
    }

}
