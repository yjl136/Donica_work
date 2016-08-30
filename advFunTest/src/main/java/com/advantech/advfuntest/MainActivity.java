package com.advantech.advfuntest;

import android.app.Activity;
import android.os.Bundle;
import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView result;
    private IHwtestService hwtestService = null;
    byte ret = 0;
    byte strinfo[] = new byte[256];
    private int value1 = 0;
    private int value2 = 0;
    private int value3 = 0;
    private Button test_audio_signal, test_audio_headset, test_audio_chip, test_ntsc;
    private Button get_ntsc_status, get_disk_mmc, get_disk_sdcard, test_wifi;
    private Button test_bluetooth, test_rfid, get_ethernet, test_sdcard, test_lcd;
    private Button get_led1, get_led2, get_led3, set_led1, set_led2, set_led3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
    }

    private void initView() {
        result = (TextView) findViewById(R.id.result);
        test_audio_signal = (Button) findViewById(R.id.test_audio_signal);
        test_audio_headset = (Button) findViewById(R.id.test_audio_headset);
        test_audio_chip = (Button) findViewById(R.id.test_audio_chip);
        test_ntsc = (Button) findViewById(R.id.test_ntsc);

        get_ntsc_status = (Button) findViewById(R.id.get_ntsc_status);
        get_disk_mmc = (Button) findViewById(R.id.get_disk_mmc);
        get_disk_sdcard = (Button) findViewById(R.id.get_disk_sdcard);
        test_wifi = (Button) findViewById(R.id.test_wifi);

        test_bluetooth = (Button) findViewById(R.id.test_bluetooth);
        test_rfid = (Button) findViewById(R.id.test_rfid);
        get_ethernet = (Button) findViewById(R.id.get_ethernet);
        test_sdcard = (Button) findViewById(R.id.test_sdcard);
        test_lcd = (Button) findViewById(R.id.test_lcd);

        get_led1 = (Button) findViewById(R.id.get_led1);
        get_led2 = (Button) findViewById(R.id.get_led2);
        get_led3 = (Button) findViewById(R.id.get_led3);
        set_led1 = (Button) findViewById(R.id.set_led1);
        set_led2 = (Button) findViewById(R.id.set_led2);
        set_led3 = (Button) findViewById(R.id.set_led3);
    }

    /**
     * 检测是否有音频信号
     *
     * @throws RemoteException
     */
    public void test_audio_signal(View view) throws RemoteException {
        String allinfo = "test_audio_signal:";
        ret = hwtestService.test_audio_signal(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测耳机是否插入
     *
     * @throws RemoteException
     */
    public void test_audio_headset(View view) throws RemoteException {
        String allinfo = "test_audio_headset:";
        ret = hwtestService.test_audio_headset(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测音频设备是否挂载成功
     *
     * @throws RemoteException
     */
    public void test_audio_chip(View view) throws RemoteException {
        String allinfo = "test_audio_chip:";
        ret = hwtestService.test_audio_chip(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测ntsc设备是否挂载成功
     *
     * @throws RemoteException
     */
    public void test_ntsc(View view) throws RemoteException {
        String allinfo = "test_ntsc:";
        ret = hwtestService.test_ntsc(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 获得7282m的连接状态
     *
     * @throws RemoteException
     */
    public void get_ntsc_status(View view) throws RemoteException {
        String allinfo = "get_ntsc_status,ret:";
        ret = hwtestService.get_ntsc_status(strinfo);
        allinfo += ret + "\n get_ntsc_status info:";
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 获取nand flash 已挂载分区的容量信息，包括总容量、已使用容量、可使用容量大小等；
     *
     * @throws RemoteException
     */
    public void get_disk_mmc(View view) throws RemoteException {
        int funcid = 1;
        String allinfo = "All:";
        ret = hwtestService.get_disk_mmc(funcid, strinfo);
        byte len = strinfo[0];
        String allmem = new String(strinfo, 1, len);
        allinfo += allmem;
        allinfo += "kb;\nUsed:";

        funcid = 2;
        ret = hwtestService.get_disk_mmc(funcid, strinfo);
        len = strinfo[0];
        String used = new String(strinfo, 1, len);
        allinfo += used;
        allinfo += "kb;\nfree:";

        funcid = 3;
        ret = hwtestService.get_disk_mmc(funcid, strinfo);
        len = strinfo[0];
        String free = new String(strinfo, 1, len);
        allinfo += free;
        allinfo += "kb;";
        result.setText(allinfo);
    }

    /**
     * 获取sd card已挂载分区的容量信息，包括总容量、已使用容量、可使用容量大小等
     *
     * @throws RemoteException
     */
    public void get_disk_sdcard(View view) throws RemoteException {
        int funcid = 1;
        String allinfo = "All:";
        ret = hwtestService.get_disk_sdcard(funcid, strinfo);
        byte len = strinfo[0];
        String allmem = new String(strinfo, 1, len);
        allinfo += allmem;
        allinfo += "kb;\nUsed:";

        funcid = 2;
        ret = hwtestService.get_disk_sdcard(funcid, strinfo);
        len = strinfo[0];
        String used = new String(strinfo, 1, len);
        allinfo += used;
        allinfo += "kb;\nfree:";

        funcid = 3;
        ret = hwtestService.get_disk_sdcard(funcid, strinfo);
        len = strinfo[0];
        String free = new String(strinfo, 1, len);
        allinfo += free;
        allinfo += "kb;";
        result.setText(allinfo);
    }

    /**
     * 检测wifi芯片是否挂载成功
     *
     * @throws RemoteException
     */
    public void test_wifi(View view) throws RemoteException {
        String allinfo = "test_wifi:";
        ret = hwtestService.test_wifi(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测bluetooth设备是否挂载成功
     */
    public void test_bluetooth(View view) throws RemoteException {
        String allinfo = "test_bluetooth:";
        ret = hwtestService.test_bluetooth(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测rfid主控制器是否ready
     */
    public void test_rfid(View view) throws RemoteException {
        int funid = 1;
        String allinfo = "test_rfid:";
        ret = hwtestService.test_rfid(funid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 获取ethernet信息
     *
     * @throws RemoteException
     */
    public void get_ethernet(View view) throws RemoteException {
        int funcid = 1;
        String allinfo = "eth0:";
        ret = hwtestService.get_ethernet(funcid, strinfo);
        byte len = strinfo[0];
        String bcon = new String(strinfo, 1, len);
        //返回“1”表示网卡连接正常，返回“0”表示未连接
        if (bcon.equals("1"))
            allinfo += "connected;\nspeed:";
        else
            allinfo += "not connected;\nspeed:";
        funcid = 2;
        ret = hwtestService.get_ethernet(funcid, strinfo);
        len = strinfo[0];
        String speed = new String(strinfo, 1, len);
        allinfo += speed;
        allinfo += ";\nduplex:";
        funcid = 3;
        ret = hwtestService.get_ethernet(funcid, strinfo);
        len = strinfo[0];
        String duplex = new String(strinfo, 1, len);
        allinfo += duplex;
        allinfo += ";\nip:";
        funcid = 4;
        ret = hwtestService.get_ethernet(funcid, strinfo);
        len = strinfo[0];
        String ip = new String(strinfo, 1, len);
        allinfo += ip;
        allinfo += ";";
        result.setText(allinfo);
    }

    /**
     * 检测sdcard是否插入
     *
     * @throws RemoteException
     */
    public void test_sdcard(View view) throws RemoteException {
        int funid = 1;
        String allinfo = "test_sdcard:";
        ret = hwtestService.test_sdcard(funid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);

    }

    /**
     * 检测LCD背光是否开启
     *
     * @throws RemoteException
     */
    public void test_lcd(View view) throws RemoteException {
        String allinfo = "test_lcd:";
        ret = hwtestService.test_lcd(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);

    }

    /**
     * 得到led1灯的状态
     *
     * @throws RemoteException
     */
    public void get_led1(View view) throws RemoteException {
        int funid = 1;
        String allinfo = "get_led1:";
        ret = hwtestService.get_led(funid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 得到led2灯的状态
     *
     * @throws RemoteException
     */
    public void get_led2(View view) throws RemoteException {
        int funid = 2;
        String allinfo = "get_led2:";
        ret = hwtestService.get_led(funid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);

    }

    /**
     * 得到led2灯的状态
     *
     * @throws RemoteException
     */
    public void get_led3(View view) throws RemoteException {
        int funid = 3;
        String allinfo = "get_led3:";
        ret = hwtestService.get_led(funid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);

    }

    /**
     * 设置led1灯的状态
     *
     * @throws RemoteException
     */
    public void set_led1(View view) throws RemoteException {
        int led = 1;
        String allinfo = "set_led1:";
        if (value1 % 2 == 0) {
            ret = hwtestService.set_led(led, 0);
        } else {
            ret = hwtestService.set_led(led, 1);
        }
        value1++;
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);

    }

    /**
     * 设置led2灯的状态
     *
     * @throws RemoteException
     */
    public void set_led2(View view) throws RemoteException {
        int led = 2;
        String allinfo = "set_led2:";
        if (value2 % 2 == 0) {
            ret = hwtestService.set_led(led, 0);
        } else {
            ret = hwtestService.set_led(led, 1);
        }
        value2++;
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);

    }

    /**
     * 设置led3灯的状态
     *
     * @throws RemoteException
     */
    public void set_led3(View view) throws RemoteException {
        int led = 3;
        String allinfo = "set_led3:";
        if (value3 % 2 == 0) {
            ret = hwtestService.set_led(led, 0);
        } else {
            ret = hwtestService.set_led(led, 1);
        }
        value3++;
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }
}
