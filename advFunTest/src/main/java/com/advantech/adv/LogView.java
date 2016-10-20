package com.advantech.adv;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advantech.advfuntest.R;

public class LogView extends Activity {

    public ExecCmd execcmd;
    public LinearLayout backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_view);
        execcmd = new ExecCmd();
        TextView Title = (TextView) findViewById(R.id.title_text);
        Title.setText("Test Log File");

        TextView logText = (TextView) findViewById(R.id.logtextView);
        logText.setText(execcmd.readLog());

        backBtn = (LinearLayout) findViewById(R.id.title_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
