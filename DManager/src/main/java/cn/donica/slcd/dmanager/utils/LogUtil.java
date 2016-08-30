/**
 * File Name: LogUtil.java
 * Description：
 * Author: Luke Huang
 * Create Time: 2015-7-24
 * <p>
 * For the SLCD Project
 * Copyright © 2015 Donica.cn All rights reserved
 */
package cn.donica.slcd.dmanager.utils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Description:	save logcat information to local log file
 *
 * @author Luke Huang 2015-7-24
 */
public class LogUtil {
    public static final String LOGCAT_TO_FILE = "logcat -f /sdcard/dmanager.log";

    public static void exec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream output = new DataOutputStream(process.getOutputStream());
            if (cmd != null) {
                cmd += "\n";
                output.write(cmd.getBytes());
                output.flush();
                output.write("exit\n".getBytes());
                output.flush();
            }
            process.waitFor();
            process.exitValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
