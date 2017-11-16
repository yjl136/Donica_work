package cn.donica.slcd.ble.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SerialCache {
    private static final String SerialCache_Key = "serialCache_Key";
    public static final String SPF_NAME = "HSJReaderDemo_SPF";

    public static void saveSerial(Context context, int serial) {
        if (context == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SPF_NAME,
                0);
        Editor editor = sp.edit();
        editor.putInt(SerialCache_Key, serial);
        editor.commit();
    }

    public static int getSerial(Context context) {
        if (context == null) {
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences(SPF_NAME,
                0);
        return sp.getInt(
                SerialCache_Key, 0);
    }

}
