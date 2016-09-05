package cn.donica.slcd.polling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import cn.donica.slcd.utils.LogUtil;
import cn.donica.slcd.utils.PollingUtils;

/**
 * 网络状态判断和更改工具
 * Created by liangmingjie on 2016/3/7.
 */
public class WifiReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
            //signal strength changed
        } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//wifi连接上与否
            LogUtil.d(TAG, "网络状态改变 ");
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                PollingUtils.stopPollingService(context, PollingService.class);
                LogUtil.d(TAG, "wifi网络连接断开 ");
            } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //获取当前wifi名称
                LogUtil.d(TAG, "连接到网络 " + wifiInfo.getSSID());
//                PollingUtils.startPollingService(context, 1, PollingService.class);
            }
        } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi打开与否
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                LogUtil.d(TAG, "系统关闭wifi ");
            } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                LogUtil.d(TAG, "系统开启wifi ");
            }
        }
    }
}