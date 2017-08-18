package cn.donica.slcd.launcher.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.donica.slcd.launcher.BaseApplication;
import cn.donica.slcd.launcher.model.AppsModel;
import cn.donica.slcd.launcher.util.LogUtil;

@SuppressLint("SdCardPath")
public class SqliteHelperUtil {

    private Context sqliteContext;
    private DataBaseHelper dbHelper;
    private SQLiteDatabase mSqliteDB = null;
    private static final String TAG = "SqliteHelperUtil";

    public SqliteHelperUtil(Context context) {
        this.sqliteContext = context;
    }

    /**
     * 打开数据库
     */
    public void open() {
        LogUtil.d(TAG, "open()...打开数据库");
        dbHelper = new DataBaseHelper(sqliteContext);
        // 如果你使用的是将数据库的文件创建在SD卡中，那么创建数据库SQLite如下操作：
        // mSqliteDB = SQLiteDatabase.openOrCreateDatabase(f, null);
        // 如果想把数据库文件默认放在系统中,那么创建数据库SQLite如下操作
        mSqliteDB = dbHelper.getWritableDatabase();
    }

    /**
     * 关闭数据库
     */
    private void close() {
        mSqliteDB.close();
    }

    /**
     * 将头像转换成byte[]以便能将图片存到数据库
     *
     * @param drawalbe
     * @return
     */
    public byte[] getBitmapByte(Drawable drawalbe) {
        BitmapDrawable db = (BitmapDrawable) drawalbe;
        Bitmap bitmap = db.getBitmap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 查询所有
     *
     * @return
     */
    public Cursor queryAllData() {
        open();
        Cursor cursor = mSqliteDB.query(DbHelperColumn.TABLE_NAME,
                DbHelperColumn.SQLITE_ALL_PROJECTION, null, null, null, null,
                null);
        close();
        return cursor;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public Cursor queryAllDataBySqlStr() {
        open();
        String sqlStr = "select * from " + DbHelperColumn.TABLE_NAME + ";";
        Cursor cursor = mSqliteDB.rawQuery(sqlStr, null);
        LogUtil.d(TAG, "queryAllDataBySqlStr()...cursor.getCount()" + cursor.getCount());
        close();
        return cursor;
    }

    /**
     * 查询带有图片的数据
     *
     * @return
     */
    public Cursor queryDataWithPic() {
        open();
        String sqlStr = "select * from " + DbHelperColumn.TABLE_NAME + "WHERE " + DbHelperColumn.PIC_SIGN + "=true And " + "WHERE " + DbHelperColumn.DISPLAY + " = true order by " + DbHelperColumn.NUMBER + "asc," + DbHelperColumn.APP_ID + " asc;";
        Cursor cursor = mSqliteDB.rawQuery(sqlStr, null);
        LogUtil.d(TAG, "queryAllDataBySqlStr()...cursor.getCount()" + cursor.getCount());
        close();
        return cursor;
    }

    /**
     * 查询无图片的数据
     *
     * @return
     */
    public Cursor queryDataWithoutPic() {
        open();
        String sqlStr = "select * from " + DbHelperColumn.TABLE_NAME + " WHERE " + DbHelperColumn.PIC_SIGN + "=false And " + "WHERE " + DbHelperColumn.DISPLAY + " = true order by " + DbHelperColumn.NUMBER + "asc," + DbHelperColumn.APP_ID + " asc;";
        Cursor cursor = mSqliteDB.rawQuery(sqlStr, null);
        LogUtil.d(TAG, "queryAllDataBySqlStr()...cursor.getCount()" + cursor.getCount());
        close();
        return cursor;

    }

    /**
     * 查询所有
     *
     * @return
     */
    public Cursor queryAllDataBySqlStrBySort() {
        open();
        //   select * from tree order by a desc,b desc
        //   先按a排序，如果两条记录a字段相同再按照b排序
        String sqlStr = "select * from " + DbHelperColumn.TABLE_NAME + " WHERE " + DbHelperColumn.DISPLAY + " = 1 order by " + DbHelperColumn.NUMBER + " asc," + DbHelperColumn.APP_ID + " asc;";
        Cursor cursor = mSqliteDB.rawQuery(sqlStr, null);
        LogUtil.d(TAG, "queryAllDataBySqlStr()...cursor.getCount()" + cursor.getCount());
        close();
        return cursor;
    }

    /**
     * 通过页数分页
     *
     * @param pageID
     * @return
     */
    public Cursor queryPageById(int pageID) {
        open();
        String sql = "select * from " + DbHelperColumn.TABLE_NAME + " Limit "
                + String.valueOf(DbHelperColumn.PAGESIZE) + " offset "
                + String.valueOf(pageID * DbHelperColumn.PAGESIZE) + " ;";
        //select * from launcher limit 10 offset pageID *10 ；
        //sql语句，其中LIMIT 10 OFFSET pageID *10的意思是说在查询结果中以第pageID *10条记录为基准（包括第pageID *10条），取10条记录.
        Cursor cursor = mSqliteDB.rawQuery(sql, null);
        cursor.getCount();
        close();
        return cursor;
    }


    /**
     * 插入应用程序数据
     *
     * @param model
     */
    public long insertAppInfo(AppsModel model) {
        if ("com.android.inputmethod.latin".equals(DbHelperColumn.PACKAGENAME)) {
            return 0;
        }
        open();
        long i = 0;
        ContentValues content = new ContentValues();
        content.put(DbHelperColumn.MAIN_ACTIVITY_NAME, model.getActivity_Name().toString());
        content.put(DbHelperColumn.PACKAGENAME, model.getPackage_Name());
        // content.put(DbHelperColumn.ACTIVITY_CATEGORY, model.getCategory());
        // content.put(DbHelperColumn.ACTIVITY_DESCRIPTION, model
        // .getDescription());
        if (BaseApplication.APP_ADD != true) {
            i = mSqliteDB.insert(DbHelperColumn.TABLE_NAME, null, content);
        } else {
            BaseApplication.APP_ADD = false;
        }
        LogUtil.d(TAG, "insertAppInfo()...+content=" + content);
        close();
        return i;
    }

    /**
     * 通过包名修改应用程序的类别ID
     *
     * @param categoryId
     * @param packageName
     */
    // public int updateAPPCategory(int categoryId, String Name) {
    // open();
    // ContentValues content = new ContentValues();
    // content.put(DbHelperColumn.ACTIVITY_CATEGORY, categoryId);
    // int i = mSqliteDB.update(DbHelperColumn.TABLE_NAME, content,
    // DbHelperColumn.MAIN_ACTIVITY_NAME + " =?", new String[] { Name });
    // close();
    // return i;
    // }

    /**
     * 通过包名删除对应的应用程序数据
     *
     * @param Name
     * @return
     */
    public int deleteAppByPackageName(String Name) {
        open();
        int i = mSqliteDB.delete(DbHelperColumn.TABLE_NAME,
                DbHelperColumn.MAIN_ACTIVITY_NAME + " =?", new String[]{Name});
        close();
        return i;
    }

    /**
     * 通过包名判断数据库中是否存在此数据
     *
     * @param Name
     * @return
     */
    public boolean isExistsByPackageName(String Name) {
        open();
        boolean isExist = true;
        Cursor cursor = mSqliteDB.query(DbHelperColumn.TABLE_NAME,
                DbHelperColumn.SQLITE_PACKAGENAME_PROJECTION,
                DbHelperColumn.MAIN_ACTIVITY_NAME + " =?", new String[]{Name},
                null, null, null);
        if (!cursor.moveToFirst()) {
            isExist = false;
        }
        cursor.close();
        close();
        return isExist;
    }

    /**
     * 查找所有数据库所有存在的包名
     *
     * @return
     */
    public String[] getAllName() {
        open();
        Cursor cursor = mSqliteDB.query(DbHelperColumn.TABLE_NAME, new String[]{DbHelperColumn.MAIN_ACTIVITY_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int count = (int) cursor.getCount();
            String[] Names = new String[count];
            for (int i = 0; i < count; i++) {
                Names[i] = String.valueOf(cursor
                        .getColumnIndex(DbHelperColumn.MAIN_ACTIVITY_NAME));
            }
            cursor.close();
            close();
            return Names;
        } else {
            cursor.close();
            close();
            return null;
        }
    }


    /**
     * 返回应用的总数
     *
     * @return
     */
    public int getAppsCount() {
        open();
        int i;
        Cursor curosor = mSqliteDB.query(DbHelperColumn.TABLE_NAME,
                new String[]{DbHelperColumn.PACKAGENAME}, null,
                null, null, null, null);
        try {
            curosor.moveToLast();
            i = (int) curosor.getCount();
        } finally {
            curosor.close();
        }
        close();
        return i;
    }

    /**
     * 返回应用的总数
     *
     * @return
     */
    public int getAppsSize() {
        open();
        int count;
        Cursor cursor = mSqliteDB.rawQuery("select * from launcher where display =?", new String[]{"1"});
        try {
            count = cursor.getCount();
        } finally {
            cursor.close();
        }
        close();
        return count;
    }

    /**
     * 获得总分页数
     *
     * @return
     */
    public List<HashMap<String, String>> getPage() {

        int count = getAppsCount();
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        int page = (int) (Math
                .ceil((double) ((double) count / (double) DbHelperColumn.PAGESIZE)));

        int pageNum = page; // count % 10 == 0 ? page : page + 1; // 取得页数

        for (int i = 0; i < pageNum; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (i <= 8) {
                map.put("num", " " + String.valueOf(i + 1) + " ");
            } else {
                map.put("num", String.valueOf(i + 1));
            }
            // map.put("num", String.valueOf(10));
            list.add(map);
        }
        return list;

    }


}
