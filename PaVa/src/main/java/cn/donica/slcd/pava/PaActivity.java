package cn.donica.slcd.pava;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-18 11:12
 * Describe:
 */

public class PaActivity extends Activity {
    private final static String ACTION_PA = "cn.donica.slcd.action.PA";
    private final static String PA_KEY = "pa";
    private PaReceiver mPaReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
