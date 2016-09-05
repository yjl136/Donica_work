package cn.donica.slcd.polling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.donica.slcd.utils.LogUtil;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 后边的XXX.class就是要启动的服务  
//		Intent mIntent = new Intent(context,MainActivity.class);
//			context.startActivity(mIntent);
        LogUtil.d("TAG", "开机自动服务自动启动.....");
//		PollingUtils.startPollingService(context, 1,PollingService.class);
    }
}
