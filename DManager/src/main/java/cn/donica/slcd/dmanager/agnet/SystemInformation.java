package cn.donica.slcd.dmanager.agnet;

/**
 * Created by liangmingjie on 2016/7/5.
 */

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Debug;
import android.os.RemoteException;
import android.os.SystemClock;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;
import java.util.List;

import cn.donica.slcd.dmanager.utils.BluetoothUtil;
import cn.donica.slcd.dmanager.utils.CpuUtil;
import cn.donica.slcd.dmanager.utils.EthernetUtil;
import cn.donica.slcd.dmanager.utils.MemoryUtil;
import cn.donica.slcd.dmanager.utils.RfidUtil;
import cn.donica.slcd.dmanager.utils.StorageUtil;
import cn.donica.slcd.dmanager.utils.UsbUtil;
import cn.donica.slcd.dmanager.utils.WifiUtil;

public class SystemInformation {

    private Context context;
    private MIBtree MIB_MAP;

    public SystemInformation(Context context) {
        this.context = context;
        MIB_MAP = new MIBtree();
    }

    public void updateSystemInformation() throws RemoteException, IOException {
        MIB_MAP = new MIBtree();
        updateDeviceModel();
        updateAndroidVersion();
        updateUptime();

        updateCpuUsageRate();
        updateCpuTemperature();
        updateMemoryUsageRate();
        updateSSDUsageRate();
        updateSDCardStatus();
        updateMMCUsageRate();
        updateUsbUsageRate();
        updateWifiIP();
        updateWifiMac();
        updateWifiModule();
        updateBTModule();
        updateRFIDModule();
        updateEthernetIP();
        updateEthernetMac();
        updateUsbStatus();
        updateSDCardStatus();

        updateRunningServices();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) updateBluetoothStatus();
        updateNetworkStatus();
        MIBtree.setNewMIB(MIB_MAP);
    }

    private void updateDeviceModel() {
        String model = Build.MANUFACTURER + " " + Build.MODEL;
        OID oid = (OID) MIBtree.SYS_MODEL_NUMBER_OID.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(model));
        MIB_MAP.set(vb);
    }

    /**
     * 更新CPU的使用率
     *
     * @throws RemoteException
     */
    private void updateCpuUsageRate() throws RemoteException {
        CpuUtil cpuUtil = new CpuUtil();
        String cpuUsageRate = cpuUtil.getCpuUsageRate();
        OID oid = (OID) MIBtree.SYS_CPU_USAGE_RATE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(cpuUsageRate));
        MIB_MAP.set(vb);
    }

    /**
     * 更新CPU的温度
     *
     * @throws RemoteException
     */
    private void updateCpuTemperature() throws RemoteException {
        CpuUtil cpuUtil = new CpuUtil();
        String cpuUsageRate = cpuUtil.getCpuTemperature();
        OID oid = (OID) MIBtree.HW_CPU_TEMPERATURE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(cpuUsageRate));
        MIB_MAP.set(vb);
    }


    /**
     * 更新内存使用率
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateMemoryUsageRate() throws RemoteException, IOException {
        MemoryUtil memoryUtil = new MemoryUtil();
        String rate = String.valueOf(memoryUtil.getMemoryUsageRate());
        OID oid = (OID) MIBtree.SYS_MEMORY_USAGE_RATE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(rate));
        MIB_MAP.set(vb);
    }


    /**
     * 更新SSD使用率
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateSSDUsageRate() throws RemoteException, IOException {
        StorageUtil storageUtil = new StorageUtil();
        String rate = String.valueOf(storageUtil.getSSDUsageRate());
        OID oid = (OID) MIBtree.SYS_SSD_USAGE_RATE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(rate));
        MIB_MAP.set(vb);
    }


    /**
     * 更新SDCard使用率
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateSDCardUsageRate() throws RemoteException, IOException {
        StorageUtil storageUtil = new StorageUtil();
        String rate = String.valueOf(storageUtil.getSDCardUsageRate());
        OID oid = (OID) MIBtree.SYS_SD_USAGE_RATE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(rate));
        MIB_MAP.set(vb);
    }


    /**
     * 更新MMC使用率
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateMMCUsageRate() throws RemoteException, IOException {
        StorageUtil storageUtil = new StorageUtil();
        String rate = String.valueOf(storageUtil.getMMCUsageRate());
        OID oid = (OID) MIBtree.SYS_MMC_USAGE_RATE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(rate));
        MIB_MAP.set(vb);
    }


    /**
     * 更新Usb使用率
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateUsbUsageRate() throws RemoteException, IOException {
        UsbUtil usbUtil = new UsbUtil();
        String rate = String.valueOf(usbUtil.getUsbUsageRate());
        OID oid = (OID) MIBtree.SYS_USB_USAGE_RATE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(rate));
        MIB_MAP.set(vb);
    }


    /**
     * 更新WIFI的IP地址
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateWifiIP() {
        WifiUtil wifiUtil = new WifiUtil();
        String wifi_ip = wifiUtil.getWiFiIPAddress(context);
        OID oid = (OID) MIBtree.SYS_WIFI_IP.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(wifi_ip));
        MIB_MAP.set(vb);
    }

    /**
     * 更新WIFI的Mac地址
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateWifiMac() {
        WifiUtil wifiUtil = new WifiUtil();
        String wifi_mac = wifiUtil.getLocalMacAddress(context);
        OID oid = (OID) MIBtree.SYS_WIFI_MAC_ADDRESS.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(wifi_mac));
        MIB_MAP.set(vb);
    }

    /**
     * 检测wifi芯片是否挂载成功
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateWifiModule() throws RemoteException {
        WifiUtil wifiUtil = new WifiUtil();
        String wifiModule = wifiUtil.test_wifi();
        OID oid = (OID) MIBtree.HW_WIFI_MODULE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(wifiModule));
        MIB_MAP.set(vb);
    }

    /**
     * 检测BT芯片是否挂载成功
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateBTModule() throws RemoteException {
        BluetoothUtil bluetoothUtil = new BluetoothUtil();
        String test_bluetooth = bluetoothUtil.test_bluetooth();
        OID oid = (OID) MIBtree.HW_BT_MODULE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(test_bluetooth));
        MIB_MAP.set(vb);
    }

    /**
     * 检测RFID芯片是否挂载成功
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateRFIDModule() throws RemoteException {
        RfidUtil rfidUtil = new RfidUtil();
        String testRfid = rfidUtil.test_rfid();
        OID oid = (OID) MIBtree.HW_RFID_MODULE.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(testRfid));
        MIB_MAP.set(vb);
    }


    /**
     * 获取以太网的IP地址
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateEthernetIP() throws RemoteException {
        EthernetUtil ethernetUtil = new EthernetUtil();
        String ethernetUtilIPAddress = ethernetUtil.getIPAddress();
        OID oid = (OID) MIBtree.SYS_ETHERNET_IP.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(ethernetUtilIPAddress));
        MIB_MAP.set(vb);
    }


    /**
     * 获取以太网的Mac地址
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateEthernetMac() throws RemoteException {
        EthernetUtil ethernetUtil = new EthernetUtil();
        String ethernetUtilMacAddress = ethernetUtil.getMacAddress();
        OID oid = (OID) MIBtree.SYS_ETHERNET_MAC.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(ethernetUtilMacAddress));
        MIB_MAP.set(vb);
    }


    /**
     * 获取USb的插入状态
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateUsbStatus() throws RemoteException, IOException {
        UsbUtil usbUtil = new UsbUtil();
        String usbInsertStatus = usbUtil.getUSBInsertStatus();
        OID oid = (OID) MIBtree.SYS_ETHERNET_MAC.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(usbInsertStatus));
        MIB_MAP.set(vb);
    }


    /**
     * 获取SDCard的插入状态
     *
     * @throws RemoteException
     * @throws IOException
     */
    private void updateSDCardStatus() throws RemoteException, IOException {
        UsbUtil usbUtil = new UsbUtil();
        String sdCardInsertStatus = usbUtil.getSDCardInsertStatus();
        OID oid = (OID) MIBtree.SYS_ETHERNET_MAC.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(sdCardInsertStatus));
        MIB_MAP.set(vb);
    }


    private void updateAndroidVersion() {
        String version = Build.VERSION.RELEASE;
        OID oid = (OID) MIBtree.SYS_ANDROID_VERSION_OID.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new OctetString(version));
        MIB_MAP.set(vb);
    }

    private void updateUptime() {
        Long time = SystemClock.uptimeMillis();
        OID oid = (OID) MIBtree.SYS_UPTIME_OID.clone();
        VariableBinding vb = new VariableBinding(oid.append(0), new TimeTicks(time.intValue()));
        MIB_MAP.set(vb);
    }

    private void updateRunningServices() {
        VariableBinding vb;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        OID oid = (OID) MIBtree.SRVC_NUMBER_OID.clone();
        vb = new VariableBinding(oid.append(0), new Integer32(runningServices.size()));
        MIB_MAP.set(vb);
        int i = 0;
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            i++;
            System.out.println(serviceInfo.pid);
            oid = (OID) MIBtree.SRVC_INDEX_OID.clone();
            vb = new VariableBinding(oid.append(i), new Integer32(serviceInfo.pid));
            MIB_MAP.set(vb);
            oid = (OID) MIBtree.SRVC_DESCR_OID.clone();
            vb = new VariableBinding(oid.append(i), new OctetString(serviceInfo.process));
            MIB_MAP.set(vb);
            Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[]{serviceInfo.pid});
            oid = (OID) MIBtree.SRVC_MEMORY_USED_OID.clone();
            vb = new VariableBinding(oid.append(i), new Integer32(memoryInfos[0].getTotalPss()));
            MIB_MAP.set(vb);
            oid = (OID) MIBtree.SRVC_RUNNING_TIME_OID.clone();
            Long time = serviceInfo.activeSince;
            vb = new VariableBinding(oid.append(i), new TimeTicks(time.intValue()));
            MIB_MAP.set(vb);
        }
    }


    private void updateBluetoothStatus() {
        VariableBinding vb;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isOn = bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON
                || bluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON;
        OID oid = (OID) MIBtree.HW_BLUETOOTH_STATUS_OID.clone();
        vb = new VariableBinding(oid.append(0), new Integer32(isOn ? 1 : 0));
        MIB_MAP.set(vb);
    }

    private void updateNetworkStatus() {
        VariableBinding vb;
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        boolean isOn = wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED
                || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING;
        OID oid = (OID) MIBtree.HW_NETWOK_STATUS_OID.clone();
        vb = new VariableBinding(oid.append(0), new Integer32(isOn ? 1 : 0));
        MIB_MAP.set(vb);
    }
}
