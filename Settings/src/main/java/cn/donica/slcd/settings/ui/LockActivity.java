package cn.donica.slcd.settings.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import cn.donica.slcd.settings.BaseApplication;
import cn.donica.slcd.settings.R;
import cn.donica.slcd.settings.services.AdminTimerService;
import cn.donica.slcd.settings.services.CountdownTimerService;
import cn.donica.slcd.settings.ui.LockPatternView.Cell;
import cn.donica.slcd.settings.utils.ActivityUtil;
import cn.donica.slcd.settings.utils.Config;
import cn.donica.slcd.settings.utils.CountdownTimer;
import cn.donica.slcd.settings.utils.LogUtil;

//import android.support.v7.widget.Toolbar;


public class LockActivity extends Activity implements LockPatternView.OnPatternListener {
    private final String TAG = "LockActivity";

    private List<Cell> lockPattern;
    private LockPatternView lockPatternView;
    private Intent mIntent;
    public static String countdownTime = "60";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInputState();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences preferences = getSharedPreferences(BaseApplication.LOCK,
                MODE_PRIVATE);
        String patternString = preferences.getString(BaseApplication.LOCK_KEY,
                null);
        Log.d(TAG, "lockPattern is:" + patternString);
        lockPattern = LockPatternView.stringToPattern(patternString);

        setContentView(R.layout.activity_lock);
        initToolbar();
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
    }

    private void initToolbar() {

        String title = "<h5>" + getString(R.string.enter_a_gesture_password) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onPatternStart() {
        Log.d(TAG, "onPatternStart");
    }

    @Override
    public void onPatternCleared() {
        Log.d(TAG, "onPatternCleared");
    }

    @Override
    public void onPatternCellAdded(List<Cell> pattern) {
        Log.e(TAG, LockPatternView.patternToString(pattern));
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {

        if (pattern.equals(lockPattern)) {
            Toast.makeText(LockActivity.this, getString(R.string.enter_password_successfully), Toast.LENGTH_SHORT).show();
            adminTimer();
            Intent intent = new Intent(LockActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();

        } else {
            // lockPatternView.setDisplayMode(DisplayMode.Wrong);
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            if (Config.lockCount != 0) {
                --Config.lockCount;
                Toast.makeText(LockActivity.this, String.format(getResources().getString(R.string.password_error), Config.lockCount), Toast.LENGTH_SHORT).show();
                if (Config.lockCount == 0) {
                    if (!ActivityUtil.isServiceWork(LockActivity.this, "cn.donica.slcd.settings.services.CountdownTimerService")) {
                        CountdownTimerService.setHandler(mCodeHandler);
                        mIntent = new Intent(LockActivity.this, CountdownTimerService.class);
                        Toast.makeText(LockActivity.this, String.format(getResources().getString(R.string.countdown), countdownTime), Toast.LENGTH_SHORT).show();
                        startService(mIntent);
                    }
                    finish();
                }
            }
        }
    }

    public void checkInputState() {
        if (Config.lockCount == 0) {
            Toast.makeText(BaseApplication.getContext(), String.format(getResources().getString(R.string.countdown), countdownTime), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void adminTimer() {
        if (!ActivityUtil.isServiceWork(BaseApplication.getContext(), "cn.donica.slcd.settings.services.SuperUserTimerService")) {
            Intent mIntent = new Intent(LockActivity.this, AdminTimerService.class);
            Config.isAdminTimerRun = true;
            startService(mIntent);
        }
    }

    /**
     * 倒计时Handler
     */
    @SuppressLint("HandlerLeak")
    Handler mCodeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == CountdownTimer.IN_RUNNING) {// 正在倒计时
                countdownTime = msg.obj.toString();
                Config.isLock = true;
                LogUtil.d(TAG, "time:" + msg.obj.toString());
            } else if (msg.what == CountdownTimer.END_RUNNING) {// 完成倒计时
                mIntent = new Intent(LockActivity.this, CountdownTimerService.class);
                Config.isLock = false;
                Config.lockCount = 3;
                countdownTime = "60";
                stopService(mIntent);
            }
        }
    };
}
