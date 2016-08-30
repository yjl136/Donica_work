package cn.donica.slcd.dmanager.snmp;

/**
 * Class:		ISnmpOID
 * Description:	Define all OIDs in SLCD
 *
 * @author Liangmingjie
 */
public interface ISnmpOID {
    /****************************** Application OID *****************************************/
    /**
     * Launcher 应用的运行状态
     */
    public static final String OID_LAUNCHER_STATE = "1.3.6.1.4.1.654.3.40.1.2.1.1.1";
    /**
     * Launcher 应用的运行时间
     */
    public static final String OID_LAUNCHER_RUNNINGTIME = "1.3.6.1.4.1.654.3.40.1.2.1.1.2";
    /**
     * Upgrade  应用的运行状态
     */
    public static final String OID_UPGRADE_STATE = "1.3.6.1.4.1.654.3.40.1.2.1.2.1";
    /**
     * Upgrade 应用的运行时间
     */
    public static final String OID_UPGRADE_RUNNINGTIME = "1.3.6.1.4.1.654.3.40.1.2.1.2.2";
    /**
     * Settings 应用的运行状态
     */
    public static final String OID_SETTINGS_STATE = "1.3.6.1.4.1.654.3.40.1.2.1.3.1";
    /**
     * Settings 应用的运行时间
     */
    public static final String OID_SETTINGS_RUNNINGTIME = "1.3.6.1.4.1.654.3.40.1.2.1.3.2";
    /**
     * OAM 应用的运行状态
     */
    public static final String OID_OAM_STATE = "1.3.6.1.4.1.654.3.40.1.2.1.4.1";
    /**
     * OAM 应用的运行时间
     */
    public static final String OID_OAM_RUNNINGTIME = "1.3.6.1.4.1.654.3.40.1.2.1.4.2";
    /**
     * Message Service 应用的运行状态
     */
    public static final String OID_MESSAGE_SERVICE_STATE = "1.3.6.1.4.1.654.3.40.1.2.1.5.1";
    /**
     * Message Service 应用的运行时间
     */
    public static final String OID_MESSAGE_SERVICE_RUNNINGTIME = "1.3.6.1.4.1.654.3.40.1.2.1.5.2";

    /****************************** HardWare OID *****************************************/

    /**
     * CPU 温度
     */
    public static final String OID_CPU_TEMPERATURE = "1.3.6.1.4.1.654.3.40.1.2.2.1";
    /**
     * CPU 闲置率
     */
    public static final String OID_CPU_IDLE_RATE = "1.3.6.1.4.1.654.3.40.1.2.2.2";
    /**
     * CPU 使用率
     */
    public static final String OID_CPU_USAGE_RATE = "1.3.6.1.4.1.654.3.40.1.2.2.3";
    /**
     * audio自检(有无audio signal)
     */
    public static final String OID_AUDIO_OUTPUT = "1.3.6.1.4.1.654.3.40.1.2.3.1";
    /**
     * 耳机自检
     */
    public static final String OID_HEADPHONE_STATE = "1.3.6.1.4.1.654.3.40.1.2.3.2";
    /**
     * audio芯片id值
     */
    public static final String OID_AUDIO_CHIP_ID = "1.3.6.1.4.1.654.3.40.1.2.3.3";
    /**
     * 音视频(AD)芯片的加载情况
     */
    public static final String OID_AD_MOUNT_STATE = "1.3.6.1.4.1.654.3.40.1.2.4.1";
    /**
     * NTSC信号值
     */
    public static final String OID_NTSC_SIGNAL_STATE = "1.3.6.1.4.1.654.3.40.1.2.4.2";

    /**
     * 获得总内存大小
     */
    public static final String OID_MEMORY_TOTAL = "1.3.6.1.4.1.654.3.40.1.2.5.1";

    /**
     * 获得已使用的内存大小
     */
    public static final String OID_MEMORY_USED = "1.3.6.1.4.1.654.3.40.1.2.5.2";
    /**
     * 获得可用内存的大小
     */
    public static final String OID_MEMORY_USAGE_RATE = "1.3.6.1.4.1.654.3.40.1.2.5.4";

    /**
     * 获得可用内存的大小
     */
    public static final String OID_MEMORY_AVAILABLE = "1.3.6.1.4.1.654.3.40.1.2.5.3";
    /**
     * 获得ssd总容量
     */
    public static final String OID_SSD_TOTAL_SIZE = "1.3.6.1.4.1.654.3.40.1.2.6.1";
    /**
     * 获得已使用的ssd的大小
     */
    public static final String OID_SSD_USED_SIZE = "1.3.6.1.4.1.654.3.40.1.2.6.2";
    /**
     * 获得可用ssd的大小
     */
    public static final String OID_SSD_AVAILABLE_SIZE = "1.3.6.1.4.1.654.3.40.1.2.6.3";
    /**
     * 获得ssd的状态，插拔，加载等状态
     */
    public static final String OID_SSD_STATUS = "1.3.6.1.4.1.654.3.40.1.2.6.4";

    /**
     * 获得SSD的使用率（已用容量/容量）
     */
    public static final String OID_SSD_USAGE_RATE = "1.3.6.1.4.1.654.3.40.1.2.6.5";
    /**
     * 获得emmc总容量
     */
    public static final String OID_EMMC_TOTAL_SIZE = "1.3.6.1.4.1.654.3.40.1.2.7.1";
    /**
     * 获得已使用的emmc的大小
     */
    public static final String OID_EMMC_USED_SIZE = "1.3.6.1.4.1.654.3.40.1.2.7.2";
    /**
     * 获得可用emmc的大小
     */
    public static final String OID_EMMC_AVAILABLE_SIZE = "1.3.6.1.4.1.654.3.40.1.2.7.3";


    /**
     * 获得emmc的状态，插拔，加载等状态
     */
    public static final String OID_EMMC_STATUS = "1.3.6.1.4.1.654.3.40.1.2.7.4";

    /**
     * 获得emmc的使用率（已用容量/容量）
     */
    public static final String OID_EMMC_USAGE_RATE = "1.3.6.1.4.1.654.3.40.1.2.7.5";
    /**
     * 获得sd总容量
     */
    public static final String OID_SD_TOTAL_SIZE = "1.3.6.1.4.1.654.3.40.1.2.8.1";
    /**
     * 获得已使用的sd的大小
     */
    public static final String OID_SD_USED_SIZE = "1.3.6.1.4.1.654.3.40.1.2.8.2";
    /**
     * 获得可用sd的大小
     */
    public static final String OID_SD_AVAILABLE_SIZE = "1.3.6.1.4.1.654.3.40.1.2.8.3";
    /**
     * 获得sd的插入状态
     */
    public static final String OID_SD_STATUS = "1.3.6.1.4.1.654.3.40.1.2.8.4";

    /**
     * 获得sd卡的使用率（已用容量/容量）
     */
    public static final String OID_SD_USAGE_RATE = "1.3.6.1.4.1.654.3.40.1.2.8.5";
    /**
     * wifi模块加载情况
     */
    public static final String OID_WIFI_MODULE = "1.3.6.1.4.1.654.3.40.1.2.9.1";
    /**
     * WIFI模块射频信号状态
     */
    public static final String OID_WIFI_RF_MODULE_STATE = "1.3.6.1.4.1.654.3.40.1.2.9.2";
    /**
     * WIFI的IP地址
     */
    public static final String OID_WIFI_IP = "1.3.6.1.4.1.654.3.40.1.2.9.3";
    /**
     * WIFI的DHCP状态
     */
    public static final String OID_WIFI_DHCP = "1.3.6.1.4.1.654.3.40.1.2.9.4";
    /**
     * WIFI的子网掩码地址
     */
    public static final String OID_WIFI_NETMASK = "1.3.6.1.4.1.654.3.40.1.2.9.5";
    /**
     * WIFI的网关地址
     */
    public static final String OID_WIFI_GATEWAY = "1.3.6.1.4.1.654.3.40.1.2.9.6";
    /**
     * WIFI的首选DNS服务器地址
     */
    public static final String OID_WIFI_DNS1 = "1.3.6.1.4.1.654.3.40.1.2.9.7";
    /**
     * WIFI的备用DNS服务器地址
     */
    public static final String OID_WIFI_DNS2 = "1.3.6.1.4.1.654.3.40.1.2.9.8";
    /**
     * WIFI的Mac(物理)地址
     */
    public static final String OID_WIFI_MAC = "1.3.6.1.4.1.654.3.40.1.2.9.9";
    /**
     * 蓝牙模块加载情况
     */
    public static final String OID_BT_MODULE = "1.3.6.1.4.1.654.3.40.1.2.10.1";
    /**
     * 蓝牙模块射频信号状态
     */
    public static final String OID_BT_RF_MODULE_STATE = "1.3.6.1.4.1.654.3.40.1.2.10.2";
    /**
     * RFID模块加载情况
     */
    public static final String OID_RFID_MODULE = "1.3.6.1.4.1.654.3.40.1.2.11.1";
    /**
     * NFC RFID配对情况
     */
    //RFID_PAIR_STATE API 没有
    public static final String OID_RFID_PAIR_STATE = "1.3.6.1.4.1.654.3.40.1.2.11.2";
    /**
     * LCD屏加载情况
     */
    public static final String OID_LCD_MODULE = "1.3.6.1.4.1.654.3.40.1.2.12.1";
    /**
     * LCD屏背光状态（检测电流判断）
     */
    public static final String OID_BACKLIGHT_STATE = "1.3.6.1.4.1.654.3.40.1.2.12.2";
    /**
     * cpu的电压值
     */
    public static final String OID_CPU_POWER_MODULE = "1.3.6.1.4.1.654.3.40.1.2.13.1";
    /**
     * 获得VCC电压值
     */
    public static final String OID_POWER_VOLTAGE_VCC = "1.3.6.1.4.1.654.3.40.1.2.13.2";
    /**
     * 获得V1电压值
     */
    public static final String OID_POWER_VOLTAGE_V1 = "1.3.6.1.4.1.654.3.40.1.2.13.3";
    /**
     * 获得V2电压值
     */
    public static final String OID_POWER_VOLTAGE_V2 = "1.3.6.1.4.1.654.3.40.1.2.13.4";
    /**
     * 获得V3电压值
     */
    public static final String OID_POWER_VOLTAGE_V3 = "1.3.6.1.4.1.654.3.40.1.2.13.5";

    /**
     * 获取网卡的连接接状态
     */
    public static final String OID_ETHERNET_CONN_STATUS = "1.3.6.1.4.1.654.3.40.1.2.14.1";
    /**
     * 网卡的速度
     */
    public static final String OID_ETHERNET_SPEED = "1.3.6.1.4.1.654.3.40.1.2.14.2";
    /**
     * 网卡的双工状态
     */
    public static final String OID_ETHERNET_DUPLEX = "1.3.6.1.4.1.654.3.40.1.2.14.3";
    /**
     * 网卡的ip地址
     */
    public static final String OID_ETHERNET_IP = "1.3.6.1.4.1.654.3.40.1.2.14.4";
    /**
     * 网卡的DHCP状态
     */
    public static final String OID_ETHERNET_DHCP = "1.3.6.1.4.1.654.3.40.1.2.14.5";
    /**
     * 网卡的MAC(物理)地址
     */
    public static final String OID_ETHERNET_MAC = "1.3.6.1.4.1.654.3.40.1.2.14.6";
    /**
     * 网卡的子网掩码地址
     */
    public static final String OID_ETHERNET_NETMASK = "1.3.6.1.4.1.654.3.40.1.2.14.7";
    /**
     * 网卡的网关地址
     */
    public static final String OID_ETHERNET_GATEWAY = "1.3.6.1.4.1.654.3.40.1.2.14.8";
    /**
     * 网卡的首选DNS服务器地址
     */
    public static final String OID_ETHERNET_DNS1 = "1.3.6.1.4.1.654.3.40.1.2.14.9";
    /**
     * 网卡的备用DNS服务器地址
     */
    public static final String OID_ETHERNET_DNS2 = "1.3.6.1.4.1.654.3.40.1.2.14.10";

    /**
     * USB存储设备的容量大小
     */
    public static final String OID_USB_TOTAL_SIZE = "1.3.6.1.4.1.654.3.40.1.2.15.1";
    /**
     * USB存储设备已使用的容量
     */
    public static final String OID_USB_USED_SIZE = "1.3.6.1.4.1.654.3.40.1.2.15.2";
    /**
     * USB存储设备可使用的容量
     */
    public static final String OID_USB_AVAILABLE_SIZE = "1.3.6.1.4.1.654.3.40.1.2.15.3";
    /**
     * USB存储设备的状态（插入，加载）
     */
    public static final String OID_USB_STATUS = "1.3.6.1.4.1.654.3.40.1.2.15.4";

    /**
     * 获得U盘的使用率（已用容量/容量）
     */
    public static final String OID_USB_USAGE_RATE = "1.3.6.1.4.1.654.3.40.1.2.15.5";
    /**
     * LED灯状态
     */
    public static final String OID_HARDKEY_LED1 = "1.3.6.1.4.1.654.3.40.1.2.16.1";
    public static final String OID_HARDKEY_LED2 = "1.3.6.1.4.1.654.3.40.1.2.16.2";
    public static final String OID_USB_PORT_LED = "1.3.6.1.4.1.654.3.40.1.2.16.3";
    /**
     * 面板按键LED灯状态（灭，橙，绿）
     */
    public static final String OID_HARDKEY_LED_STATUS = "1.3.6.1.4.1.654.3.40.1.2.16.2";

    /*******
     * Trap OIDs
     *********/
    /* Notification */
//    public static final int[] OID_NOTIFICATION = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 2, 1};
//    public static final int[] OID_SNMP_TRAP = new int[]{1, 3, 6, 1, 6, 3, 1, 1, 4, 1, 0};
//    public static final int[] OID_TRAP_ID = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 1};
//    public static final int[] OID_DEVICE_ID = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 2};
//    public static final int[] OID_MODULE_NAME = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 3};
//    public static final int[] OID_CATEGORY = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 4};
//    public static final int[] OID_LEVEL = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 5};
//    public static final int[] OID_TIME = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 6};
//    public static final int[] OID_IP_ADDRESS = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 7};


    // logTime  Log时间
    // packageName 包名
    // appVername app版本名字
    // appVerCode app版本号
    // tag  log的tag信息，包含类名和方法名，方便定位出现错误的地方
    // level 保存告警信息的等级，有i,w,e三个等级,i为普通信息(info),w为警告信息(warn),e为错误信息（error）
    // type  log的类别，有0,1,2,3,4,5六种。0--未知类别（无法判断哪种类别）1--系统告警 2--数据库告警 3—网络告警 4--处理错误告警 5--服务质量告警
    // logContent log的具体文本内容
    // trapId 唯一的ID识别号
    // ipAddress 设备IP地址
    // deviceId  设备唯一ID号

    public static final int[] OID_TIME = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 1};
    public static final int[] OID_PACKAGENAME = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 2, 2};
    public static final int[] OID_APP_VERSION_NAME = new int[]{1, 3, 6, 1, 6, 3, 1, 1, 4, 1, 3};
    public static final int[] OID_APP_VERSION_CODE = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 4};
    public static final int[] OID_TAG = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 5};
    public static final int[] OID_LEVEL = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 6};
    public static final int[] OID_TYPE = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 7};
    public static final int[] OID_CONTENT = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 8};
    public static final int[] OID_TRAP_ID = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 9};
    public static final int[] OID_IP_ADDRESS = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 10};
    public static final int[] OID_DEVICE_ID = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 11};
    public static final int[] OID_LOG_LOCATION = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 12};
    public static final int[] OID_LOG_TYPE = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 13};
    public static final int[] OID_LOG_STATUS = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 14};
    public static final int[] OID_LOG_CONTENT = new int[]{1, 3, 6, 1, 4, 1, 654, 3, 20, 2, 1, 15};


}
