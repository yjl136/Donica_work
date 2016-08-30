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
        SharedPreferences prefercences;
        String NAME = "ip_seat";
        String tmp_seat = null;
        Context c = null;
        try {
            c = BaseApplication.getContext().createPackageContext("cn.donica.slcd.settings", android.content.Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (c != null) {
            prefercences = c.getSharedPreferences(NAME, android.content.Context.MODE_PRIVATE);
            tmp_seat = prefercences.getString("seat", "");
        }
        if (tmp_seat == null || tmp_seat.equals("")) {
            //返回空字符串
            return "";
            //返回未配置座位信息提示
            // return BaseApplication.getContext().getResources().getString(R.string.seat_number_is_not_configured);
        } else {
            String seat_name = BaseApplication.getContext().getResources().getString(R.string.seat_number);
            return seat_name + tmp_seat;
        }
    }


}
