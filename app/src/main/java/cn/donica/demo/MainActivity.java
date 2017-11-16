package cn.donica.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
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
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.bt);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        StorageManager ms = (StorageManager) getSystemService(Context.STORAGE_SERVICE);

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
                ConnectivityManager conMgr = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);


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
