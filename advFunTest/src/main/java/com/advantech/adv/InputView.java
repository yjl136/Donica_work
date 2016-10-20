package com.advantech.adv;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.advantech.advfuntest.R;

public class InputView extends Activity {

    public LinearLayout backBtn;
    public static ToggleButton toggleBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_view);//
        TextView Title = (TextView) findViewById(R.id.title_text);
        Title.setText("Input Enable/Disable Demo");

        Log.e("input", "onCreate");
        OnCheckedChangeListener funclistener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleBt.setChecked(isChecked);
                Log.e("input", "checked is " + isChecked);
                if (isChecked) {
                    Log.e("input", "enabled");
                    SystemProperties.set("adv.input", "enable");
                } else {
                    Log.e("input", "disabled");
                    SystemProperties.set("adv.input", "off");
                }

            }
        };

        toggleBt = (ToggleButton) findViewById(R.id.toggleButton);//
        toggleBt.setOnCheckedChangeListener(funclistener);//

        backBtn = (LinearLayout) findViewById(R.id.title_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
