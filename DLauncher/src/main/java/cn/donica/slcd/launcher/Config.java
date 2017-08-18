package cn.donica.slcd.launcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

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
    public static String getSeatPosition() {
        String FILENAME = "Ip_And_Seat";
        String QUERY_KEY = "Seat";
        String seat = "";
        Context c = null;
        try {
            c = BaseApplication.getContext().createPackageContext("cn.donica.slcd.settings", android.content.Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (c != null) {
            SharedPreferences prefercences = c.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
            seat = prefercences.getString(QUERY_KEY, "");
        }
        return seat;
    }

}
