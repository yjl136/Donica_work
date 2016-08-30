package com.advantech.advfuntest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;
import android.util.Log;

public class HeadsetPlugReceiver extends BroadcastReceiver {

    private static final String TAG = "HeadsetPlugReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("state")) {
            if (intent.getIntExtra("state", 0) == 0) {
                Toast.makeText(context, "headset not connected", Toast.LENGTH_LONG).show();
            } else if (intent.getIntExtra("state", 0) == 1) {
                Toast.makeText(context, "headset connected", Toast.LENGTH_LONG).show();
            }
        }

    }

}

