/**
 * File Name: SnmpBroadcaseReceiver.java
 * Description：
 * Author: Luke Huang
 * Create Time: 2015-7-24
 * <p>
 * For the SLCD Project
 * Copyright © 2015 Donica.cn All rights reserved
 */
package cn.donica.slcd.dmanager.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.donica.slcd.dmanager.service.BootService;

/**
 * Description:
 *
 * @author Luke Huang 2015-7-24
 */
public class BootBroadcaseReceiver extends BroadcastReceiver {

    /**
     * (non-Javadoc)
     *
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            onBootCompleted(context);
        }
    }

    /**
     * Description: handle event of boot completed
     *
     * @param context
     */
    private void onBootCompleted(Context context) {
        // TODO start OAM service
        Intent newIntent = new Intent(context, BootService.class);
        context.startService(newIntent);
//        LoopTask.startLoopTaskService(context,Config.LOOP_TIME,BootService.class, BootService.ACTION);
//                Intent newIntent = new Intent(context,TestService.class);
//                context.startService(newIntent);
    }
}
