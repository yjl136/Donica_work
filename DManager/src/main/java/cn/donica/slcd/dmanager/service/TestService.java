package cn.donica.slcd.dmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import cn.donica.slcd.dmanager.test.TrapReceiverTest;

public class TestService extends Service {
    private static final String TAG = "TestService";

    public TestService() {

    }

    //    @Override
//    public void onCreate() {
//        super.onCreate();
//        Check check = new Check();
//        try {
//            check.bootStart();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "开机服务onStart启动");
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);

            }
        }).start();

    }

    //定义Handler对象
    private Handler handler = new Handler() {
        @Override
//当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TrapReceiverTest trapReceiverTest = new TrapReceiverTest();
            trapReceiverTest.run();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //执行文件的下载或者播放等操作
        return super.onStartCommand(intent, flags, startId);
    }
}
