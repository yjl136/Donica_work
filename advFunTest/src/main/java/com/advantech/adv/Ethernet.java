package com.advantech.adv;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Ethernet {

    private ExecCmd execcmd;
    public Handler mainhandler;

    Ethernet() {
        execcmd = new ExecCmd();
    }

    public void Ping(String str) {

        String resString = execcmd.resultExeCmd("ping -c 3 -w 100 " + str);
        String spStr[] = resString.split("%");
        String spStr2[] = spStr[0].split(", ");

        Log.e("+++yixuan+++", spStr2[spStr2.length - 1]);
        if (spStr2[spStr2.length - 1].equals("0")) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("result", true);
            msg.setData(bundle);// mes利用Bundle传递数据
            mainhandler.sendMessage(msg);// 用activity中的handler发送消息
        } else {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("result", false);
            msg.setData(bundle);// mes利用Bundle传递数据
            mainhandler.sendMessage(msg);// 用activity中的handler发送消息
            execcmd.writeLog("Ethernet Failed");
            execcmd.writeLog("====================================\n");
        }
        Log.e("+++yixuan+++", resString);

    }

    public String startTest(Handler handler) {
        mainhandler = handler;
        execcmd.execShell("ifconfig eth0 192.168.0.2 netmask 255.255.255.0 up");
        String s = "";
        Ping("192.168.0.1");
        return s;
    }
}