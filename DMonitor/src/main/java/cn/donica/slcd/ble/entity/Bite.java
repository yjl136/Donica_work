package cn.donica.slcd.ble.entity;

import org.litepal.crud.DataSupport;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-22 09:46
 * Describe:自检类实体
 */

public class Bite extends DataSupport {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
