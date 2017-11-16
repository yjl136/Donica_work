package cn.donica.slcd.ble.task.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-11-09 11:20
 * Describe:      包含多个TLV数据
 * <p/>
 * 一个TLV数据集合包含有多个tlv数据
 */
public class TLVSet {
    private HashMap<Byte, TLVEntity> map;
    private List<Byte> list;

    public TLVSet() {
        map = new HashMap<>();
    }

    /**
     * 将内容字节添加到集合
     *
     * @param b
     */
    public void addBytes(byte b) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(b);
    }

    /**
     * 将数据分割成多个TLV数据
     */
    public void split() {
        //首先把0x01和0x03格式数据提取出来


    }

    /**
     * 向map中添加一个tlv
     *
     * @param entity
     */
    public void addEntity(TLVEntity entity) {
        if (entity == null) {
            return;
        }
        byte type = entity.getType();
        map.put(type, entity);
    }

    /**
     * 获取到一个Entity
     *
     * @param type 获得类型实体
     * @return
     */
    public TLVEntity getEntity(byte type) {
        return map.get(type);
    }

    /**
     * 将集合清空
     */
    public void clear() {
        if (list != null) {
            list.clear();
        }
        if (map != null) {
            map.clear();
        }

    }
}
