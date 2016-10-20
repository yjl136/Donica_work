package com.advantech.adv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.advantech.advfuntest.R;


public class Backlight_View extends Activity implements OnSeekBarChangeListener {

    private final static String TAG = "Backlight_View";
    private SeekBar seekBar;
    private TextView mTextView;
    private LinearLayout backBtn;
    private Resources res;
    public Intent intent;
    public ExecCmd execcmd;
    public int isautotest = 0;
    private int mOldBrightness = 0;
    private static final int MINIMUM_BACKLIGHT = 0;
    private static final int MAXIMUM_BACKLIGHT = 255;
    private boolean isRun = true;
    private boolean isAutoBrightness = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.backlight_view);
        res = getResources();
        TextView Title = (TextView) findViewById(R.id.title_text);
        Title.setText("Backlight demo");
        execcmd = new ExecCmd();
        seekBar = (SeekBar) findViewById(R.id.bl_seekbar);
        mTextView = (TextView) findViewById(R.id.bl_textview);
        backBtn = (LinearLayout) findViewById(R.id.title_back);
        initBrightness();


        seekBar.setOnSeekBarChangeListener(this);
        backBtn.setOnClickListener(new OnClickListener() {

            //@Override  
            public void onClick(View v) {
                new AlertDialog.Builder(Backlight_View.this)
                        .setTitle(res.getString(R.string.backlight_title))
                        .setMessage(res.getString(R.string.backlight_msg))
                        .setCancelable(false)
                        .setPositiveButton(res.getString(R.string.yes), dialogClick)
                        .setNegativeButton(res.getString(R.string.no), dialogClick)
                        .create()
                        .show();
            }
        });


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
            execcmd.writeLog("LCD Test --> Backlight Test:PASS\n");
            execcmd.writeLog("====================================\n");
        } else {
            execcmd.writeLog("====================================\n");
            execcmd.writeLog("LCD Test --> Backlighth Test:FAILED\n");
            execcmd.writeLog("====================================\n");
        }
        if (isAutoBrightness)
            startAutoBrightness(this);
        else
            setBrightness(Backlight_View.this, mOldBrightness);
/*
        if(1==isautotest)
        	intent.putExtra("isauto", 1); 
        else
        	intent.putExtra("isauto", 0);					        
        intent.putExtra("backlight", flag);					      
        intent.putExtra("lcdcode", 2); 
        //设置返回数据   
        setResult(0, intent); 
*/
        finish();
    }

    //@Override  
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        //int step = 25;  
        //保存为系统亮度
        //Settings.System.putInt(Backlight_View.this.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, progress + MINIMUM_BACKLIGHT);
        setBrightness(Backlight_View.this, progress + MINIMUM_BACKLIGHT);
        mTextView.setText("" + progress * 100 / 255 + "%");
    }

    //@Override  
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    //@Override  
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    public static boolean isAutoBrightness(Activity activity) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    public static void setBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);

    }

    public static void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    public static void startAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    private void initBrightness() {
        // 获取当前背光亮度                                       
        mOldBrightness = getScreenBrightness(this);
        isAutoBrightness = isAutoBrightness(this);
        if (isAutoBrightness)
            stopAutoBrightness(this);
        seekBar.setMax(255);
        seekBar.setProgress(mOldBrightness);
        mTextView.setText("" + mOldBrightness * 100 / 255 + "%");

        intent = getIntent();
        isautotest = intent.getIntExtra("isautotest", 0);
        if (1 == isautotest) {
            new Thread(new Runnable() {
                int value = 0;

                public void run() {
                    while (isRun) {
                        try {
                            Thread.sleep(150);
                            value = value + 5;
                            Message message = new Message();
                            message.what = value;
                            handler.sendEmptyMessage(message.what);
                            if (value > 255)
                                isRun = false;
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }).start();
        }
        //Log.v("Brightness max", "setmax:" + (MAXIMUM_BACKLIGHT - MINIMUM_BACKLIGHT));  
        //seekBar.setProgress(mOldBrightness - MINIMUM_BACKLIGHT);  
        //seekBar.setProgress(mOldBrightness);
        //mTextView.setText(""+mOldBrightness*100/255+"%");
        //seekBar.setProgress(0);                    

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what <= MAXIMUM_BACKLIGHT) {
                seekBar.setProgress(msg.what);
                //保存为系统亮度
                //Settings.System.putInt(BacklightActivity.this.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, msg.what + MINIMUM_BACKLIGHT);       
                setBrightness(Backlight_View.this, msg.what);
            } else {
                //isRun = false;
                backBtn.performClick();
            }

        }

        ;
    };


}  
