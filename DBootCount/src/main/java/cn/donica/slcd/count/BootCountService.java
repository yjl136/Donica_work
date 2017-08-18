package cn.donica.slcd.count;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-07-25 10:22
 * Describe:
 */

public class BootCountService extends Service {
    private static String BOOT_NUM_KEY = "count";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initCount();
        saveFile();
        stopSelf();
    }

    private void saveFile() {
        FileOutputStream fos = null;
        try {
            File file = new File(getFilesDir(), "time.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = openFileOutput("time.txt", MODE_APPEND);
            String time = getTime();
            int num = getBootCount();
            String out = "第" + num + "次启动时间：" + time;
            fos.write(out.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCount() {
        SharedPreferences sp = getSharedPreferences("bootcount", Context.MODE_PRIVATE);
        int num = sp.getInt(BOOT_NUM_KEY, 0);
        num++;
        sp.edit().putInt(BOOT_NUM_KEY, num).commit();
    }

    private int getBootCount() {
        SharedPreferences sp = getSharedPreferences("bootcount", Context.MODE_PRIVATE);
        return sp.getInt(BOOT_NUM_KEY, 0);
    }

    private String getTime() {
        Calendar calender = Calendar.getInstance(TimeZone.getDefault());
        calender.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calender.getTime()) + "\n";
    }
}
