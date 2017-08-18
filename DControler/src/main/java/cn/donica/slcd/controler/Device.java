package cn.donica.slcd.controler;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-08-11 17:49
 * Describe:
 */

public class Device {
    private String name;
    private String mac;

    public Device() {
    }

    public Device(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
