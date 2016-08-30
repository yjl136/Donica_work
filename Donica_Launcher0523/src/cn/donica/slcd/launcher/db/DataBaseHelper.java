package cn.donica.slcd.launcher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.donica.slcd.launcher.util.LogUtil;


public class DataBaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DataBaseHelper";

    /**
     * 调用super方法，传入数据库的地址，及版本号
     *
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DbHelperColumn.DATABASENAME, null,
                DbHelperColumn.DATABASE_VERSION);
    }

    /**
     * 创建数据表
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DbHelperColumn.TABLE_NAME + " ("
                + DbHelperColumn.APP_ID + " INTEGER, "
                + DbHelperColumn.MAIN_ACTIVITY_NAME + " varchar(64),"
                + DbHelperColumn.PACKAGENAME + " varchar(64),"
                + DbHelperColumn.NUMBER + " INTEGER(8) DEFAULT 100,"
                + DbHelperColumn.PIC_SIGN + " INTEGER(1) DEFAULT 0,"
                + DbHelperColumn.DISPLAY + " INTEGER(1) DEFAULT 1,"
                + DbHelperColumn.PIC_NAME + " text(20),"
                + "PRIMARY KEY (" + DbHelperColumn.APP_ID + " ASC))"
        );
        LogUtil.d(TAG, "onCreate()...创建数据表");
    }

    /**
     * 更新数据库
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS  " + DbHelperColumn.TABLE_NAME + "";
        db.execSQL(sql);
        onCreate(db);
        LogUtil.d(TAG, "更新数据库");
    }
}
