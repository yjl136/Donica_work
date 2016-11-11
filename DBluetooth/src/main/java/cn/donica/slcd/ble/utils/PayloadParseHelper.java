package cn.donica.slcd.ble.utils;


import java.nio.charset.Charset;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-28 14:50
 * Describe:
 */
public class PayloadParseHelper {
    /**
     * 获得蓝牙mac
     *
     * @param payload
     * @return
     */
    public static byte[] getBlutoothMac(byte[] payload) {
        //payload前两个字节代表是长度,后6个字节代表是BluetoothMac，后面是多个EIR（扩展数据）
        byte[] macBytes = null;
        if (payload.length > 8) {
            macBytes = StringUtil.subBytes(payload, 2, 6);
        }

        return StringUtil.reverse(macBytes);
    }

    /**
     * 获得蓝牙本地名字
     *
     * @param payload
     * @return
     */
    public static String getBlutoothLocalName(byte[] payload) {
        //1d 00 f8 8f 3a 83 fc 22 14 09 42 6c 75 65 74 6f 6f 74 68 20 20 4b 65 79 62 6f 61 72 64
        //在蓝牙mac后一个byte代表是第一个EIR长度
        if (payload.length > 9) {
            //判断长度是否大于9，如果大于表示至少有一个EIR
            //获取第一个Eir长度
            String hex = Integer.toHexString(payload[8]);
            int lenght = Integer.valueOf(hex, 16);
            byte type = payload[9];
            byte[] nameBytes = StringUtil.subBytes(payload, 10, lenght - 1);
            if (type == (byte) 0x09 || type == (byte) 0x08) {
                String bln = null;
                try {
                    bln = new String(nameBytes, 0, lenght - 1, Charset.forName("US-ASCII"));
                } catch (Exception e) {
                    DLog.error("getBlutoothLocalName error: " + e.getMessage());
                }
                return bln;
            }
            return "null";
        }
        return "null";
    }
}
