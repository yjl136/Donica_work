package cn.donica.slcd.polling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.donica.slcd.utils.LogUtil;

/**
 * 监听NTSC状态改变广播
 */
public class NtscReceiver extends BroadcastReceiver {
    private static final String TAG = "NtscReceiver";

    public NtscReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        String status = intent.getStringExtra("ntsc");
//        Toast.makeText(context, "NTSC状态改变，状态为：" + status, Toast.LENGTH_SHORT).show();
        LogUtil.d(TAG, "NTSC状态改变，状态为：" + status);
//        if (status.equals("open")) {
//            LogUtil.d(TAG, "切换到NTSC源");
//            String commandStr[] = {MyApplication.ShellAppSuStr, MyApplication.ShellAppRunStr};
//            ShellUtils.execCommand(commandStr, true);
//        } else if(status.equals("close")) {
//            ShellUtils.execCommand(MyApplication.ShellKillAllStr, true);
//            LogUtil.d(TAG, "切回Android画面");
//        }
    }
}
