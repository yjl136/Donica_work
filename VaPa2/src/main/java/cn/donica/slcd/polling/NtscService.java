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
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import cn.donica.slcd.utils.LogUtil;
import cn.donica.slcd.utils.ShellUtils;

public class NtscService extends Service {
    private IHwtestService hwtestService = null;
    private Context mContext;
    private WindowManager windowManager;
    private ScreenSaverView screenView;
    private static final String TAG = "NtscService";
    private WorkerThread workerThread;

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
        byte strinfo[] = new byte[256];
        byte ret = hwtestService.get_ntsc_status(strinfo);
        if (ret == 0) {
            byte len = strinfo[0];
            String info = new String(strinfo, 1, len);
            if (info.contains("1")) {
                return true;
            } else {
                return false;
            }
        } else {
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
                if (!MyApplication.ntscIsOpen) {
                    MyApplication.ntscIsOpen = true;
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessageDelayed(msg, 1750);
                    shellAppRunStr();
                }
                Log.i("NTSC", "true");
            } else {
                if (MyApplication.ntscIsOpen) {
                    MyApplication.ntscIsOpen = false;
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessageDelayed(msg, 10);
                    shellKillAllStr();
                }
                Log.i("NTSC", "false");
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
        ShellUtils.execCommand(MyApplication.ShellAppRunStr, true);
    }

    private void shellKillAllStr() {
        ShellUtils.execCommand(MyApplication.ShellKillAllStr, true);
    }

    public void addView() {
        if (screenView == null) {
            screenView = new ScreenSaverView(mContext);
            LayoutParams param = new LayoutParams();
            param.type = LayoutParams.TYPE_SYSTEM_ALERT;
            param.format = PixelFormat.RGBA_8888;
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
}


