package com.advantech.adv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.advantech.advfuntest.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public abstract class SerialPortActivity extends Activity {

    class SerialDev {
        public String devId;
        public boolean ispass;
        public String msg;

        SerialDev(String devId, boolean ispass, String msg) {
            this.devId = devId;
            this.ispass = ispass;
            this.msg = msg;
        }
    }

    public SerialDev[] serial_dev;
    public int serialCount;

    protected SerialPort mSerialPort1;
    protected OutputStream mOutputStream1;
    private InputStream mInputStream1;
    private ReadThread1 mReadThread1;

    protected SerialPort mSerialPort2;
    protected OutputStream mOutputStream2;
    private InputStream mInputStream2;
    private ReadThread2 mReadThread2;

    protected SerialPort mSerialPort3;
    protected OutputStream mOutputStream3;
    private InputStream mInputStream3;
    private ReadThread3 mReadThread3;

    protected SerialPort mSerialPort4;
    protected OutputStream mOutputStream4;
    private InputStream mInputStream4;
    private ReadThread4 mReadThread4;

    protected SerialPort mSerialPort5;
    protected OutputStream mOutputStream5;
    private InputStream mInputStream5;
    private ReadThread5 mReadThread5;

    protected SerialPort mSerialPort6;
    protected OutputStream mOutputStream6;
    private InputStream mInputStream6;
    private ReadThread6 mReadThread6;

    protected SerialPort mSerialPort7;
    protected OutputStream mOutputStream7;
    private InputStream mInputStream7;
    private ReadThread7 mReadThread7;

    protected SerialPort mSerialPort8;
    protected OutputStream mOutputStream8;
    private InputStream mInputStream8;
    private ReadThread8 mReadThread8;

    private class ReadThread1 extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream1 == null) return;
                    size = mInputStream1.read(buffer);
                    if (size > 0) {
                        onDataReceived1(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class ReadThread2 extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream2 == null) return;
                    size = mInputStream2.read(buffer);
                    if (size > 0) {
                        onDataReceived2(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class ReadThread3 extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream3 == null) return;
                    size = mInputStream3.read(buffer);
                    if (size > 0) {
                        onDataReceived3(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class ReadThread4 extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream4 == null) return;
                    size = mInputStream4.read(buffer);
                    if (size > 0) {
                        onDataReceived4(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class ReadThread5 extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream5 == null) return;
                    size = mInputStream5.read(buffer);
                    if (size > 0) {
                        onDataReceived5(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class ReadThread6 extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream6 == null) return;
                    size = mInputStream6.read(buffer);
                    if (size > 0) {
                        onDataReceived6(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class ReadThread7 extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream7 == null) return;
                    size = mInputStream7.read(buffer);
                    if (size > 0) {
                        onDataReceived7(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class ReadThread8 extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream8 == null) return;
                    size = mInputStream8.read(buffer);
                    if (size > 0) {
                        onDataReceived8(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SerialPortActivity.this.finish();
            }
        });
        b.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //添加读取jSon文件的配置
            serialCount = 8;
            serial_dev = new SerialDev[serialCount];
            serial_dev[0] = new SerialDev("/dev/ttymxc1", false, "");
            serial_dev[1] = new SerialDev("/dev/ttymxc2", false, "");
            serial_dev[2] = new SerialDev("/dev/ttymxc3", false, "");
            serial_dev[3] = new SerialDev("/dev/ttymxc4", false, "");
            serial_dev[4] = new SerialDev("/dev/ttyUSB0", false, "");
            serial_dev[5] = new SerialDev("/dev/ttyUSB1", false, "");
            serial_dev[6] = new SerialDev("/dev/ttyUSB2", false, "");
            serial_dev[7] = new SerialDev("/dev/ttyUSB3", false, "");

            mSerialPort1 = new SerialPort(new File("/dev/ttymxc1"), 115200, 0);
            mOutputStream1 = mSerialPort1.getOutputStream();
            mInputStream1 = mSerialPort1.getInputStream();

            mSerialPort2 = new SerialPort(new File("/dev/ttymxc2"), 115200, 0);
            mOutputStream2 = mSerialPort2.getOutputStream();
            mInputStream2 = mSerialPort2.getInputStream();

            mSerialPort3 = new SerialPort(new File("/dev/ttymxc3"), 115200, 0);
            mOutputStream3 = mSerialPort3.getOutputStream();
            mInputStream3 = mSerialPort3.getInputStream();

            mSerialPort4 = new SerialPort(new File("/dev/ttymxc4"), 115200, 0);
            mOutputStream4 = mSerialPort4.getOutputStream();
            mInputStream4 = mSerialPort4.getInputStream();

            mSerialPort5 = new SerialPort(new File("/dev/ttyUSB0"), 115200, 0);
            mOutputStream5 = mSerialPort5.getOutputStream();
            mInputStream5 = mSerialPort5.getInputStream();

            mSerialPort6 = new SerialPort(new File("/dev/ttyUSB1"), 115200, 0);
            mOutputStream6 = mSerialPort6.getOutputStream();
            mInputStream6 = mSerialPort6.getInputStream();

            mSerialPort7 = new SerialPort(new File("/dev/ttyUSB2"), 115200, 0);
            mOutputStream7 = mSerialPort7.getOutputStream();
            mInputStream7 = mSerialPort7.getInputStream();

            mSerialPort8 = new SerialPort(new File("/dev/ttyUSB3"), 115200, 0);
            mOutputStream8 = mSerialPort8.getOutputStream();
            mInputStream8 = mSerialPort8.getInputStream();

			/* Create a receiving thread */
            mReadThread1 = new ReadThread1();
            mReadThread1.start();

            mReadThread2 = new ReadThread2();
            mReadThread2.start();

            mReadThread3 = new ReadThread3();
            mReadThread3.start();

            mReadThread4 = new ReadThread4();
            mReadThread4.start();

            mReadThread5 = new ReadThread5();
            mReadThread5.start();

            mReadThread6 = new ReadThread6();
            mReadThread6.start();

            mReadThread7 = new ReadThread7();
            mReadThread7.start();

            mReadThread8 = new ReadThread8();
            mReadThread8.start();

        } catch (SecurityException e) {
            DisplayError(R.string.error_security);
        } catch (IOException e) {
            DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            DisplayError(R.string.error_configuration);
        }
    }

    protected abstract void onDataReceived1(final byte[] buffer, final int size);

    protected abstract void onDataReceived2(final byte[] buffer, final int size);

    protected abstract void onDataReceived3(final byte[] buffer, final int size);

    protected abstract void onDataReceived4(final byte[] buffer, final int size);

    protected abstract void onDataReceived5(final byte[] buffer, final int size);

    protected abstract void onDataReceived6(final byte[] buffer, final int size);

    protected abstract void onDataReceived7(final byte[] buffer, final int size);

    protected abstract void onDataReceived8(final byte[] buffer, final int size);

    @Override
    protected void onDestroy() {
        if (mReadThread1 != null)
            mReadThread1.interrupt();
        if (mReadThread2 != null)
            mReadThread2.interrupt();
        if (mReadThread3 != null)
            mReadThread3.interrupt();
        if (mReadThread4 != null)
            mReadThread4.interrupt();
        if (mReadThread5 != null)
            mReadThread5.interrupt();
        if (mReadThread6 != null)
            mReadThread6.interrupt();
        if (mReadThread7 != null)
            mReadThread7.interrupt();
        if (mReadThread8 != null)
            mReadThread8.interrupt();
        mSerialPort1.close();
        mSerialPort2.close();
        mSerialPort3.close();
        mSerialPort4.close();
        mSerialPort5.close();
        mSerialPort6.close();
        mSerialPort7.close();
        mSerialPort8.close();
//		mSerialPort1 = null;
//		mSerialPort2 = null;
//		mSerialPort3 = null;
        super.onDestroy();
    }
}