package cn.donica.slcd.settings.ui;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import cn.donica.slcd.settings.R;

//import android.support.v7.widget.Toolbar;


public class SuperUserActivity extends Activity {
    private final String TAG = "SuperUserActivity";
    public final String LOCK = "lock";
    private RelativeLayout resetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_super_user);
        initToolBar();
        resetPwd = (RelativeLayout) findViewById(R.id.resetPwd);
        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getSharedPreferences(LOCK, MODE_PRIVATE).edit().clear().commit();
                Intent intent = new Intent(SuperUserActivity.this, LockSetupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }


    private void initToolBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ico_left);
//        toolbar.setTitle(getString(R.string.SuperUser));//设置标题，也可以在xml中静态实现
//        toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
//        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
//        setSupportActionBar(toolbar);//使活动支持ToolBar

        String title = "<h5>" + getString(R.string.SuperUser) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Intent mIntent = new Intent(SuperUserActivity.this, AboutActivity.class);
//            startActivity(mIntent);
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
}



