package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/23.
 */
public class VoltageUtil {
    private IHwtestService hwtestService;

    /**
     * 获得Vcc的电压，由研华的API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getVccVoltage() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_vcc(0, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获得V1的电压，由研华的API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getV1Voltage() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_vcc(1, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获得V2的电压，由研华的API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getV2Voltage() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_vcc(2, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }

    /**
     * 获得V3的电压，由研华的API提供
     *
     * @return
     * @throws RemoteException
     */
    public String getV3Voltage() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_vcc(3, strinfo);//获取到的信息保存在数组strinfo中
        byte len = strinfo[0];//strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);//得到最终的信息
        return info;
    }


    /**
     * 判断Vcc电压是否正常
     *
     * @return
     */
    public boolean isVccVoltageNormal() throws RemoteException, IOException {
        double v = Double.valueOf(getVccVoltage());
        if (Config.VCC_LOW <= v && v <= Config.VCC_HIGH) {
            sendWarn("i", "Vcc Voltage is" + v, AppCommonUtil.getTag());
            return true;
        }
        sendWarn("e", "Vcc Voltage is" + v, AppCommonUtil.getTag());
        return false;
    }

    /**
     * 判断V1电压是否正常
     *
     * @return
     */
    public boolean isV1VoltageNormal() throws RemoteException, IOException {
        double v = Double.valueOf(getV1Voltage());
        if (Config.V1_LOW <= v && v <= Config.V1_HIGH) {
            sendWarn("i", "V1 Voltage is" + v, AppCommonUtil.getTag());
            return true;
        }
        sendWarn("e", "V1 Voltage is" + v, AppCommonUtil.getTag());
        return false;
    }

    /**
     * 判断V2电压是否正常
     *
     * @return
     */
    public boolean isV2VoltageNormal() throws RemoteException, IOException {
        double v = Double.valueOf(getV2Voltage());
        if (Config.V2_LOW <= v && v <= Config.V1_HIGH) {
            sendWarn("i", "V2 Voltage is" + v, AppCommonUtil.getTag());
            return true;
        }
        sendWarn("e", "V2 Voltage is" + v, AppCommonUtil.getTag());
        return false;
    }

    /**
     * 判断V3电压是否正常
     *
     * @return
     */
    public boolean isV3VoltageNormal() throws RemoteException, IOException {
        double v = Double.valueOf(getV3Voltage());
        if (Config.V3_LOW <= v && v <= Config.V3_HIGH) {
            sendWarn("i", "V3 Voltage is" + v, AppCommonUtil.getTag());
            return true;
        }
        sendWarn("e", "V3 Voltage is" + v, AppCommonUtil.getTag());
        return false;
    }


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
        logWarnTrap.sendWarn(level, 1, 1, "Voltage info:" + logContent, location);
    }

    /**
     * 开机启动检查电压
     *
     * @throws IOException
     * @throws RemoteException
     */
    public void bootCheck() throws IOException, RemoteException {
        isVccVoltageNormal();
        isV1VoltageNormal();
        isV2VoltageNormal();
        isV3VoltageNormal();
    }
}


