/**
 *
 */
package cn.donica.slcd.dmanager.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * NetworkUtil is a tool for network, it provide functions to get network
 * parameters.
 */
public class NetworkUtil {
    /**
     * Get the first IPv4 address no matter whether network connected
     *
     * @return IPv4 address
     */
    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            Log.e("IP Address", ex.getMessage());
        }
        return null;
    }


    /**
     * 获取WIFI的Mac地址
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        return getWiFiInfo(context).getMacAddress();
    }


    /**
     * 获取WIFI的IP地址
     *
     * @param context
     * @return
     */
    public static String getWiFiIPAddress(Context context) {
        int ipAddress = getWiFiInfo(context).getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
    }


    /**
     * 获取WIFI的信息
     *
     * @param context
     * @return
     */
    private static WifiInfo getWiFiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }
}
