package cn.donica.slcd.dmanager.check;

import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

import cn.donica.slcd.dmanager.common.ThreadPriorityQueue;
import cn.donica.slcd.dmanager.utils.CpuUtil;
import cn.donica.slcd.dmanager.utils.LedUtil;

/**
 * Created by liangmingjie on 2015/12/22.
 */
public class Check {
    private static final String TAG = "Check";
    private ThreadPriorityQueue mThreadPriorityQueue;
    private Thread thread1, thread2, thread3, thread4, thread5, thread6;

    public void bootCheck() throws RemoteException, IOException {
        createThread();
        mThreadPriorityQueue = new ThreadPriorityQueue.QueueBuilder()
                .addThread(thread1, 3).addThread(thread2, 7)
                .addThread(thread3, 9).addThread(thread4, 6)
                .addThread(thread5, 8).addThread(thread6, 5)
                .addThread(cpuThread, 10)
                .create();
        mThreadPriorityQueue.run();

    }

    public void bootStart() throws RemoteException {
        LedUtil ledUtil = new LedUtil();
        ledUtil.bootStart();
    }

    Thread cpuThread = new Thread(new Runnable() {
        @Override
        public void run() {
            CpuUtil cpuUtil = new CpuUtil();
            float i = cpuUtil.getCpuLoad();
            Log.d(TAG, "CPU 的负载为" + i);
        }
    });

    public void createThread() {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "这是线程1，执行了10s");
            }
        });
        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "这是线程2，执行了1.5s");
            }
        });
        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "这是线程3，执行了1s");
            }
        });
        thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "这是线程4，执行了0.8s");
            }
        });
        thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "这是线程5，执行了1s");
            }
        });
        thread6 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "这是线程6，执行了0.5s");
            }
        });
    }
}
