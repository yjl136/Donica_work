package cn.donica.slcd.ble.utils;

public class Constant {

    /**
     * 复位命令
     */
    public final static byte[] RESET_CMD = {0x20, 0x00, 0x29, 0x01, 0x01, (byte) 0xD6, 0x03};

    /**
     * 对应设备的串口3
     */
    public static final String devName = "/dev/ttymxc1";

    /**
     * 波特率
     */
    public static final long baud = 9600;

    /**
     * 数据位
     */
    public static final int dataBits = 8;

    /**
     * 停止位
     */
    public static final int stopBits = 1;

    /**
     * 读写第二块
     */
    public static final int block = 2;


}
