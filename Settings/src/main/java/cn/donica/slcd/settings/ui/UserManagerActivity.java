package cn.donica.slcd.settings.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.donica.slcd.settings.R;

//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.Toolbar;


public class UserManagerActivity extends Activity implements OnClickListener {
    private final static String Password = "123456";
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_manager);
        initToolBar();
        addSettingItem(R.id.SystemManager, getString(R.string.SystemManager), R.mipmap.ic_launcher);
        addSettingItem(R.id.SuperUser, getString(R.string.SuperUser), R.mipmap.ic_launcher);
    }

    protected void addSettingItem(int layout_id, String title, int minmap_pic_id) {
        RelativeLayout layout = (RelativeLayout) findViewById(layout_id);
        ((TextView) layout.findViewById(R.id.SettingItemTitle)).setText(title);
        ((ImageView) layout.findViewById(R.id.btnPic)).setBackgroundResource(minmap_pic_id);
        layout.setOnClickListener(this);
    }

    private void initToolBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_setting);//设置ToolBar头部图标
//        toolbar.setTitle(getString(R.string.UserManger));//设置标题，也可以在xml中静态实现
//        toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
//        toolbar.setTitleTextAppearance(this,R.style.Toolbar_TitleText);
//        setSupportActionBar(toolbar);//使活动支持ToolBar
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.SystemManager:
                intent = new Intent(UserManagerActivity.this, LockActivity.class);
                startActivity(intent);
                break;
            case R.id.SuperUser:
                builder = new AlertDialog.Builder(UserManagerActivity.this);
                builder.setTitle(getString(R.string.enter_password));
//                builder.setIcon(android.R.drawable.btn_star);
                final EditText editText = new EditText(UserManagerActivity.this);
                editText.setHint(getString(R.string.password));
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(editText);
                builder.setPositiveButton(getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                if (editText.getText().toString().equals(Password)) {
                                    Toast.makeText(UserManagerActivity.this, getString(R.string.enter_password_successfully),
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UserManagerActivity.this, SuperUserActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(UserManagerActivity.this,
                                            getString(R.string.enter_password_error),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Toast.makeText(UserManagerActivity.this, getString(R.string.cancel),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
                break;
            default:
                return;
        }
    }
}
