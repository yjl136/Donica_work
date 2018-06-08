package cn.donica.slcd.server;

import android.app.Instrumentation;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import cn.donica.slcd.server.tools.ClsUtils;
import cn.donica.slcd.server.tools.DLog;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2018-05-08 08:42
 * Describe:
 */

public class BluetoothServerService extends Service {
    private final static String PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    private BluetoothAdapter mBluetoothAdapter;
    private PairReceiver mPairReceiver;

    @Override
    public void onCreate() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        registerReceiver();
        waitConnect();
    }


    private void waitConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    BluetoothServerSocket serverSocket = createServerSocket();
                    DLog.info("v1.6");
                    BluetoothSocket socket = listenConnect(serverSocket);

                    if (socket == null) {
                        closeConnect(socket, serverSocket);
                        continue;
                    }
                    readMsg(socket);
                    closeConnect(socket, serverSocket);
                }
            }
        }
        ).start();
    }

    private void registerReceiver() {
        mPairReceiver = new PairReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PAIRING_REQUEST);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mPairReceiver, filter);
    }

    /**
     * 关闭连接
     *
     * @param socket
     */
    private void closeConnect(BluetoothSocket socket, BluetoothServerSocket serverSocket) {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BluetoothServerSocket createServerSocket() {
        BluetoothServerSocket serSocket = null;
        try {
            serSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(mBluetoothAdapter.getName(), UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            DLog.info("server socket create...");
        } catch (IOException e) {
            DLog.info("server socket create error");
            e.printStackTrace();
        } finally {
            return serSocket;
        }
    }

    /**
     * 读取数据
     *
     * @param socket
     */
    private void readMsg(BluetoothSocket socket) {
        InputStream in = null;
        try {
            byte[] buffer = new byte[64];
            in = socket.getInputStream();
            while (true) {
                if ((in.read(buffer)) != 0) {
                    String msg = new String(buffer);
                    DLog.info(msg.trim());
                    if (!parserMsg(msg)) {
                        //数据错乱，断开重新连接
                        DLog.info("数据错乱，断开重新连接");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析数据
     *
     * @param msg
     */
    private boolean parserMsg(String msg) {
        boolean isResp = false;
        try {
            JSONObject obj = JSON.parseObject(msg.trim());
            int code = obj.getIntValue("code");
            String desc = obj.getString("desc");
            DLog.info("desc:" + desc + "  code:" + code);
            Instrumentation instrumentation = new Instrumentation();
            instrumentation.sendKeyDownUpSync(code);
            isResp = true;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return isResp;
        }
    }

    /**
     * 监听连接
     *
     * @return
     */
    private BluetoothSocket listenConnect(BluetoothServerSocket serverSocket) {
        BluetoothSocket socket = null;
        try {
            DLog.info("listening....");
            socket = serverSocket.accept();
            EventBus.getDefault().post(socket.getRemoteDevice().getName());
            DLog.info("new device coming....");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return socket;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPairReceiver != null) {
            unregisterReceiver(mPairReceiver);
        }
    }

    private class PairReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice btDevice = null;
            btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (PAIRING_REQUEST.equals(intent.getAction())) {
                DLog.info("----------------------------pairing request--------------------");
                try {
                    ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
                int state = btDevice.getBondState();
                DLog.info("----------------------------bond state--------------------: " + state);
            }
        }
    }

}
