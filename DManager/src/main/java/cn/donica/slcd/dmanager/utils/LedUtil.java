package cn.donica.slcd.dmanager.utils;

import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;

/**
 * Created by liangmingjie on 2015/12/28.
 */
public class LedUtil {
    private IHwtestService hwtestService = null;


    /**
     * 设置led3灯的状态
     *
     * @throws RemoteException
     */
    public String set_led23() throws RemoteException {
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.set_led(3, 0);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        return info;
    }

    /**
     * buf内容为“0”表示灯亮，为“1”表示灯灭；buf存放读取到的值
     *
     * @return
     * @throws RemoteException
     */
    public String getUsbLightStatus() throws RemoteException {
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_led(3, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        return info;
    }

    /**
     * buf内容为“0”表示灯亮，为“1”表示灯灭；buf存放读取到的值
     *
     * @return
     * @throws RemoteException
     */
    public String getLed1LightStatus() throws RemoteException {
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_led(1, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        return info;
    }

    /**
     * buf内容为“0”表示灯亮，为“1”表示灯灭；buf存放读取到的值
     *
     * @return
     * @throws RemoteException
     */
    public String getLed2LightStatus() throws RemoteException {
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.get_led(2, strinfo);
        byte len = strinfo[0];
        String info = new String(strinfo, 1, len);
        return info;
    }

    /**
     * 点亮USB灯
     *
     * @throws RemoteException
     */
    public void lightUSBLED() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.set_led(3, 0);// 获取到的信息保存在数组strinfo中
        byte len = strinfo[0];// strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);// 得到最终的信息
    }

    /**
     * 熄灭USB灯
     *
     * @throws RemoteException
     */
    public void extinguishUSBLED1() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager
                .getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.set_led(3, 1);// 获取到的信息保存在数组strinfo中
        byte len = strinfo[0];// strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);// 得到最终的信息
    }


    /**
     * 熄灭LED1
     *
     * @throws RemoteException
     */
    public void extinguishLED1() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.set_led(1, 0);// 获取到的信息保存在数组strinfo中
        byte len = strinfo[0];// strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);// 得到最终的信息
    }

    /**
     * 熄灭LED2
     *
     * @throws RemoteException
     */
    public void extinguishLED2() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.set_led(2, 0);// 获取到的信息保存在数组strinfo中
        byte len = strinfo[0];// strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);// 得到最终的信息
    }

    /**
     * 点亮LED1
     *
     * @return
     * @throws RemoteException
     */
    public String hardkeyLed1Green() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.set_led(1, 0);// 获取到的信息保存在数组strinfo中
        byte len = strinfo[0];// strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);// 得到最终的信息
        return info;
    }

    /**
     * 点亮LED2
     *
     * @return
     * @throws RemoteException
     */
    public String hardkeyLed2Green() throws RemoteException {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));
        byte ret = 0;
        byte strinfo[] = new byte[256];
        ret = hwtestService.set_led(2, 0);// 获取到的信息保存在数组strinfo中
        byte len = strinfo[0];// strinfo数组的第一个字节保存的是信息的长度
        String info = new String(strinfo, 1, len);// 得到最终的信息
        return info;
    }


    public void bootStart() throws RemoteException {
        lightUSBLED();
        hardkeyLed1Green();
        hardkeyLed2Green();
    }
}
