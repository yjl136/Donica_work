package cn.donica.slcd.launcher;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import cn.donica.slcd.launcher.db.DataBaseHelper;
import cn.donica.slcd.launcher.util.ActivityCollectorUtil;
import cn.donica.slcd.launcher.util.LogUtil;

public class InstallOrUninstallReceiver extends BroadcastReceiver {
    private DataBaseHelper dbHelper;

    private BaseApplication myApplication;
    private static final String TAG = "InstallOrUninstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //应用新增广播，把新增的应用添加
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            myApplication = new BaseApplication();
            String activityName = myApplication.GetMainActivityNameWithPackageName(context, packageName);
            LogUtil.d(TAG, "New ActivityName is :" + activityName);
            if (packageName.contains("com.android.inputmethod")) {
                return;
            }
            dbHelper = new DataBaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sqlStr = "insert into launcher(mainActivityName,packageName) select '" + activityName + "'," + packageName + " where not exists(select * from launcher where name='" + activityName + "' and age='" + packageName + "')";
            LogUtil.d(TAG, "sqlStr" + sqlStr);
            String sql = "REPLACE INTO launcher (mainActivityName,packageName) VALUES ('" + activityName + "','" + packageName + "')";
            db.execSQL(sql);
            BaseApplication.APP_ADD = true;
            db.close();
            ActivityCollectorUtil.finishAll();
        }
        //应用删除时广播，及时把
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            dbHelper = new DataBaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "delete from launcher where packageName = '" + packageName + "'";
            db.execSQL(sql);
            db.close();
            ActivityCollectorUtil.finishAll();
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Toast.makeText(context, packageName + "应用替换", Toast.LENGTH_LONG).show();
//            ActivityCollectorUtil.finishAll();
        }
    }
}

