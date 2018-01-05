package cn.donica.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.net.ethernet.EthernetManager;
import android.os.Bundle;
import android.os.IHwtestService;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Timer;

public class MainActivity extends Activity {
    private final static String PACKET_NAME = "cn.donica.slcd.ble";
    private final static String ACTION_PA = "cn.donica.slcd.action.PA";
    private final static String PA_KEY = "pa";
    private static String TAG = "MainActivity";
    private Button button;
    private TextView paTv;
    private IHwtestService hwtestService;
    private Timer timer;
    private EthernetManager mEthernetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.bt);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        StorageManager ms = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        Log.i(TAG, "onCreate");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filter.addDataScheme("file");
        registerReceiver(new USBReceiver(), filter);
      /*  IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkConnectChangedReceiver(), filter);*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBasePackage();

            }
        });





       /* timer = new Timer();
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                   test_audio_signal();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        },0,500);*/
    }

    private void getBasePackage() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0);
        Log.i(TAG, "BaseActvity: " + task.baseActivity.getPackageName());
    }

    private void getBackGroudProcess() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                // activityManager.killBackgroundProcesses("org.videolan.vlc.debug");
                List<ActivityManager.RunningAppProcessInfo> processInfoList = activityManager.getRunningAppProcesses();
                StringBuffer buffer = new StringBuffer();
                for (ActivityManager.RunningAppProcessInfo info : processInfoList) {

                    buffer.append("\nprocessName: " + info.processName);
                    buffer.append("  pid: " + info.pid);
                    buffer.append("  uid: " + info.uid);
                    buffer.append("  importance: " + info.importance);
                }
                Log.i(TAG, buffer.toString());
                Log.i(TAG, "#########################kill####################");
                for (ActivityManager.RunningAppProcessInfo info : processInfoList) {
                    activityManager.killBackgroundProcesses(info.processName);
                }
                List<ActivityManager.RunningAppProcessInfo> processInfoList2 = activityManager.getRunningAppProcesses();
                StringBuffer buffer2 = new StringBuffer();
                for (ActivityManager.RunningAppProcessInfo info : processInfoList2) {

                    buffer2.append("\nprocessName: " + info.processName);
                    buffer2.append("  pid: " + info.pid);
                    buffer2.append("  uid: " + info.uid);
                    buffer2.append("  importance: " + info.importance);
                }
                Log.i(TAG, buffer2.toString());
            }
        }).start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged");
    }

    private class NetworkConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    private void queryMonitor() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://cn.donica.slcd.provider/monitor/pa"), null, null, null, null, null);
        cursor.moveToFirst();
        int valueIndex = cursor.getColumnIndex("value");
        int nameIndex = cursor.getColumnIndex("name");
        int value = cursor.getInt(valueIndex);
        String name = cursor.getString(nameIndex);

    }

    public void test_audio_signal() throws RemoteException {
        byte strinfo[] = new byte[256];
        int ret = hwtestService.test_audio_signal(strinfo);
        String info = "";
        if (ret == 0) {
            int len = strinfo[0];
            info = new String(strinfo, 1, len);
        }
        Log.i(TAG, "test_audio_signal:" + ret + "       info:" + info);
    }

    /**
     * 监听5000端口，获取Pa状态
     */
    private void initPA() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket(5000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                if (socket == null) {
                    Log.i(TAG, "socket==null");
                    return;
                }
                while (true) {
                    byte[] buffer = null;
                    DatagramPacket packet = null;
                    try {
                        buffer = new byte[512];
                        packet = new DatagramPacket(buffer, 512);
                        Log.i(TAG, "wait data!!");
                        socket.receive(packet);
                        int len = packet.getLength();
                        byte[] content = packet.getData();
                        Log.i(TAG, "content[85]:" + StringUtil.byte2Hex(content[85]) + "  content[86]:" + StringUtil.byte2Hex(content[86]));
                        if (content != null && len > 86 && ("31".equals(StringUtil.byte2Hex(content[85])) || "31".equals(StringUtil.byte2Hex(content[86])))) {
                            Log.w(TAG, "PA");
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        packet = null;
                        buffer = null;
                    }
                }
            }
        }).start();

    }

    class USBReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "action:" + intent.getAction());
            String mountPath = intent.getData().getPath();
            Log.d(TAG, "mountPath = " + mountPath);
            if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
                //  String mountPath = intent.getData().getPath();
                Log.d(TAG, "mountPath = " + mountPath);
            }
        }
    }

    class PaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "pa");
            int pa = intent.getIntExtra(PA_KEY, 0);
            if (pa == 1) {
                paTv.setTextColor(Color.GREEN);
            } else {
                paTv.setTextColor(Color.GRAY);
            }
        }
    }
}
