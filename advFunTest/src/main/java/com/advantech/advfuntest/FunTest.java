package com.advantech.advfuntest;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.lang.String;
import java.lang.Runtime;

public class FunTest {

    public String sataOutputCmd = "dd bs=1M count=128 if=/dev/zero of=/data/test conv=fdatasync";
    public String sataInputCmd = "dd bs=1M count=128 if=/data/test of=/dev/null conv=fdatasync";
    public String usbOutputCmd = "dd bs=1M count=128 if=/dev/zero of=/data/test conv=fdatasync";
    public String usbInputCmd = "dd bs=1M count=128 if=/data/test of=/dev/null conv=fdatasync";

    //private String sataOutputCmd = "dd bs=1M count=128 if=/dev/zero of=test conv=fdatasync";
    //private String sataOutputCmd = "ls";

    /*public String resultExeCmd() {
        String sataOutputCmd = "dd bs=1M count=128 if=/dev/zero of=/data/test conv=fdatasync";
        /*String returnString = "abcd";

        try {
            String sataOutputCmd = "ls";
            Process ps = Runtime.getRuntime().exec(sataOutputCmd);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            returnString = sb.toString();
            }
        catch (Exception e) {
            e.printStackTrace();
            }

        return returnString;*/
        /*String[] cmdStrings = new String[] {"sh", "-c", sataOutputCmd};
        String retString = "";

        try{
            Process process = Runtime.getRuntime().exec(cmdStrings);
            BufferedReader stdout =
            new BufferedReader(new InputStreamReader(
                process.getInputStream()), 7777);
            BufferedReader stderr =
            new BufferedReader(new InputStreamReader(
                process.getErrorStream()), 7777);

            String line = null;

            while ((null != (line = stdout.readLine()))
            || (null != (line = stderr.readLine())))
            {
                //retString += line + "\n";
            	retString += line;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return retString;
	}*/
    public String resultExeCmd(String cmd) {
        String[] cmdStrings = new String[]{"sh", "-c", cmd};
        String retString = "";

        try {
            Process process = Runtime.getRuntime().exec(cmdStrings);
            BufferedReader stdout =
                    new BufferedReader(new InputStreamReader(
                            process.getInputStream()), 7777);
            BufferedReader stderr =
                    new BufferedReader(new InputStreamReader(
                            process.getErrorStream()), 7777);

            String line = null;

            while ((null != (line = stdout.readLine()))
                    || (null != (line = stderr.readLine()))) {
                //retString += line + "\n";
                retString += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retString;
    }

    public String cutStr(String string) {
        if (!string.endsWith("Permission denied")) {
            //String a[] = string.split("(");
            //String b[] = a[1].split(")");
            //char str[]=string.toCharArray();
            //string.getChars(string.indexOf('('), string.indexOf(')'), str, 0);
            string = string.substring(string.indexOf("(") + 1, string.lastIndexOf(")"));
        }
        return string;
    }

    public void SataTest(TextView sataSpeed) {
        //sataSpeed.setText(resultExeCmd());
        //String sataOutputCmd = "dd bs=1M count=128 if=/dev/zero of=/data/test conv=fdatasync";
        //String sataInputCmd = "dd bs=1M count=128 if=/data/test of=/dev/null conv=fdatasync";
        String outputStr = resultExeCmd(sataOutputCmd);
        String inputStr = resultExeCmd(sataInputCmd);
        outputStr = cutStr(outputStr);
        inputStr = cutStr(inputStr);
        sataSpeed.setText(inputStr + "\n" + outputStr);
    }

    public void UsbTest(TextView usbSpeed) {
        String outputStr = resultExeCmd(usbOutputCmd);
        String inputStr = resultExeCmd(usbInputCmd);
        outputStr = cutStr(outputStr);
        inputStr = cutStr(inputStr);
        usbSpeed.setText(inputStr + "\n" + outputStr);
    }
}
