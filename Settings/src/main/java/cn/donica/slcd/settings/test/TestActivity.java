package cn.donica.slcd.settings.test;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.donica.slcd.settings.R;
import cn.donica.slcd.settings.services.AdminTimerService;
import cn.donica.slcd.settings.utils.AdminTimer;

/**
 * 主界面
 *
 * @author zihao
 */
public class TestActivity extends Activity {

    private Button mGetCodeBtn;// 倒计时按钮
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mGetCodeBtn = (Button) findViewById(R.id.get_code_btn);
//		AdminTimerService.setHandler(mCodeHandler);
        mIntent = new Intent(TestActivity.this, AdminTimerService.class);
        mGetCodeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mGetCodeBtn.setEnabled(false);
                startService(mIntent);
            }
        });
    }

    /**
     * 倒计时Handler
     */
    @SuppressLint("HandlerLeak")
    Handler mCodeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == AdminTimer.IN_RUNNING) {// 正在倒计时
                mGetCodeBtn.setText(msg.obj.toString());
            } else if (msg.what == AdminTimer.END_RUNNING) {// 完成倒计时
                mGetCodeBtn.setEnabled(true);
                mGetCodeBtn.setText(msg.obj.toString());
            }
        }

        ;
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        stopService(mIntent);
    }

}