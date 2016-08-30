package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;

import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/24.
 */
public class BluetoothUtil {

    private IHwtestService hwtestService = null;

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
        logWarnTrap.sendWarn(level, 1, 1, "Bluetooth info:" + logContent, location);
    }


    /**
     * 检测bluetooth设备是否挂载成功,由研华API提供
     * 0：相关设备挂载成功或已经ready
     * 非0：相关设备未挂载或者准备好
     *
     * @throws RemoteException
     */
    public String test_bluetooth() throws RemoteException {
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.test_bluetooth(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        return info;
    }

    /**
     * 检测bluetooth设备是否挂载成功
     *
     * @return
     * @throws RemoteException
     */
    public boolean get_bluetooth_status() throws RemoteException {
        if ((test_bluetooth()).equals("0")) {
            return true;
        }
        return false;
    }


    /**
     * 开机自检项目
     */
    public void bootCheck() {


    }

}
