/**
 *
 */
package cn.donica.slcd.dmanager.utils;

import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.donica.slcd.dmanager.ntp.SntpClient;

/**
 * NTPUtil is to synchronize date and time with head server via NTP protocol
 *
 * @author Luke Huang
 */
public class NTPUtil {
    private static final String TAG = "NTP";
    private static final String DEFAULT_NTP_SERVER = "pool.ntp.org";
    private static final int DEFAULT_NTP_TIMEOUT = 30000;

    public void syncTime() {
        syncTime("192.168.2.99");
    }

    public void syncTime(String host) {
        SntpClient client = new SntpClient();
        if (client.requestTime(host, DEFAULT_NTP_TIMEOUT)) {//sync time with head server
            setSystemTime(client);
        } else if (client.requestTime(DEFAULT_NTP_SERVER, DEFAULT_NTP_TIMEOUT)) {//sync time with standard ntp server
            setSystemTime(client);
        }
    }

    private void setSystemTime(SntpClient client) {
        //elapsedRealtime为自开机后，经过的时间，包括深度睡眠的时间
        long now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
        boolean succeed = SystemClock.setCurrentTimeMillis(now);
        if (succeed) {
            String currentTime = SimpleDateFormat.getDateTimeInstance().format(new Date(now));
            Log.i(TAG, "system current time is set to " + currentTime);
        } else {
            Log.e(TAG, "set system time failed");
        }
    }
}
