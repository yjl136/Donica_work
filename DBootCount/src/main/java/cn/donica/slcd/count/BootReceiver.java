package cn.donica.slcd.count;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-25 09:18
 * Describe:       启动检测服务
 */
public class BootReceiver extends BroadcastReceiver {
    private final String TAG = "BootCount";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
            Log.i(TAG, "audio");
        }
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Log.i(TAG, "boot completed");
        }
        if ("cn.donica.slcd.action.BOOT_COMPLETED".equals(action)) {
            Log.i(TAG, "donica boot completed");
        }
        //  startService(context);
    }

    /**
     * 启动服务
     *
     * @param context
     */
    private void startService(Context context) {
        Intent intent = new Intent(context, BootCountService.class);
        context.startService(intent);
    }
}
