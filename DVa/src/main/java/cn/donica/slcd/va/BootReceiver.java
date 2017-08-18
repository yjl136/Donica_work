package cn.donica.slcd.va;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-25 09:18
 * Describe:       启动检测服务
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Va", "action:" + intent.getAction());
        startService(context);
    }

    /**
     * 启动服务
     *
     * @param context
     */
    private void startService(Context context) {
        Intent intent = new Intent(context, VaService.class);
        context.startService(intent);
    }
}
