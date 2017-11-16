package cn.donica.slcd.ble.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.shell.Shell;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-25 09:18
 * Describe:       启动检测服务
 */
public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DLog.info("shutdown");
        String cmd = "wm size 1220x800";
        executeCMD(cmd);
    }

    /**
     * 执行命令
     *
     * @param cmd
     */
    private void executeCMD(String cmd) {
        List<String> result = Shell.SU.run(cmd);
        if (result != null) {
            for (String line : result) {
                DLog.info(line);
            }
        }
    }
}
