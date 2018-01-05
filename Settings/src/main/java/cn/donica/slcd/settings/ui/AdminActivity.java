package cn.donica.slcd.settings.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.donica.slcd.settings.R;
import cn.donica.slcd.settings.bite.BiteActivity;
import cn.donica.slcd.settings.lang.LocaleDialog;
import cn.donica.slcd.settings.restore.RestoreFactoryDialog;
import cn.donica.slcd.settings.upgrade.UpgradeDialog;
import cn.donica.slcd.settings.utils.XmlUtils;

import static cn.donica.slcd.settings.R.id.installCB;

public class AdminActivity extends Activity implements OnClickListener {
    private final String TAG = "AdminActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        initToolBar();
        initView();
        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        //  finish();
    }

    private void initView() {
        addSettingItem(R.id.restore_factory_settings, getString(R.string.restore_factory_settings), R.mipmap.ico_recovery);
        addSettingItem(R.id.network, getString(R.string.network), R.mipmap.ico_lan_h);
        addSettingItem(R.id.language, getString(R.string.language_input), R.mipmap.ico_language);
        addSettingItem(R.id.install_configuration, getString(R.string.install_config), R.mipmap.ico_setup);
        addSettingItem(R.id.bite, getString(R.string.system_bite), R.mipmap.ico_m_bite_h);
        addSettingItem(R.id.about, getString(R.string.action_about), R.mipmap.ico_about);
        addSettingItem(R.id.reset_password, getString(R.string.reset_password), R.mipmap.ico_m_password_h);
        addSettingItem(R.id.debug, getString(R.string.debug), R.mipmap.ico_m_password_h);
        addSettingItem(R.id.upgrade, getString(R.string.system_upgrade), R.mipmap.ico_backup);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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
        DialogFragment dialog = null;
        switch (v.getId()) {
            case R.id.network:
                intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
                finish();
                break;
            case R.id.language:
                dialog = new LocaleDialog();
                dialog.show(getFragmentManager(), "LocaleDialog");
                break;
            case R.id.restore_factory_settings:
                dialog = new RestoreFactoryDialog();
                dialog.show(getFragmentManager(), "RestoreDialog");
                break;
            case R.id.upgrade:
                dialog = new UpgradeDialog();
                dialog.show(getFragmentManager(), "UpgradeDialog");
                break;
            case R.id.install_configuration:
                showInstallConfigDialog();
                break;
            case R.id.debug:
                showDebugDialog();
                break;
            case R.id.bite:
                intent = new Intent(this, BiteActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.reset_password:
                intent = new Intent(this, LockSetupActivity.class);
                startActivity(intent);
                break;
            default:
                return;
        }
    }

    /* @Override
     public void onConfigurationChanged(Configuration newConfig) {
         Log.i(TAG, "onConfigurationChanged");
         super.onConfigurationChanged(newConfig);
     }
 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private boolean checkSeat(String seat) {
        String rexp = "^[A-Za-z0-9]+$";
        return seat.matches(rexp);
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
     * 获取ip配置
     *
     * @return
     */
    private String getIpConfig() {
        Cursor cursor = null;
        String ip = "";
        try {
            Uri uri = Uri.parse("content://cn.donica.slcd.provider/config/ip");
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("value");
                ip = cursor.getString(index);
            }
        } catch (Exception e) {
            ip = "";
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            return ip;
        }
    }

    /**
     * 获取seat配置
     *
     * @return
     */
    private String getSeatConfig() {
        String seat = "";
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.donica.slcd.provider/config/seat");
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("value");
                seat = cursor.getString(index);
            }
        } catch (Exception e) {
            seat = "";
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            return seat;
        }
    }

    /**
     * 保存ip配置
     */
    private void saveIpConfig(String ip) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.donica.slcd.provider/config/ip");
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                //更新
                ContentValues values = new ContentValues();
                values.put("value", ip);
                resolver.update(uri, values, null, null);
            } else {
                //保存
                ContentValues values = new ContentValues();
                values.put("name", "ip");
                values.put("value", ip);
                resolver.insert(uri, values);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }

    /**
     * 保存seat配置
     */
    private void saveSeatConfig(String seat) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.donica.slcd.provider/config/seat");
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                //更新
                ContentValues values = new ContentValues();
                values.put("value", seat);
                resolver.update(uri, values, null, null);
            } else {
                //保存
                ContentValues values = new ContentValues();
                values.put("name", "seat");
                values.put("value", seat);
                resolver.insert(uri, values);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }

    /**
     * 显示IP地址和座位位置配置对话框
     */
    public void showInputDialog() {
        final String ip = getIpConfig();
        final String seat = getSeatConfig();
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
        final AlertDialog ip_seat_dialog = builder.create();
        ip_seat_dialog.show();
        ip_seat_dialog.setCanceledOnTouchOutside(false);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!checkSeat(et_seat.getText().toString())) {
                    Toast.makeText(AdminActivity.this, R.string.seat_validate_tip, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isIP(et_ip.getText().toString())) {
                    Toast.makeText(AdminActivity.this, R.string.ip_format_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                saveIpConfig(et_ip.getText().toString());
                saveSeatConfig(et_seat.getText().toString());
                ip_seat_dialog.dismiss();
            }
        });
        //取消操作
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ip_seat_dialog.dismiss();
            }
        });
    }

    /**
     * 获取安装配置
     *
     * @return
     */
    private boolean isSeatBack() {
       /* boolean isSeatBack = false;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.donica.slcd.provider/monitor/seatback");
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("value");
                int value = cursor.getInt(index);
                if (value == 1) {
                    isSeatBack = true;
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            return isSeatBack;
        }*/
        if (XmlUtils.getSeatBack() == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取调试模式
     *
     * @return
     */
    private boolean isDebug() {
        boolean isDebug = false;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.donica.slcd.provider/monitor/debug");
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("value");
                int value = cursor.getInt(index);
                if (value == 1) {
                    isDebug = true;
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            return isDebug;
        }
    }

    /**
     * 保存安装配置
     */
    private void saveInstallConfig(boolean isCheck) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.donica.slcd.provider/monitor/seatback");
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                //更新
                ContentValues values = new ContentValues();
                values.put("value", isCheck ? 1 : 0);
                resolver.update(uri, values, null, null);
            } else {
                //保存
                ContentValues values = new ContentValues();
                values.put("name", "seatback");
                values.put("value", isCheck ? 1 : 0);
                resolver.insert(uri, values);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    /**
     * 保存debug配置
     */
    private void saveDebugConfig(boolean isCheck) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.donica.slcd.provider/monitor/debug");
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(uri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                //更新
                ContentValues values = new ContentValues();
                values.put("value", isCheck ? 1 : 0);
                resolver.update(uri, values, null, null);
            } else {
                //保存
                ContentValues values = new ContentValues();
                values.put("name", "debug");
                values.put("value", isCheck ? 1 : 0);
                resolver.insert(uri, values);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    /**
     * 显示是否调试模式dialog
     */
    public void showDebugDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.debug_config, null);
        Button btn_ok = (Button) view.findViewById(R.id.ok);
        Button btn_cancel = (Button) view.findViewById(R.id.cancel);
        final CheckBox debugCB = (CheckBox) view.findViewById(installCB);
        debugCB.setChecked(isDebug());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveDebugConfig(debugCB.isChecked());
                dialog.dismiss();
            }
        });
        //取消操作
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示安装配置对话框
     */
    public void showInstallConfigDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.install_config, null);
        Button btn_ok = (Button) view.findViewById(R.id.ok);
        Button btn_cancel = (Button) view.findViewById(R.id.cancel);
        final CheckBox installCB = (CheckBox) view.findViewById(R.id.installCB);
        installCB.setChecked(isSeatBack());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                XmlUtils.saveConfigValue(AdminActivity.this, installCB.isChecked() ? 1 : 0);
                saveInstallConfig(installCB.isChecked());
                dialog.dismiss();
            }
        });
        //取消操作
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}