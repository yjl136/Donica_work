package cn.donica.slcd.controler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothInputDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.R.attr.start;
import static java.security.CryptoPrimitive.MAC;

public class MainActivity extends AppCompatActivity {
    private final String MAC_EXTRA = "mac_extra";
    private final String NAME_EXTRA = "name_extra";
    private final static String TAG = "GattClient";
    public static final int INPUT_DEVICE = 4;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothInputDevice hid;
    private BluetoothGatt gatt;
    private String mac;
    private String name;
    private TextView nameTv;
    private TextView macTv;
    private TextView connectTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        mac = intent.getStringExtra(MAC_EXTRA);
        name = intent.getStringExtra(NAME_EXTRA);
        macTv = (TextView) findViewById(R.id.macTv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        connectTv = (TextView) findViewById(R.id.connectTv);
        nameTv.setText(name);
        macTv.setText(mac);
        connectTv.setText("connecting");
        initAdapter();
        BluetoothDevice device = adapter.getRemoteDevice(mac);
        //adapter.getProfileProxy(this,new HidListener(),INPUT_DEVICE);
        gatt = device.connectGatt(this, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
            /*    if(newState==BluetoothProfile.STATE_DISCONNECTED){
                    connectTv.setText("disconnected");
                }else if(newState==BluetoothProfile.STATE_CONNECTED){
                    connectTv.setText("connected");
                }else {
                    connectTv.setText(""+newState);
                }*/
                Log.i(TAG, "onConnectionStateChange" + " status:" + status + "  newstate:" + newState + "  newstate:" + newState);
                gatt.discoverServices();

            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                Log.i(TAG, "onServicesDiscovered" + " status:" + status);
                List<BluetoothGattService> services = gatt.getServices();
                for (BluetoothGattService service : services) {
                    String suuid = service.getUuid().toString();
                    int type = service.getType();
                    Log.i(TAG, "-------------" + suuid + "---------------" + type + "------------------------");
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        String cuuid = characteristic.getUuid().toString();
                        if ("0000188e-0000-1000-8000-00805f9b34fb".equals(suuid)) {
                            gatt.readCharacteristic(characteristic);
                        }
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

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                Log.i(TAG, "onCharacteristicRead values:" + new String(characteristic.getValue()));

            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                Log.i(TAG, "onCharacteristicWrite values:" + new String(characteristic.getValue()));
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.i(TAG, "onCharacteristicChanged value:" + new String(characteristic.getValue()));
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
                String uuid = descriptor.getUuid().toString();

                Log.i(TAG, "onDescriptorRead  uuid:" + uuid);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                Log.i(TAG, "onDescriptorWrite");
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
                Log.i(TAG, "onReliableWriteCompleted");
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
                Log.i(TAG, "onReadRemoteRssi");
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
                Log.i(TAG, "onMtuChanged");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gatt.disconnect();
        gatt.close();
        gatt = null;
        Log.i(TAG, "onDestroy");
    }

    private void initAdapter() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        Iterator<BluetoothDevice> iterator = devices.iterator();
        while (iterator.hasNext()) {
            BluetoothDevice device = iterator.next();
            ParcelUuid[] uuids = device.getUuids();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < uuids.length; i++) {
                ParcelUuid uuid = uuids[i];
                String id = uuid.getUuid().toString();
                builder.append(id + "\n");
            }
            Log.i(TAG, "name:" + device.getName() + "  mac:" + device.getAddress() + "  uuids:" + builder.toString());

        }
    }

    public class HidListener implements BluetoothProfile.ServiceListener {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            try {
                hid = (BluetoothInputDevice) proxy;
                BluetoothDevice device = adapter.getRemoteDevice(mac);
                boolean connect = hid.connect(device);
                Log.i(TAG, "connect:" + connect);
                /* Method method1 = hid.getClass().getMethod("setPriority", BluetoothDevice.class, int.class);
                boolean setPriority = (boolean) method1.invoke(hid, device, 100);
                Method method = hid.getClass().getMethod("connect", BluetoothDevice.class);
                boolean isConnect = (boolean) method.invoke(hid, device);
                Log.i(TAG, "connect:" + isConnect + "  priority:" + setPriority);
                boolean send = hid.sendData(device, "adb shell input keyevent 4");
                Log.i(TAG, "send:" + send);
                int state = hid.getConnectionState(device);
                Log.i(TAG, "state:" + state);*/
             /*   List<BluetoothDevice> devices =hid.getConnectedDevices();
                for(BluetoothDevice device1 : devices){
                    Log.i(TAG,"name:"+device1.getName()+"  mac:"+device1.getAddress());
                }*/
            } catch (Exception e) {

            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            hid = null;
        }
    }

}
