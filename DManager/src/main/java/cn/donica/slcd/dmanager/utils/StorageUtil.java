package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/7.
 */
public class StorageUtil {
    private static final String TAG = "StorageUtil";
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
        logWarnTrap.sendWarn(level, 1, 1, "Storage info:" + logContent, location);
    }

    /**
     * 获取MMC总容量大小
     *
     * @return
     * @throws RemoteException
     */
    public String getMMCTotalSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_mmc(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取MMC已用容量,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getMMCUsedSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_mmc(2, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取MMC可用容量,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getMMCAvailableSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_mmc(3, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * MMC 使用率
     *
     * @throws RemoteException
     */
    public Double getMMCUsageRate() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getMMCTotalSize());
        Double usedSize = Double.parseDouble(getMMCUsedSize());
        Double usage_rate = usedSize / totalSize;
        return usage_rate;
    }

    /**
     * MMC使用情况自检,由研华API提供
     *
     * @throws RemoteException
     */
    public Boolean MMCUsageCheck() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getMMCTotalSize());
        Double usedSize = Double.parseDouble(getMMCUsedSize());
        //  Double AvailableSize = Double.parseDouble(getMMCAvailableSize());
        if ((usedSize / totalSize) > Config.STORE_ALARM_THRESHOLD) {
            sendWarn("w", "MMC disk use of more than 95%,usedSize is" + usedSize + ",totalSize is" + totalSize, AppCommonUtil.getTag());
            return false;
        }
        sendWarn("w", "MMC disk space is enough,usedSize is" + usedSize + ",totalSize is" + totalSize, AppCommonUtil.getTag());
        return true;
    }

    /**
     * 获取SDCard的总容量大小,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getSDCardTotalSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_sdcard(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取SDCard已用容量大小,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getSDCardUsedSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_sdcard(2, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取SDCard能用容量大小,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getSDCardAvailableSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_sdcard(3, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }


    /**
     * SDCard 使用率
     *
     * @throws RemoteException
     */
    public Double getSDCardUsageRate() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getSDCardTotalSize());
        Double usedSize = Double.parseDouble(getSDCardUsedSize());
        Double usage_rate = usedSize / totalSize;
        return usage_rate;
    }

    /**
     * SDCard使用情况自检,由研华API提供
     *
     * @throws RemoteException
     */
    public Boolean SdcardUsageCheck() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getSDCardTotalSize());
        Double usedSize = Double.parseDouble(getSDCardUsedSize());
        //  Double AvailableSize = Double.parseDouble(getMMCAvailableSize());
        if ((usedSize / totalSize) > Config.STORE_ALARM_THRESHOLD) {
            sendWarn("w", "SDcard disk use of more than 95%,usedSize is" + usedSize + ",totalSize is" + totalSize, AppCommonUtil.getTag());
            return false;
        }
        sendWarn("w", "SDcard disk space ss enough,usedSize is" + usedSize + ",totalSize is" + totalSize, AppCommonUtil.getTag());
        return true;
    }

    /**
     * 获取SSD总容量大小,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getSSDTotalSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_mmc(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取SSD已用容量大小,由研华API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getSSDUsedSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_mmc(2, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获取SSD能用容量大小
     *
     * @return
     * @throws RemoteException
     */
    public String getSSDAvailableSize() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_disk_mmc(3, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }


    /**
     * SSD 使用率
     *
     * @throws RemoteException
     */
    public Double getSSDUsageRate() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getSSDTotalSize());
        Double usedSize = Double.parseDouble(getSSDUsedSize());
        Double usage_rate = usedSize / totalSize;
        return usage_rate;
    }


    /**
     * SSD使用情况自检
     *
     * @throws RemoteException
     */
    public Boolean SSDUsageCheak() throws RemoteException, IOException {
        Double totalSize = Double.parseDouble(getSSDTotalSize());
        Double usedSize = Double.parseDouble(getSSDUsedSize());
        //  Double AvailableSize = Double.parseDouble(getSSDAvailableSize());
        if ((usedSize / totalSize) > Config.STORE_ALARM_THRESHOLD) {
            sendWarn("w", "SSD disk use of more than 95%,usedSize is" + usedSize + ",totalSize is" + totalSize, AppCommonUtil.getTag());
            return false;
        }
        sendWarn("w", "SSD disk space is enough,usedSize is" + usedSize + ",totalSize is" + totalSize, AppCommonUtil.getTag());
        return true;
    }

    /**
     * 开机自检项目
     *
     * @throws IOException
     * @throws RemoteException
     */
    public void bootCheck() throws IOException, RemoteException {
        MMCUsageCheck();
        SSDUsageCheak();
        SdcardUsageCheck();
    }
}
