package cn.donica.slcd.dmanager.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.donica.slcd.dmanager.R;
import cn.donica.slcd.dmanager.adapter.BiteAdapter;

public class BiteActivity extends Activity {
    private static final String TAG = "BiteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bite);

        ListView listView = (ListView) findViewById(R.id.list_view);

        String[] data = getResources().getStringArray(R.array.bite_types);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < data.length; i++) {
            list.add(data[i]);
        }

        final BiteAdapter adapter = new BiteAdapter(this, list);

        // set adapter
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) adapter.getItem(position);
                try {
                    bite(item);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }

                adapter.setSelectedPosition(position);
            }
        });

        // set the first item selected as default
        adapter.setSelectedPosition(0);
    }

    private void bite(String type) throws RemoteException {
        IHwtestService hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));

        byte buffer[] = new byte[256];
        int errorCode = 0;
        StringBuilder builder = new StringBuilder();

        if (getResources().getString(R.string.cpu).equals(type)) {
            hwtestService.get_cpu(1, buffer);
            builder.append("CPU Usage: " + new String(buffer, 1, buffer[0]) + "%").append("\n");
            hwtestService.get_temperature(buffer);
            builder.append("CPU Temperature: " + new String(buffer, 1, buffer[0])).append("\n");
        } else if (getResources().getString(R.string.power).equals(type)) {
//            hwtestService.get_power(0, buffer);
//            builder.append("VCC VoltageUtil: " + new String(buffer, 1, buffer[0]) + "mV").append("\n");
//            hwtestService.get_power(1, buffer);
//            builder.append("V1 VoltageUtil: " + new String(buffer, 1, buffer[0]) + "mV").append("\n");
//            hwtestService.get_power(2, buffer);
//            builder.append("V2 VoltageUtil: " + new String(buffer, 1, buffer[0]) + "mV").append("\n");
//            hwtestService.get_power(3, buffer);
//            builder.append("V3 VoltageUtil: " + new String(buffer, 1, buffer[0]) + "mV").append("\n");
        } else if (getResources().getString(R.string.audio).equals(type)) {
            errorCode = hwtestService.test_audio_chip(buffer);
            builder.append("Test audio chip: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");
            errorCode = hwtestService.test_audio_headset(buffer);
            builder.append("Test audio headset: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");
//            hwtestService.get_audio(buffer);
//            builder.append("Audio Chip: " + new String(buffer, 1, buffer[0])).append("\n");
        } else if (getResources().getString(R.string.memory).equals(type)) {
            hwtestService.get_memory(1, buffer);
            builder.append("Total Memory: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
            hwtestService.get_memory(2, buffer);
            builder.append("Used Memory: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
            hwtestService.get_memory(3, buffer);
            builder.append("Avail Memory: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
        } else if (getResources().getString(R.string.disk).equals(type)) {
            hwtestService.get_disk_mmc(1, buffer);
            builder.append("Total MMC: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
            hwtestService.get_disk_mmc(2, buffer);
            builder.append("Used MMC: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
            hwtestService.get_disk_mmc(3, buffer);
            builder.append("Avail MMC: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
            hwtestService.get_disk_ssd(1, buffer);
            builder.append("Total SSD: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
            hwtestService.get_disk_ssd(2, buffer);
            builder.append("Used SSD: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
            hwtestService.get_disk_ssd(3, buffer);
            builder.append("Avail SSD: " + new String(buffer, 1, buffer[0]) + "KB").append("\n");
        } else if (getResources().getString(R.string.connectivity).equals(type)) {
            errorCode = hwtestService.test_wifi(buffer);
            builder.append("Test WiFi Module: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");
            errorCode = hwtestService.test_bluetooth(buffer);
            builder.append("Test bluetooth Module: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");
        } else if (getResources().getString(R.string.rfid).equals(type)) {
            errorCode = hwtestService.test_rfid(1, buffer);
            builder.append("Test RFID: " + errorCode).append("\n");
        } else if (getResources().getString(R.string.ethernet).equals(type)) {
            hwtestService.get_ethernet(1, buffer);
            builder.append("Link Status: " + new String(buffer, 1, buffer[0])).append("\n");
            hwtestService.get_ethernet(2, buffer);
            builder.append("Speed: " + new String(buffer, 1, buffer[0]) + "M").append("\n");
            hwtestService.get_ethernet(3, buffer);
            builder.append("Duplex: " + new String(buffer, 1, buffer[0])).append("\n");
            hwtestService.get_ethernet(4, buffer);
            builder.append("IP: " + new String(buffer, 1, buffer[0])).append("\n");
        } else if (getResources().getString(R.string.usb).equals(type)) {
//            errorCode = hwtestService.get_usb(1, buffer);
//            builder.append("USB: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");
        } else if (getResources().getString(R.string.sdcard).equals(type)) {
//            errorCode = hwtestService.get_sdcard(1, buffer);
//            builder.append("SD Card: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");
        } else if (getResources().getString(R.string.lcd).equals(type)) {
            errorCode = hwtestService.test_lcd(buffer);
            builder.append("Test LCD: " + errorCode).append("\n");
        } else if (getResources().getString(R.string.led).equals(type)) {
            hwtestService.set_led(1, 0);
            builder.append("Set LED[1] To 0").append("\n");
            hwtestService.set_led(2, 1);
            builder.append("Set LED[2] To 1").append("\n");
            hwtestService.set_led(3, 0);
            builder.append("Set LED[3] To 1").append("\n");
            errorCode = hwtestService.get_led(1, buffer);
            builder.append("Get LED[1] State: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");

            errorCode = hwtestService.get_led(2, buffer);
            builder.append("Get LED[2] State: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");

            errorCode = hwtestService.get_led(3, buffer);
            builder.append("Get LED[3] State: " + errorCode + "," + new String(buffer, 1, buffer[0])).append("\n");
        }
        Log.i(TAG, builder.toString());
        Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
    }
}
