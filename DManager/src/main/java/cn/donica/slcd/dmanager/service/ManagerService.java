/**
 * File Name: ManagerService.java
 * Description：
 * Author: Luke Huang
 * Create Time: 2015-7-28
 * <p>
 * For the SLCD Project
 * Copyright © 2015 Donica.cn All rights reserved
 */
package cn.donica.slcd.dmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Description:
 *
 * @author Luke Huang 2015-7-28
 */
public class ManagerService extends Service {
    private static final String TAG = ManagerService.class.getName();
    private IBinder mBinder = new ManagerService.LocalBinder();

    /**
     * (non-Javadoc)
     *
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "ManagerService is created.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "ManagerService starts on command");
        //TODO 
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        // 返回本地服务
        public ManagerService getService() {
            return ManagerService.this;
        }
    }
}
