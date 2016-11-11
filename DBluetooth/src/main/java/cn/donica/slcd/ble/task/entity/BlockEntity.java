package cn.donica.slcd.ble.task.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.ble.utils.StringUtil;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-11-09 11:57
 * Describe:      块实体类
 */
public class BlockEntity {
    private int block;
    private byte[] cmdBytes;
    private InputStream is;
    private OutputStream out;

    public BlockEntity(int block, byte[] cmdBytes, InputStream is, OutputStream out) {
        this.block = block;
        this.cmdBytes = cmdBytes;
        this.is = is;
        this.out = out;
        check();
    }

    /**
     * 检测参数是否合法
     */
    private void check() {
        if (is == null || out == null) {

            throw new NullPointerException("is || out不能为空");

        }
        if (cmdBytes == null || cmdBytes.length <= 6) {

            throw new IllegalArgumentException("cmdbytes参数不合法");
        }
    }

    /**
     * 获取块号
     *
     * @return
     */
    public int getBlock() {
        return block;
    }

    /**
     * 写命令
     * <p/>
     * 放在子线程中
     */
    public boolean write() {
        boolean b = false;
        try {
            DLog.info("write: " + StringUtil.bytes2HexString(cmdBytes));
            out.write(cmdBytes);
            b = true;
        } catch (IOException e) {
            e.printStackTrace();
            b = false;
            DLog.error("Fail to write " + e.getMessage());
        } finally {
            return b;
        }
    }

    /**
     * 读块 并且校验
     * <p/>
     * 放在子线程中
     */
    public ResultEntity read() {
        ResultEntity result = new ResultEntity();
        result.setBlock(block);
        try {
            byte[] buf = new byte[512];
            int retSize = is.read(buf);
            // DLog.info("数据块: " + block + " 数据: " + StringUtil.bytes2HexString(StringUtil.subBytes(buf, 0, retSize)) + " length: " + retSize);
            if (retSize > 0) {
                buf = StringUtil.subBytes(buf, retSize);
                if (buf.length >= 6) {
                    int dataLength = buf[3];
                    int length = dataLength + 6;
                    byte[] data = buf;
                    if (data[0] == 0x20 && data[length - 1] == 0x03
                            && data[2] == 0) {
                        byte temp = 0x00;
                        for (int i = 0; i < dataLength; i++) {
                            temp = (byte) (temp ^ data[4 + i]);
                        }
                        byte BCC = (byte) ~(data[1] ^ data[2] ^ data[3] ^ temp);
                        if (BCC == data[length - 2]) {
                            result.setState(ResultEntity.State.BLOCK_READ_SUCCESS);
                            result.setBuf(data);
                        } else {
                            result.setState(ResultEntity.State.BLOCK_DATA_ERROR);
                            DLog.info("block: " + block + " 校验失败");
                        }
                    } else {
                        result.setState(ResultEntity.State.BLOCK_DATA_ERROR);
                        DLog.info("block: " + block + " 状态不对");
                    }
                } else {
                    result.setState(ResultEntity.State.BLOCK_DATA_ERROR);
                    DLog.info("block: " + block + " 数据长度不对");
                }
            } else {
                DLog.info("block: " + block + " 读写错误");

                result.setState(ResultEntity.State.BLOCK_READ_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setState(ResultEntity.State.BLOCK_READ_ERROR);
            DLog.error("Fail to Block:  " + block + " message:" + e.getMessage());
        } finally {

            return result;
        }
    }


}
