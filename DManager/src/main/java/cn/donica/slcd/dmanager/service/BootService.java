package cn.donica.slcd.dmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;

import cn.donica.slcd.dmanager.check.Check;
import cn.donica.slcd.dmanager.utils.CpuUtil;

public class BootService extends Service {
    public BootService() {
    }

    public static final String TAG = "BootService";
    public static final String ACTION = "cn.donica.slcd.dmanager.service.BootService";
    private LoopTaskThread loopTaskThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Check check = new Check();
        try {
//            check.bootCheck();
            check.bootStart();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onStartCommand(Intent intent, int startId) throws IOException, RemoteException {
//        this.loopTaskThread = new LoopTaskThread();
//        this.loopTaskThread.start();

    }

    @Override
    public void onStart(Intent intent, int startId) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return null;
    }

    private class LoopTaskThread extends Thread {
        @Override
        public void run() {
            try {
                try {
                    CpuUtil cpuUtil = new CpuUtil();
                    cpuUtil.getCpuTemperatureAlarm();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stopSelf();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
