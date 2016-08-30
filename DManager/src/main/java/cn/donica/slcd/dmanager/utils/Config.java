package cn.donica.slcd.dmanager.utils;

import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by liangmingjie on 2015/12/7.
 */
public class Config {

    private static SharedPreferences prefercences;

    /**
     * 保存IP地址和座位号的SharedPreferences文件名
     */
    private static String PREF_NAME = "id_seat";
    /**
     * CPU温度告警阀值一，现为85摄氏度
     */
    public static final int CPU_TEMPERATURE_THRESHOLD_1 = 85;
    /**
     * CPU温度告警阀值二，现为95摄氏度
     */
    public static final int CPU_TEMPERATURE_THRESHOLD_2 = 95;

    /**
     * 存储空间告警，当存储使用空间超过90%时，发出告警信息
     */
    public static final double STORE_ALARM_THRESHOLD = 0.90;

    /**
     * 重启系统的Shell命令
     */
    public static final String SHELL_REBOOT_COMMAND = "reboot";

    /**
     * 定时自检时间
     */
    public static final int LOOP_TIME = 300;

    /**
     * SLCD的IP地址，可配置
     */
    public static final String IP = AppCommonUtil.getIpAddress();

    /**
     * SLCD的座位号
     */
    public static final String SEAT = AppCommonUtil.getSeatPosition();

    /**
     * 系统版本号
     */
    public static String OS_VERSION = Build.VERSION.RELEASE;

    /**
     * 软件版本号
     */
    public static String SOFTWARE_VERSION = android.os.Build.DISPLAY;

    /**
     * VCC的最低电压值
     */
    public static double VCC_LOW = 18;
    /**
     * VCC的正常电压值
     */
    public static double VCC_NORMAL = 28;
    /**
     * VCC的最高电压值
     */
    public static double VCC_HIGH = 36;
    /**
     * V1的最低电压值 （板子电路图APL1117处电压）
     */
    public static double V1_LOW = 1.71;
    /**
     * V1的正常电压值 （板子电路图APL1117处电压）
     */
    public static double V1_NORMAL = 1.8;
    /**
     * V1的最高电压值 （板子电路图APL1117处电压）
     */
    public static double V1_HIGH = 1.89;
    /**
     * V2的最低电压值 （板子电路图MP1495处电压）
     */
    public static double V2_LOW = 4.75;
    /**
     * V2的正常电压值 （板子电路图MP1495处电压）
     */
    public static double V2_NORMAL = 5.0;
    /**
     * V2的最高电压值 （板子电路图MP1495处电压）
     */
    public static double V2_HIGH = 5.25;
    /**
     * V3的最低电压值 (板子电路图TRLML6402处电压)
     */
    public static double V3_LOW = 9.5;
    /**
     * V3的正常电压值 (板子电路图TRLML6402处电压)
     */
    public static double V3_NORMAL = 10;
    /**
     * V3的最高电压值 (板子电路图TRLML6402处电压)
     */
    public static double V3_HIGH = 10.5;


}