package cn.donica.slcd.dmanager.utils;

import android.content.Intent;
import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/4.
 */
public class CpuUtil {
    private String CPU_PATH_PROC_LOADAVG = "/proc/loadavg";
    private String TAG = "CpuUtil";
    private IHwtestService hwtestService;

    /**
     * 发送告警信息
     *
     * @param level      告警信息级别，有i,w,e三种.其中i为info(普通信息)，w为warn（警告信息），e为error（错误信息）
     * @param logContent 告警信息的内容
     * @param location   告警信息的触发代码位置
     * @throws IOException
     */
    public void sendWarn(String level, String logContent, String location) throws IOException {
        LogWarnTrap logWarnTrap = new LogWarnTrap();
        logWarnTrap.sendWarn(level, 1, 1, "Ethernet Info:" + logContent, location);
    }

    /**
     * 获取CPU的平均占用率，这和Android开发者选项里面自带的"显示CPU使用情况"原理是一样的
     * 原理与Linux上的 "cat /proc/loadavg" 一样
     * 命令结果example: 5.27 4.56 4.57 3/847 13051
     * 前三个值分别对应系统在1分钟、5分钟、15分钟内的平均负载
     * 第四个值的分子是正在运行的进程数，分母是进程总数，最后一个是最近运行的进程ID号
     * 一般来说只要每个核心的当前活动进程数不大于3那么系统的性能就是良好的。
     * 如果每个核心的任务数大于5，那么就表示这台机器的性能有严重问题。
     * 假设设备CPU有双核心，那么其每个核心的当前任务数为：5.27/2=2.635。这表示该系统的性能是可以接受的。
     *
     * @return CPU最后一分钟的平均负载
     */
    public float getCpuLoad() {
        float cpuLoad = 0;
        Scanner s = null;
        try {
            s = new Scanner(new BufferedReader(new FileReader(CPU_PATH_PROC_LOADAVG)));
            float last1Min = Float.parseFloat(s.next());
            float last5Min = Float.parseFloat(s.next());
            float last15Min = Float.parseFloat(s.next());
            //因为SLCD是双核CPU,所以要除以2,10为比例放大系数，为了让数据更直观
            cpuLoad = (last1Min / 2) * 10;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.close();
            }
        }
        return cpuLoad;
    }

    /**
     * 获得实时的cpu的使用率，由研华的API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getCpuUsageRate() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_cpu(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取CPU温度，由研华的API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getCpuTemperature() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_temperature(strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取CPU警告信息
     *
     * @throws RemoteException
     */
    public int getCpuTemperatureAlarm() throws RemoteException, IOException {
        String cpuTemperature = getCpuTemperature();
        Intent intent = null;
        int temp = Integer.parseInt(cpuTemperature);
        if (temp >= Config.CPU_TEMPERATURE_THRESHOLD_1 && temp < Config.CPU_TEMPERATURE_THRESHOLD_2) {
            sendWarn("w", "Current CPU temperature is more than 85 centigrade", AppCommonUtil.getTag());
        } else if (temp >= Config.CPU_TEMPERATURE_THRESHOLD_2) {
            sendWarn("e", "Current CPU temperature is more than 95 centigrade", AppCommonUtil.getTag());
        }
        return temp;
    }

    /**
     * 开机自检项目
     */
    public void bootCheck() throws IOException, RemoteException {
        getCpuTemperatureAlarm();
        getCpuLoad();
    }

    public void regularCheck() throws IOException, RemoteException {
        getCpuTemperatureAlarm();
        getCpuUsageRate();
    }
}
