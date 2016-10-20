package com.advantech.adv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.content.Context;
import android.media.AudioManager;

import com.advantech.advfuntest.R;


public class Volume_View extends Activity implements OnSeekBarChangeListener {

    private final static String TAG = "Volume_View";
    private SeekBar seekBar;
    private TextView mTextView;
    private LinearLayout backBtn;
    private Resources res;
    public Intent intent;
    public ExecCmd execcmd;
    private int mOldVolume = 0; //save before volume
    private static final int MINIMUM_VOLUME = 0;
    private static final int MAXIMUM_VOLUME = 100;
    private static final int volume_type = AudioManager.STREAM_MUSIC;//Can be:  STREAM_VOICE_CALL, STREAM_SYSTEM, STREAM_RING or STREAM_ALARM
    private boolean isRun = true;
    private AudioManager mAudioManager;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.volume_view);
        res = getResources();
        TextView Title = (TextView) findViewById(R.id.title_text);
        Title.setText("Volume demo");
        execcmd = new ExecCmd();
        //
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        seekBar = (SeekBar) findViewById(R.id.vl_seekbar);
        mTextView = (TextView) findViewById(R.id.vl_textview);
        backBtn = (LinearLayout) findViewById(R.id.title_back);
        mOldVolume = getVolume(volume_type);


        seekBar.setOnSeekBarChangeListener(this);
        backBtn.setOnClickListener(new OnClickListener() {

            //@Override  
            public void onClick(View v) {
                new AlertDialog.Builder(Volume_View.this)
                        .setTitle(res.getString(R.string.volume_title))
                        .setMessage(res.getString(R.string.volume_msg))
                        .setCancelable(false)
                        .setPositiveButton(res.getString(R.string.yes), dialogClick)
                        .setNegativeButton(res.getString(R.string.no), dialogClick)
                        .create()
                        .show();
            }
        });


    }


    private int getVolume(int type) {
        int ret;
        ret = mAudioManager.getStreamVolume(type);
        return ret;
    }

    private void setVolume(int type, int value) {
        mAudioManager.setStreamVolume(type, value, 0);
    }

    private android.content.DialogInterface.OnClickListener dialogClick = new android.content.DialogInterface.OnClickListener() {

        //@Override  
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    resultRquest(true);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    resultRquest(false);
                    break;
                default:
                    break;
            }
        }
    };

    private void resultRquest(boolean flag) {
        if (flag == true) {
            execcmd.writeLog("====================================\n");
            execcmd.writeLog("VOLUME Test --> Volume Test:PASS\n");
            execcmd.writeLog("====================================\n");
        } else {
            execcmd.writeLog("====================================\n");
            execcmd.writeLog("VOLUME Test --> Volumeh Test:FAILED\n");
            execcmd.writeLog("====================================\n");
        }

        setVolume(volume_type, mOldVolume);
        finish();
    }

    //@Override  
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        setVolume(volume_type, max * progress / 100);
        mTextView.setText("" + progress + "%");
    }

    //@Override  
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    //@Override  
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


}  
