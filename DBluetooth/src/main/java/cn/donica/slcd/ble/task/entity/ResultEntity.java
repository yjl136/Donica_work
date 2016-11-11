package cn.donica.slcd.ble.task.entity;

import cn.donica.slcd.ble.utils.StringUtil;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-11-09 14:37
 * Describe:       结果
 */
public class ResultEntity {
    //结果状态
    private int state;
    //结果数据
    private byte[] buf;

    //块号
    private int block;

    public ResultEntity() {
    }

    public ResultEntity(int block, byte[] buf, int state) {
        this.block = block;
        this.buf = buf;
        this.state = state;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        if (state == State.BLOCK_READ_SUCCESS) {
            return "数据块号:" + block + "  数据:" + StringUtil.bytes2HexString(buf);
        } else {
            return "数据块号:" + block + " 状态:" + state;
        }
    }

    public static class State {
        //块读取失败
        public final static int BLOCK_READ_ERROR = 0x100;
        //成功
        public final static int BLOCK_READ_SUCCESS = 0x101;
        //块数据错误
        public final static int BLOCK_DATA_ERROR = 0x102;
        //块写失败
        public final static int BLOCK_WRITE_ERROR = 0x103;
    }
}
