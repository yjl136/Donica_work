package cn.donica.slcd.ble.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothInputDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialPort;
import cn.donica.slcd.ble.cmd.CmdManage;
import cn.donica.slcd.ble.task.AsyncTask;
import cn.donica.slcd.ble.task.entity.ResultEntity;
import cn.donica.slcd.ble.task.entity.TLVEntity;
import cn.donica.slcd.ble.task.entity.TLVSet;
import cn.donica.slcd.ble.utils.ClsUtils;
import cn.donica.slcd.ble.utils.Constant;
import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.ble.utils.FloatWindowManager;
import cn.donica.slcd.ble.utils.PayloadParseHelper;
import cn.donica.slcd.ble.utils.StringUtil;
import cn.donica.slcd.ble.utils.UnitUtils;

/**
 * Created by yejianlin 2016/10/19.
 * 用于检测是否有card靠近
 */
public class DetectService extends Service implements AsyncTask.IStatus {
    private final static int DELAY_TIME = 1500;
    //测试用的蓝牙mac地址
    private final static String SP_MAC = "68:FB:7E:EE:C7:95";
    private final static String jin_MAC = "00:07:5B:00:B9:1B";
    private final static String MAC = "DC:2C:26:02:41:2C";
    private final static String H60_MAC = "24:09:95:B2:65:23";
    private final static String PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private SerialPort mSerialPort;
    private boolean isIdel;
    //当前正在读取卡的数据类型，目前只支持（0x01 和 0x03格式的数据）
    private byte cardByte;

    private BluetoothAdapter adapter;
    /**
     * 串口文件描述符
     */
    private final int BUFSIZE = 512;
    private List<Byte> ndef;
    //需要读取多少个字节数据
    private int count;
    //剩余需要读取多少块数据
    private int blk;
    private Timer timer = new Timer();
    private static final int DELAY_REMOVE_CMD = 0x102;
    private static final int READ_CARD_CMD = 0x100;
    private static final int READ_CARD = 0x200;

    protected final int READ_BLOCK_SUCCESS = 0x101;
    protected final int READ_BLOCK_FAIL = 0x103;
    private PairReceiver mPairReceiver;
    private ExecutorService mExecutorService;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.info("onCreate");
        //打开蓝牙
        enable();
        if (open()) {
            //打开串口成功
            this.mInputStream = mSerialPort.getInputStream();
            this.mOutputStream = mSerialPort.getOutputStream();
            registerReceiver();
            isIdel = true;
            //创建一个线程池对象
            mExecutorService = Executors.newSingleThreadExecutor();
            timer.schedule(task, 0, 1500);
        } else {
            DLog.warn("Fail to open " + Constant.devName + "!");
            stopSelf();
        }

    }

    private TimerTask task = new TimerTask() {
        public void run() {
            if (isIdel) {
                Message message = new Message();
                message.what = READ_CARD;
                handler.sendMessageDelayed(message, 200);
                Message msg = new Message();
                msg.what = READ_CARD_CMD;
                handler.sendMessage(msg);
            } else {
                DLog.info("wait a moment");
            }
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int block;
            switch (msg.what) {
                case READ_CARD:
                    checkCardId();
                    break;
                case READ_CARD_CMD:
                    write(CmdManage.getReadCardId());
                    break;
                case READ_BLOCK_SUCCESS:
                    TLVSet set = (TLVSet) msg.obj;
                    FloatWindowManager.updateFloatView("读取数据成功！！");
                    TLVEntity tlv = set.getEntity((byte) 0x03);
                    if (tlv == null) {
                        FloatWindowManager.updateFloatView("不包含ndef格式数据！！");
                        setIdel(true);
                    } else {
                        initNdefMessage(tlv.getNdefBytes());
                    }
                    break;
                case READ_BLOCK_FAIL:
                    int state = (Integer) msg.obj;
                    FloatWindowManager.updateFloatView("读取数据失败，请重新读取");
                    if (state == ResultEntity.State.BLOCK_WRITE_ERROR) {
                        DLog.error("写失败");
                    } else if (state == ResultEntity.State.BLOCK_READ_ERROR) {
                        DLog.error("读块失败");
                    }
                    setIdel(true);
                    break;
                case DELAY_REMOVE_CMD:
                    FloatWindowManager.removeFloatView(DetectService.this);
                    break;

            }
        }
    };

    private void write(byte[] cmdBytes) {
        try {
            DLog.info("write: " + StringUtil.bytes2HexString(cmdBytes));
            mOutputStream.write(cmdBytes);
            setIdel(false);
        } catch (IOException e) {
            e.printStackTrace();
            setIdel(true);
            DLog.error("Fail to write " + e.getMessage());
        }
    }


    /**
     * 检测cardid
     *
     * @throws IOException
     */
    private void checkCardId() {
        try {
            byte[] buf = new byte[BUFSIZE];
            int retSize = mInputStream.read(buf);
            DLog.info("read: " + StringUtil.bytes2HexString(StringUtil.subBytes(buf, 0, retSize)));
            if (retSize > 0) {
                buf = StringUtil.subBytes(buf, retSize);
                if (buf.length >= 6) {
                    int dataLength = buf[3];
                    int length = dataLength + 6;
                    byte[] data = StringUtil.subBytes(buf, length);
                    if (data[0] == 0x20 && data[length - 1] == 0x03
                            && data[2] == 0) {
                        byte temp = 0x00;
                        for (int i = 0; i < dataLength; i++) {
                            temp = (byte) (temp ^ data[4 + i]);
                        }
                        byte BCC = (byte) ~(data[1] ^ data[2] ^ data[3] ^ temp);
                        if (BCC == data[length - 2]) {
                            handleCardReceive(data);
                        } else {
                            setIdel(true);
                        }
                    } else {
                        setIdel(true);
                    }
                } else {
                    setIdel(true);
                }
            } else {
                setIdel(true);
            }
        } catch (Exception e) {
            setIdel(true);
            e.printStackTrace();
            DLog.error("Fail to read " + e.getMessage());
        }
    }

    private void registerReceiver() {
        mPairReceiver = new PairReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PAIRING_REQUEST);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mPairReceiver, filter);
    }

    /**
     * 打开串口打开
     *
     * @return
     */
    private boolean open() {
        boolean isOpen = false;
        try {
            mSerialPort = new SerialPort(new File(Constant.devName), 9600, 0);
            isOpen = true;
        } catch (Exception e) {
            e.printStackTrace();
            isOpen = false;
            DLog.error("open error:" + e.getMessage());
        } finally {
            return isOpen;
        }
    }

    public void setIdel(boolean isIdel) {
        this.isIdel = isIdel;
        if (isIdel && FloatWindowManager.isFloating()) {
            handler.sendEmptyMessageDelayed(DELAY_REMOVE_CMD, DELAY_TIME);
        }
    }

    /**
     * 处理数据
     *
     * @param data
     */
    public void handleCardReceive(byte[] data) {
        //20 01 00 0b 44 00 00 07 05 3b 44 0c 42 b0 c1 f3 03
        int dataLength = data[3];
        if (dataLength > 0 && data.length > 7) {
            int cardIDLength = data[7];
            byte[] cardId = new byte[cardIDLength];
            cardId = StringUtil.reverse(StringUtil.subBytes(data, 8, cardIDLength));
            DLog.info("读取到卡号为：\n" + StringUtil.bytes2HexString(cardId));
            checkCardType(data[5], data[4]);
        } else {
            setIdel(true);
        }
    }

    /**
     * 检测卡的类型
     *
     * @param type1
     * @param type2
     */
    private void checkCardType(byte type1, byte type2) {
        if (type1 == (byte) 0x00 && type2 == (byte) 0x44) {
            FloatWindowManager.createFloatView(this, UnitUtils.dp2px(this, 500), UnitUtils.dp2px(this, 300));
            FloatWindowManager.updateFloatView("请将卡靠近NFC,读取NDEF数据中。。。。");
            AsyncTask.getInstance(DetectService.this).submit(3, mInputStream, mOutputStream);
        } else {
            // FloatWindowManager.createFloatView(this, UnitUtils.dp2px(this, 500), UnitUtils.dp2px(this, 300));
            DLog.warn("暂时不支持此卡");
            FloatWindowManager.updateFloatView("暂时不支持此类型卡");
            setIdel(true);
        }

    }


    private void initNdefMessage(byte[] ndefBytes) {
        try {
            NdefMessage ndefMessage = new NdefMessage(ndefBytes);
            NdefRecord record = ndefMessage.getRecords()[0];
            byte[] payload = record.getPayload();
            DLog.info("record[0] payload : " + StringUtil.bytes2HexString(payload));
            String localName = PayloadParseHelper.getBlutoothLocalName(payload);
            byte[] macBytes = PayloadParseHelper.getBlutoothMac(payload);
            DLog.info("[" + localName + "]: " + StringUtil.bytes2HexString(macBytes));
            FloatWindowManager.updateFloatView(localName + " : " + StringUtil.bytes2HexString(macBytes));
            pair(macBytes);
        } catch (Exception e) {
            setIdel(true);
            DLog.error("init NdnfMessage error  " + e.getMessage());
        }
    }






    /**
     * 获取到蓝牙mac，进行蓝牙配对
     */
    public void pair(String mac) {
        enable();
        BluetoothDevice device = adapter.getRemoteDevice(mac);
        pair(device);
    }

    /**
     * 获取到蓝牙mac，进行蓝牙配对
     */
    public void pair(byte[] mac) {
        enable();
        BluetoothDevice device = adapter.getRemoteDevice(mac);
        pair(device);
    }

    /**
     * 判断蓝牙模块是否可以用并且打开蓝牙
     */
    private void enable() {
        if (adapter == null) {
            adapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (adapter == null) {
            DLog.warn("Fail to get BluetoothAdapter ");
            setIdel(true);
            stopSelf();
        }
        //如果蓝牙没有打开将蓝牙打开
        if (!adapter.isEnabled()) {

            if (adapter.enable()) {
                DLog.info("open Bluetooth success");
            } else {
                //蓝牙打开失败
                DLog.warn("Fail to open Bluetooth");
                //  stopSelf();
            }
        }
    }

    /**
     * 配对
     *
     * @param device
     */
    public void pair(BluetoothDevice device) {
        DLog.info("[" + device.getName() + "]" + ":" + device.getAddress() + "  [" + device.getBondState() + "]");
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            DLog.info("attemp to bond:" + "[" + device.getName() + "]");
            try {
                boolean isBond = ClsUtils.createBond(device.getClass(),
                        device);
                DLog.info("createBond:" + isBond);
            } catch (Exception e) {
                FloatWindowManager.updateFloatView("蓝牙配对失败。。。。");
                setIdel(true);
                DLog.error("create bond error " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            setIdel(true);
        }
    }

    private class PairReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 创建一个蓝牙device对象
            BluetoothDevice btDevice = null;
            // 从Intent中获取设备对象
            btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (PAIRING_REQUEST.equals(intent.getAction())) {
                DLog.info("----------------------pairing request----------------------");
                try {
                    // 1.确认配对
                    ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);

                } catch (Exception e) {
                    setIdel(true);
                    DLog.error("setPairingConfirmation" + e.getMessage());
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {

                int state = btDevice.getBondState();
                DLog.info("----------------------bond state changed----------------  " + state);
                if (state == BluetoothDevice.BOND_NONE) {
                    FloatWindowManager.updateFloatView("蓝牙配对失败。。。。");
                    setIdel(true);
                } else if (state == BluetoothDevice.BOND_BONDING) {

                } else if (state == BluetoothDevice.BOND_BONDED) {
                    FloatWindowManager.updateFloatView("蓝牙配对成功！！");
                    enable();
                    setIdel(true);
                    boolean isGetProxy = adapter.getProfileProxy(context, new MyServiceListener(btDevice), 4);
                    DLog.info("getProfileProxy:" + isGetProxy);
                }
            }
        }
    }


    private BluetoothInputDevice mService = null;

    private class MyServiceListener
            implements BluetoothProfile.ServiceListener {
        private BluetoothDevice device;

        public MyServiceListener(BluetoothDevice device) {

            this.device = device;
        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            DLog.info("onServiceConnected profile: " + profile);
            mService = (BluetoothInputDevice) proxy;
            connect(device);

        }

        @Override
        public void onServiceDisconnected(int profile) {
            mService = null;
            DLog.info("onServiceDisconnected");
        }
    }


    public boolean connect(BluetoothDevice device) {
        if (mService == null) {
            setIdel(true);
            DLog.info("mService == null");
            return false;
        }
        try {
            Method method1 = mService.getClass().getMethod("setPriority", BluetoothDevice.class, int.class);
            boolean setPriority = (boolean) method1.invoke(mService, device, 100);
            DLog.info("setPriority：" + setPriority);
            Method method = mService.getClass().getMethod("connect", BluetoothDevice.class);
            boolean isConnect = (boolean) method.invoke(mService, device);
            DLog.info("isConnect：" + isConnect);
            setIdel(true);
            return isConnect;
        } catch (Exception e) {
            setIdel(true);
            DLog.error("Fail to connect!!" + e.getMessage());
        }
        return false;
    }

    /**
     * 任务失败
     * @param state
     */
    @Override
    public void onFail(int state) {
        Message msg = handler.obtainMessage(READ_BLOCK_FAIL);
        msg.obj = state;
        handler.sendMessage(msg);
    }

    /**
     * 任务成功
     *
     * @param set
     */
    @Override
    public void onSuccess(TLVSet set) {
        Message msg = handler.obtainMessage(READ_BLOCK_SUCCESS);
        msg.obj = set;
        handler.sendMessage(msg);
    }

    @Override
    public void onDestroy() {
        DLog.info("onDestroy");
        if (timer != null) {
            timer.cancel();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
        }
        if (mPairReceiver != null) {
            unregisterReceiver(mPairReceiver);
        }
        if (mExecutorService != null) {
            mExecutorService.shutdown();
            mExecutorService = null;
        }
        super.onDestroy();
    }
}
