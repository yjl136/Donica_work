package com.advantech.adv;

import android.app.Activity;
import android.os.Bundle;
import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.advantech.advfuntest.R;

public class MainActivity extends Activity {
    private final static String TAG = "Hwtester";
    private TextView result;
    private IHwtestService hwtestService = null;
    byte ret = 0;

    private int value1 = 0;
    private int value2 = 0;
    private int value3 = 0;
    private Button test_audio_signal, test_audio_headset, test_audio_chip, test_ntsc;
    private Button get_ntsc_status, get_disk_mmc, get_disk_sdcard, test_wifi;
    private Button test_bluetooth, test_rfid, get_ethernet, test_sdcard, test_lcd;
    private Button get_led1, get_led2, get_led3, set_led1, set_led2, set_led3;
    private Button get_temperature, get_angle_status;

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
     * 获取cpu使用率
     * @param view
     */
    public void get_cpu(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        String allinfo = "get_cpu:";
        ret = hwtestService.get_cpu(1, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    public void get_memory(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        int funcid = 1;
        String allinfo = "all:";
        ret = hwtestService.get_memory(funcid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";


        funcid = 2;
        allinfo += "\nused:";
        ret = hwtestService.get_memory(funcid, strinfo);
        len = strinfo[0];
        info = new String(strinfo, 1, len);
        allinfo += info + ";";

        funcid = 3;
        allinfo += "\nfree:";
        ret = hwtestService.get_memory(funcid, strinfo);
        len = strinfo[0];
        info = new String(strinfo, 1, len);
        allinfo += info + ";";

        result.setText(allinfo);
    }

    public void get_disk_ssd(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        int funcid = 1;
        String allinfo = "ssd all:";
        ret = hwtestService.get_disk_ssd(funcid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";


        funcid = 2;
        allinfo += "\nssd used:";
        ret = hwtestService.get_disk_ssd(funcid, strinfo);
        len = strinfo[0];
        info = new String(strinfo, 1, len);
        allinfo += info + ";";

        funcid = 3;
        allinfo += "\nssd free:";
        ret = hwtestService.get_disk_ssd(funcid, strinfo);
        len = strinfo[0];
        info = new String(strinfo, 1, len);
        allinfo += info + ";";

        result.setText(allinfo);
    }

    public void get_disk_usb(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        int funcid = 1;
        String allinfo = "usb all:";
        ret = hwtestService.get_disk_usb(funcid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";


        funcid = 2;
        allinfo += "\nusb used:";
        ret = hwtestService.get_disk_usb(funcid, strinfo);
        len = strinfo[0];
        info = new String(strinfo, 1, len);
        allinfo += info + ";";

        funcid = 3;
        allinfo += "\nusb free:";
        ret = hwtestService.get_disk_usb(funcid, strinfo);
        len = strinfo[0];
        info = new String(strinfo, 1, len);
        allinfo += info + ";";

        result.setText(allinfo);
    }

    /**
     * 获取cpu使用率
     *
     * @param view
     */
    public void get_power(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        int funcid = 0;
        String allinfo = "VCC:";
        ret = hwtestService.get_vcc(funcid, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";


        funcid = 1;
        allinfo += "\nV1:";
        ret = hwtestService.get_vcc(funcid, strinfo);
        len = strinfo[0];
        info = new String(strinfo, 1, len);
        allinfo += info + ";";

        funcid = 2;
        allinfo += "\nV2:";
        ret = hwtestService.get_vcc(funcid, strinfo);
        len = strinfo[0];
        info = new String(strinfo, 1, len);
        allinfo += info + ";";

        result.setText(allinfo);
    }

    /**
     * 检测是否有音频信号
     *
     * @throws RemoteException
     */
    public void test_audio_signal(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        String allinfo = "test_audio_signal:";
        ret = hwtestService.test_audio_signal(strinfo);
        Log.i(TAG, "test_audio_signal:" + ret);
      /*  byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测耳机是否插入
     *
     * @throws RemoteException
     */
    public void test_audio_headset(View view) {

        byte strinfo[] = new byte[256];
        String allinfo = "test_audio_headset:";
        try {
            ret = hwtestService.test_audio_headset(strinfo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "test_audio_headset:" + ret);
       /* byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测音频设备是否挂载成功
     *
     * @throws RemoteException
     */
    public void test_audio_chip(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        String allinfo = "test_audio_chip:";
        ret = hwtestService.test_audio_chip(strinfo);
        Log.i(TAG, "test_audio_chip:" + ret);
       /* byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);
    }

    public void get_temperature(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        String allinfo = "test_cpu_tempertature:";
        ret = hwtestService.get_temperature(strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";
        result.setText(allinfo);
    }

    public void get_angle_status(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        String allinfo = "test_angle_status:";
        ret = hwtestService.get_angle_status(strinfo);
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
        byte strinfo[] = new byte[256];
        String allinfo = "test_ntsc:";
        ret = hwtestService.test_ntsc(strinfo);
        /*byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/

        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 获得7282m的连接状态
     *
     * @throws RemoteException
     */
    public void get_ntsc_status(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        String allinfo = "get_ntsc_status,ret:";
        ret = hwtestService.get_ntsc_status(strinfo);
        //  Log.i(TAG,"get_ntsc_status:"+ret);
        allinfo += ret + "\ninfo:";
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
        byte strinfo[] = new byte[256];
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
        byte strinfo[] = new byte[256];
        int funcid = 1;
        String allinfo = " sdcard all:";
        ret = hwtestService.get_disk_sdcard(funcid, strinfo);
        Log.i(TAG, "sdcard ret1:"+ret);
        byte len = strinfo[0];
        String allmem = new String(strinfo, 1, len);
        allinfo += allmem;
        allinfo += "kb;\nsdcard used:";

        funcid = 2;
        ret = hwtestService.get_disk_sdcard(funcid, strinfo);
        Log.i(TAG, "sdcard ret2:"+ret);
        len = strinfo[0];
        String used = new String(strinfo, 1, len);
        allinfo += used;
        allinfo += "kb;\nsdcard free:";

        funcid = 3;
        ret = hwtestService.get_disk_sdcard(funcid, strinfo);
        Log.i(TAG, "sdcard ret3:"+ret);
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
        byte strinfo[] = new byte[256];
        String allinfo = "test_wifi:";
        ret = hwtestService.test_wifi(strinfo);
        Log.i(TAG, "test_wifi:" + ret);
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
      /*  byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/
        result.setText(allinfo);
    }

    /**
     * 检测bluetooth设备是否挂载成功
     */
    public void test_bluetooth(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        String allinfo = "test_bluetooth:";
        ret = hwtestService.test_bluetooth(strinfo);
       /* byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/
        Log.i(TAG, "test_bluetooth:" + ret);
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测rfid主控制器是否ready
     */
    public void test_rfid(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        int funid = 1;
        String allinfo = "test_rfid:";
        ret = hwtestService.test_rfid(funid, strinfo);
       /* byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/

        Log.i(TAG, "test_bluetooth:" + ret);
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 获取ethernet信息
     *
     * @throws RemoteException
     */
    public void get_ethernet(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
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
        byte strinfo[] = new byte[256];
        int funid = 1;
        String allinfo = "test_sdcard:";
        ret = hwtestService.test_sdcard(funid, strinfo);
       /* byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/
        Log.i(TAG, "test_sdcard:" + ret);
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);

    }

    /**
     * 检测sdcard是否插入
     *
     * @throws RemoteException
     */
    public void test_usb(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        int funid = 1;
        String allinfo = "test_usb:";
        ret = hwtestService.test_usb(funid, strinfo);
       /* byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/
        Log.i(TAG, "test_usb:" + ret);
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);
    }

    /**
     * 检测LCD背光是否开启
     *
     * @throws RemoteException
     */
    public void test_lcd(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
        String allinfo = "test_lcd:";
        ret = hwtestService.test_lcd(strinfo);
        /*byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        allinfo += info + ";";*/


        //Log.i(TAG,"test_lcd:"+ret);
        String info;
        if (ret == 0) {
            info = "success";
        } else {
            info = "failed";
        }
        allinfo += info + ";";
        result.setText(allinfo);

    }

    /**
     * 得到led1灯的状态
     *
     * @throws RemoteException
     */
    public void get_led1(View view) throws RemoteException {
        byte strinfo[] = new byte[256];
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
        byte strinfo[] = new byte[256];
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
        byte strinfo[] = new byte[256];
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
        byte strinfo[] = new byte[256];
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
        byte strinfo[] = new byte[256];
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
        byte strinfo[] = new byte[256];
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
