package cn.donica.slcd.settings.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.settings.BaseApplication;
import cn.donica.slcd.settings.R;
import cn.donica.slcd.settings.ui.LockPatternView.Cell;



public class LockSetupActivity extends Activity implements
        LockPatternView.OnPatternListener, OnClickListener {
    private static final String TAG = "LockSetupActivity";
    private LockPatternView lockPatternView;
    private TextView tipTv;
    private List<Cell> choosePattern;
    private LinearLayout btLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock_setup);
        initToolbar();
        initView();
    }

    private void initView() {
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        btLayout = (LinearLayout) findViewById(R.id.btLayout);
        lockPatternView.setOnPatternListener(this);
        tipTv = (TextView) findViewById(R.id.tipTv);
        tipTv.setText(R.string.enter_pattern);
        btLayout.setVisibility(View.GONE);
    }


    private void initToolbar() {
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                tipTv.setText(R.string.enter_pattern);
                btLayout.setVisibility(View.GONE);
                choosePattern = null;
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case R.id.right_btn:
                tipTv.setText(R.string.enter_pattern_again);
                btLayout.setVisibility(View.GONE);
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
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
            Toast.makeText(this, R.string.lockpattern_recording_incorrect_too_short, Toast.LENGTH_SHORT).show();
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            return;
        }
        if (choosePattern == null) {
            choosePattern = new ArrayList<Cell>(pattern);
            btLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (choosePattern.equals(pattern)) {
            SharedPreferences preferences = getSharedPreferences(BaseApplication.LOCK, MODE_PRIVATE);
            preferences.edit().putString(BaseApplication.LOCK_KEY, LockPatternView.patternToString(choosePattern)).commit();
            Toast.makeText(this, R.string.setting_success, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, R.string.diff_password, Toast.LENGTH_LONG).show();
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
        }
    }
}
