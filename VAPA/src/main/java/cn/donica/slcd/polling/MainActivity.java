package cn.donica.slcd.polling;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import cn.donica.slcd.utils.PollingUtils;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private ImageView bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bg = (ImageView) findViewById(R.id.bg);
        // Start polling service
        // LogUtil.d(TAG, "Start polling service...");
        PollingUtils.startPollingService(this, 2, NtscService.class);

//        Intent i = new Intent(MainActivity.this, NtscService.class);
//        i.setAction(NtscService.LOCK_ACTION);
//        startService(i);
        finish();
    }

    private void closeNavigationbar() {
        try {
            String ProcID = "79";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                ProcID = "42"; // ICS
            // 需要root 权限
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "service call activity " + ProcID + " s16 com.android.systemui"}); // WAS
            proc.waitFor();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*
     * 恢复运行Android 4.0以上系统的平板的屏幕下方的状态栏
     */
    private void openNavigationbar() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"am", "startservice", "-n", "com.android.systemui/.SystemUIService"});
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //		PollingUtils.stopPollingService(this, PollingService.class,
        //			PollingService.ACTION);
    }
}
