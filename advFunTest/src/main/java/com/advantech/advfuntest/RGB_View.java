package com.advantech.advfuntest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class RGB_View extends Activity {

    private RelativeLayout rgbView;
    private int currentIndex = 0;

    void sleep(int x) {
        try {
            Thread.currentThread().sleep(x);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (currentIndex) {
                case 0:
                    rgbView.setBackgroundColor(Color.RED);
                    break;
                case 1:
                    rgbView.setBackgroundColor(Color.GREEN);
                    break;
                case 2:
                    rgbView.setBackgroundColor(Color.BLUE);
                    break;
            }
        }
    };

    private Thread udiskThread = new Thread(new Runnable() {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            sleep(2000);
            currentIndex++;
            handler.sendEmptyMessage(0);
            sleep(2000);
            currentIndex++;
            handler.sendEmptyMessage(0);
            sleep(2000);
            finish();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
*/
        setContentView(R.layout.rgb_view);
        rgbView = (RelativeLayout) findViewById(R.id.rgbRelativeLayout);

        udiskThread.start();
    }
}
