package cn.donica.slcd.client;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2018-05-18 09:13
 * Describe:
 */

public class DonicaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setDebugMode(false);
        UMConfigure.init(this, 0, null);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setCheckDevice(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
}
