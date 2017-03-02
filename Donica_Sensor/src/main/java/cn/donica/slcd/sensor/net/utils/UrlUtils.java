package cn.donica.slcd.sensor.net.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-12-30 09:22
 * Describe:
 */
public class UrlUtils {
    public final static String CLIENT_FIX_KEY = "E7C3F81E00E3904B24586B037EAAE1C0";
    public final static String PRODUCT_ID = "3";
    public final static String ITEM_COUNT = "15";
    public final static String HOST = "http://market.kyd2002.cn/";
    public final static String QUALITY_MAIN_URI = "API/Market/MarketIndex";

    /**
     * 密钥组成     var serKey=”E7C3F81E00E3904B24586B037EAAE1C0”，【固定值】;
     * <p/>
     * 　　 Var clientkey=”客户端定义值”,【由客户端根据机器硬件特性或其他因素决定】;
     * 　　 Var firstkey=MD5(MD5(serKey+”-”+clientkey));
     * 　　 Var lastkey=MD5(serkey+”-”+firstkey);
     * <p/>
     * 最终客户端传递KEY值的格式为:AppKey=clientKey+”-”+lastKey;
     *
     * @return
     */
    public static String getAppKey(Context context) {
        String clientKey = getDeviceInfo(context);
        String serKey = CLIENT_FIX_KEY;
        String firstKey = Md5.stringToMd5(Md5.stringToMd5(serKey + "-" + clientKey));
        String lastKey = Md5.stringToMd5(serKey + "-" + firstKey);
        return clientKey + "-" + lastKey;

    }

    /**
     * 得到机器的特征码，先获取telephoneId，mac，序列号，设置id
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {

        String device = getTelephoneId(context);
        if (TextUtils.isEmpty(device)) {
            //获取Android的序列号
            device = getSerivalNo();
        }
        if (TextUtils.isEmpty(device)) {
            //获取Android的的mac
            device = getWifiMac(context);
        }
        if (TextUtils.isEmpty(device)) {
            device = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return device;
    }

    /**
     * 得到wifi mac地址
     *
     * @param context
     * @return
     */
    private static String getWifiMac(Context context) {
        String device;
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        device = wifi.getConnectionInfo().getMacAddress();
        return device;
    }

    /**
     * @param context
     * @return
     */
    private static String getTelephoneId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取机器的序列号
     *
     * @return
     */
    private static String getSerivalNo() {
        String device = null;
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("get", String.class, String.class);
            device = (String) method.invoke(c, "ro.serialno", "unknow");
        } catch (Exception e) {
        }
        return device;
    }

    public static String getMachineType() {
        return "Kimi i8".replace(" ", "%20");
    }

}
