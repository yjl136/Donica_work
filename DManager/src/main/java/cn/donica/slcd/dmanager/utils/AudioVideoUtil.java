package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;

import java.io.IOException;

import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

/**
 * Created by liangmingjie on 2015/12/24.
 * 音视频的工具类
 */
public class AudioVideoUtil {
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
        logWarnTrap.sendWarn(level, 1, 1, "Audio and Video info:" + logContent, location);
    }

    /**
     * 检测是否有音频信号
     *
     * @throws RemoteException
     */
    public String test_audio_signal() throws RemoteException {

        return null;
    }

    /**
     * 检测耳机是否插入
     *
     * @throws RemoteException
     */
    public String test_audio_headset() throws RemoteException {

        return null;
    }

    /**
     * 检测音频设备是否挂载成功
     */
    public String test_audio_chip() {
        return null;
    }

    /**
     * 检测ntsc设备是否挂载成功
     *
     * @return
     */
    public String test_ntsc() {
        return null;
    }

    /**
     * 获得7282m的连接状态
     *
     * @return
     */
    public String get_ntsc_status() {
        return null;
    }


    /**
     * 开机自检项目
     */
    public void bootCheck() {

    }
}
