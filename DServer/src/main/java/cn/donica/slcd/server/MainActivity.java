package cn.donica.slcd.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "DGattServer";
    private BluetoothGattServer server;
    private BluetoothAdapter adapter;
    private BluetoothManager manager;
    private BluetoothLeAdvertiser advertiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = manager.getAdapter();
        advertiser = adapter.getBluetoothLeAdvertiser();
        initGattServer();
        startAdvertising();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAdvertising();
        server.close();
        server = null;
        Log.i(TAG, "onDestroy");

    }

    private void initGattServer() {
        server = manager.openGattServer(this, new BluetoothGattServerCallback() {
            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                super.onConnectionStateChange(device, status, newState);
                Log.i(TAG, "onConnectionStateChange" + " name:" + device.getName() + " newstate:" + newState);
            }
            @Override
            public void onServiceAdded(int status, BluetoothGattService service) {
                super.onServiceAdded(status, service);
                Log.i(TAG, "onServiceAdded  status:" + status);
            }
            @Override
            public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
                boolean send = server.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, new String("test").getBytes());
                Log.i(TAG, "onCharacteristicReadRequest send:" + send + " offset:" + offset + "  ");
            }
            @Override
            public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                Log.i(TAG, "onCharacteristicWriteRequest");
            }
            @Override
            public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
                super.onDescriptorReadRequest(device, requestId, offset, descriptor);
                Log.i(TAG, "onDescriptorReadRequest");

            }
            @Override
            public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
                Log.i(TAG, "onDescriptorWriteRequest");
            }
            @Override
            public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
                super.onExecuteWrite(device, requestId, execute);
                Log.i(TAG, "onExecuteWrite");
            }
            @Override
            public void onNotificationSent(BluetoothDevice device, int status) {
                super.onNotificationSent(device, status);
                Log.i(TAG, "onNotificationSent");
            }
            @Override
            public void onMtuChanged(BluetoothDevice device, int mtu) {
                super.onMtuChanged(device, mtu);
                Log.i(TAG, "onMtuChanged");
            }
        });

        addService();
        printService();
    }

    /**
     * 输出service
     */
    private void printService() {
        List<BluetoothGattService> services = server.getServices();
        for (BluetoothGattService service : services) {
            String suuid = service.getUuid().toString();
            int type = service.getType();
            Log.i(TAG, "-------------" + suuid + "---------------" + type + "------------------------");
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                String cuuid = characteristic.getUuid().toString();
                Log.i(TAG, "cuuid:" + cuuid);
                List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    String duuid = descriptor.getUuid().toString();
                    String desc = descriptor.toString();
                    Log.i(TAG, "duuid:" + duuid + " desc:" + desc);
                }
            }
            Log.i(TAG, "---------------------------------" + "end" + "----------------------------------");
        }
    }


    private void addService() {
        final String SERVICE_DEVICE_INFORMATION = "0000188ef-0000-1000-8000-00805f9b34fb";
        final String SOFTWARE_REVISION_STRING = "00002130-0000-1000-8000-00805f9b34fb";

        server.clearServices();

        BluetoothGattCharacteristic softwareVerCharacteristic = new BluetoothGattCharacteristic(
                UUID.fromString(SOFTWARE_REVISION_STRING),
                BluetoothGattCharacteristic.PROPERTY_READ
                        | BluetoothGattCharacteristic.PROPERTY_NOTIFY
                        | BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_READ
                        | BluetoothGattCharacteristic.PERMISSION_WRITE
        );
        BluetoothGattService deviceInfoService = new BluetoothGattService(
                UUID.fromString(SERVICE_DEVICE_INFORMATION),
                BluetoothGattService.SERVICE_TYPE_PRIMARY);
        softwareVerCharacteristic.setValue(new String("12138").getBytes());
        deviceInfoService.addCharacteristic(softwareVerCharacteristic);
        server.addService(deviceInfoService);
    }

    private AdvertiseSettings buildAdvertiseSettings() {
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setConnectable(true);
        settingsBuilder.setTimeout(0);
        return settingsBuilder.build();
    }


    private AdvertiseData buildAdvertiseData() {
        final String SERVICE_DEVICE_INFORMATION = "0000180b-0000-1000-8000-00805f9b34fb";
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addServiceUuid(ParcelUuid.fromString(SERVICE_DEVICE_INFORMATION));
        dataBuilder.setIncludeTxPowerLevel(true);
        dataBuilder.setIncludeDeviceName(true);
        return dataBuilder.build();
    }

    private AdvertiseCallback callback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            // initGattServer();
            Log.i(TAG, "onStartSuccess");
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.i(TAG, "onStartFailure code:" + errorCode);
        }

    };


    private void startAdvertising() {
        AdvertiseSettings settings = buildAdvertiseSettings();
        AdvertiseData packet = buildAdvertiseData();
        advertiser.startAdvertising(settings, packet, callback);
    }

    private void stopAdvertising() {
        advertiser.stopAdvertising(callback);
    }
}
