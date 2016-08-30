package com.advantech.advfuntest;

import java.io.File;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.app.Service;
import android.content.Intent;

public class SataFun {

    public class Sata {
        public String sataId;
        public boolean isPass;
        public String sataMsg;

        Sata(String Id, boolean isP, String msg) {
            this.sataId = Id;
            this.isPass = isP;
            this.sataMsg = msg;
        }
    }

    public Handler mainhandler;
    public TextView resultTextView;
    public ExecCmd execcmd;
    private Device dev;
    private int sataCount;
    private Sata[] sata;
    private AdvFunTest mainActivity = new AdvFunTest();

    private Thread sataThread = new Thread(new Runnable() {

        @Override
        public void run() {
            SataFunTest();
            for (int i = 0; i < sataCount; i++) {
                if (sata[i].isPass == false) {
                    dev.sataFun = false;
                    dev.sataMsg = dev.sataMsg + sata[i].sataMsg;
                }
            }
            if (dev.sataFun == false) {
                execcmd.writeLog("====================================\n");
                execcmd.writeLog(dev.sataMsg);
                execcmd.writeLog("====================================\n");
            }
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("result", dev.sataFun);
            msg.setData(bundle);// mes利用Bundle传递数据
            mainhandler.sendMessage(msg);// 用activity中的handler发送消息
        }

    });

//	private Handler handler = new Handler();
//	private Runnable reportRunable = new Runnable(){
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			mainActivity.ItemTest(dev.sataFun);
//		}
//		
//	};

    SataFun() {
        execcmd = new ExecCmd();
        dev = new Device();
        dev.sataFun = true;
        dev.sataMsg = new String("");

        //这里添加：读取jSon文件关于Udisk的配置。Udisk的数量及挂载点
        sataCount = 1;

        sata = new Sata[sataCount];
        String[] sataId = {"/sys/devices/platform/ahci.0/host2/target2:0:0/2:0:0:0/block", "/sys/devices/pci0000:00/0000:00:00.0/0000:01:00.0/host0/target0:0:0/0:0:0:0/block"};

        for (int i = 0; i < sataCount; i++) {
            sata[i] = new Sata(sataId[i], false, "");
        }
    }

    void startTest(Handler handler, TextView textview) {
        mainhandler = handler;
        resultTextView = textview;
        if (sataThread.isAlive())
            sataThread.interrupt();
        sataThread.start();
    }

    public void SataFunTest() {
        for (int i = 0; i < sataCount; i++) {
            if (sata[i].isPass == false) {
                String addr = sata[i].sataId.toString();
                File file = new File(addr);
                if (file.exists()) {
                    String cmd = "ls " + sata[i].sataId.toString();
                    String dev_node = execcmd.resultExeCmd(cmd);
                    cmd = "dd if=/dev/urandom of=/data/data/com.advantech.advfuntest/hdd_data bs=1024 count=1 conv=fdatasync";
                    execcmd.resultExeCmd(cmd);

                    //cmd = "busybox umount /mnt/sata/";
                    //execcmd.execShell(cmd);
                    //cmd = "mount -t vfat /dev/block/" + dev_node + "[1234]" + " /mnt/sata/";
                    //execcmd.execShell(cmd);

                    cmd = "dd if=/data/data/com.advantech.advfuntest/hdd_data of=/mnt/sata/testFile bs=1024 count=1 conv=fdatasync";
                    execcmd.execShell(cmd);
                    cmd = "dd if=/mnt/sata/testFile of=/data/data/com.advantech.advfuntest/hdd_data1 bs=1024 count=1 conv=fdatasync";
                    execcmd.execShell(cmd);
                    execcmd.execShell("chown system:system /data/data/com.advantech.advfuntest/hdd* ");
                    execcmd.execShell("chmod 777 /data/data/com.advantech.advfuntest/hdd* ");
                    byte hdd_data[] = execcmd.readFile("/data/data/com.advantech.advfuntest/hdd_data").getBytes();
                    byte hdd_data1[] = execcmd.readFile("/data/data/com.advantech.advfuntest/hdd_data1").getBytes();
                    for (int j = 0; j < 1024; j++) {
                        if (hdd_data[j] != hdd_data1[j]) {
                            //mainActivity.ItemTest(false);
                            //isPassText[i].setText("Faile");
                            //isPassText[i].setTextColor(Color.RED);
                            sata[i].isPass = false;
                            sata[i].sataMsg = "Not Found device in" + sata[i].sataId + "\n";
                            break;
                        }
                        sata[i].isPass = true;
                        sata[i].sataMsg = "";
                        //mainActivity.ItemTest(true);
                        //isPassText[i].setText("Pass");
                        //isPassText[i].setTextColor(Color.GREEN);
                    }
                } else {
                    //handler.post(reportRunable);
                    sata[i].isPass = false;
                    sata[i].sataMsg = "Not Found direction of " + sata[i].sataId + "\n";
                }
            }
        }
    }

}
