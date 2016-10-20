package cn.donica.slcd.settings.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.settings.BaseApplication;
import cn.donica.slcd.settings.R;

//import android.support.v7.widget.Toolbar;

public class MainActivity extends Activity {
    private static final String TAG = "AppIconManageActivity";

    private AudioManager mAudioManager;
    private boolean mIsAutoMode = true;
    private SeekBar brightness_seekbar, volume_seekbar;
    private List<LockPatternView.Cell> choosePattern;
    MenuItem moreItem;
    /**
     * 设置第一次点击的时间
     */
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initToolBar();
        initDefaultPattern();
        brightness_seekbar = (SeekBar) findViewById(R.id.brightness_seekbar);
        volume_seekbar = (SeekBar) findViewById(R.id.volume_seekbar);
        brightness_seekbar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volume_seekbar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        initSeekBar();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 设置标题栏
     */
    private void initToolBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ico_system2);//设置ToolBar头部图标
//        toolbar.setTitle(getString(R.string.system_Settings));//设置标题，也可以在xml中静态实现
//        toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
//        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
//        setSupportActionBar(toolbar);//使活动支持ToolBar
//        Drawable image = getResources().getDrawable(R.mipmap.ico_more);
//        toolbar.setOverflowIcon(image);

        String title = "<h5>" + getString(R.string.system_settings) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setLogo(R.mipmap.ico_system2);
        actionBar.show();
    }


    /**
     * 设置默认手势密码
     */
    public void initDefaultPattern() {
        SharedPreferences preferences = getSharedPreferences(BaseApplication.LOCK,
                MODE_PRIVATE);
        String patternString = preferences.getString(BaseApplication.LOCK_KEY,
                null);
        if (patternString == null) {
            choosePattern = new ArrayList<LockPatternView.Cell>();
            //1为列序号，0为行序号
            choosePattern.add(LockPatternView.Cell.of(1, 0));
            choosePattern.add(LockPatternView.Cell.of(2, 0));
            choosePattern.add(LockPatternView.Cell.of(1, 1));
            choosePattern.add(LockPatternView.Cell.of(0, 2));
            preferences.edit().putString(BaseApplication.LOCK_KEY, LockPatternView.patternToString(choosePattern)).commit();
        }
    }

    private void initSeekBar() {
        //volume_seekbar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volume_seekbar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        try {
            mIsAutoMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

            if (!mIsAutoMode) {
                // 获取系统亮度
                int brightness = Settings.System.getInt(BaseApplication.getContext().getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, 100);
                brightness_seekbar.setProgress(brightness);
            } else {
                Toast.makeText(BaseApplication.getContext(), "Auto mode!!!", Toast.LENGTH_SHORT).show();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void changeCurrentBrightness(int progress) {

        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
//        WindowManager.LayoutParams windowLayoutParams = getWindow().getAttributes();
//        float brightnessPercentage = (float) progress / 255;
//        if (brightnessPercentage > 0 && brightnessPercentage <= 1) {
//            windowLayoutParams.screenBrightness = brightnessPercentage;
//        }
//        this.getWindow().setAttributes(windowLayoutParams);
    }

    public class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * (non-Javadoc)
         *
         * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int viewId = seekBar.getId();
            switch (viewId) {
                case R.id.brightness_seekbar:
                    // save brightness to system settings
                    Settings.System.putInt(BaseApplication.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
                    // change current window's brightness
                    // Toast.makeText(TestActivity.this,"progress is:"+progress,Toast.LENGTH_SHORT).show();

                    changeCurrentBrightness(progress);
                    break;
                case R.id.volume_seekbar:
                    // save brightness to system settings
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
                    // change current window's brightness
                    break;
                default:
                    break;
            }
        }

        /**
         * (non-Javadoc)
         *
         * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        /**
         * (non-Javadoc)
         *
         * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Toast.makeText(BaseApplication.getContext(), "Value: " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断，如果点击时间为按下BACK键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent mIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(mIntent);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
