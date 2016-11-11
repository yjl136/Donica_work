package cn.donica.slcd.ble.utils;

import android.content.Context;

/**
 * 作者： yejianlin
 * 日期：2016/5/4
 * 作用：单位转换
 */
public class UnitUtils {

    /**
     * 将dp转换为px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px转换为dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}