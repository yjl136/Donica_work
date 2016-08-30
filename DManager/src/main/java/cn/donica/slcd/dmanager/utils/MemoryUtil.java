package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/24.
 */
public class MemoryUtil {
    private static String TAG = "MemoryUtil";
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
        logWarnTrap.sendWarn(level, 1, 1, "Memory info:" + logContent, location);
    }

    /**
     * 获取Memory总容量大小，由研华提供的PAI
     *
     * @return
     * @throws RemoteException
     */
    public String getMemoryTotalSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_memory(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取Memory已用容量，由研华提供的PAI
     *
     * @return
     * @throws RemoteException
     */
    public String getMemoryUsedSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_memory(2, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取Memory可用容量，由研华提供的PAI
     *
     * @return
     * @throws RemoteException
     */
    public String getMemoryAvailableSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_memory(3, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * Memory使用情况自检
     *
     * @throws RemoteException
     */
    public Boolean MemoryUsageCheck() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getMemoryTotalSize());
        Double usedSize = Double.parseDouble(getMemoryUsedSize());
        //  Double AvailableSize = Double.parseDouble(getMMCAvailableSize());
        if ((usedSize / totalSize) > Config.STORE_ALARM_THRESHOLD) {
            sendWarn("w", "Memory capacity use of more than 95%,usedSize is" + usedSize + ",totalSize is" + totalSize, AppCommonUtil.getTag());
            return false;
        }
        sendWarn("w", "Memory capacity  is enough,usedSize is" + usedSize + ",totalSize is" + totalSize, AppCommonUtil.getTag());
        return true;
    }


    /**
     * Memory 使用率
     *
     * @throws RemoteException
     */
    public Double getMemoryUsageRate() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getMemoryTotalSize());
        Double usedSize = Double.parseDouble(getMemoryUsedSize());
        Double usage_rate = usedSize / totalSize;
        return usage_rate;
    }


    public void bootCheck() throws IOException, RemoteException {
        MemoryUsageCheck();
    }
}
