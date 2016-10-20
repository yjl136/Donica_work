package cn.donica.slcd.ble.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;
import cn.donica.slcd.ble.cmd.CmdManage;
import cn.donica.slcd.ble.utils.ClsUtils;
import cn.donica.slcd.ble.utils.Constant;
import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.ble.utils.StringUtil;

/**
 * Created by yejianlin 2016/10/19.
 * 用于检测是否有card靠近
 */
public class DetectService extends Service {
    //测试用的蓝牙mac地址
    private final static String SP_MAC = "68:FB:7E:EE:C7:95";
    private final static String MAC = "DC:2C:26:02:41:2C";
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private SerialPort mSerialPort;
    /**
     * 串口文件描述符
     */
    private final int BUFSIZE = 512;
    private byte[] buf = new byte[BUFSIZE];

    private Timer timer = new Timer();
    /**
     * 写块的字节数组
     */
    private byte[] cmdBytes;
    /**
     * 待写入的16字节数组
     */
    private byte[] writeData = new byte[16];
    /**
     * 收发数据类型，包含以下三种
     */
    private int controlType;
    protected final int READ_CARD_ID = 0;// 读卡号
    protected final int WRITE_BLOCK = 1;// 写块
    protected final int READ_BLOCK = 2;// 读块
    private static final int WHAT_READ = 1;
    private static final int WHAT_WRITE = 2;
    private PairReceiver mPairReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 1:将服务永远运行在后台
     * 2：每隔2秒去读card数据
     * 3：取得card数据，建立蓝牙配对
     */
    @Override
    public void onCreate() {
        super.onCreate();
        DLog.info("onCreate");
        if (open()) {
            //打开串口成功
            this.mInputStream = mSerialPort.getInputStream();
            this.mOutputStream = mSerialPort.getOutputStream();
            setControlType(READ_CARD_ID);
            registerReceiver();
            timer.schedule(task, 0, 2000);
        } else {
            DLog.error("Fail to open " + Constant.devName + "!");
            stopSelf();
        }

    }

    private TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = WHAT_READ;
            handler.sendMessageDelayed(message, 500);

            Message msg = new Message();
            msg.what = WHAT_WRITE;
            handler.sendMessage(msg);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_READ:
                    try {
                        // 查询打开的设备或文件是否有数据可读。如果fd有数据可读，返回1
                        //int retSize = hw.read(devfd, buf, BUFSIZE);
                        int retSize = mInputStream.read(buf);
                        // Log.d(TAG, "read: " + StringUtil.bytes2HexString(StringUtil.subBytes(buf, 0, retSize)));
                        DLog.info("read: " + StringUtil.bytes2HexString(StringUtil.subBytes(buf, 0, retSize)));
                        // 从打开的设备或文件中读取数据。
                        if (retSize > 0) {
                            // 成功则返回读取的字节数，出错返回-1
                            if (buf.length >= 6) {
                                int dataLength = buf[3];
                                int length = dataLength + 6;
                                byte[] data = new byte[length];
                                data = StringUtil.subBytes(buf, 0, length);
                                // 从buf中截取有效数据，数据长度为6 + data字节数组长度
                                if (data[0] == 0x20 && data[length - 1] == 0x03
                                        && data[2] == 0) {
                                    // 开始符和结束符校验，status为0表示函数调用成功
                                    byte temp = 0x00;
                                    // 0异或任何值都为该值本身
                                    for (int i = 0; i < dataLength; i++) {
                                        temp = (byte) (temp ^ data[4 + i]);
                                        // 如果data域有数据则内部进行异或运算
                                    }
                                    byte BCC = (byte) ~(data[1] ^ data[2] ^ data[3] ^ temp);
                                    // 异或取反得到BCC
                                    if (BCC == data[length - 2]) {
                                        // 校验BCC
                                        handleReceive(data);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        DLog.error("Fail to read " + e.getMessage());
                    }
                    break;
                case WHAT_WRITE:
                    try {
                        if (controlType == READ_CARD_ID) {
                            cmdBytes = CmdManage.getReadCardId();
                        } else if (controlType == WRITE_BLOCK) {
                            cmdBytes = CmdManage.getWriteBlock(writeData);
                        } else if (controlType == READ_BLOCK) {
                            cmdBytes = CmdManage.getReadBlock();
                        }
                        DLog.info("write: " + StringUtil.bytes2HexString(cmdBytes));
                        mOutputStream.write(cmdBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                        DLog.error("Fail to write " + e.getMessage());
                    }
                    break;

            }
        }
    };

    private void registerReceiver() {
        mPairReceiver = new PairReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
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

    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    public void setWriteData(byte[] writeData) {
        this.writeData = writeData;
    }

    /**
     * 处理数据
     *
     * @param data
     */
    public void handleReceive(byte[] data) {
        int dataLength = data[3];
        if (dataLength > 0 && data.length > 7) {
            int cardIDLength = data[7];//卡片序列号的长度
            byte[] cardId = new byte[cardIDLength];
            cardId = StringUtil.reverse(StringUtil.subBytes(data, 8, cardIDLength));//逆序输出
            DLog.info("读取到卡号为：\n" + StringUtil.bytes2HexString(cardId));
            pair(MAC);
        }

    }

    /**
     * 获取到蓝牙mac，进行蓝牙配对
     */
    public void pair(String mac) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            DLog.error("Fail to get BluetoothAdapter ");
            stopSelf();
        }
        //如果蓝牙没有打开将蓝牙打开
        if (!adapter.isEnabled()) {

            if (adapter.enable()) {
                DLog.error("open Bluetooth success");
            } else {
                //蓝牙打开失败
                DLog.error("Fail to open Bluetooth");
                return;
                //  stopSelf();
            }
        }
        BluetoothDevice device = adapter.getRemoteDevice(mac);
        pair(device);
    }

    /**
     * 获取到蓝牙mac，进行蓝牙配对
     */
    public void pair(byte[] mac) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            DLog.error("Fail to get BluetoothAdapter ");
            stopSelf();
        }
        //如果蓝牙没有打开将蓝牙打开
        if (!adapter.isEnabled()) {

            if (adapter.enable()) {

            } else {
                //蓝牙打开失败
                DLog.error("Fail to open Bluetooth");
                return;
                //  stopSelf();
            }
        }
        BluetoothDevice device = adapter.getRemoteDevice(mac);
        pair(device);
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
                // 通过工具类ClsUtils,调用createBond方法
                boolean b = ClsUtils.createBond(device.getClass(),
                        device);
                DLog.info("createBond:" + b);
            } catch (Exception e) {
                DLog.error("create bond error " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private class PairReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            DLog.info("----------------------PairReceiver----------------------");
            BluetoothDevice btDevice = null; // 创建一个蓝牙device对象
            // 从Intent中获取设备对象
            btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            try {
                // 1.确认配对
                ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);
            } catch (Exception e) {
                DLog.error("setPairingConfirmation" + e.getMessage());
            }

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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
        super.onDestroy();
    }
}
