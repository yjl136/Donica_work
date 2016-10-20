package cn.donica.slcd.settings.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.donica.slcd.settings.BaseApplication;
import cn.donica.slcd.settings.R;
import cn.donica.slcd.settings.services.SuperUserTimerService;
import cn.donica.slcd.settings.utils.ActivityUtil;
import cn.donica.slcd.settings.utils.Config;

/**
 * 关于页面
 */
public class AboutActivity extends Activity {

    private static final String TAG = "AboutActivity";
    private android.app.AlertDialog password_dialog;
    private TextView version_title, version_content, developer_title, developer_content, updateTime_title, updateTime_content;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
        initToolBar();
        initView();
        version_content.setOnClickListener(new View.OnClickListener() {
            //数组存储点击次数
            long[] mHits = new long[5];

            @Override
            public void onClick(View arg0) {
                if (Config.isAdminTimerRun == true) {
                    Intent intent = new Intent();
                    intent.setClass(AboutActivity.this, AdminActivity.class);
                    startActivity(intent);
                } else {
                    System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                    //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于1250，即五次点击
                    mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                    if (mHits[0] >= (SystemClock.uptimeMillis() - 1250)) {
                        Intent intent = new Intent();
                        intent.setClass(AboutActivity.this, LockActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        developer_content.setOnClickListener(new View.OnClickListener() {
            //数组存储点击次数
            long[] mHits = new long[5];

            @Override
            public void onClick(View arg0) {
                if (Config.isSuperUserTimerRun == true) {
                    Intent intent = new Intent(AboutActivity.this, SuperUserActivity.class);
                    startActivity(intent);
                } else {
                    System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                    //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于1250，即五次点击
                    mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                    if (mHits[0] >= (SystemClock.uptimeMillis() - 1250)) {
                        showPasswordDialog();
                    }
                }
            }
        });
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
        updateTime_content.setText(R.string.updateTime_content);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mIntent = new Intent(AboutActivity.this, MainActivity.class);
            startActivity(mIntent);
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

    private void superUserTimer() {
        if (!ActivityUtil.isServiceWork(BaseApplication.getContext(), "cn.donica.slcd.settings.services.SuperUserTimerService")) {
            Intent mIntent = new Intent(AboutActivity.this, SuperUserTimerService.class);
            Config.isSuperUserTimerRun = true;
            startService(mIntent);
        }
    }

    /**
     * 输入管理员密码框
     */
    private void showPasswordDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_password, null);
        final EditText password = (EditText) view.findViewById(R.id.password);
        Button btn_ok = (Button) view.findViewById(R.id.positiveButton);
        Button btn_cancel = (Button) view.findViewById(R.id.negativeButton);
        builder.setView(view);
        password_dialog = builder.create();
        password_dialog.show();
        password_dialog.setCancelable(false);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(Config.superUserPassword)) {
                    superUserTimer();
                    Toast.makeText(AboutActivity.this, getString(R.string.enter_password_successfully),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AboutActivity.this, SuperUserActivity.class);
                    startActivity(intent);
                    password_dialog.dismiss();
                } else {
                    Toast.makeText(AboutActivity.this,
                            getString(R.string.enter_password_error),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_dialog.dismiss();
            }
        });
    }
}

