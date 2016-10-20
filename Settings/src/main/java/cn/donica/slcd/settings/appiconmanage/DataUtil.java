package cn.donica.slcd.settings.appiconmanage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by liangmingjie on 2016/6/12.
 */
public class DataUtil {

    /**
     * @param context
     */
    public static ArrayList<String> getDisplayApp(Context context) {
        Log.d("DataUtil", "获取显示App");
        ArrayList<String> displayAppList = new ArrayList<String>();

        Uri uri = Uri.parse("content://cn.donica.slcd.launcher.contentprovider/launcher");
        //更新指定记录的display的值
        Cursor cursor = context.getContentResolver().query(uri, null, "display = ?", new String[]{"1"}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String mainActivityName = cursor.getString(cursor.getColumnIndex("mainActivityName"));
                Log.d("DataUtil", "getDisplayApp...mainActivityName:" + mainActivityName);
                String packagename = cursor.getString(cursor
                        .getColumnIndex("packageName"));
                Log.d("DataUtil", "getDisplayApp...packagename:" + packagename);
                displayAppList.add(packagename);
            }
            cursor.close();
        }
        Log.d("DataUtil", "displayAppList:" + displayAppList.toString());
        return displayAppList;
    }


    /**
     * @param context
     */
    public static ArrayList<String> getHiddenApp(Context context) {
        ArrayList<String> hiddenAppList = new ArrayList<String>();
        Uri uri = Uri.parse("content://cn.donica.slcd.launcher.contentprovider/launcher");
        //更新指定记录的display的值
        Cursor cursor = context.getContentResolver().query(uri, null, "display = ?", new String[]{"0"}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String mainActivityName = cursor.getString(cursor.getColumnIndex("mainActivityName"));
                Log.d("DataUtil", "getHiddenApp...mainActivityName:" + mainActivityName);
                String packagename = cursor.getString(cursor
                        .getColumnIndex("packageName"));
                Log.d("DataUtil", "getHiddenApp...packagename:" + packagename);
                hiddenAppList.add(packagename);
            }
            cursor.close();
        }
        Log.d("DataUtil", "hiddenAppList:" + hiddenAppList.toString());
        return hiddenAppList;
    }

    /**
     * 将packageName的记录display字段更新为1
     *
     * @param packageName
     * @param display
     * @param context
     */
    public static void updateDisplay(String packageName, int display, Context context) {

        Uri uri = Uri.parse("content://cn.donica.slcd.launcher.contentprovider/launcher");
        ContentValues values = new ContentValues();
        values.put("display", display);
        //更新指定记录的display的值
        int count = context.getContentResolver().update(uri, values, "packageName = ?", new String[]{packageName});
        //更新全部记录的display的值
        //int count = this.getContentResolver().update(uri, values, null, null);
    }
}
