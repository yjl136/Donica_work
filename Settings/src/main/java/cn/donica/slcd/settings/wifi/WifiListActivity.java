package cn.donica.slcd.settings.wifi;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.settings.R;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-10 09:12
 * Describe:
 */

public class WifiListActivity extends ListActivity {
    private Button openBt;
    private TextView wifiStatusTv;
    private WifiManager mWifiManager;
    private List<APEntity> aps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);
        openBt = (Button) findViewById(R.id.openBt);
        wifiStatusTv = (TextView) findViewById(R.id.wifiStatusTv);
        openBt.setOnClickListener(clickListener);
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(new ScanResultReceiver(), filter);
        if (!mWifiManager.isWifiEnabled()) {
            wifiStatusTv.setText("要查看wifi，请先打开WLAN");
        } else {
            boolean startSCan = mWifiManager.startScan();
            wifiStatusTv.setVisibility(View.GONE);
        }
        aps = new ArrayList<APEntity>();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(true);
                boolean startSCan = mWifiManager.startScan();
                wifiStatusTv.setVisibility(View.GONE);
            }
        }
    };


    private class ScanResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResults = mWifiManager.getScanResults();
            for (ScanResult result : scanResults) {
                String ssid = result.SSID;
                String bssid = result.BSSID;
                String capabilities = result.capabilities;
                int level = result.level;
                addAP(ssid, bssid, capabilities, level);
                Log.i("MonitorService", "ssid:" + ssid + "  bssid:" + bssid + "  capabilities" + capabilities + " level:" + level);
            }
        }
    }

    /**
     * 将ap添加到aps集合
     *
     * @param ssid
     * @param bssid
     * @param capabilities
     * @param level
     */
    private void addAP(String ssid, String bssid, String capabilities, int level) {
        APEntity ap = new APEntity();
        ap.setBssid(bssid);
        ap.setSsid(ssid);
        ap.setCapabilities(capabilities);
        ap.setLevel(level);
        aps.add(ap);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
