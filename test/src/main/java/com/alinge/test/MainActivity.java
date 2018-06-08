package com.alinge.test;

import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothInputDevice;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Instrumentation instrumentation = new Instrumentation();
       adapter = BluetoothAdapter.getDefaultAdapter();

       adapter.getProfileProxy(this,new MyServiceListener(),BluetoothProfile.GATT_SERVER);
    }

    class MyServiceListener implements BluetoothProfile.ServiceListener{


        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.i(TAG,"onServiceConnected profile: "+profile);
            BluetoothInputDevice input = (BluetoothInputDevice)proxy;
           List<BluetoothDevice> devices = input.getConnectedDevices();
           for(BluetoothDevice device : devices){
               Log.i(TAG,"device name:"+device.getName()+"  type:"+device.getType());
           }
        }


        @Override
        public void onServiceDisconnected(int profile) {
            Log.i(TAG,"onServiceDisconnected profile: "+profile);
        }
    }
}
