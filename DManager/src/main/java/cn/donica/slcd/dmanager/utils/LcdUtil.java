package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;

import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/24.
 */
public class LcdUtil {
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
        logWarnTrap.sendWarn(level, 1, 1, "Screen info:" + logContent, location);
    }

    /**
     * 获取LCD屏背光情况 0：lcd背光已经开启 非0：lcd背光未开启
     *
     * @return
     */
    public String getLCDBacklightStatus() throws RemoteException {
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.test_lcd(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        return info;
    }


    /**
     * 判断LCD背光状态是否正常
     */
    public void isLCDBacklightStatusNormal() {

    }

    /**
     * 开机自检项目
     */
    public void bootCheck() {

    }
}
