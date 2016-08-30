package cn.donica.slcd.dmanager.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IHwtestService;
import android.os.RemoteException;

import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/24.
 */
public class WifiUtil {
    private IHwtestService hwtestService = null;

    /**
     * 发送告警信息
     *
     * @param level      告警信息级别，有i,w,e三种.其中i为info(普通信息)，w为warn（警告信息），e为error（错误信息）
     * @param logContent 告警信息的内容
     * @param location   告警信息的触发代码位置
     * @throws IOException
     */
    public void sendWarn(String level, String logContent, String location) throws IOException {
        LogWarnTrap logWarnTrap = new LogWarnTrap();
        logWarnTrap.sendWarn(level, 1, 1, "WIFI info:" + logContent, location);
    }


    /**
     * 检测wifi芯片是否挂载成功,由研华API提供
     * 0：相关设备挂载成功或已经ready
     * 非0：相关设备未挂载或者准备好
     *
     * @throws RemoteException
     */
    public String test_wifi() throws RemoteException {
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.test_wifi(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        return info;
    }

    /**
     * 检测wifi芯片是否挂载成功
     *
     * @return
     * @throws RemoteException
     */
    public boolean get_wifi_status() throws RemoteException {
        if ((test_wifi()).equals("0")) {
            return true;
        }
        return false;
    }

    /**
     * When WiFi is connected, return the wireless network's IP address
     *
     * @param context context of android system
     * @return WiFi network's IP address
     */

    public static String getWiFiIPAddress(Context context) {
        int ipAddress = getWiFiInfo(context).getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
    }

    /**
     * Get the device's MAC address
     *
     * @param context context of android system
     * @return MAC address
     */
    public static String getLocalMacAddress(Context context) {
        return getWiFiInfo(context).getMacAddress();
    }

    private static WifiInfo getWiFiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }

    /**
     * 开机自检项目
     */
    public void bootCheck() {

    }

    /**
     * 网络开启后自检项目
     */
    public void netWorkCheck() {

    }
}
