package cn.donica.slcd.ble.check;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

import cn.donica.slcd.ble.entity.Bite;
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
        get_cpu();
        get_temperature();
        get_ntsc_status();
        get_memory_all();
        get_memory_used();
        get_memory_free();
        get_ssd_all();
        get_ssd_free();
        get_ssd_used();
        DLog.warn("test_wifi:" + test_wifi() + " test_audio_chip:" + test_audio_chip() + " test_ntsc:" + test_ntsc() + "  test_lcd:" + test_lcd());
      /* if (test_wifi() && test_audio_chip() && test_ntsc() && test_lcd()) {
            checkSuccess();
        } else {
            checkFail();
        }*/
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
            updateBiteValue("wifi", mount ? "1" : "0");
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
            updateBiteValue("ntsc", mount ? "1" : "0");
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
            updateBiteValue("audio_chip", mount ? "1" : "0");
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
            updateBiteValue("bluetooth", mount ? "1" : "0");
            return mount;
        }
    }

    /**
     * 各个部件状态
     *
     * @param name
     * @param value
     */
    private static void updateBiteValue(String name, String value) {
        try {
            Bite bite = new Bite();
            bite.setValue(value);
            bite.setName(name);
            bite.saveOrUpdate("name=?", name);
        } catch (Exception e) {
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
            updateBiteValue("rfid", mount ? "1" : "0");
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
            updateBiteValue("lcd", mount ? "1" : "0");
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
            updateBiteValue("ntsc_status", isNtscOpen ? "1" : "0");
            return isNtscOpen;
        }
    }

    /**
     * 获取cpu温度
     *
     * @return
     */
    public static void get_temperature() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        String info = null;
        try {
            int ret = hwtest.get_temperature(strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                info = new String(strinfo, 1, len);
            } else {
                info = "0";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            updateBiteValue("temperature", info.trim());
        }
    }

    /**
     * 获取cpu使用率
     *
     * @return
     */
    public static void get_cpu() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        String info = "0";
        try {
            int ret = hwtest.get_cpu(1, strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                info = new String(strinfo, 1, len);
            } else {
                info = "0";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            updateBiteValue("cpu", info.trim());
        }
    }

    /**
     * 总内存
     */
    public static void get_memory_all() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        String info = "0";
        try {
            int ret = hwtest.get_memory(1, strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                info = new String(strinfo, 1, len);
            } else {
                info = "0";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            updateBiteValue("memory_all", info.trim());
        }
    }

    /**
     * 获取已经使用内存
     */
    public static void get_memory_used() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        String info = "0";
        try {
            int ret = hwtest.get_memory(2, strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                info = new String(strinfo, 1, len);
            } else {
                info = "0";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            updateBiteValue("memory_used", info.trim());
        }
    }

    /**
     * 获取还剩余内存
     */
    public static void get_memory_free() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        String info = "0";
        try {
            int ret = hwtest.get_memory(3, strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                info = new String(strinfo, 1, len);
            } else {
                info = "0";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            updateBiteValue("memory_free", info.trim());
        }
    }


    /**
     * 总ssd
     */
    public static void get_ssd_all() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        String info = "0";
        try {
            int ret = hwtest.get_disk_ssd(1, strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                info = new String(strinfo, 1, len);
            } else {
                info = "0";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            updateBiteValue("ssd_all", info.trim());
        }
    }

    /**
     * 获取已经使用ssd
     */
    public static void get_ssd_used() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        String info = "0";
        try {
            int ret = hwtest.get_disk_ssd(2, strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                info = new String(strinfo, 1, len);
            } else {
                info = "0";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            updateBiteValue("ssd_used", info.trim());
        }
    }

    /**
     * 获取还剩余ssd
     */
    public static void get_ssd_free() {
        IHwtestService hwtest = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte strinfo[] = new byte[256];
        String info = "0";
        try {
            int ret = hwtest.get_disk_ssd(3, strinfo);
            if (ret == 0) {
                byte len = strinfo[0];
                info = new String(strinfo, 1, len);
            } else {
                info = "0";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            updateBiteValue("ssd_free", info.trim());
        }
    }
}
