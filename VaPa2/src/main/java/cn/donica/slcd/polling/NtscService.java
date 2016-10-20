package cn.donica.slcd.polling;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.IHwtestService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import cn.donica.slcd.utils.LogUtil;
import cn.donica.slcd.utils.ShellUtils;

public class NtscService extends Service {
    private IHwtestService hwtestService = null;
    public static final String LOCK_ACTION = "lock";
    public static final String UNLOCK_ACTION = "unlock";
    private Context mContext;
    private WindowManager windowManager;
    private ScreenSaverView screenView;
    private static final String TAG = "NtscService";
    byte ret = 0;
    byte strinfo[] = new byte[256];
    private WorkerThread workerThread;
    private int value = 0;

    public NtscService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        String action = intent.getAction();
//        if(TextUtils.equals(action, LOCK_ACTION)) {
//            addView();
////            closeNavigationbar();
//        }
//        else if(TextUtils.equals(action, UNLOCK_ACTION))
//        {
//            removeView();
////            openNavigationbar();
//            stopSelf();
//        }
        // LogUtil.d(TAG, "onStartCommand");
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        workerThread = new WorkerThread();
        workerThread.start();
        mContext = getApplicationContext();
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获得7282m的连接状态
     *
     * @throws RemoteException
     */
    public boolean get_ntsc_status() throws RemoteException {
        String info;
        ret = hwtestService.get_ntsc_status(strinfo);
        //  LogUtil.d(TAG, "get_ntsc_status ret:" + ret);
        byte len = strinfo[0];
        info = new String(strinfo, 1, len);
        LogUtil.d(TAG, "get_ntsc_status info:" + info);
        if (value % 2 == 0) {
            value++;
            return true;
        } else {
            value++;
            return false;
        }
    }

    private class WorkerThread extends Thread {
        public void run() {
            processData();
        }
    }

    private void processData() {
        try {
            if (get_ntsc_status() == true) {
                LogUtil.d(TAG, "get_ntsc_status()==true");
                if (MyApplication.ntscIsOpen == false) {
                    MyApplication.ntscIsOpen = true;
//                    sendNtscOpenBroadCast();
                    Message msg = new Message();
                    msg.what = 1;
                    //这里设置为1750毫秒是因为切换到VA需要1S多时间，把静态画面延迟1750ms再取消，为了在视觉上更好的过渡
                    mHandler.sendMessageDelayed(msg, 1750);
                    shellAppRunStr();

                }
            } else {
                if (MyApplication.ntscIsOpen) {
                    LogUtil.d(TAG, "get_ntsc_status()==false");
                    MyApplication.ntscIsOpen = false;
//                    sendNtscCloseBroadCast();
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessageDelayed(msg, 10);
                    shellKillAllStr();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 操作：发送一个NTSC信号开启广播
     */

    private void sendNtscOpenBroadCast() {
        Intent mIntent = new Intent();
        mIntent.setAction("cn.donica.slcd.vapa.ntsc");
        mIntent.putExtra("ntsc", "open");
        sendBroadcast(mIntent);
    }

    /**
     * 操作：发送一个NTSC信号关闭广播
     */
    private void sendNtscCloseBroadCast() {
        Intent mIntent = new Intent();
        mIntent.setAction("cn.donica.slcd.vapa.ntsc");
        mIntent.putExtra("ntsc", "close");
        sendBroadcast(mIntent);
    }

    private void shellAppRunStr() {
        // String commandStr[] = {MyApplication.ShellAppSuStr, MyApplication.ShellAppRunStr};
        ShellUtils.execCommand(MyApplication.ShellAppRunStr, true);

        // command(MyApplication.ShellAppSuStr);
//        shellRun();
////        command("pwd");
//
////        command(MyApplication.ShellAppRunStr);
        LogUtil.d(TAG, "切换到NTSC源");
//        LogUtil.d(TAG, "shellRun():" + shellRun());
//        LogUtil.d(TAG, "command():"+command(MyApplication.ShellAppRunStr));
//       LogUtil.d(TAG, "sysCmd2():"+sysCmd2(MyApplication.ShellAppRunStr));
    }

    private void shellKillAllStr() {
        ShellUtils.execCommand(MyApplication.ShellKillAllStr, true);
        LogUtil.d(TAG, "切回Android画面");
        //  command(MyApplication.ShellKillAllStr);
    }

    public void addView() {
        if (screenView == null) {
            screenView = new ScreenSaverView(mContext);
            LayoutParams param = new LayoutParams();
            param.type = LayoutParams.TYPE_SYSTEM_ALERT;
            param.format = PixelFormat.RGBA_8888;
            // mParam.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            param.width = LayoutParams.MATCH_PARENT;
            param.height = LayoutParams.MATCH_PARENT;
            windowManager.addView(screenView, param);
        }
    }

    public void removeView() {
        if (screenView != null) {
            windowManager.removeView(screenView);
            screenView = null;
        }
    }

    private void closeNavigationbar() {
        try {
            String ProcID = "79";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                ProcID = "42"; // ICS
            // 需要root 权限
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "service call activity " + ProcID + " s16 com.android.systemui"}); // WAS
            proc.waitFor();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*
     * 恢复运行Android 4.0以上系统的平板的屏幕下方的状态栏
     */
    private void openNavigationbar() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"am", "startservice", "-n", "com.android.systemui/.SystemUIService"});
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    addView();
                    break;
                case 1:
                    removeView();
                    stopSelf();
                    break;
            }
        }
    };

    static {
        // System.loadLibrary("shell");
    }

    public native static String command(String cmd);

    public native static String sysCmd1(String cmd);

    public native static String sysCmd2(String cmd);

    public native static String shellRun();
}


