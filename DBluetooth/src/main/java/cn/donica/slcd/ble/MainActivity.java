package cn.donica.slcd.ble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button openBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openBt = (Button) findViewById(R.id.detectBt);
        openBt.setOnClickListener(this);
        /// getSystemService()
    }

    @Override
    public void onClick(View v) {
      /* Intent intent = new Intent(this, DetectService.class);
        startService(intent);*/
    }

}
