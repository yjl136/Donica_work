package cn.donica.slcd.settings.ui;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import cn.donica.slcd.settings.BaseApplication;
import cn.donica.slcd.settings.R;
import cn.donica.slcd.settings.ui.LockPatternView.Cell;



public class LockActivity extends Activity implements LockPatternView.OnPatternListener {
    private final String TAG = "LockActivity";
    private List<Cell> lockPattern;
    private LockPatternView lockPatternView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences preferences = getSharedPreferences(BaseApplication.LOCK,
                MODE_PRIVATE);
        String patternString = preferences.getString(BaseApplication.LOCK_KEY,
                null);
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onPatternStart() {

    }

    @Override
    public void onPatternCleared() {

    }

    @Override
    public void onPatternCellAdded(List<Cell> pattern) {

    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        if (pattern.equals(lockPattern)) {
            Toast.makeText(LockActivity.this, getString(R.string.enter_password_successfully), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LockActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LockActivity.this, getString(R.string.enter_password_error), Toast.LENGTH_SHORT).show();
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            }
        }


}
