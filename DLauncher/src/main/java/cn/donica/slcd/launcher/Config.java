package cn.donica.slcd.launcher;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by liangmingjie on 2016/4/8.
 */
public class Config {
    public static String picFile = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/donica/pic/";
    public static int picWidth = 240;
    public static int picHeight = 240;
    /**
     * PNG格式壁纸路径
     */
    public static String pngPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/donica/wallpaper/wallpaper.png";
    /**
     * JPG格式壁纸路径
     */
    public static String jpgPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/donica/wallpaper/wallpaper.jpg";

    /**
     * @return 获取座位位置信息
     */
    public static String getSeatPosition(Context context) {
        String seat = "";
        Uri uri = Uri.parse("content://cn.donica.slcd.provider/config/seat");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex("value");
            seat = cursor.getString(index);
        }
        return seat;
    }

}
