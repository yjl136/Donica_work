package cn.donica.slcd.settings.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.donica.slcd.settings.R;
import cn.donica.slcd.settings.appiconmanage.AppIconManageActivity;
import cn.donica.slcd.settings.utils.Config;


public class AdminActivity extends Activity implements OnClickListener {
    private AlertDialog.Builder builder;
    private SharedPreferences prference;
    private AlertDialog ip_seat_dialog;

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        initToolBar();
        initView();
    }

    private void initView() {
        addSettingItem(R.id.appIconManager, getString(R.string.appIconManager), R.mipmap.ico_program);
        addSettingItem(R.id.system_settings, getString(R.string.system_settings), R.mipmap.ico_setup);
        addSettingItem(R.id.uninstall, getString(R.string.uninstall), R.mipmap.ico_uninstall);
        addSettingItem(R.id.restore_factory_settings, getString(R.string.restore_factory_settings), R.mipmap.ico_recovery);
        addSettingItem(R.id.wlan, getString(R.string.wlan), R.mipmap.ico_wifi);
        addSettingItem(R.id.bluetooth, getString(R.string.bluetooth), R.mipmap.ico_bluetooth);
        addSettingItem(R.id.ethernet, getString(R.string.Ethernet_Configuration), R.mipmap.ico_wlan);
        addSettingItem(R.id.language, getString(R.string.language_input), R.mipmap.ico_language);
        addSettingItem(R.id.ip_seat_configuration, getString(R.string.ip_seat_configuration), R.mipmap.ico_language);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mIntent = new Intent(AdminActivity.this, AboutActivity.class);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {
        String title = "<h5>" + getString(R.string.System_Settings) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();
    }


    protected void addSettingItem(int layout_id, String title, int minmap_pic_id) {
        LinearLayout layout = (LinearLayout) findViewById(layout_id);
        ((TextView) layout.findViewById(R.id.SettingItemTitle)).setText(title);
        ((ImageView) layout.findViewById(R.id.btnPic)).setImageResource(minmap_pic_id);
        layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.appIconManager:
                intent = new Intent(AdminActivity.this, AppIconManageActivity.class);
                startActivity(intent);
                break;
            case R.id.wlan:
                intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
                break;
            case R.id.bluetooth:
                intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intent);
                break;
            case R.id.ethernet:
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$EthernetSettingsActivity");
                intent.setComponent(comp);
                intent.setAction("android.intent.action.VIEW");
                startActivity(intent);
                break;
            case R.id.language:
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName compLanguage = new ComponentName("com.android.settings", "com.android.settings.Settings$InputMethodAndLanguageSettingsActivity");
                intent.setComponent(compLanguage);
                intent.setAction("android.intent.action.VIEW");
                startActivity(intent);
                break;
            case R.id.system_settings:
                intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.uninstall:
                intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                startActivity(intent);
                break;
            case R.id.restore_factory_settings:
                Toast.makeText(AdminActivity.this, "点击了恢复出厂设置", Toast.LENGTH_LONG).show();
                break;
            case R.id.ip_seat_configuration:
                showInputDialog();
                break;
            default:
                return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 检查是否是数字
     *
     * @param value
     * @return
     */
    public static boolean checkNumber(String value) {
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return value.matches(regex);
    }

    /**
     * 判断是否为IP地址
     *
     * @param addr
     * @return
     */
    public boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        return ipAddress;
    }

    /**
     * 显示IP地址和座位位置配置对话框
     */
    public void showInputDialog() {
        prference = getSharedPreferences(Config.PREF_NAME, MODE_WORLD_READABLE);
        String ip = prference.getString("IP", "192.168.2.99");
        String seat = prference.getString("Seat", "A10");
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.show_ip_seat_dialog, null);
        final EditText et_seat = (EditText) view.findViewById(R.id.seat);
        final EditText et_ip = (EditText) view.findViewById(R.id.ip);
        Button btn_ok = (Button) view.findViewById(R.id.ok);
        Button btn_cancel = (Button) view.findViewById(R.id.cancel);
        et_ip.setText(ip);
        et_seat.setText(seat);
        builder.setView(view);
        ip_seat_dialog = builder.create();

        ip_seat_dialog.show();
        //
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isIP(et_ip.getText().toString())) {
                    prference.edit().clear();
                    prference.edit().putString("IP", et_ip.getText().toString()).commit();
                    prference.edit().putString("Seat", et_seat.getText().toString()).commit();
                    ip_seat_dialog.dismiss();
                } else {
                    Toast.makeText(AdminActivity.this, "输入非法格式IP地址", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消操作
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ip_seat_dialog.dismiss();
            }
        });
    }
}