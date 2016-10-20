package cn.donica.slcd.settings.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class TimeReceiver extends BroadcastReceiver {
    public static String TIME_CHANGED_ACTION = "cn.donica.slcd.settings.services.action.TIME_CHANGED_ACTION";

    public TimeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        if (TIME_CHANGED_ACTION.equals(action)) {
            Bundle bundle = intent.getExtras();
            String strtime = bundle.getString("time");
            //此处实现不够优雅，为了在UITimeReceiver中使用DynamicUIActivity中的TextView组件time，而将其设置为public类型，
            //更好的实现是将UITimeReceiver作为DynamicUIActivity的内部类
//            dUIActivity.time.setText(strtime);
        }
    }
}
