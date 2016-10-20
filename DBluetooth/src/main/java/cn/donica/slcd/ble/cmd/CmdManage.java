package cn.donica.slcd.ble.cmd;


import java.nio.ByteBuffer;

import cn.donica.slcd.ble.utils.AppTool;
import cn.donica.slcd.ble.utils.Constant;

/**
 * 主机发送到从机的命令，参照HSJ522协议文档
 *
 * @author wenchao
 */
public class CmdManage {

    /**
     * 读取卡号
     *
     * @return
     */
    public static byte[] getReadCardId() {
        int len = 7;
        byte[] buf = new byte[len];
        buf[0] = 0x20;
        int serial = AppTool.getSerial();
        buf[1] = (byte) serial;
        buf[2] = 0x21;
        buf[3] = 0x01;
        buf[4] = 0x00;//ReqCode＝0（IDLE），请求天线范围内IDLE状态的卡（HALT状态的除外）
        buf[5] = getBBC(buf[1], buf[2], buf[3], buf[4]);
        buf[6] = 0x03;
        return buf;
    }

    /**
     * 写块
     *
     * @param data
     * @return
     */
    public static byte[] getWriteBlock(byte[] data) {//data字节数组长度为16
        ByteBuffer buf = ByteBuffer.allocate(23);
        buf.put((byte) 0x20);
        int serial = AppTool.getSerial();
        buf.put((byte) serial);
        buf.put((byte) 0x23);
        buf.put((byte) 0x011);
        buf.put((byte) Constant.block);
        buf.put(data);
        byte temp = (byte) Constant.block;
        for (int i = 0; i < data.length; i++) {
            temp = (byte) (temp ^ data[i]);
        }
        byte bcc = (byte) ~((byte) serial ^ (byte) 0x23 ^ (byte) 0x011 ^ temp);//校验和。从包号（SEQNR）开始到数据（DATA）的最后一字节异或取反。
        buf.put((byte) bcc);
        buf.put((byte) 0x03);

        buf.flip();
        byte[] out = new byte[23];
        buf.get(out);
        return out;
    }

    /**
     * 读块
     *
     * @return
     */
    public static byte[] getReadBlock() {
        int len = 7;
        byte[] buf = new byte[len];
        buf[0] = 0x20;
        int serial = AppTool.getSerial();
        buf[1] = (byte) serial;
        buf[2] = 0x22;
        buf[3] = 0x01;
        buf[4] = (byte) Constant.block;
        buf[5] = getBBC(buf[1], buf[2], buf[3], buf[4]);
        buf[6] = 0x03;
        return buf;
    }

    /**
     * 读块
     *
     * @return
     */
    public static byte[] getReadBlock(byte block) {
        int len = 7;
        byte[] buf = new byte[len];
        buf[0] = 0x20;
        int serial = AppTool.getSerial();
        buf[1] = (byte) serial;
        buf[2] = 0x22;
        buf[3] = 0x01;
        buf[4] = block;
        buf[5] = getBBC(buf[1], buf[2], buf[3], buf[4]);
        buf[6] = 0x03;
        return buf;
    }

    /**
     * 校验和。从包号（SEQNR）开始到数据（DATA）的最后一字节异或取反。
     *
     * @param SEQNR
     * @param CMD
     * @param Length
     * @param data
     * @return
     */
    public static byte getBBC(byte SEQNR, byte CMD, byte Length, byte data) {
        return (byte) ~(SEQNR ^ CMD ^ Length ^ data);
    }
}
