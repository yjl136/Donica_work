package cn.donica.slcd.launcher;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.donica.slcd.launcher.db.DataBaseHelper;
import cn.donica.slcd.launcher.util.LogUtil;

public class AppsContentProvider extends ContentProvider {
    private DataBaseHelper dbHelper;
    private static UriMatcher uriMatcher;
    private static final String TAG = "AppsContentProvider";
    public static final int LAUNCHER_DIR = 0;
    public static final int LAUNCHER_ITEM = 1;
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.donica.launcher";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.donica.launcher";
    public static final String AUTHORITY = "cn.donica.slcd.launcher.contentprovider";

    static {
        //常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "launcher", LAUNCHER_DIR);
        uriMatcher.addURI(AUTHORITY, "launcher/#", LAUNCHER_ITEM);
    }

    /*
   * 第一次创建该ContentProvider时调用此方法
   *
   * @see android.content.ContentProvider#onCreate()
   */
    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        dbHelper = new DataBaseHelper(this.getContext());
        return true;

    }

    /*
 * 该方法的返回值代表了该ContentProvider所提供数据的MIME类型
 *
 * @see android.content.ContentProvider#getType(android.net.Uri)
 */
    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        switch (uriMatcher.match(uri)) {
            case LAUNCHER_DIR:
                return "vnd.android.cursor.dir/vnd.cn.donica.slcd.launcher.contentprovider.launcher";
            case LAUNCHER_ITEM:
                return "vnd.android.cursor.item/vnd.cn.donica.slcd.launcher.contentprovider.launcher";
        }
        return null;
    }

    /*
     * 实现了删除方法，该方法应该返回删除的记录条数
     * @see android.content.ContentProvider#delete(android.net.Uri,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        LogUtil.d(TAG, "delete with uri: " + uri.toString());
        //Log的内容 ： delete with uri: content://cn.donica.launcher.contentprovider/launcher/packagename
        LogUtil.d(TAG, "selection的值为：" + selection);
        //Log的内容为：selection的值为：cn.donica.launcher
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        if (uri.toString().equals("content://cn.donica.slcd.launcher.contentprovider/launcher/packagename")) {
            LogUtil.d(TAG, "delete sql");
            String sql = "delete from launcher where packageName = '" + selection + "'";
            LogUtil.d(TAG, "sql语句：" + sql);
            db.execSQL(sql);
            deletedRows = db.delete("launcher", "packageName = " + selection, selectionArgs);
        }
        switch (uriMatcher.match(uri)) {
            case LAUNCHER_DIR:
                deletedRows = db.delete("launcher", selection, selectionArgs);
                break;
            case LAUNCHER_ITEM:
                String sql = "delete from launcher where packageName=" + selection;
                db.execSQL(sql);
//SQLiteDatabase.delete("users", "user_name = ?", new String[] {"Talib"});		    
                deletedRows = db.delete("launcher", "packageName = ?", selectionArgs);
                break;
            default:
                break;
        }


        return deletedRows;
    }


    /*
     * 实现了插入的方法，该方法应该返回新插入的记录的Uri
     *
     * @see android.content.ContentProvider#insert(android.net.Uri,
     * android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case LAUNCHER_DIR:
            case LAUNCHER_ITEM:
                long Id = db.insert("launcher", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/launcher/" + Id);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numValues = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction(); //开始事务
        try {
            //数据库操作
            numValues = values.length;
            for (int i = 0; i < numValues; i++) {
                insert(uri, values[i]);
            }
            db.setTransactionSuccessful(); //别忘了这句 Commit
        } finally {
            db.endTransaction(); //结束事务
        }
        return numValues;
    }

    /*
     * 实现查询方法，该方法应该返回查询到的Cursor 从内容提供器中查询数据。使用 uri 参数来确定查询哪张表， projection
     * 参数用于确定查询哪些列， selection 和 selectionArgs 参数用于约束查询哪些行， sortOrder
     * 参数用于对结果进行排序， 查询的结果存放在 Cursor 对象中返回
     * @see android.content.ContentProvider#query(android.net.Uri,
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        LogUtil.d(TAG, uri + "===query 方法被调用===");
        LogUtil.d(TAG, "selection的参数为：" + selection);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case LAUNCHER_DIR:
                LogUtil.d(TAG, "LAUNCHER_ITEM的参数为：" + LAUNCHER_ITEM);
                cursor = db.query("launcher", projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case LAUNCHER_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("launcher", projection, "id = ?",
                        new String[]{bookId}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    /*
     * 实现了更新方法，该方法应该返回更新的记录条数
     *
     * @see android.content.ContentProvider#update(android.net.Uri,
     * android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case LAUNCHER_DIR:
                LogUtil.d(TAG, "LAUNCHER_DIR");
                updatedRows = db.update("launcher", values, selection,
                        selectionArgs);
                break;
            case LAUNCHER_ITEM:
                LogUtil.d(TAG, "LAUNCHER_ITEM");
                long _id = ContentUris.parseId(uri);
                selection = "_id = ?";
                selectionArgs = new String[]{String.valueOf(_id)};
                break;
            default:
                break;
        }
        return updatedRows;
    }
}
