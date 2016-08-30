/**
 * File Name: ConnectivityBroadcastReceiver.java
 * Description：
 * Author: Luke Huang
 * Create Time: 2015-7-27
 * <p>
 * For the SLCD Project
 * Copyright © 2015 Donica.cn All rights reserved
 */
package cn.donica.slcd.dmanager.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.widget.Toast;

import cn.donica.slcd.dmanager.R;
import cn.donica.slcd.dmanager.service.BootService;

/**
 * Description:	Handle event when wifi/network state is changed
 *
 * @author Luke Huang 2015-7-27
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver {
    /**
     * (non-Javadoc)
     *
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            onWiFiStateChanged(context, intent);
        } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            onNetworkStateChanged(context, intent);
        }
    }

    /**
     * Description: handle event when wifi state changed
     *
     * @param context
     */
    private void onWiFiStateChanged(Context context, Intent intent) {
        // WifiManager wifiManager = context.getSystemService(Context.WIFI_SERVICE)
        // int wifiState = wifiManager.getWifiState();
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
        switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                Toast.makeText(context, R.string.wifi_disabled, Toast.LENGTH_LONG).show();
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                Toast.makeText(context, R.string.wifi_enabled, Toast.LENGTH_LONG).show();
                Intent newIntent = new Intent(context, BootService.class);
                context.startService(newIntent);
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                break;
        }
    }

    /**
     * Description: handle event when network state changed
     *
     * @param context
     * @param intent
     */
    private void onNetworkStateChanged(Context context, Intent intent) {
        Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != parcelableExtra) {
            NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
            boolean connected = networkInfo.isAvailable();
            if (connected)
                Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, R.string.wifi_disconnected, Toast.LENGTH_LONG).show();
        }
    }
}
