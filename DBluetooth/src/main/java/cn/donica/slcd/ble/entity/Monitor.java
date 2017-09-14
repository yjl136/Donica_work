package cn.donica.slcd.ble.entity;

import org.litepal.crud.DataSupport;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-08 11:35
 * Describe:
 */

public class Monitor extends DataSupport {
    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
