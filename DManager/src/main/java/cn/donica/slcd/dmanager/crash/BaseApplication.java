/**
 * File Name: BaseApplication.java
 * Description：
 * Author: Luke Huang
 * Create Time: 2015-7-24
 * <p>
 * For the SLCD Project
 * Copyright © 2015 Donica.cn All rights reserved
 */
package cn.donica.slcd.dmanager.crash;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:	Customize the application to handle uncaught exceptions
 *
 * @author Luke Huang 2015-7-24
 */
public class BaseApplication extends Application {
    private List<Activity> mActivityList = new ArrayList<Activity>();
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

    public static Context getContext() {
        return context;
    }

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    public void finishActivity() {

    }
}
