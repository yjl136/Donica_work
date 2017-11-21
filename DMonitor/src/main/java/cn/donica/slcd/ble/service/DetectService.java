package cn.donica.slcd.ble.service;

import android.app.ActivityManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothInputDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Handler;
import android.os.IBinder;
import android.os.IHwtestService;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialPort;
import cn.donica.slcd.ble.activity.PaActivity;
import cn.donica.slcd.ble.activity.VaActivity;
import cn.donica.slcd.ble.check.SlcdTest;
import cn.donica.slcd.ble.cmd.CmdManage;
import cn.donica.slcd.ble.entity.Monitor;
import cn.donica.slcd.ble.ota.OTAManager;
import cn.donica.slcd.ble.receiver.USBReceiver;
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
import cn.donica.slcd.ble.utils.XmlUtils;
import cn.donica.slcd.shell.Shell;

import static cn.donica.slcd.ble.check.SlcdTest.get_ntsc_status;

/**
 * Created by yejianlin 2016/10/19.
 */
public class DetectService extends Service implements AsyncTask.IStatus {
    //是否正在播放va
    private final static int DELAY_TIME = 4000;
    private final static String PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    private final static String ACTION_PA = "cn.donica.slcd.action.PA";
    private final static String ACTION_VA = "cn.donica.slcd.action.VA";
    private final static String SSID = "wifi-slcd";
    private final static Uri SEATBACK_URI = Uri.parse("content://cn.donica.slcd.provider");
    private final static String PA_KEY = "pa";
    private final static String VA_KEY = "va";
    private final static String EXTRA_KEY = "FINISH_ONTOUCH_OUTSIDE";
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private SerialPort mSerialPort;
    private boolean isIdel;
    private BluetoothAdapter adapter;
    private final int BUFSIZE = 512;
    private Timer timer = new Timer();
    private final int DELAY_REMOVE_CMD = 0x102;
    private final int READ_CARD_CMD = 0x100;
    private final int READ_CARD = 0x200;
    protected final int READ_BLOCK_SUCCESS = 0x101;
    protected final int READ_BLOCK_FAIL = 0x103;
    protected final int SHOW_PA_VIEW = 0x104;
    protected final int REMOVE_PA_VIEW = 0x105;
    protected final int SHOW_VA_VIEW = 0x106;
    protected final int REMOVE_VA_VIEW = 0x107;
    private PairReceiver mPairReceiver;
    private ExecutorService mExecutorService;
    private WifiManager mWifiManager;
    private SeatBackObserver mSeatBackObserver;
    private USBReceiver mUSBReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        initDB();
        initWifi();
        initLCD(true);
        //国航需要将led灯关闭
        initLed();
        //自检
        initTest();
        mSeatBackObserver = new SeatBackObserver();
        getContentResolver().registerContentObserver(SEATBACK_URI, true, mSeatBackObserver);
        //监听5000端口，获取PA状态
        initPA();
        //开启Va轮询
        initVa();
        //设置休眠时间
        setScreenOffTime(5 * 60 * 1000);
        //initNFC();
        initUsbReceiver();
        //清楚Cache分区下的update.zip
        initCache();
    }

    /**
     * 如果存在update.zip ，则删除，防止下次升级报错
     */
    private void initCache() {
        String filePath = "/cache/update.zip";
        File file = new File(filePath);
        if (file.exists()) {
            boolean delete = file.delete();
            DLog.info("delete:" + delete);
        }
    }

    private void initUsbReceiver() {
        mUSBReceiver = new USBReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filter.addDataScheme("file");
        registerReceiver(mUSBReceiver, filter);
    }

    /**
     * 开启wifi
     */
    private void initWifi() {
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            if (!mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(true);
            }
        }
        timer.schedule(wifiTask, 20000, 30000);
    }

    private TimerTask wifiTask = new TimerTask() {
        @Override
        public void run() {
            if (!connectSSID()) {
                return;
            }
            if (OTAManager.checkVersion(DetectService.this)) {
                if (!isTopActivity("com.fsl.android.ota.OtaAppActivity")) {
                    saveMonitor("system_upgrade", 1);
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.fsl.android.ota", "com.fsl.android.ota.OtaAppActivity"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(EXTRA_KEY, false);
                    startActivity(intent);
                } else {
                    DLog.info("isTopActivity....");
                }
            } else {
                DLog.info("Server can not connect or system version is up to date");
            }
        }
    };


    /*
     * @param className
     * @return
     */
    private boolean isTopActivity(String className) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(className)) {
            isTop = true;
        }
        return isTop;
    }

    /**
     * 周围是否存在指定的ssid
     *
     * @return
     */
    private boolean isExistSSid(String ssid) {
        DLog.info("isExistSSid");
        boolean isExist = false;
        List<ScanResult> mScanResults = mWifiManager.getScanResults();
        for (ScanResult result : mScanResults) {
            DLog.info("ssid:" + result.SSID);
            if (ssid.equals(result.SSID)) {
                DLog.info("true");
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * 连接指定的ssid
     *
     * @return
     */
    private boolean connectSSID() {
        //判断是否有指定ssid
        DLog.info("isExistSSid:" + isExistSSid(SSID));
        if (!isExistSSid(SSID)) {
            return false;
        }
        String ssid = "\"" + SSID + "\"";
        String connectedSSid = mWifiManager.getConnectionInfo().getSSID();
        DLog.info("connectedSSid:" + connectedSSid);
        if (ssid.equals(connectedSSid)) {
            return true;
        }

        WifiConfiguration cfg = new WifiConfiguration();
        cfg.allowedAuthAlgorithms.clear();
        cfg.allowedGroupCiphers.clear();
        cfg.allowedKeyManagement.clear();
        cfg.allowedPairwiseCiphers.clear();
        cfg.allowedProtocols.clear();
        cfg.SSID = ssid;
        cfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int netWorkID = mWifiManager.addNetwork(cfg);
        boolean enableNetwork = mWifiManager.enableNetwork(netWorkID, true);
        DLog.info("enableNetwork:" + enableNetwork + " netWorkID" + netWorkID);
        return enableNetwork;
    }


    /**
     * 初始化NFC读取相关
     */
    private void initNFC() {
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
            timer.schedule(task, 0, 500);
        } else {
            DLog.warn("Fail to open " + Constant.devName + "!");
            stopSelf();
        }
    }

    private void initDB() {
        Connector.getDatabase();
        saveMonitor("va", 0);
        saveMonitor("pa", 0);
        saveMonitor("system_upgrade", 0);
        saveMonitor("program_upgrade", 0);
        saveMonitor("debug", 0);
    }

    //每隔1000毫秒去查询Va状态
    private TimerTask VaTask = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent();
            if (get_ntsc_status()) {
                intent.putExtra(VA_KEY, 1);
                saveMonitor("va", 1);
                handler.sendEmptyMessage(SHOW_VA_VIEW);
            } else {
                intent.putExtra(VA_KEY, 0);
                saveMonitor("va", 0);
                handler.sendEmptyMessage(REMOVE_VA_VIEW);
            }
            intent.setAction(ACTION_VA);
            UserHandle uh = getUserHandle();
            if (uh != null) {
                sendBroadcastAsUser(intent, uh);
            } else {
                sendBroadcast(intent);
            }

        }
    };

    private UserHandle getUserHandle() {
        UserHandle uh = null;
        try {
            Class clazz = UserHandle.class;
            Field field = clazz.getField("ALL");
            uh = (UserHandle) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return uh;
        }
    }

    private TimerTask checkTask = new TimerTask() {
        @Override
        public void run() {
            DLog.info("self-----Test");
            SlcdTest.selfCheck(DetectService.this);
        }
    };

    /**
     * 设置休眠时间
     */
    private void setScreenOffTime(int time) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    time);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 保存va 、pa状态
     *
     * @param name
     * @param value
     */
    private void saveMonitor(String name, int value) {
        try {
            Monitor monitor = new Monitor();
            monitor.setValue(value);
            monitor.setName(name);
            monitor.saveOrUpdate("name=?", name);
        } catch (Exception e) {

        }
    }


    /**
     * 执行命令
     *
     * @param cmd
     */
    private void executeCMD(String cmd) {
        List<String> result = Shell.SU.run(cmd);
        if (result != null) {
            for (String line : result) {
                DLog.info(line);
            }
        }
    }

    /**
     * 查询Va状态
     */
    private void initVa() {
        timer.schedule(VaTask, 0, 200);
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
                    DLog.info("socket==null");
                    return;
                }
                while (true) {
                    byte[] buffer = null;
                    DatagramPacket packet = null;
                    try {
                        buffer = new byte[BUFSIZE];
                        packet = new DatagramPacket(buffer, BUFSIZE);
                        DLog.info("wait data!!");
                        socket.receive(packet);
                        int len = packet.getLength();
                        byte[] content = packet.getData();
                        DLog.info("content[43]:" + StringUtil.byte2Hex(content[43]) + "  content[44]:" + StringUtil.byte2Hex(content[44]));
                        Intent intent = new Intent();
                        if (content != null && len > 45 && ("31".equals(StringUtil.byte2Hex(content[43])) || "31".equals(StringUtil.byte2Hex(content[44])))) {
                            DLog.warn("PA");
                            intent.putExtra(PA_KEY, 1);
                            saveMonitor("pa", 1);
                            handler.sendEmptyMessage(SHOW_PA_VIEW);
                        } else {
                            DLog.warn("No PA");
                            intent.putExtra(PA_KEY, 0);
                            saveMonitor("pa", 0);
                            handler.sendEmptyMessage(REMOVE_PA_VIEW);
                        }
                        intent.setAction(ACTION_PA);
                        UserHandle uh = getUserHandle();
                        if (uh != null) {
                            DLog.warn("uh != null");
                            sendBroadcastAsUser(intent, uh);
                        } else {
                            sendBroadcast(intent);
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

    /**
     * 针对国航10.1寸屏改装
     */
    private void initLCD(final boolean delay) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //延迟10s，等待sata挂载
                if (delay) {
                    SystemClock.sleep(8000);
                }
                String cmd = "";
                if (isSeatBack()) {
                    cmd = "wm size 1220x800";
                } else {
                    cmd = "wm size 1280x800";
                }
                executeCMD(cmd);
            }
        }).start();
    }

    /**
     * 判断是否安装在椅背
     *
     * @return
     */
    private boolean isSeatBack() {
      /*  Monitor monitor = DataSupport.where("name=?", "seatback").findFirst(Monitor.class);
        if (monitor != null && monitor.getValue() == 1) {
            return true;
        }*/

        DLog.info("XmlUtils.getSeatBack():" + XmlUtils.getSeatBack());
        if (XmlUtils.getSeatBack() == 1) {

            return true;
        }
        return false;
    }

    /**
     * 判断是否正在Pa
     *
     * @return
     */
    private boolean isPa() {
        Monitor monitor = DataSupport.where("name=?", "pa").findFirst(Monitor.class);
        if (monitor != null && monitor.getValue() == 1) {
            return true;
        }
        return false;
    }

    /**
     * 自检，开机自检，定时自检
     */
    private void initTest() {
        timer.schedule(checkTask, 10000, 60000);
    }

    private void initLed() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        if (hwtest != null) {
            try {
                hwtest.set_led(1, 0);
                hwtest.set_led(2, 1);
                hwtest.set_led(3, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            DLog.warn("hwtest==null");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private TimerTask task = new TimerTask() {
        public void run() {
            if (isIdel) {
                Message message = new Message();
                message.what = READ_CARD;
                handler.sendMessageDelayed(message, 85);
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
            Intent intent = null;
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
                case SHOW_PA_VIEW:
                    intent = new Intent(DetectService.this, PaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DetectService.this.startActivity(intent);
                    break;
                case REMOVE_PA_VIEW:
                    break;
                case SHOW_VA_VIEW:
                    if (!isPa()) {
                        intent = new Intent(DetectService.this, VaActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        DetectService.this.startActivity(intent);
                    }
                    break;
                case REMOVE_VA_VIEW:
                    break;
            }
        }
    };

    private class SeatBackObserver extends ContentObserver {
        public SeatBackObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            DLog.info("onChange");
            initLCD(false);
        }

    }

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

    public void setIdel(final boolean isIdel) {
        if (isIdel) {
            //是空闲延迟设置idel
            if (FloatWindowManager.isFloating()) {
                handler.sendEmptyMessageDelayed(DELAY_REMOVE_CMD, DELAY_TIME);
                //设置延迟
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DetectService.this.isIdel = isIdel;
                    }
                }, DELAY_TIME);
            } else {
                this.isIdel = isIdel;
            }
        } else {
            //不是空闲
            this.isIdel = isIdel;
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
            FloatWindowManager.updateFloatView("Ndef数据不完整");
            setIdel(true);
            DLog.error("init NdnfMessage error  " + e.getMessage());
        }
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
                if (!isBond) {
                    FloatWindowManager.updateFloatView("请重新读取。。。。");
                    setIdel(true);
                }
            } catch (Exception e) {
                FloatWindowManager.updateFloatView("蓝牙配对失败。。。。");
                setIdel(true);
                DLog.error("create bond error " + e.getMessage());
                e.printStackTrace();
            }
        } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            FloatWindowManager.updateFloatView("蓝牙配对成功！！");
            enable();
            setIdel(true);
            boolean isGetProxy = adapter.getProfileProxy(this, new MyServiceListener(device), 4);
            DLog.info("getProfileProxy:" + isGetProxy);
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
     *
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

    /**
     * 启动自己
     */
    public void startSelf() {
        Intent intent = new Intent(this, DetectService.class);
        startService(intent);
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
        if (mSeatBackObserver != null) {
            getContentResolver().unregisterContentObserver(mSeatBackObserver);
        }
        if (mUSBReceiver != null) {
            unregisterReceiver(mUSBReceiver);
        }
        startSelf();
        super.onDestroy();
    }
}
