package cn.donica.slcd.settings.ui;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import cn.donica.slcd.settings.BaseApplication;
import cn.donica.slcd.settings.R;

public class MainActivity extends Activity {
    private AudioManager mAudioManager;
    private boolean mIsAutoMode = true;
    private SeekBar brightness_seekbar, volume_seekbar;
    private ImageView moreLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        brightness_seekbar = (SeekBar) findViewById(R.id.brightness_seekbar);
        volume_seekbar = (SeekBar) findViewById(R.id.volume_seekbar);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initSeekBar();
        moreLv = (ImageView) findViewById(R.id.moreLv);
        moreLv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LockActivity.class);
                MainActivity.this.startActivity(intent);
                //  MainActivity.this.finish();
            }
        });
        setFinishOnTouchOutside(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      /*  if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {
        String title = "<h5>" + getString(R.string.volume_and_brightness) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();
    }



    private void initSeekBar() {
        volume_seekbar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volume_seekbar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        brightness_seekbar.setMax(255);
        try {
            mIsAutoMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
            if (!mIsAutoMode) {
                // 获取系统亮度
                int brightness = Settings.System.getInt(BaseApplication.getContext().getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, 100);
                if (brightness >= 255) {
                    brightness = 255;
                }
                if (brightness <= 10) {
                    brightness = 10;
                }
                brightness_seekbar.setProgress(brightness);
            } else {
                Toast.makeText(BaseApplication.getContext(), "Auto mode!!!", Toast.LENGTH_SHORT).show();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        brightness_seekbar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        volume_seekbar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }

    private void changeCurrentBrightness(int progress) {
        if (progress >= 255) {
            progress = 255;
        }
        if (progress <= 10) {
            progress = 10;
        }
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
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
                    changeCurrentBrightness(progress);
                    break;
                case R.id.volume_seekbar:
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
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
            //   Toast.makeText(BaseApplication.getContext(), "Value: " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断，如果点击时间为按下BACK键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
