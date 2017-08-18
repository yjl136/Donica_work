package cn.donica.slcd.controler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-08-11 17:26
 * Describe:
 */

public class DevicesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final String TAG = "DControler";
    private final String MAC_EXTRA = "mac_extra";
    private final String NAME_EXTRA = "name_extra";
    private ListView devicesLv;
    private List<Device> devices;
    private BluetoothAdapter adapter;
    private BluetoothLeScanner scanner;
    private DeviceAdapter deviceAdapter;
    private ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice bluetoothDevice = result.getDevice();
            String name = bluetoothDevice.getName();
            String mac = bluetoothDevice.getAddress();
            Device device = new Device(name, mac);
            if (!isInArray(devices, mac)) {
                devices.add(device);
                deviceAdapter.notifyDataSetChanged();
            }
            Log.i(TAG, "onScanResult device name:" + bluetoothDevice.getName() + " device mac:" + bluetoothDevice.getAddress());
        }


        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.i(TAG, "onBatchScanResults size:" + results.size());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i(TAG, "onScanFailed error:" + errorCode);
        }
    };
    private BluetoothAdapter.LeScanCallback leCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            String name = bluetoothDevice.getName();
            String mac = bluetoothDevice.getAddress();
            Device device = new Device(name, mac);
            if (!isInArray(devices, mac)) {
                devices.add(device);
                deviceAdapter.notifyDataSetChanged();
            }
            Log.i(TAG, "onScanResult device name:" + bluetoothDevice.getName() + " device mac:" + bluetoothDevice.getAddress());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        adapter = BluetoothAdapter.getDefaultAdapter();
        devices = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(this, devices);
        devicesLv = (ListView) findViewById(R.id.lv);
        devicesLv.setOnItemClickListener(this);
        devicesLv.setAdapter(deviceAdapter);
        adapter.startLeScan(leCallback);
        //scanner = adapter.getBluetoothLeScanner();
        // scanner.startScan(callback);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.stopLeScan(leCallback);
                // scanner.stopScan(callback);
            }
        }, 8000);
    }

    @Override
    protected void onDestroy() {
        adapter.stopLeScan(leCallback);
        //   scanner.stopScan(callback);
        super.onDestroy();
    }

    /**
     * 判断mac地址是否在队列里面
     *
     * @param devices
     * @param mac
     * @return
     */
    private boolean isInArray(List<Device> devices, String mac) {
        boolean isInArray = false;
        for (Device device : devices) {
            if (mac.equals(device.getMac())) {
                isInArray = true;
                break;
            }
        }
        return isInArray;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        Device device = devices.get(position);
        String mac = device.getMac();
        String name = device.getName();
        if (TextUtils.isEmpty(name)) {
            name = "-null-";
        }
        intent.putExtra(MAC_EXTRA, mac);
        intent.putExtra(NAME_EXTRA, name);
        startActivity(intent);
    }
}
