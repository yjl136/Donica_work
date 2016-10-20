package cn.donica.slcd.ble.utils;

public class StringUtil {


    public static String bytes2HexString(byte[] b) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                ret.append("0");
            }
            ret.append(hex.toLowerCase());
            ret.append(" ");
        }
        return ret.toString().trim();
    }


    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++) bs[i - begin] = src[i];
        return bs;
    }


    public static byte[] reverse(byte[] myByte) {
        byte[] newByte = new byte[myByte.length];

        for (int i = 0; i < myByte.length; i++) {
            newByte[i] = myByte[myByte.length - 1 - i];
        }
        return newByte;
    }


}
