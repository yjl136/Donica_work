package cn.donica.slcd.settings.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.donica.slcd.settings.BaseApplication;
import cn.donica.slcd.settings.R;
import cn.donica.slcd.settings.ui.LockPatternView.Cell;
import cn.donica.slcd.settings.ui.LockPatternView.DisplayMode;

//import android.support.v7.widget.Toolbar;


public class LockSetupActivity extends Activity implements
        LockPatternView.OnPatternListener, OnClickListener {
    private static final String TAG = "LockSetupActivity";
    private LockPatternView lockPatternView;
    private Button leftButton;
    private Button rightButton;

    private Intent mIntent;

    /**
     * 开始`
     */
    private static final int STEP_1 = 1; // 开始
    /**
     * 第一次设置手势完成
     */
    private static final int STEP_2 = 2; // 第一次设置手势完成
    /**
     * 按下继续按钮
     */
    private static final int STEP_3 = 3; // 按下继续按钮
    /**
     * 第二次设置手势完成
     */
    private static final int STEP_4 = 4; // 第二次设置手势完成

    private int step;

    private List<Cell> choosePattern;

    private boolean confirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock_setup);
        initToolbar();

        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        leftButton = (Button) findViewById(R.id.left_btn);
        rightButton = (Button) findViewById(R.id.right_btn);
        step = STEP_1;
        updateView();
    }


    private void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_setting);//设置ToolBar头部图标
//        toolbar.setTitle(getString(R.string.reset_password));//设置标题，也可以在xml中静态实现
//        toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
//        toolbar.setTitleTextAppearance(this,R.style.Toolbar_TitleText);
//        setSupportActionBar(toolbar);

        String title = "<h5>" + getString(R.string.reset_password) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mIntent = new Intent(LockSetupActivity.this, SuperUserActivity.class);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateView() {
        switch (step) {
            case STEP_1:
                leftButton.setText(R.string.cancel);
                rightButton.setText("");
                rightButton.setEnabled(false);
                choosePattern = null;
                confirm = false;
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_2:
                leftButton.setText(R.string.try_again);
                rightButton.setText(R.string.goon);
                rightButton.setEnabled(true);
                lockPatternView.disableInput();
                break;
            case STEP_3:
                leftButton.setText(R.string.cancel);
                rightButton.setText("");
                rightButton.setEnabled(false);
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_4:
                leftButton.setText(R.string.cancel);
                if (confirm) {
                    rightButton.setText(R.string.confirm);
                    rightButton.setEnabled(true);
                    lockPatternView.disableInput();
                } else {
                    rightButton.setText("");
                    lockPatternView.setDisplayMode(DisplayMode.Wrong);
                    lockPatternView.enableInput();
                    rightButton.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                if (step == STEP_1 || step == STEP_3 || step == STEP_4) {
                    finish();
                } else if (step == STEP_2) {
                    step = STEP_1;
                    updateView();
                }
                break;
            case R.id.right_btn:
                if (step == STEP_2) {
                    step = STEP_3;
                    updateView();
                } else if (step == STEP_4) {
                    SharedPreferences preferences = getSharedPreferences(BaseApplication.LOCK, MODE_PRIVATE);
                    preferences.edit().putString(BaseApplication.LOCK_KEY, LockPatternView.patternToString(choosePattern)).commit();
                    Intent intent = new Intent(this, AboutActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
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
        Log.d(TAG, "onPatternCellAdded");
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        Log.d(TAG, "onPatternDetected");
        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
            Toast.makeText(this, R.string.lockpattern_recording_incorrect_too_short, Toast.LENGTH_LONG).show();
            lockPatternView.setDisplayMode(DisplayMode.Wrong);
            return;
        }
        if (choosePattern == null) {
            choosePattern = new ArrayList<Cell>(pattern);
            Log.d(TAG, "choosePattern = " + Arrays.toString(choosePattern.toArray()));
            step = STEP_2;
            updateView();
            return;
        }
        Log.d(TAG, "choosePattern = " + Arrays.toString(choosePattern.toArray()));
        Log.d(TAG, "pattern = " + Arrays.toString(pattern.toArray()));
        if (choosePattern.equals(pattern)) {
            Log.d(TAG, "pattern = " + Arrays.toString(pattern.toArray()));
            confirm = true;
        } else {
            confirm = false;
        }
        step = STEP_4;
        updateView();
    }
}
