package cn.donica.slcd.settings.bite;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-22 15:12
 * Describe:
 */

public class BiteEntity {
    private String name;
    private String value;

    public BiteEntity() {
    }

    public BiteEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

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
