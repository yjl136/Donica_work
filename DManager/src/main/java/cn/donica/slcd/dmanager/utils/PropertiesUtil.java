/**
 * File Name: PropertiesUtil.java
 * Description： To read application's properties.
 * Author: Luke Huang
 * Create Time: 2015-7-23
 * <p>
 * For the SLCD Project
 * Copyright © 2015 Donica.cn All rights reserved
 */
package cn.donica.slcd.dmanager.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Description:	To read application's properties.
 *
 * @author Luke Huang
 * @date 2015-07-23
 */
public class PropertiesUtil {
    private Properties properties;

    public PropertiesUtil() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/assets/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public int getInteger(String name) {
        return Integer.parseInt(getProperty(name));
    }
}
