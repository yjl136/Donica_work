package cn.donica.slcd.sensor.net.utils;

import java.security.MessageDigest;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-12-30 09:26
 * Describe:
 */
public class Md5 {
    /**
     * 将string转换md5值
     *
     * @param string
     * @return
     */
    public static String stringToMd5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("md5").digest(string.getBytes("utf-8"));
        } catch (Exception e) {
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
