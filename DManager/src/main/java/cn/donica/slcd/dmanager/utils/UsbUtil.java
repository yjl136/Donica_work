package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/24.
 */
public class UsbUtil {
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
        logWarnTrap.sendWarn(level, 1, 1, "USB info:" + logContent, location);
    }

    /**
     * 获取USB存储设备的插入状态（是否插入）
     *
     * @return 0：检测到u盘插入  非0：未检测到有u盘插入
     * @throws RemoteException
     */
    public String getUSBInsertStatus() throws RemoteException, IOException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.test_usb(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        double status = Double.valueOf(info);
        if (status == 0) {
            sendWarn("i", "USB flash disk was inserted", AppCommonUtil.getTag());
        } else {
            sendWarn("i", "USB flash disk was not inserted", AppCommonUtil.getTag());
        }
        return info;
    }


    /**
     * 获取U盘的总容量大小,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getUsbTotalSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_usb(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取U盘已用容量大小,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getUsbUsedSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_usb(2, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取U盘能用容量大小,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getUsbAvailableSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_usb(3, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * Usb 使用率
     *
     * @throws RemoteException
     */
    public Double getUsbUsageRate() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getUsbTotalSize());
        Double usedSize = Double.parseDouble(getUsbUsedSize());
        Double usage_rate = usedSize / totalSize;
        return usage_rate;
    }

    /**
     * 获取SDCard的状态（是否插入）
     *
     * @return 0:检测到有sd卡插入  非0:未检测到sd卡插入
     * @throws RemoteException
     */
    public String getSDCardInsertStatus() throws RemoteException, IOException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.test_sdcard(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        double status = Double.valueOf(info);
        if (status == 0) {
            sendWarn("i", "SDCard  was inserted", AppCommonUtil.getTag());
        } else {
            sendWarn("i", "SDCard  was not inserted", AppCommonUtil.getTag());
        }
        return info;
    }


    /**
     * 开机自检项目
     */
    public void bootCheck() throws IOException, RemoteException {
        getSDCardInsertStatus();
        getUSBInsertStatus();
    }
}
