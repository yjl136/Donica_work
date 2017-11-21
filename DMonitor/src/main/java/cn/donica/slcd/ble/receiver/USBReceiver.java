package cn.donica.slcd.ble.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IMountService;

import org.litepal.crud.DataSupport;

import cn.donica.slcd.ble.entity.Monitor;
import cn.donica.slcd.ble.utils.DLog;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-11-07 16:15
 * Describe:
 */

public class USBReceiver extends BroadcastReceiver {
    private IMountService mMountService;

    public USBReceiver() {
        mMountService = IMountService.Stub.asInterface(ServiceManager.getService("mount"));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String mountPath = intent.getData().getPath();
        DLog.info("mount path:" + mountPath + "  isDebug:" + isDebug());
        if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
            try {
                if ("/mnt/udisk".equals(mountPath)) {
                    if (isDebug()) {
                        mMountService.mountVolume(mountPath);
                    } else {
                        mMountService.unmountVolume(mountPath, true, true);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 判断是否正在调试
     *
     * @return
     */
    private boolean isDebug() {
        Monitor monitor = DataSupport.where("name=?", "debug").findFirst(Monitor.class);
        if (monitor != null && monitor.getValue() == 1) {
            return true;
        }
        return false;
    }
}
