package cn.donica.slcd.settings.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import cn.donica.slcd.settings.utils.AdminTimer;
import cn.donica.slcd.settings.utils.Config;

/**
 * 注册倒计计时服务
 *
 * @author liangmingjie
 */
public class AdminTimerService extends Service {

    private static final String TAG = "AdminTimerService";
    private static Handler mHandler;
    private AdminTimer mCodeTimer;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        setHandler(mCodeHandler);
        if (mCodeTimer == null) {
            mCodeTimer = new AdminTimer(20 * 1000, 1000, mHandler);
            mCodeTimer.start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
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
    @SuppressLint("HandlerLeak")
    Handler mCodeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == AdminTimer.IN_RUNNING) {// 正在倒计时
            } else if (msg.what == AdminTimer.END_RUNNING) {// 完成倒计时
//				mCodeTimer.cancel();
                mCodeTimer = null;
                Config.isAdminTimerRun = false;
                stopSelf();
                System.gc();
            }
        }
    };
}
