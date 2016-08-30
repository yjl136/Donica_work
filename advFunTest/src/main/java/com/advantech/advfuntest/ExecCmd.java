package com.advantech.advfuntest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.util.EncodingUtils;

public class ExecCmd {
    public void execShell(String cmd) {

        String retString = "";
        try {
            // 权限设置
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            DataOutputStream dataOutputStream = new DataOutputStream(
                    process.getOutputStream());
            // 将命令写入
            dataOutputStream.writeBytes(cmd);
            // 提交命令
            dataOutputStream.flush();
            // 关闭流操作
            dataOutputStream.close();
            process.waitFor();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String resultExeCmd(String cmd) {
        String[] cmdStrings = new String[]{"sh", "-c", cmd};
        String retString = "";

        try {
            Process process = Runtime.getRuntime().exec(cmdStrings);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(
                    process.getInputStream()), 7777);
            BufferedReader stderr = new BufferedReader(new InputStreamReader(
                    process.getErrorStream()), 7777);

            String line = null;

            while ((null != (line = stdout.readLine()))
                    || (null != (line = stderr.readLine()))) {
                // retString += line + "\n";
                retString += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retString;
    }

    public String readFile(String fileName) {
        String res = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            fis.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public void writeLog(String write_str) {

        File file = new File("/data/data/com.advantech.advfuntest/test.log");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] bytes = write_str.getBytes();

        try {
            fos.write(bytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void cleanLog() {
        String write_str = new String("");
        File file = new File("/data/data/com.advantech.advfuntest/test.log");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] bytes = write_str.getBytes();

        try {
            fos.write(bytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String readLog() {
        String fileName = new String(
                "/data/data/com.advantech.advfuntest/test.log");
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];
            fin.read(buffer);

            res = EncodingUtils.getString(buffer, "UTF-8");

            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // JNI

    public native static void flash_to_file();

	/*
    static {
		System.loadLibrary("serial_port");
	}
	*/
}
