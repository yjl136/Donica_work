package cn.donica.slcd.ble.check;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

import cn.donica.slcd.ble.utils.DLog;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-11 08:54
 * Describe:多尼卡自检类
 */

public class SlcdTest {
    /**
     * 自检，自检项有蓝牙，wifi，ntsc，lcd，rfid，audiochip
     *
     * @return
     */
    public static void selfCheck(Context context) {
        //自检前先开启wifi，蓝牙。要不然一定自检失败
        // beforeCheck(context);
        DLog.warn("test_bluetooth:" + test_bluetooth() + "  test_wifi:" + test_wifi() + " test_audio_chip:" + test_audio_chip() + " test_ntsc:" + test_ntsc() + "  test_lcd:" + test_lcd());
        if (test_bluetooth() && test_wifi() && test_audio_chip() && test_ntsc() && test_lcd()) {
            checkSuccess();
        } else {
            checkFail();
        }
    }

    public static void beforeCheck(Context context) {
        //打开蓝牙
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                if (adapter.enable()) {
                    DLog.info("open Bluetooth success");
                } else {
                    DLog.warn("Fail to open Bluetooth");
                }
            }
        } else {
            DLog.info("BluetoothAdapter==null");
        }

        //开启wifi
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
                DLog.info("open wifi success");
            } else {
            }
        } else {
            DLog.info("WifiManager==null");
        }

    }

    /**
     * 自检成功,usb和电源led灯变为绿色
     */
    private static void checkSuccess() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        if (hwtest != null) {
            try {
                hwtest.set_led(1, 0);
                hwtest.set_led(2, 0);
                hwtest.set_led(3, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            DLog.warn("hwtest==null");
        }
    }

    /**
     * 自检失败，usb灯为绿色，电源灯变为黄色
     */
    private static void checkFail() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        if (hwtest != null) {
            try {
                hwtest.set_led(1, 1);
                hwtest.set_led(2, 0);
                hwtest.set_led(3, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            DLog.warn("hwtest==null");
        }
    }

    /**
     * wifi设备是否挂载
     *
     * @return
     */
    private static boolean test_wifi() {
        boolean mount = false;
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        try {
            byte strinfo[] = new byte[256];
            byte ret = hwtest.test_wifi(strinfo);
            if (ret == 0) {
                mount = true;
            } else {
                mount = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            mount = false;
        } finally {
            return mount;
        }
    }

    /**
     * ntsc设备是否挂载
     *
     * @return
     */
    private static boolean test_ntsc() {
        boolean mount = false;
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        try {
            byte strinfo[] = new byte[256];
            byte ret = hwtest.test_ntsc(strinfo);
            if (ret == 0) {
                mount = true;
            } else {
                mount = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            mount = false;
        } finally {
            return mount;
        }
    }

    /**
     * 音频设备是否挂载
     *
     * @return
     */
    private static boolean test_audio_chip() {
        boolean mount = false;
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        try {
            byte strinfo[] = new byte[256];
            byte ret = hwtest.test_audio_chip(strinfo);
            if (ret == 0) {
                mount = true;
            } else {
                mount = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            mount = false;
        } finally {
            return mount;
        }
    }

    /**
     * 蓝牙设备是否挂载
     */
    private static boolean test_bluetooth() {
        boolean mount = false;
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        try {
            byte strinfo[] = new byte[256];
            byte ret = hwtest.test_bluetooth(strinfo);
            if (ret == 0) {
                mount = true;
            } else {
                mount = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            mount = false;
        } finally {
            return mount;
        }
    }

    /**
     * rfid设备是否挂载
     */
    private static boolean test_rfid() {
        boolean mount = false;
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        try {
            byte strinfo[] = new byte[256];
            byte ret = hwtest.test_rfid(1, strinfo);
            if (ret == 0) {
                mount = true;
            } else {
                mount = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            mount = false;
        } finally {
            return mount;
        }
    }

    /**
     * lcd是否点亮
     */
    private static boolean test_lcd() {
        boolean mount = false;
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        try {
            byte strinfo[] = new byte[256];
            byte ret = hwtest.test_lcd(strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                String info = new String(strinfo, 1, len);
                if ("0".equals(info.trim())) {
                    mount = true;
                } else {
                    mount = false;
                }
            } else {
                mount = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            mount = false;
        } finally {
            return mount;
        }
    }


    /**
     * 获得7282m的连接状态
     *
     * @throws RemoteException
     */
    public static boolean get_ntsc_status() {
        boolean isNtscOpen = false;
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        try {
            byte ret = hwtest.get_ntsc_status(strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                String info = new String(strinfo, 1, len);
                if (info.contains("1")) {
                    isNtscOpen = true;
                } else {
                    isNtscOpen = false;
                }
            } else {
                isNtscOpen = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            isNtscOpen = false;
        } finally {
            return isNtscOpen;
        }

    }

}
