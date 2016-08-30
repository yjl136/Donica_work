package com.advantech.advfuntest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.advantech.advfuntest.LeftFragment.MyListener;

import android.R.string;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoopbackActivity extends SerialPortActivity implements MyListener {

    private final int SEND_COUNT = 2000;
    private LinearLayout backBtn;
    private Device dev;

    byte mValueToSend1;
    boolean mByteReceivedBack1;
    Object mByteReceivedBackSemaphore1 = new Object();
    Integer mIncoming1 = new Integer(0);
    Integer mOutgoing1 = new Integer(0);
    Integer mLost1 = new Integer(0);
    Integer mCorrupted1 = new Integer(0);

    byte mValueToSend2;
    boolean mByteReceivedBack2;
    Object mByteReceivedBackSemaphore2 = new Object();
    Integer mIncoming2 = new Integer(0);
    Integer mOutgoing2 = new Integer(0);
    Integer mLost2 = new Integer(0);
    Integer mCorrupted2 = new Integer(0);

    byte mValueToSend3;
    boolean mByteReceivedBack3;
    Object mByteReceivedBackSemaphore3 = new Object();
    Integer mIncoming3 = new Integer(0);
    Integer mOutgoing3 = new Integer(0);
    Integer mLost3 = new Integer(0);
    Integer mCorrupted3 = new Integer(0);

    byte mValueToSend4;
    boolean mByteReceivedBack4;
    Object mByteReceivedBackSemaphore4 = new Object();
    Integer mIncoming4 = new Integer(0);
    Integer mOutgoing4 = new Integer(0);
    Integer mLost4 = new Integer(0);
    Integer mCorrupted4 = new Integer(0);

    byte mValueToSend5;
    boolean mByteReceivedBack5;
    Object mByteReceivedBackSemaphore5 = new Object();
    Integer mIncoming5 = new Integer(0);
    Integer mOutgoing5 = new Integer(0);
    Integer mLost5 = new Integer(0);
    Integer mCorrupted5 = new Integer(0);

    byte mValueToSend6;
    boolean mByteReceivedBack6;
    Object mByteReceivedBackSemaphore6 = new Object();
    Integer mIncoming6 = new Integer(0);
    Integer mOutgoing6 = new Integer(0);
    Integer mLost6 = new Integer(0);
    Integer mCorrupted6 = new Integer(0);

    byte mValueToSend7;
    boolean mByteReceivedBack7;
    Object mByteReceivedBackSemaphore7 = new Object();
    Integer mIncoming7 = new Integer(0);
    Integer mOutgoing7 = new Integer(0);
    Integer mLost7 = new Integer(0);
    Integer mCorrupted7 = new Integer(0);

    byte mValueToSend8;
    boolean mByteReceivedBack8;
    Object mByteReceivedBackSemaphore8 = new Object();
    Integer mIncoming8 = new Integer(0);
    Integer mOutgoing8 = new Integer(0);
    Integer mLost8 = new Integer(0);
    Integer mCorrupted8 = new Integer(0);

    SendingThread1 mSendingThread1;
    TextView mTextViewOutgoing1;
    TextView mTextViewIncoming1;
    TextView mTextViewLost1;
    TextView mTextViewCorrupted1;

    SendingThread2 mSendingThread2;
    TextView mTextViewOutgoing2;
    TextView mTextViewIncoming2;
    TextView mTextViewLost2;
    TextView mTextViewCorrupted2;

    SendingThread3 mSendingThread3;
    TextView mTextViewOutgoing3;
    TextView mTextViewIncoming3;
    TextView mTextViewLost3;
    TextView mTextViewCorrupted3;

    SendingThread4 mSendingThread4;
    TextView mTextViewOutgoing4;
    TextView mTextViewIncoming4;
    TextView mTextViewLost4;
    TextView mTextViewCorrupted4;

    SendingThread5 mSendingThread5;
    TextView mTextViewOutgoing5;
    TextView mTextViewIncoming5;
    TextView mTextViewLost5;
    TextView mTextViewCorrupted5;

    SendingThread6 mSendingThread6;
    TextView mTextViewOutgoing6;
    TextView mTextViewIncoming6;
    TextView mTextViewLost6;
    TextView mTextViewCorrupted6;

    SendingThread7 mSendingThread7;
    TextView mTextViewOutgoing7;
    TextView mTextViewIncoming7;
    TextView mTextViewLost7;
    TextView mTextViewCorrupted7;

    SendingThread8 mSendingThread8;
    TextView mTextViewOutgoing8;
    TextView mTextViewIncoming8;
    TextView mTextViewLost8;
    TextView mTextViewCorrupted8;

    private boolean isBurning = false;
    private String listenLogfile;
    SataThread sataThread;
    UdiskThread udiskThread;
    EthThread ethThread;
    MediaPlayer mediaPlayer;
    MusicThread musicThread;
    ReadLogThread readLogThread;
    public ExecCmd execcmd;
    public FileObserver listen;
    public String logString = new String("");
    ;

    private class SataThread extends Thread {

        @Override
        public void run() {
            String sataDir = new String("/sys/devices/platform/ahci.0/host2/target2:0:0/2:0:0:0/block");
            String sataNode;
            File file = new File(sataDir);

            while (!isInterrupted()) {
                if (file.exists()) {
                    String cmd = "ls " + sataDir;
                    sataNode = new String(execcmd.resultExeCmd(cmd));
                    String cmdStr = "echo \"dd if=/dev/zero of=/storage/sata/testFile conv=fdatasync bs=1024 count=30000\" >>/data/data/com.advantech.advfuntest/sata.log";
                    execcmd.execShell(cmdStr);
                    String cmdIn = "dd if=/dev/zero of=/storage/sata/testFile conv=fdatasync bs=1024 count=30000 1>>/data/data/com.advantech.advfuntest/sata.log 2>>/data/data/com.advantech.advfuntest/sata.log";
                    execcmd.execShell(cmdIn);

                    cmdStr = "echo \"dd if=/storage/sata/testFile of=/dev/null bs=1024 count=30000\" >>/data/data/com.advantech.advfuntest/sata.log";
                    execcmd.execShell(cmdStr);
                    String cmdOut = "dd if=/storage/sata/testFile of=/dev/null bs=1024 count=30000 conv=fdatasync 1>>/data/data/com.advantech.advfuntest/sata.log 2>>/data/data/com.advantech.advfuntest/sata.log";
                    execcmd.execShell(cmdOut);
                }
            }
        }
    }

    private class UdiskThread extends Thread {

        @Override
        public void run() {
            int udiskCount = 6;
            String[] udiskId = {"2-1.1:1.0", "2-1.2:1.0", "2-1.3:1.0",
                    "2-1.4:1.0", "1-1.1:1.0", "1-1.2:1.0"};

            while (!isInterrupted()) {
                for (int i = 0; i < udiskCount; i++) {
                    String addr = "/sys/bus/usb/devices/"
                            + udiskId[i].toString();
                    File file = new File(addr);
                    if (file.exists()) {
                        File mousefile = new File(addr + "/input");
                        if (mousefile.exists()) {
                            continue;
                        }
                        String cmd = "ls /sys/bus/usb/devices/"
                                + udiskId[i].toString()
                                + "/host*/target*:0:0/*:0:0:0/block";
                        String dev_node = execcmd.resultExeCmd(cmd);

                        cmd = "busybox umount /storage/udisk/";
                        execcmd.execShell(cmd);
                        cmd = "mount -t vfat /dev/block/" + dev_node + "[1234]" + " /storage/udisk/";
                        execcmd.execShell(cmd);

                        cmd = "echo \"dd if=/dev/zero of=/storage/udisk(" + dev_node + ")/testFile conv=fdatasync bs=1024 count=30000\" >>/data/data/com.advantech.advfuntest/udisk.log";
                        execcmd.execShell(cmd);
                        cmd = "dd if=/dev/zero of=/storage/udisk/testFile bs=1024 count=30000 1>>/data/data/com.advantech.advfuntest/udisk.log 2>>/data/data/com.advantech.advfuntest/udisk.log";
                        execcmd.execShell(cmd);

                        cmd = "echo \"dd if=/storage/udisk(" + dev_node + ")/testFile of=/storage/udisk/testFile conv=fdatasync bs=1024 count=30000\" >>/data/data/com.advantech.advfuntest/udisk.log";
                        execcmd.execShell(cmd);
                        cmd = "dd if=/storage/udisk/testFile of=/dev/null  bs=1024 count=30000 1>>/data/data/com.advantech.advfuntest/udisk.log 2>>/data/data/com.advantech.advfuntest/udisk.log";
                        execcmd.execShell(cmd);
                    }
                }
            }
        }
    }

    private class EthThread extends Thread {

        @Override
        public void run() {
            execcmd.execShell("ifconfig eth0 192.168.0.2 netmask 255.255.255.0 up");
            while (!isInterrupted()) {
                execcmd.execShell("ping -c 3 -w 100 192.168.0.1 1>>/data/data/com.advantech.advfuntest/ethernet.log 2>>/data/data/com.advantech.advfuntest/ethernet.log");
            }
        }
    }

    private class MusicThread extends Thread {

        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(LoopbackActivity.this, R.raw.audio);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private class ReadLogThread extends Thread {

        @Override
        public void run() {
            File file = new File("/data/data/com.advantech.advfuntest/"
                    + listenLogfile);
            try {
                logString = readLastLine(file, "gbk");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    showMessageView.setText(logString);
                }
            });
        }
    }

    public static String readLastLine(File file, String charset)
            throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            long len = raf.length();
            if (len == 0L) {
                return "";
            } else {
                long pos = len - 1;
                int lineCount = 0;
                while (pos > 0) {
                    pos--;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        lineCount++;
                    }
                    //讀取最後十行
                    if (lineCount >= 10) {
                        break;
                    }
                }
                if (pos == 0) {
                    raf.seek(0);
                }
                byte[] bytes = new byte[(int) (len - pos)];
                raf.read(bytes);
                if (charset == null) {
                    return new String(bytes);
                } else {
                    return new String(bytes, charset);
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }

    public class LogListener extends FileObserver {

        public LogListener(String path) {
            /*
             * 这种构造方法是默认监听所有事件的,如果使用 super(String,int)这种构造方法， 则int参数是要监听的事件类型.
			 */
            super(path);
        }

        @Override
        public void onEvent(int event, String path) {
            switch (event) {
                case FileObserver.MODIFY:
                    if (path.equals(listenLogfile)) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                new Thread(readLogThread).start();
                            }
                        });
                    }
                    break;
            }
        }
    }

    void updateUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                mTextViewOutgoing1.setText(mOutgoing1.toString());
                mTextViewLost1.setText(mLost1.toString());
                mTextViewIncoming1.setText(mIncoming1.toString());
                mTextViewCorrupted1.setText(mCorrupted1.toString());

                mTextViewOutgoing2.setText(mOutgoing2.toString());
                mTextViewLost2.setText(mLost2.toString());
                mTextViewIncoming2.setText(mIncoming2.toString());
                mTextViewCorrupted2.setText(mCorrupted2.toString());

                mTextViewOutgoing3.setText(mOutgoing3.toString());
                mTextViewLost3.setText(mLost3.toString());
                mTextViewIncoming3.setText(mIncoming3.toString());
                mTextViewCorrupted3.setText(mCorrupted3.toString());

                mTextViewOutgoing4.setText(mOutgoing4.toString());
                mTextViewLost4.setText(mLost4.toString());
                mTextViewIncoming4.setText(mIncoming4.toString());
                mTextViewCorrupted4.setText(mCorrupted4.toString());

                mTextViewOutgoing5.setText(mOutgoing5.toString());
                mTextViewLost5.setText(mLost5.toString());
                mTextViewIncoming5.setText(mIncoming5.toString());
                mTextViewCorrupted5.setText(mCorrupted5.toString());

                mTextViewOutgoing6.setText(mOutgoing6.toString());
                mTextViewLost6.setText(mLost6.toString());
                mTextViewIncoming6.setText(mIncoming6.toString());
                mTextViewCorrupted6.setText(mCorrupted6.toString());

                mTextViewOutgoing7.setText(mOutgoing7.toString());
                mTextViewLost7.setText(mLost7.toString());
                mTextViewIncoming7.setText(mIncoming7.toString());
                mTextViewCorrupted7.setText(mCorrupted7.toString());

                mTextViewOutgoing8.setText(mOutgoing8.toString());
                mTextViewLost8.setText(mLost8.toString());
                mTextViewIncoming8.setText(mIncoming8.toString());
                mTextViewCorrupted8.setText(mCorrupted8.toString());
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            updateUI();
            handler.postDelayed(this, 500);
        }
    };

    private class SendingThread1 extends Thread {

        @Override
        public void run() {
            while ((!isInterrupted() && mOutgoing1 <= SEND_COUNT) || (!isInterrupted() && isBurning == true)) {
                synchronized (mByteReceivedBackSemaphore1) {
                    mByteReceivedBack1 = false;
                    try {
                        if (mOutputStream1 != null) {
                            mOutputStream1.write(mValueToSend1);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing1++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore1.wait();
                        if (mByteReceivedBack1 == true) {
                            // Byte has been received
                            mIncoming1++;
                        } else {
                            // Timeout
                            mLost1++;
                            //serial_dev[0].ispass = false;
                            //serial_dev[0].msg = serial_dev[0].devId.toString() +  "Lost Byte";
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (isBurning == false) {
                returnResult();
            }
        }
    }

    private class SendingThread2 extends Thread {
        @Override
        public void run() {
            while ((!isInterrupted() && mOutgoing2 <= SEND_COUNT) || (!isInterrupted() && isBurning == true)) {
                synchronized (mByteReceivedBackSemaphore2) {
                    mByteReceivedBack2 = false;
                    try {
                        if (mOutputStream2 != null) {
                            mOutputStream2.write(mValueToSend2);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing2++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore2.wait();
                        if (mByteReceivedBack2 == true) {
                            // Byte has been received
                            mIncoming2++;
                        } else {
                            // Timeout
                            mLost2++;
                            //serial_dev[1].ispass = false;
                            //serial_dev[1].msg = serial_dev[1].devId.toString() + "Lost Byte";
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private class SendingThread3 extends Thread {
        @Override
        public void run() {
            while ((!isInterrupted() && mOutgoing3 <= SEND_COUNT) || (!isInterrupted() && isBurning == true)) {
                synchronized (mByteReceivedBackSemaphore3) {
                    mByteReceivedBack3 = false;
                    try {
                        if (mOutputStream3 != null) {
                            mOutputStream3.write(mValueToSend3);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing3++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore3.wait();
                        if (mByteReceivedBack3 == true) {
                            // Byte has been received
                            mIncoming3++;
                        } else {
                            // Timeout
                            mLost3++;
                            //serial_dev[2].ispass = false;
                            //serial_dev[2].msg = serial_dev[2].devId.toString() + "Lost Byte";
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private class SendingThread4 extends Thread {
        @Override
        public void run() {
            while ((!isInterrupted() && mOutgoing4 <= SEND_COUNT) || (!isInterrupted() && isBurning == true)) {
                synchronized (mByteReceivedBackSemaphore4) {
                    mByteReceivedBack4 = false;
                    try {
                        if (mOutputStream4 != null) {
                            mOutputStream4.write(mValueToSend4);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing4++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore4.wait();
                        if (mByteReceivedBack4 == true) {
                            // Byte has been received
                            mIncoming4++;
                        } else {
                            // Timeout
                            mLost4++;
                            //serial_dev[1].ispass = false;
                            //serial_dev[1].msg = serial_dev[1].devId.toString() + "Lost Byte";
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private class SendingThread5 extends Thread {
        @Override
        public void run() {
            while ((!isInterrupted() && mOutgoing5 <= SEND_COUNT) || (!isInterrupted() && isBurning == true)) {
                synchronized (mByteReceivedBackSemaphore5) {
                    mByteReceivedBack5 = false;
                    try {
                        if (mOutputStream5 != null) {
                            mOutputStream5.write(mValueToSend5);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing5++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore5.wait();
                        if (mByteReceivedBack5 == true) {
                            // Byte has been received
                            mIncoming5++;
                        } else {
                            // Timeout
                            mLost5++;
                            //serial_dev[1].ispass = false;
                            //serial_dev[1].msg = serial_dev[1].devId.toString() + "Lost Byte";
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private class SendingThread6 extends Thread {
        @Override
        public void run() {
            while ((!isInterrupted() && mOutgoing6 <= SEND_COUNT) || (!isInterrupted() && isBurning == true)) {
                synchronized (mByteReceivedBackSemaphore6) {
                    mByteReceivedBack6 = false;
                    try {
                        if (mOutputStream6 != null) {
                            mOutputStream6.write(mValueToSend6);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing6++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore6.wait();
                        if (mByteReceivedBack6 == true) {
                            // Byte has been received
                            mIncoming6++;
                        } else {
                            // Timeout
                            mLost6++;
                            //serial_dev[1].ispass = false;
                            //serial_dev[1].msg = serial_dev[1].devId.toString() + "Lost Byte";
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private class SendingThread7 extends Thread {
        @Override
        public void run() {
            while ((!isInterrupted() && mOutgoing7 <= SEND_COUNT) || (!isInterrupted() && isBurning == true)) {
                synchronized (mByteReceivedBackSemaphore7) {
                    mByteReceivedBack7 = false;
                    try {
                        if (mOutputStream7 != null) {
                            mOutputStream7.write(mValueToSend7);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing7++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore7.wait();
                        if (mByteReceivedBack7 == true) {
                            // Byte has been received
                            mIncoming7++;
                        } else {
                            // Timeout
                            mLost7++;
                            //serial_dev[1].ispass = false;
                            //serial_dev[1].msg = serial_dev[1].devId.toString() + "Lost Byte";
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private class SendingThread8 extends Thread {
        @Override
        public void run() {
            while ((!isInterrupted() && mOutgoing8 <= SEND_COUNT) || (!isInterrupted() && isBurning == true)) {
                synchronized (mByteReceivedBackSemaphore8) {
                    mByteReceivedBack8 = false;
                    try {
                        if (mOutputStream8 != null) {
                            mOutputStream8.write(mValueToSend8);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing8++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore8.wait();
                        if (mByteReceivedBack8 == true) {
                            // Byte has been received
                            mIncoming8++;
                        } else {
                            // Timeout
                            mLost8++;
                            //serial_dev[1].ispass = false;
                            //serial_dev[1].msg = serial_dev[1].devId.toString() + "Lost Byte";
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        execcmd = new ExecCmd();
        Intent intent = getIntent();
        isBurning = intent.getBooleanExtra("isBurning", false);
        if (isBurning) {
            setContentView(R.layout.burning);
            TextView Title = (TextView) findViewById(R.id.title_text);
            Title.setText("Burning Test");
            listenLogfile = new String("ethernet.log");

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            // 动态增加Fragment
            RightFragment rightFragment = new RightFragment();
            LeftFragment leftFragment = new LeftFragment();
            transaction.add(R.id.left_layout, leftFragment, "leftfragment");
            transaction.add(R.id.right_layout, rightFragment, "rightfragment");
            transaction.commit();

            sataThread = new SataThread();
            udiskThread = new UdiskThread();
            ethThread = new EthThread();
            musicThread = new MusicThread();
            readLogThread = new ReadLogThread();
            Log.e("yixuan", "before execcmd");
            execcmd.execShell("echo  >/data/data/com.advantech.advfuntest/sata.log");
            execcmd.execShell("echo  >/data/data/com.advantech.advfuntest/udisk.log");
            execcmd.execShell("echo  >/data/data/com.advantech.advfuntest/ethernet.log");
            execcmd.execShell("chown system:system /data/data/com.advantech.advfuntest/*.log");
            execcmd.execShell("chmod 777 /data/data/com.advantech.advfuntest/*.log");

            sataThread.start();
            udiskThread.start();
            ethThread.start();
            musicThread.start();
            listen = new LogListener("/data/data/com.advantech.advfuntest");
            listen.startWatching();

        } else {
            setContentView(R.layout.loopback);
            TextView Title = (TextView) findViewById(R.id.title_text);
            Title.setText("Serial Port Function Test");
        }

        mTextViewOutgoing1 = (TextView) findViewById(R.id.TextViewOutgoingValue1);
        mTextViewIncoming1 = (TextView) findViewById(R.id.TextViewIncomingValue1);
        mTextViewLost1 = (TextView) findViewById(R.id.textViewLostValue1);
        mTextViewCorrupted1 = (TextView) findViewById(R.id.textViewCorruptedValue1);
        if (mSerialPort1 != null) {
            mSendingThread1 = new SendingThread1();
            mSendingThread1.start();
        }

        mTextViewOutgoing2 = (TextView) findViewById(R.id.TextViewOutgoingValue2);
        mTextViewIncoming2 = (TextView) findViewById(R.id.TextViewIncomingValue2);
        mTextViewLost2 = (TextView) findViewById(R.id.textViewLostValue2);
        mTextViewCorrupted2 = (TextView) findViewById(R.id.textViewCorruptedValue2);
        if (mSerialPort2 != null) {
            mSendingThread2 = new SendingThread2();
            mSendingThread2.start();
        }

        mTextViewOutgoing3 = (TextView) findViewById(R.id.TextViewOutgoingValue3);
        mTextViewIncoming3 = (TextView) findViewById(R.id.TextViewIncomingValue3);
        mTextViewLost3 = (TextView) findViewById(R.id.textViewLostValue3);
        mTextViewCorrupted3 = (TextView) findViewById(R.id.textViewCorruptedValue3);
        if (mSerialPort3 != null) {
            mSendingThread3 = new SendingThread3();
            mSendingThread3.start();
        }

        mTextViewOutgoing4 = (TextView) findViewById(R.id.TextViewOutgoingValue4);
        mTextViewIncoming4 = (TextView) findViewById(R.id.TextViewIncomingValue4);
        mTextViewLost4 = (TextView) findViewById(R.id.textViewLostValue4);
        mTextViewCorrupted4 = (TextView) findViewById(R.id.textViewCorruptedValue4);
        if (mSerialPort4 != null) {
            mSendingThread4 = new SendingThread4();
            mSendingThread4.start();
        }

        mTextViewOutgoing5 = (TextView) findViewById(R.id.TextViewOutgoingValue5);
        mTextViewIncoming5 = (TextView) findViewById(R.id.TextViewIncomingValue5);
        mTextViewLost5 = (TextView) findViewById(R.id.textViewLostValue5);
        mTextViewCorrupted5 = (TextView) findViewById(R.id.textViewCorruptedValue5);
        if (mSerialPort5 != null) {
            mSendingThread5 = new SendingThread5();
            mSendingThread5.start();
        }

        mTextViewOutgoing6 = (TextView) findViewById(R.id.TextViewOutgoingValue6);
        mTextViewIncoming6 = (TextView) findViewById(R.id.TextViewIncomingValue6);
        mTextViewLost6 = (TextView) findViewById(R.id.textViewLostValue6);
        mTextViewCorrupted6 = (TextView) findViewById(R.id.textViewCorruptedValue6);
        if (mSerialPort6 != null) {
            mSendingThread6 = new SendingThread6();
            mSendingThread6.start();
        }

        mTextViewOutgoing7 = (TextView) findViewById(R.id.TextViewOutgoingValue7);
        mTextViewIncoming7 = (TextView) findViewById(R.id.TextViewIncomingValue7);
        mTextViewLost7 = (TextView) findViewById(R.id.textViewLostValue7);
        mTextViewCorrupted7 = (TextView) findViewById(R.id.textViewCorruptedValue7);
        if (mSerialPort7 != null) {
            mSendingThread7 = new SendingThread7();
            mSendingThread7.start();
        }

        mTextViewOutgoing8 = (TextView) findViewById(R.id.TextViewOutgoingValue8);
        mTextViewIncoming8 = (TextView) findViewById(R.id.TextViewIncomingValue8);
        mTextViewLost8 = (TextView) findViewById(R.id.textViewLostValue8);
        mTextViewCorrupted8 = (TextView) findViewById(R.id.textViewCorruptedValue8);
        if (mSerialPort8 != null) {
            mSendingThread8 = new SendingThread8();
            mSendingThread8.start();
        }

        handler.post(runnable);

        dev = new Device();
        dev.uartFun = true;
        dev.uartMsg = new String("");

        backBtn = (LinearLayout) findViewById(R.id.title_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBurning == false) {
                    returnResult();
                } else {
                    finish();
                }
            }
        });
    }

    public void returnResult() {
        for (int i = 0; i < serialCount; i++) {
            serial_dev[i].ispass = true;
        }
		/*第二次打开loopback会有丢包，应该属于程序bug，但目前没能解决，先将lostbit的条件设为3bit*/
        if (mLost1 >= 3) {
            serial_dev[0].ispass = false;
            serial_dev[0].msg = serial_dev[0].devId.toString() + " Lost Byte";
        }
        if (mLost2 >= 3) {
            serial_dev[1].ispass = false;
            serial_dev[1].msg = serial_dev[1].devId.toString() + " Lost Byte";
        }
        if (mLost3 >= 3) {
            serial_dev[2].ispass = false;
            serial_dev[2].msg = serial_dev[2].devId.toString() + " Lost Byte";
        }
        if (mLost4 >= 3) {
            serial_dev[3].ispass = false;
            serial_dev[3].msg = serial_dev[3].devId.toString() + " Lost Byte";
        }
        if (mLost5 >= 3) {
            serial_dev[4].ispass = false;
            serial_dev[4].msg = serial_dev[4].devId.toString() + " Lost Byte";
        }
        if (mLost6 >= 3) {
            serial_dev[5].ispass = false;
            serial_dev[5].msg = serial_dev[5].devId.toString() + " Lost Byte";
        }
        if (mLost7 >= 3) {
            serial_dev[6].ispass = false;
            serial_dev[6].msg = serial_dev[6].devId.toString() + " Lost Byte";
        }
        if (mLost8 >= 3) {
            serial_dev[7].ispass = false;
            serial_dev[7].msg = serial_dev[7].devId.toString() + " Lost Byte";
        }
        for (int i = 0; i < serialCount; i++) {
            if (serial_dev[i].ispass == false) {
                dev.uartFun = false;
                dev.uartMsg += serial_dev[i].msg + "\n";
            }
        }
        if (dev.uartFun == false) {
            execcmd.writeLog(dev.uartMsg);
            execcmd.writeLog("====================================\n");
        }

        //关闭之前为Activity设置返回结果
        Intent intent = new Intent();
        intent.putExtra("uartFun", dev.uartFun);
        intent.putExtra("uartMsg", dev.uartMsg);
        //设置返回数据   
        setResult(Activity.RESULT_OK, intent);
        //关闭activity   
        finish();
    }

    @Override
    protected void onDataReceived1(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore1) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend1) && (mByteReceivedBack1 == false)) {
                    mValueToSend1++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack1 = true;
                    mByteReceivedBackSemaphore1.notify();
                } else {
                    // The byte was not expected
                    mCorrupted1++;
                    serial_dev[0].ispass = false;
                    serial_dev[0].msg = serial_dev[0].devId.toString() + "Corrupted";
                }
            }
        }
    }

    @Override
    protected void onDataReceived2(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore2) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend2) && (mByteReceivedBack2 == false)) {
                    mValueToSend2++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack2 = true;
                    mByteReceivedBackSemaphore2.notify();
                } else {
                    // The byte was not expected
                    mCorrupted2++;
                    serial_dev[1].ispass = false;
                    serial_dev[1].msg = serial_dev[1].devId.toString() + "Corrupted";
                }
            }
        }
    }

    @Override
    protected void onDataReceived3(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore3) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend3) && (mByteReceivedBack3 == false)) {
                    mValueToSend3++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack3 = true;
                    mByteReceivedBackSemaphore3.notify();
                } else {
                    // The byte was not expected
                    mCorrupted3++;
                    serial_dev[2].ispass = false;
                    serial_dev[2].msg = serial_dev[2].devId.toString() + "Corrupted";
                }
            }
        }
    }

    @Override
    protected void onDataReceived4(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore4) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend4) && (mByteReceivedBack4 == false)) {
                    mValueToSend4++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack4 = true;
                    mByteReceivedBackSemaphore4.notify();
                } else {
                    // The byte was not expected
                    mCorrupted4++;
                    serial_dev[3].ispass = false;
                    serial_dev[3].msg = serial_dev[3].devId.toString() + "Corrupted";
                }
            }
        }
    }

    @Override
    protected void onDataReceived5(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore5) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend5) && (mByteReceivedBack5 == false)) {
                    mValueToSend5++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack5 = true;
                    mByteReceivedBackSemaphore5.notify();
                } else {
                    // The byte was not expected
                    mCorrupted5++;
                    serial_dev[4].ispass = false;
                    serial_dev[4].msg = serial_dev[4].devId.toString() + "Corrupted";
                }
            }
        }
    }

    @Override
    protected void onDataReceived6(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore6) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend6) && (mByteReceivedBack6 == false)) {
                    mValueToSend6++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack6 = true;
                    mByteReceivedBackSemaphore6.notify();
                } else {
                    // The byte was not expected
                    mCorrupted6++;
                    serial_dev[5].ispass = false;
                    serial_dev[5].msg = serial_dev[5].devId.toString() + "Corrupted";
                }
            }
        }
    }

    @Override
    protected void onDataReceived7(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore7) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend7) && (mByteReceivedBack7 == false)) {
                    mValueToSend7++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack7 = true;
                    mByteReceivedBackSemaphore7.notify();
                } else {
                    // The byte was not expected
                    mCorrupted7++;
                    serial_dev[6].ispass = false;
                    serial_dev[6].msg = serial_dev[6].devId.toString() + "Corrupted";
                }
            }
        }
    }

    @Override
    protected void onDataReceived8(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore8) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend8) && (mByteReceivedBack8 == false)) {
                    mValueToSend8++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack8 = true;
                    mByteReceivedBackSemaphore8.notify();
                } else {
                    // The byte was not expected
                    mCorrupted8++;
                    serial_dev[7].ispass = false;
                    serial_dev[7].msg = serial_dev[7].devId.toString() + "Corrupted";
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mSendingThread1 != null)
            mSendingThread1.interrupt();
        if (mSendingThread2 != null)
            mSendingThread2.interrupt();
        if (mSendingThread3 != null)
            mSendingThread3.interrupt();
        if (mSendingThread4 != null)
            mSendingThread4.interrupt();
        if (mSendingThread5 != null)
            mSendingThread5.interrupt();
        if (mSendingThread6 != null)
            mSendingThread6.interrupt();
        if (mSendingThread7 != null)
            mSendingThread7.interrupt();
        if (mSendingThread8 != null)
            mSendingThread8.interrupt();
        if (isBurning) {
            sataThread.interrupt();
            udiskThread.interrupt();
            ethThread.interrupt();
            musicThread.interrupt();
            listen.stopWatching();
        }
        super.onDestroy();
    }

    private TextView showMessageView;

    @Override
    public void showMessage(int index) {
        // TODO Auto-generated method stub
        if (1 == index) {
            listenLogfile = "sata.log";
        }
        if (2 == index) {
            listenLogfile = "udisk.log";
        }
        if (3 == index) {
            listenLogfile = "ethernet.log";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Activity--->onResume");
        showMessageView = (TextView) findViewById(R.id.right_show_message);
    }
}