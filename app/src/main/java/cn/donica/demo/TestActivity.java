package cn.donica.demo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-11-30 17:10
 * Describe:
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager();
    }
}
