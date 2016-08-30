/**
 * File Name: CrashHandler.java
 * Description：Handle exceptions when the application is crashed
 * Author: Luke Huang
 * Create Time: 2015-7-24
 * <p>
 * For the SLCD Project
 * Copyright © 2015 Donica.cn All rights reserved
 */
package cn.donica.slcd.dmanager.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.donica.slcd.dmanager.R;

/**
 * Description:	Handle exceptions when the application is crashed
 *
 * @author Luke Huang 2015-7-24
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";

    // make it as default handler
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // single instance
    private static CrashHandler INSTANCE = new CrashHandler();
    // context
    private Context mContext;
    // used to store information about device and exception
    private Map<String, String> mCrashInfos = new HashMap<String, String>();

    // date format for local
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
            Locale.getDefault());

    /**
     * private constructor for single instance pattern
     */
    private CrashHandler() {

    }

    /**
     * Description: get the single CrashHandler instance
     *
     * @return CrashHandler instance
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Description: Initialize the context and set system's default handler
     *
     * @param context
     */
    public void init(Context context) {
        this.mContext = context;

        // get the system's default handler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // set this as default handler
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * (non-Javadoc)
     *
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // use the default handler to handle uncaught exception
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Uncaught Exception make the application crashed : ", e);
            }
            // exit application
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * Handle uncaught exception, collect device information, and save it to local log file.
     *
     * @param e uncaught exception
     * @return true: handle it successfully; false: failed to handle it.
     */
    private boolean handleException(Throwable e) {
        if (e == null) {
            return false;
        }

        // use toast to show the crash message friendly
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "Sorry! The application is crashed.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        // collect device information
        collectDeviceInfo();

        // save the exception message to local file
        saveCrashInfoToFile(e);
        return true;
    }

    /**
     * Collect device information about the application, such as application version, package name, build and so on.
     *
     * @param
     */
    public void collectDeviceInfo() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mCrashInfos.put("versionName", versionName);
                mCrashInfos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "An error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mCrashInfos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "An error occured when collect crash info", e);
            }
        }
    }

    /**
     * save crash information to local log file
     *
     * @param uncaughtException
     * @return file name
     */
    private String saveCrashInfoToFile(Throwable uncaughtException) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mCrashInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        uncaughtException.printStackTrace(printWriter);
        Throwable cause = uncaughtException.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = mContext.getString(R.string.crash_log_dir);
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "Write crash information failed", e);
        }
        return null;
    }
}
