package cn.donica.slcd.settings.wifi;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import cn.donica.slcd.settings.BaseApplication;
import cn.donica.slcd.settings.utils.Config;

public class WiFiService extends Service {
    private static final String TAG = "WiFiService";

    public WiFiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        WifiConnector wifiConnector = new WifiConnector(BaseApplication.getContext(), connectListener);
        wifiConnector.connect(Config.getSSIDName(), Config.getSSIDPassword(), WifiConnector.SecurityMode.WPA2);
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public WifiConnector.WifiConnectListener connectListener = new WifiConnector.WifiConnectListener() {
        @Override
        public void OnWifiConnectCompleted(boolean isConnected) {
            Log.d(TAG, "WIFI连接完成，连接" + isConnected);
        }
    };
}
