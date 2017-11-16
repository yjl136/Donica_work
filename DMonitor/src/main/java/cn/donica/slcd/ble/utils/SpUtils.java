package cn.donica.slcd.ble.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-07 13:51
 * Describe:
 */

public class SpUtils {
    public final static String FILENAME = "config";
    public final static String KEY_PA = "pa";
    public final static String KEY_VA = "va";

    /**
     * 存储pa状态看是否处于pa
     *
     * @param context
     * @param pa      1：表示处于pa状态 0：表示不是处于pa状态
     */
    public static void putPA(Context context, int pa) {
        SharedPreferences sp = context.getSharedPreferences(FILENAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_PA, pa);
        editor.commit();
    }

    /**
     * 存储pa状态看是否处于VA
     *
     * @param context
     * @param va      1：表示处于VA状态 0：表示不是处于VA状态
     */
    public static void putVA(Context context, int va) {
        SharedPreferences sp = context.getSharedPreferences(FILENAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_VA, va);
        editor.commit();
    }

    /**
     * 获取PA状态
     *
     * @param context
     * @return 返回PA当前的值
     */
    public static int getPA(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILENAME, Context.MODE_MULTI_PROCESS);
        return sp.getInt(KEY_PA, 0);
    }

    /**
     * 获取PA状态
     *
     * @param context
     * @return 返回VA当前的值
     */
    public static int getVA(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILENAME, Context.MODE_MULTI_PROCESS);
        return sp.getInt(KEY_VA, 0);
    }

}
