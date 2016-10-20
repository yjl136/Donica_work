package com.advantech.adv;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class UdiskFun {

    public class Udisk {
        public String udiskId;
        public boolean isPass;
        public String udiskMsg;

        Udisk(String Id, boolean isP, String msg) {
            this.udiskId = Id;
            this.isPass = isP;
            this.udiskMsg = msg;
        }
    }

    public ExecCmd execcmd;
    public Handler mainhandler;
    public TextView[] udiskItemTextView;

    private LinearLayout backBtn;
    private Device dev;
    public int udiskCount;
    private Udisk[] udisk;
    private AdvFunTest mainActivity = new AdvFunTest();
    private Handler itemhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int passItem = msg.getData().getInt("udiskItem");
            udiskItemTextView[passItem].setBackgroundColor(Color.GREEN);
        }

    };

    private Thread udiskThread = new Thread(new Runnable() {

        @Override
        public void run() {
            for (int i = 0; i < 15; i++) {
                if (udisk[0].isPass == true && udisk[1].isPass == true
                        && udisk[2].isPass == true && udisk[3].isPass == true
                        && udisk[4].isPass == true && udisk[5].isPass == true) {
                    break;
                }
                UdiskFunTest();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            dev.udiskFun = true;
            for (int i = 0; i < udiskCount; i++) {
                if (udisk[i].isPass == false) {
                    dev.udiskFun = false;
                    dev.udiskMsg = dev.udiskMsg + udisk[i].udiskMsg;
                }
            }
            if (dev.udiskFun == false) {
                execcmd.writeLog("====================================\n");
                execcmd.writeLog(dev.udiskMsg);
                execcmd.writeLog("====================================\n");
            }
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("result", dev.udiskFun);
            msg.setData(bundle);// mes利用Bundle传递数据
            mainhandler.sendMessage(msg);// 用activity中的handler发送消息
        }

    });

    public void UdiskFunTest() {
        for (int i = 0; i < udiskCount; i++) {
            if (udisk[i].isPass == false) {
                String addr = "/sys/bus/usb/devices/"
                        + udisk[i].udiskId.toString();
                Log.e("+++yixuan+++", addr);
                File file = new File(addr);
                if (file.exists()) {
                    File mousefile = new File(addr + "/input");
                    if (mousefile.exists()) {
                        udisk[i].isPass = true;
                        udisk[i].udiskMsg = "";
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("result", dev.udiskFun);
                        bundle.putInt("udiskItem", i);
                        msg.setData(bundle);// mes利用Bundle传递数据
                        itemhandler.sendMessage(msg);// 用activity中的handler发送消息
                        continue;
                    }
                    String cmd = "ls /sys/bus/usb/devices/"
                            + udisk[i].udiskId.toString()
                            + "/host*/target*:0:0/*:0:0:0/block";
                    String dev_node = execcmd.resultExeCmd(cmd);

                    cmd = "busybox umount /mnt/udisk/";
                    execcmd.execShell(cmd);
                    cmd = "mount -t vfat /dev/block/" + dev_node + "[1234]" + " /mnt/udisk/";
                    execcmd.execShell(cmd);

                    cmd = "dd if=/dev/urandom of=/data/hdd_data bs=1024 count=1";
                    cmd = "dd if=/data/hdd_data of=/mnt/udisk/testFile bs=1024 count=1 conv=fdatasync";
                    // resultExeCmd(cmd);
                    execcmd.execShell(cmd);
                    cmd = "dd if=/mnt/udisk/testFile of=/data/hdd_data1 bs=1024 count=1 conv=fdatasync";
                    // resultExeCmd(cmd);
                    execcmd.execShell(cmd);
                    execcmd.execShell("chown system:system /data/hdd* ");
                    execcmd.execShell("chmod 777 /data/hdd* ");
                    byte hdd_data[] = execcmd.readFile(
                            "/data/hdd_data")
                            .getBytes();
                    byte hdd_data1[] = execcmd.readFile(
                            "/data/hdd_data1")
                            .getBytes();
                    for (int j = 0; j < 1024; j++) {
                        if (hdd_data[j] != hdd_data1[j]) {
                            udisk[i].isPass = false;
                            udisk[i].udiskMsg = "Not Found device in"
                                    + udisk[i].udiskId + "\n";
                            break;
                        }
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("result", dev.udiskFun);
                        bundle.putInt("udiskItem", i);
                        msg.setData(bundle);// mes利用Bundle传递数据
                        itemhandler.sendMessage(msg);// 用activity中的handler发送消息

                        udisk[i].isPass = true;
                        udisk[i].udiskMsg = "";
                    }
                    //execShell("rm -rf /data/hdd* ; sync ;");
                } else {
                    udisk[i].isPass = false;
                    udisk[i].udiskMsg = "Not Found direction of "
                            + udisk[i].udiskId + "\n";
                }
            }
        }
    }

    UdiskFun() {
        execcmd = new ExecCmd();
        dev = new Device();
        dev.udiskFun = false;
        dev.udiskMsg = new String("");

        // 这里添加：读取jSon文件关于Udisk的配置。Udisk的数量及挂载点
        udiskCount = 6;
        udisk = new Udisk[udiskCount];
        String[] udiskId = {"2-1.1:1.0", "2-1.2:1.0", "2-1.3:1.0",
                "2-1.4:1.0", "1-1.1:1.0", "1-1.2:1.0"};

        for (int i = 0; i < udiskCount; i++) {
            udisk[i] = new Udisk(udiskId[i], false, "");
        }
        udiskItemTextView = new TextView[udiskCount];
    }

    void startTest(Handler handler, TextView[] textview) {
        //sataThread.start();
        mainhandler = handler;
        for (int i = 0; i < udiskCount; i++) {
            udiskItemTextView[i] = textview[i];
        }

        if (udiskThread.isAlive())
            udiskThread.interrupt();
        udiskThread.start();
    }
}
