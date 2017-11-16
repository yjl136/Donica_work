package cn.donica.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-26 13:38
 * Describe:
 */

public class SettingActivity extends Activity {
    private final String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        setFinishOnTouchOutside(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
