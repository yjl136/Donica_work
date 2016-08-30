package cn.donica.slcd.dmanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cn.donica.slcd.dmanager.R;
import cn.donica.slcd.dmanager.service.TestService;
import cn.donica.slcd.dmanager.snmp.SnmpAgent;
import cn.donica.slcd.dmanager.utils.LogUtil;
import cn.donica.slcd.dmanager.utils.NetworkUtil;
import cn.donica.slcd.dmanager.utils.PropertiesUtil;

/**
 * Class:		MainActivity
 * Description:
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();
    private SnmpAgent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText((new PropertiesUtil()).getProperty("snmp.agent.address"));
        Intent newIntent = new Intent(MainActivity.this, TestService.class);
        startService(newIntent);
        Thread thread = new Thread() {
            public void run() {
                LogUtil.exec(LogUtil.LOGCAT_TO_FILE);
            }
        };
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startAgent(View view) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                if (agent == null)
                    agent = new SnmpAgent(MainActivity.this);
                if (!agent.isStarted()) {
                    agent.start();
                    Log.i(TAG, "agent is started");
                } else {
                    Log.i(TAG, "agent is already started");
                }
            }
        };
        thread.start();
        Log.i(TAG, "IP: " + NetworkUtil.getWiFiIPAddress(this));
    }

    public void stopAgent(View view) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                if (agent != null && agent.isStarted())
                    agent.stop();
                Log.i(TAG, "agent is stopped");
            }
        };
        thread.start();
    }

    public void showTest(View view) {
        Intent intent = new Intent(this, BiteActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (agent != null)
            agent.stop();
        super.onDestroy();
    }
}
