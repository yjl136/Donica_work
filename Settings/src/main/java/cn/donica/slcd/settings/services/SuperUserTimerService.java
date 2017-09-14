package cn.donica.slcd.settings.services;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import cn.donica.slcd.settings.utils.ActivityUtil;
import cn.donica.slcd.settings.utils.Config;
import cn.donica.slcd.settings.utils.LogUtil;
import cn.donica.slcd.settings.utils.SuperUserTimer;

/**
 * Created by liangmingjie on 2016/6/14.
 */
public class SuperUserTimerService extends Service {

    private static final String TAG = "SuperUserTimerService";
    private static Handler mHandler;
    private SuperUserTimer mCodeTimer;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        LogUtil.d(TAG, "SuperUserTimerService onCreate");
    }

    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        LogUtil.d(TAG, "onStartCommand启动了计时服务");
        setHandler(mCodeHandler);
        if (mCodeTimer == null) {
            mCodeTimer = new SuperUserTimer(30 * 1000, 1000, mHandler);
            mCodeTimer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ActivityUtil.isAdditionOn = false;
        LogUtil.d(TAG, "销毁计时服务");
        ActivityUtil.isAdditionOn = false;
    }

    /**
     * 设置Handler
     */
    public static void setHandler(Handler handler) {
        mHandler = handler;
    }

    /**
     * 倒计时Handler
     */

    Handler mCodeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SuperUserTimer.IN_RUNNING) {// 正在倒计时
            } else if (msg.what == SuperUserTimer.END_RUNNING) {// 完成倒计时
                mCodeTimer = null;
                stopSelf();
                Config.isSuperUserTimerRun = false;
                System.gc();
            }
        }
    };
}
