package cn.donica.slcd.ble.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import cn.donica.slcd.ble.R;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-18 11:12
 * Describe:
 */

public class PaActivity extends Activity {
    private final static String ACTION_PA = "cn.donica.slcd.action.PA";
    private final static String ACTION_VA = "cn.donica.slcd.action.VA";
    private final static String PA_KEY = "pa";
    private PaReceiver mPaReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_pa);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PA);
        mPaReceiver = new PaReceiver();
        registerReceiver(mPaReceiver, filter);
    }

    class PaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pa = intent.getIntExtra(PA_KEY, 0);
            if (pa == 0) {//伪代码
                //处于pa状态
                PaActivity.this.finish();
                overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPaReceiver != null) {
            unregisterReceiver(mPaReceiver);
        }
    }
}
