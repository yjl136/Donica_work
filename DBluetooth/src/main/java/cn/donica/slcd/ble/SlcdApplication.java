package cn.donica.slcd.ble;

import android.content.Context;

import org.litepal.LitePalApplication;

public class SlcdApplication extends LitePalApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getAppContext() {
        return context;
    }
}
