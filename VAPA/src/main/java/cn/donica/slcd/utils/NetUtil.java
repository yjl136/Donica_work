package cn.donica.slcd.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 网络状态判断和更改工具
 * Created by liangmingjie on 2016/3/7.
 */
public class NetUtil {

    /**
     * 判断Network是否开启(包括移动网络和wifi)
     *
     * @param mContext 上下文环境
     * @return 判断结果
     */
    public static boolean isNetworkEnabled(Context mContext) {
        return (isNetEnabled(mContext) || isWIFIEnabled(mContext));
    }


    /**
     * 判断Network是否连接成功(包括移动网络和wifi)
     *
     * @param mContext 上下文环境
     * @return 判断结果
     */
    public static boolean isNetworkConnected(Context mContext) {
        return (isWifiContected(mContext) || isNetContected(mContext));
    }

    /**
     * 判断移动网络是否开启
     *
     * @return
     */
    public static boolean isNetEnabled(Context context) {
        boolean enable = false;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                enable = true;
                Log.i(Thread.currentThread().getName(), "isNetEnabled");
            }
        }
        return enable;
    }

    /**
     * 判断wifi是否开启
     *
     * @param context 上下文环境
     */
    public static boolean isWIFIEnabled(Context context) {
        boolean enable = false;
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            enable = true;
            Log.i(Thread.currentThread().getName(), "isWifiEnabled");
        }

        Log.i(Thread.currentThread().getName(), "isWifiDisabled");
        return enable;
    }

    /**
     * 判断移动网络是否连接成功！
     *
     * @param context 上下文环境
     * @return 判断结果
     */
    public static boolean isNetContected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetworkInfo.isConnected()) {

            Log.i(Thread.currentThread().getName(), "isNetContected");
            return true;
        }
        Log.i(Thread.currentThread().getName(), "isNetDisconnected");
        return false;

    }

    /**
     * 判断wifi是否连接成功
     *
     * @param context 上下文环境
     * @return 判断结果
     */
    public static boolean isWifiContected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {

            Log.i(Thread.currentThread().getName(), "isWifiContected");
            return true;
        }
        Log.i(Thread.currentThread().getName(), "isWifiDisconnected");
        return false;
    }

    /**
     * 开启或关闭移动网络
     *
     * @param context 上下文环境
     * @param enabled 是否开启
     */
    public static void setMobileDataStatus(Context context, boolean enabled) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // ConnectivityManager类
        Class<?> conMgrClass;
        // ConnectivityManager类中的字段
        Field iConMgrField;
        // IConnectivityManager类的引用
        Object iConMgr;
        // IConnectivityManager类
        Class<?> iConMgrClass;
        // setMobileDataEnabled方法
        Method setMobileDataEnabledMethod;
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            // 取得ConnectivityManager类中的对象Mservice
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            // 设置setMobileDataEnabled方法是否可访问
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启wifi
     *
     * @param context 上下文环境
     * @param isenble 设置状态
     */
    public static void setWIFI(Context context, boolean isenble) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(isenble);
    }


}