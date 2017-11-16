package cn.donica.slcd.settings.wifi;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-10 10:40
 * Describe:无线接入点实例
 */

public class APEntity {
    private String ssid;
    private String bssid;
    private String capabilities;
    private int level;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
