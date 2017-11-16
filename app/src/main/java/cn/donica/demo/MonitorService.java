package cn.donica.demo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ethernet.EthernetManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-20 14:23
 * Describe:
 */

public class MonitorService extends Service implements OTAServerManager.OTAStateChangeListener {

    private final static String SSID = "wifi-slcd-1";
    private WifiManager mWifiManager;
    private EthernetManager mEthernetManager;
    private Timer timer = new Timer();

    private OTAServerManager mOTAManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED
                && mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
            //开启wifi
            boolean wifiEnable = mWifiManager.setWifiEnabled(true);
            Log.i("MonitorService", "wifiEnable:" + wifiEnable);
        }

        try {
            mOTAManager = new OTAServerManager(this);
        } catch (MalformedURLException e) {
            mOTAManager = null;
            e.printStackTrace();
        }
        mOTAManager.setmListener(this);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isWifiAvaliable()) {
                    mOTAManager.startCheckingVersion();
                }
            }
        }, 100, 1000);
    }


    boolean checkURLOK(URL url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isWifiAvaliable() {
        WifiConfiguration tempConfig = IsExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        WifiConfiguration cfg = new WifiConfiguration();
        cfg.allowedAuthAlgorithms.clear();
        cfg.allowedGroupCiphers.clear();
        cfg.allowedKeyManagement.clear();
        cfg.allowedPairwiseCiphers.clear();
        cfg.allowedProtocols.clear();
        cfg.SSID = "\"" + SSID + "\"";
        cfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int netWorkID = mWifiManager.addNetwork(cfg);
        boolean enableNetwork = mWifiManager.enableNetwork(netWorkID, true);
        Log.i("MonitorService", "enableNetwork:" + enableNetwork + " netWorkID" + netWorkID);
        return enableNetwork;
    }

    @Override
    public void onStateOrProgress(int message, int error, Object info) {

    }

    private class ScanResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResults = mWifiManager.getScanResults();
            for (ScanResult result : scanResults) {
                String ssid = result.SSID;
                String bssid = result.BSSID;
                String capabilities = result.capabilities;
                int level = result.level;
                Log.i("MonitorService", "ssid:" + ssid + "  bssid:" + bssid + "  capabilities" + capabilities + " level:" + level);
                if (SSID.equalsIgnoreCase(ssid.trim())) {
                    //连接指定SSID
                    WifiConfiguration tempConfig = IsExsits(SSID);
                    if (tempConfig != null) {
                        mWifiManager.removeNetwork(tempConfig.networkId);
                    }
                    WifiConfiguration cfg = new WifiConfiguration();
                    cfg.allowedAuthAlgorithms.clear();
                    cfg.allowedGroupCiphers.clear();
                    cfg.allowedKeyManagement.clear();
                    cfg.allowedPairwiseCiphers.clear();
                    cfg.allowedProtocols.clear();
                    cfg.SSID = "\"" + SSID + "\"";
                    cfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    int netWorkID = mWifiManager.addNetwork(cfg);
                    boolean enableNetwork = mWifiManager.enableNetwork(netWorkID, true);
                    Log.i("MonitorService", "enableNetwork:" + enableNetwork + " netWorkID" + netWorkID);
                    if (enableNetwork) {
                    }
                    break;
                }
            }
        }


    }

    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }
}
