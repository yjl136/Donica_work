package cn.donica.slcd.launcher;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.donica.slcd.launcher.db.DbHelperColumn;
import cn.donica.slcd.launcher.util.DbBackupUtil;
import cn.donica.slcd.launcher.util.LogUtil;

public class BaseApplication extends Application implements Thread.UncaughtExceptionHandler {
    private String SD_DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DbHelperColumn.DATABASENAME;
    DbBackupUtil dbBackupUtil;

    private static Context context;
    public static boolean APP_ADD = false;

    private final static String TAG = "BaseApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //设置Thread Exception Handler
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    public static Context getContext() {
        return context;
    }

    /**
     * 判断文件是否存在
     *
     * @param strFile
     * @return
     */
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 判断SD卡的根目录是否存在launcher.db
     *
     * @return
     */
    public boolean sdDbIsExists() {
        try {
            File db = new File(SD_DB_PATH);
            if (!db.exists()) {
                LogUtil.d(TAG, "数据库文件不存在");
                return false;
            } else {
                LogUtil.d(TAG, "数据库文件存在");
                return true;
            }
        } catch (Exception e) {
        }
        return true;
    }


    /**
     * 判断data/data/packagename/files的目录下是否存在文件
     *
     * @return
     */
    public boolean appDbIsExists() {
        try {
//			File db = new File(SD_DB_PATH);
            if ((this.fileList().length == 0)) {
                LogUtil.d(TAG, "没有文件");
                return false;
            } else {
                LogUtil.d(TAG, "文件存在");
                return true;
            }
        } catch (Exception e) {
        }
        return true;
    }

    // 数据恢复
    private void dataRecover() {
        // TODO Auto-generated method stub
        new DbBackupUtil(this).execute("restroeDatabase");
    }

    // 数据备份
    private void dataBackup() {
        // TODO Auto-generated method stub
        new DbBackupUtil(this).execute("backupDatabase");
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }

    /**
     * 根据包名获取主activity名
     *
     * @return className
     */
    public String GetMainActivityNameWithPackageName(Context context, String packagename) {
        String packageName = null;
        String className = null;
        LogUtil.d(TAG, "GetMainActivityNameWithPackageName方法执行");
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            LogUtil.d(TAG, "packageinfo1 = " + packageinfo);
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
            LogUtil.d(TAG, "packageinfo2 = " + packageinfo);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            LogUtil.d(TAG, "packageinfo == null");
            return null;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//			// 设置ComponentName参数1:packagename参数2:MainActivity路径
//			ComponentName cn = new ComponentName(packageName, className);
//
//			intent.setComponent(cn);
//			startActivity(intent);
            LogUtil.d(TAG, "className1 = " + className);
            return className;
        }
        LogUtil.d(TAG, "className2 = " + className);
        return className;

    }

    /**
     * 根据包名启动应用
     *
     * @return className
     */
    public void openApp(Context context, String packageName) {
        final PackageManager packageManager = getPackageManager();
        PackageInfo pi = null;
        String pkgName = null;
        String className = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (pi == null || pi.packageName == null) {
            Toast.makeText(this, "还没安装！", Toast.LENGTH_LONG).show();
            return;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            pkgName = ri.activityInfo.packageName;
            className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(pkgName, className);
            intent.setComponent(cn);
            startActivity(intent);
        }

    }
}
