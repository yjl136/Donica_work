package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/24.
 */
public class EthernetUtil {
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
     * 获取网卡的链接状态
     *
     * @return 返回“1”表示网卡连接正常，返回“0”表示未连接
     * @throws RemoteException
     */
    public String getLinkState() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_ethernet(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获得网卡的speed
     *
     * @return 返回“100”表示速度为100M，返回“10”表示速度为10M
     * @throws RemoteException
     */
    public String getSpeed() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_ethernet(2, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }


    /**
     * 获得网卡的duplex
     *
     * @return 返回“Full”表示全双工，返回“Half”表示半双工
     * @throws RemoteException
     */
    public String getDuplexState() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_ethernet(3, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获的网卡的ip地址
     *
     * @return 返回网卡的ip地址
     * @throws RemoteException
     */
    public String getIPAddress() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_ethernet(4, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    public boolean isEthernetLinkStateNormal() throws RemoteException, IOException {
        int linkState = Integer.valueOf(getLinkState());
        if (linkState == 1) {
            sendWarn("w", "Ethernet connection is normal", AppCommonUtil.getTag());
            return true;
        }
        sendWarn("i", "Ethernet connection is abnormality", AppCommonUtil.getTag());
        return false;
    }

    /**
     * @return 以太网卡Mac地址
     */
    public static String getMacAddress() {
        try {
            return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取字符串文件
     *
     * @param filePath
     * @return
     * @throws java.io.IOException
     */
    public static String loadFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * 开机自检项目
     */
    public void bootCheck() throws IOException, RemoteException {
        isEthernetLinkStateNormal();
    }
}
