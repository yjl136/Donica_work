package cn.donica.slcd.settings.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cn.donica.slcd.settings.R;

/**
 * 关于页面
 */
public class AboutActivity extends Activity {
    private TextView version_title, version_content, developer_title, developer_content, updateTime_title, updateTime_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
        initToolBar();
        initView();
    }
    private void initToolBar() {
        String title = "<h5>" + getString(R.string.action_about) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();
    }
    private void initView() {
        version_title = (TextView) findViewById(R.id.version_title);
        version_content = (TextView) findViewById(R.id.version_content);
        version_title.setText(getString(R.string.version_title));
        version_content.setText(android.os.Build.DISPLAY);

        developer_title = (TextView) findViewById(R.id.developer_title);
        developer_content = (TextView) findViewById(R.id.developer_content);
        developer_title.setText(getString(R.string.developer_title));
        developer_content.setText(R.string.developer_content);

        updateTime_title = (TextView) findViewById(R.id.updateTime_title);
        updateTime_content = (TextView) findViewById(R.id.updateTime_content);
        updateTime_title.setText(getString(R.string.updateTime_title));
        long time = Build.TIME;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String content = sdf.format(time);
        updateTime_content.setText(content);
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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

