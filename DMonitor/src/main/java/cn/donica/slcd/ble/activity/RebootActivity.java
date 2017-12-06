package cn.donica.slcd.ble.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import cn.donica.slcd.ble.R;
import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.shell.Shell;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-11-24 13:41
 * Describe:
 */

public class RebootActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_reboot);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DLog.info("reboot!!");
                SystemClock.sleep(3000);
                Shell.SU.run("reboot");
            }
        }).start();
        setFinishOnTouchOutside(false);
    }
}
