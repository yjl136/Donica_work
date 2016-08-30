package cn.donica.slcd.dmanager.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.donica.slcd.dmanager.utils.Config;

/**
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        int temp = intent.getIntExtra("msg", 1);
        if (temp == Config.CPU_TEMPERATURE_THRESHOLD_1) {
            Log.d(TAG, "温度超过85摄氏度了");
        } else if (temp == Config.CPU_TEMPERATURE_THRESHOLD_2) {
            Log.d(TAG, "温度超过95摄氏度了，即将重启");
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
