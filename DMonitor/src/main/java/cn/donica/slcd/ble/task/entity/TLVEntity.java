package cn.donica.slcd.ble.task.entity;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.ble.utils.StringUtil;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-11-09 11:09
 * Describe:       TLV格式 T：格式 L:数据长度  V:数据
 */
public class TLVEntity {
    //整个tlv数据，包含T和L
    private List<Byte> list;
    //这个tlv的类型
    private byte type;
    //整个tlv长度
    private int length;

    //这个tlv开始块号索引
    private int startIndex;
    //这个tlv结束块号索引
    private int endIndex;
    //这个tlv数据最后一个字节的位置
    private int end;
    //这个字节在当前块的开始位置
    private int start;


    public TLVEntity() {
    }

    public TLVEntity(byte[] blockBytes, int startIndex, int start) {

        this.start = start;
        this.startIndex = startIndex;
        //计算v长度
        this.length = getVLen(blockBytes, start) + 2;
        //计算type
        this.type = blockBytes[start];
        //计算endindex
        this.endIndex = getEndIndex(startIndex, length, start);
        //计算end
        this.end = getEnd(length, start);
        //将当前数据块数据添加进集合
        if (start + length < 4) {
            addBytes(blockBytes, start, end);
        } else {
            addBytes(blockBytes, start, 4);
        }

        // start:1  startindex:5 length:2  type:3  endindex:6  end:-1
        DLog.warn("start:" + start + "  startindex:" + startIndex + " length:" + length + "  type:" + type + "  endindex:" + endIndex + "  end:" + end);


    }

    /**
     * 获取最有个字节位置
     */
    private int getEnd(int length, int start) {
        if ((length - 4 + start) % 4 == 0) {
            return 4;
        }
        if (start + length < 4) {
            return start + length;
        }
        return (length - 4 + start) % 4;
    }

    public void addBytes(byte[] src, int start, int end) {
        for (int i = start; i < end; i++) {
            addBytes(src[i]);
        }
    }

    /**
     * 将tlv数据添加进集合
     *
     * @param b
     */
    private void addBytes(byte b) {
        if (list == null) {
            list = new ArrayList<Byte>();
        }
        list.add(b);
    }

    /**
     * 获取已经读取的字节数
     *
     * @return
     */
    public int getListSize() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public byte[] getNdefBytes() {
        byte[] ndefBytes = new byte[list.size() - 2];
        for (int i = 2; i < list.size(); i++) {
            byte value = list.get(i).byteValue();
            ndefBytes[i - 2] = value;
        }
        return ndefBytes;
    }

    /**
     * 获取最后一块数据索引
     *
     * @param startIndex
     * @param length
     * @param start
     * @return
     */
    private int getEndIndex(int startIndex, int length, int start) {
        if ((length - 4 + start) % 4 == 0) {
            return (length - 4 + start) / 4 + startIndex;
        } else {
            return (length - 4 + start) / 4 + startIndex + 1;
        }
    }

    /**
     * 计算v的长度
     *
     * @param blocks
     * @param start
     * @return
     */
    private int getVLen(byte[] blocks, int start) {
        /**
         * 1:点start在0时：
         *
         * 2：当start在3时：
         *
         * 3：当start在1,2时
         *
         */
        String hex = Integer.toHexString(blocks[start + 1]);
        return Integer.valueOf(hex, 16);
    }


    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }


    @Override
    public String toString() {

        return StringUtil.bytes2HexString(getNdefBytes());

    }
}
