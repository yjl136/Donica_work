package cn.donica.slcd.dmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.donica.slcd.dmanager.crash.BaseApplication;

/**
 * Created by liangmingjie on 2015/12/15.
 */
public class AppCommonUtil {
    private static SharedPreferences prefercences;
    public static String customTagPrefix = "Donica"; // 自定义Tag的前缀，可以是作者名
    private static String PREF_NAME = "ip_seat";

    /**
     * 获取SLCD的记载服务器IP地址
     *
     * @return
     */
    public static String getIpAddress() {
        Context c = null;
        String tmp_ip = null;
        try {
            c = BaseApplication.getContext().createPackageContext("cn.donica.slcd.settings", android.content.Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (c != null) {
            prefercences = c.getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE);
            tmp_ip = prefercences.getString("IP", "192.168.2.99");
            return tmp_ip;
        }
        return tmp_ip;
    }

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
            tmp_seat = prefercences.getString("seat", "A10");
        }
        return tmp_seat;
    }


    /**
     * 获取Android当前的系统时间
     *
     * @return time
     */
    public static String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(date);
        return time;
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":"
                + tag;
        return tag;
    }

    /**
     * 获取调用记录Log处的类名，方法名，保存Log时就无需手动写各个TAG.
     *
     * @return TAG
     */
    public static String getTag() {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        return tag;
    }

    /**
     * 获取包名
     *
     * @param cxt
     * @return
     */
    private static String getPkgName(Context cxt) {
        PackageManager pManager = cxt.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pManager.getPackageInfo(cxt.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkgInfo.packageName;
    }


    /**
     * @param str 生成MD5校验码
     * @return
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * @return 暂时未具体实现
     */
    public static String getStaticIP() {
        return "192.168.2.102";
    }
}
