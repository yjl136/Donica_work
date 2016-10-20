package com.advantech.adv;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advantech.advfuntest.R;

public class FuncView extends Activity {

    public LinearLayout backBtn;
    public static TextView txbacklight;
    public static TextView txproper;
    public static TextView txvolume;
    public static TextView txcamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_view);
        TextView Title = (TextView) findViewById(R.id.title_text);
        Title.setText("Function Examples");

        OnClickListener funclistener = new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_backlight:
                        Intent intent = new Intent(FuncView.this, Backlight_View.class);
                        startActivity(intent);
                        break;
                    case R.id.tv_inputcontrol:
                        Intent intent1 = new Intent(FuncView.this, InputView.class);
                        startActivity(intent1);
                        break;
                    case R.id.tv_volume:
                        Intent intent2 = new Intent(FuncView.this, Volume_View.class);
                        startActivity(intent2);
                        break;
                    /*
                                        case R.id.tv_camera:
						Intent intent3 = new Intent(FuncView.this, CameraFun.class);
						startActivity(intent3);
                                                break;
					*/
                }
            }
        };
        //test backlight
        txbacklight = (TextView) findViewById(R.id.tv_backlight);
        txbacklight.setOnClickListener(funclistener);
        //test input enable/disable
        txproper = (TextView) findViewById(R.id.tv_inputcontrol);
        txproper.setOnClickListener(funclistener);
        //test volume
        txvolume = (TextView) findViewById(R.id.tv_volume);
        txvolume.setOnClickListener(funclistener);
        //test camera
        //txcamera =  (TextView)findViewById(R.id.tv_camera);
        //txcamera.setOnClickListener(funclistener);

        backBtn = (LinearLayout) findViewById(R.id.title_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
