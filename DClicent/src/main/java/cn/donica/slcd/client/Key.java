package cn.donica.slcd.client;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2018-05-07 15:14
 * Describe:
 */

public class Key {
    private String desc;
    private int code;

    public Key() {
    }

    public Key(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
