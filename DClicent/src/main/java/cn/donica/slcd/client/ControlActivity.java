package cn.donica.slcd.client;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.donica.slcd.client.tools.ClsUtils;
import cn.donica.slcd.client.tools.DLog;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2018-05-10 17:40
 * Describe:
 */

public class ControlActivity extends AppCompatActivity {
    private final static String PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    private final static int REQUEST_SCAN_CODE = 1;
    private final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private String mac;
    private final static String TAG = "MainActivityClient";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice device;
    private PairReceiver mPairReceiver;
    private BluetoothSocket socket;
    @BindView(R.id.leftIv)
    public ImageView leftIv;
    @BindView(R.id.rightIv)
    public ImageView rightIv;
    @BindView(R.id.upIv)
    public ImageView upIv;
    @BindView(R.id.downIv)
    public ImageView downIv;
    @BindView(R.id.closeIv)
    public ImageView closeIv;
    @BindView(R.id.homeIv)
    public ImageView homeIv;
    @BindView(R.id.okIv)
    public ImageView okIv;
    @BindView(R.id.backIv)
    public ImageView backIv;
    @BindView(R.id.powerIv)
    public ImageView powerIv;
    @BindView(R.id.volumedownIV)
    public ImageView volumedownIV;
    @BindView(R.id.volumeupIv)
    public ImageView volumeupIv;
    @BindView(R.id.brightnessdownIV)
    public ImageView brightnessdownIV;
    @BindView(R.id.brightnessupIv)
    public ImageView brightnessupIv;
    @BindView(R.id.camera)
    public ImageView cameraBt;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);
        executor = Executors.newSingleThreadExecutor();
        registerReceiver();
        openBluetooth();
        DLog.info("onCreate");
    }

    /**
     * 配对
     */
    private void pair() {
        try {
            boolean isBond = ClsUtils.createBond(device.getClass(),
                    device);
            DLog.info("isBond: " + isBond);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开蓝牙
     */
    private void openBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            boolean enable = mBluetoothAdapter.enable();
            if (!enable) {
                Toast.makeText(this, "请同意打开蓝牙,否则无法使用此应用", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * 连接
     */
    private void connect() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                createClientSocket();
            }
        });
    }

    /**
     * 断开
     */
    private void disConnect() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                closeConnect();
            }
        });

    }

    private void closeConnect() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否连接
     *
     * @return true 已经连接
     * false 未连接或者已经断开
     */
    private boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    /**
     * 连接以后获取输出流
     *
     * @param socket
     * @return
     */
    private OutputStream getOutputStream(BluetoothSocket socket) {
        OutputStream out = null;
        try {
            if (socket != null) {
                out = socket.getOutputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return out;
        }
    }

    /**
     * 创建socket 并连接
     *
     * @return
     */
    private BluetoothSocket createClientSocket() {
        try {
            if (device != null) {
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                socket.connect();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ControlActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ControlActivity.this, "请先扫码配对连接", Toast.LENGTH_SHORT).show();
                    }
                });
                DLog.info("device == null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return socket;
        }
    }


    private void registerReceiver() {
        mPairReceiver = new PairReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PAIRING_REQUEST);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mPairReceiver, filter);
    }

    @OnClick(R.id.leftIv)
    public void toLeft() {
        Key key = getKey(KeyCode.AKEYCODE_DPAD_LEFT, "left");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.rightIv)
    public void toRight() {
        Key key = getKey(KeyCode.AKEYCODE_DPAD_RIGHT, "right");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.downIv)
    public void toDown() {
        Key key = getKey(KeyCode.AKEYCODE_DPAD_DOWN, "down");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.upIv)
    public void toUp() {
        Key key = getKey(KeyCode.AKEYCODE_DPAD_UP, "up");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.homeIv)
    public void goHome() {
        Key key = getKey(KeyCode.AKEYCODE_HOME, "home");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.backIv)
    public void back() {
        Key key = getKey(KeyCode.AKEYCODE_BACK, "back");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.powerIv)
    public void power() {
        Key key = getKey(KeyCode.AKEYCODE_POWER, "power");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.okIv)
    public void ok() {
        Key key = getKey(KeyCode.AKEYCODE_ENTER, "ok");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.camera)
    public void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openScan();
                    return;
                } else {
                    Toast.makeText(ControlActivity.this, "请打开相机和存储权限,否则无法使用此功能", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ControlActivity.this, "请打开相机和存储权限,否则无法使用此功能", Toast.LENGTH_LONG).show();
            }
            Uri packageURI = Uri.parse("package:" + getPackageName());
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openScan() {
        Intent intent = new Intent(ControlActivity.this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);
        config.setShake(true);
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_SCAN_CODE);
    }

    @OnClick(R.id.volumeupIv)
    public void volumeUp() {
        Key key = getKey(KeyCode.AKEYCODE_VOLUME_UP, "volumeup");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.brightnessupIv)
    public void brightnessUp() {
        Key key = getKey(KeyCode.KEYCODE_BRIGHTNESS_UP, "brightnessup");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.brightnessdownIV)
    public void brightnessDown() {
        Key key = getKey(KeyCode.KEYCODE_BRIGHTNESS_DOWN, "brightnessdown");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.volumedownIV)
    public void volumeDown() {
        Key key = getKey(KeyCode.AKEYCODE_VOLUME_DOWN, "volumedown");
        sendMsg(key);
        DLog.info("json: " + JSON.toJSON(key).toString());
    }

    @OnClick(R.id.closeIv)
    public void clickDisconnect() {
        finish();
    }
    /*@OnClick(R.id.reconnection)
    public void clickReconnection() {
        connect();
    }*/

    @NonNull
    private Key getKey(int code, String desc) {
        Key key = new Key();
        key.setCode(code);
        key.setDesc(desc);
        return key;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DLog.info("onPause");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DLog.info("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DLog.info("onDestroy");
        if (mPairReceiver != null) {
            unregisterReceiver(mPairReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_SCAN_CODE) {
            mac = data.getStringExtra(Constant.CODED_CONTENT);
            DLog.info("mac: " + mac + " requestCode:" + requestCode);
            device = mBluetoothAdapter.getRemoteDevice(mac.trim());
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                connect();
                DLog.info("connect");
            } else {
                DLog.info("pair");
                pair();
            }
        }
    }

    private void sendMsg(final Key key) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (isConnected()) {
                    try {
                        byte[] origin = JSON.toJSON(key).toString().getBytes();
                        byte[] dest = Arrays.copyOf(origin, 64);
                        OutputStream out = getOutputStream(socket);
                        out.write(dest);
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeConnect();
                        createClientSocket();
                    }
                } else {
                    DLog.info("not connected......");
                    closeConnect();
                    createClientSocket();
                }
            }
        });

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
                if (state == BluetoothDevice.BOND_BONDED) {
                    connect();
                }
            }
        }
    }

}
