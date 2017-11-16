package cn.donica.slcd.settings.bite;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-22 15:59
 * Describe:
 */

public class BiteHelper {
    /**
     * 将kb转换为mb
     *
     * @param src
     * @return
     */
    public static String kb2mb(String src) {
        Double srcValue = Double.valueOf(src);
        if (srcValue >= 1024 && srcValue < 1024 * 1024) {
            double destValue = Math.ceil(srcValue / 1024);
            return String.valueOf(destValue) + "MB";
        } else if (srcValue >= 1024 * 1024) {
            double destValue = Math.ceil(srcValue / 1024 / 1024);
            return String.valueOf(destValue) + "GB";
        } else {
            double destValue = Math.ceil(srcValue);
            return String.valueOf(destValue) + "KB";
        }


    }

    /**
     * 格式化温度
     *
     * @param src
     * @return
     */
    public static String formatTemperature(String src) {
        Double srcValue = Double.valueOf(src);
        double destValue = Math.ceil(srcValue);
        return String.valueOf(destValue) + "℃";
    }

    /**
     * 格式化cpu使用率
     *
     * @param src
     * @return
     */
    public static String formatCPU(String src) {
        Double srcValue = Double.valueOf(src);
        double destValue = Math.ceil(srcValue);
        return String.valueOf(destValue) + "%";
    }

}
