/**
 *
 */
package cn.donica.slcd.dmanager.snmp.scalar;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IHwtestService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

import java.util.HashMap;
import java.util.Map;

import cn.donica.slcd.dmanager.crash.BaseApplication;
import cn.donica.slcd.dmanager.snmp.ISnmpOID;


/**
 * @author Luke Huang
 */
public class SystemScalar implements IScalar {
    private static final String TAG = SystemScalar.class.getName();

    private IHwtestService hwtestService = null;
    private byte errorCode;
    private byte buffer[] = new byte[256];
    private Map<String, BITEMethod> map;

    private static final int FUNC_DEFAULT = 1;
    private static final int FUNC_TOTAL = 1;
    private static final int FUNC_USED = 2;
    private static final int FUNC_AVAILABLE = 3;

    private static final int FUNC_ETH_CONN = 1;
    private static final int FUNC_ETH_SPEED = 2;
    private static final int FUNC_ETH_DUPLEX = 3;
    private static final int FUNC_ETH_IP = 4;

    private static final int NUMBER_ONE = 1;
    private static final int NUMBER_TWO = 2;
    private static final int NUMBER_THREE = 3;

    private static final int LED_OFF = 0;
    private static final int LED_ON = 1;

    public enum BITEType {
        CPU_USAGE,
        CPU_TEMPERATURE,
        POWER_VOLTAGE,

        MEMORY,
        DISK_MMC,
        DISK_SSD,
        ETHERNET,
        USB,
        SD,
        NTSC_SIGNAL,
        AUDIO_CHIP,
        TEST_AUDIO_HEADSET,
        TEST_AUDIO_OUT_PUT,
        TEST_WIFI,
        TEST_BLUETOOTH,
        TEST_RFID,
        TEST_LCD,
        TEST_LED
    }

    public class BITEMethod {
        public BITEType type;
        public int funcID;

        public BITEMethod(BITEType type, int funcID) {
            this.type = type;
            this.funcID = funcID;
        }
    }

    public SystemScalar() {
        hwtestService = IHwtestService.Stub.asInterface(ServiceManager.getService("hwtest"));

        map = new HashMap<String, BITEMethod>();
        map.put(ISnmpOID.OID_CPU_TEMPERATURE, new BITEMethod(BITEType.CPU_TEMPERATURE, FUNC_DEFAULT));
        map.put(ISnmpOID.OID_CPU_USAGE_RATE, new BITEMethod(BITEType.CPU_USAGE, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_CPU_POWER_MODULE, new BITEMethod(BITEType.POWER_VOLTAGE, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_AUDIO_OUTPUT, new BITEMethod(BITEType.TEST_AUDIO_OUT_PUT, FUNC_DEFAULT));
        map.put(ISnmpOID.OID_HEADPHONE_STATE, new BITEMethod(BITEType.TEST_AUDIO_HEADSET, FUNC_DEFAULT));
        map.put(ISnmpOID.OID_AUDIO_CHIP_ID, new BITEMethod(BITEType.AUDIO_CHIP, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_AD_MOUNT_STATE, new BITEMethod(BITEType.TEST_AUDIO_OUT_PUT, FUNC_DEFAULT));
        map.put(ISnmpOID.OID_NTSC_SIGNAL_STATE, new BITEMethod(BITEType.NTSC_SIGNAL, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_MEMORY_TOTAL, new BITEMethod(BITEType.MEMORY, FUNC_TOTAL));
        map.put(ISnmpOID.OID_MEMORY_USED, new BITEMethod(BITEType.MEMORY, FUNC_USED));
        map.put(ISnmpOID.OID_MEMORY_AVAILABLE, new BITEMethod(BITEType.MEMORY, FUNC_AVAILABLE));

        map.put(ISnmpOID.OID_SSD_TOTAL_SIZE, new BITEMethod(BITEType.DISK_SSD, FUNC_TOTAL));
        map.put(ISnmpOID.OID_SSD_AVAILABLE_SIZE, new BITEMethod(BITEType.DISK_SSD, FUNC_AVAILABLE));
        map.put(ISnmpOID.OID_SSD_USED_SIZE, new BITEMethod(BITEType.DISK_SSD, FUNC_USED));

        map.put(ISnmpOID.OID_EMMC_TOTAL_SIZE, new BITEMethod(BITEType.DISK_MMC, FUNC_TOTAL));
        map.put(ISnmpOID.OID_EMMC_USED_SIZE, new BITEMethod(BITEType.DISK_MMC, FUNC_USED));
        map.put(ISnmpOID.OID_EMMC_AVAILABLE_SIZE, new BITEMethod(BITEType.DISK_MMC, FUNC_AVAILABLE));

//        map.put(ISnmpOID.OID_SD_TOTAL_SIZE, new BITEMethod(BITEType.SD_CARD, FUNC_TOTAL));
//        map.put(ISnmpOID.OID_SD_USED_SIZE, new BITEMethod(BITEType.SD_CARD, FUNC_USED));
//        map.put(ISnmpOID.OID_SD_AVAILABLE_SIZE, new BITEMethod(BITEType.SD_CARD, FUNC_AVAILABLE));
        map.put(ISnmpOID.OID_ETHERNET_CONN_STATUS, new BITEMethod(BITEType.DISK_MMC, FUNC_ETH_CONN));
        map.put(ISnmpOID.OID_ETHERNET_SPEED, new BITEMethod(BITEType.DISK_MMC, FUNC_ETH_SPEED));
        map.put(ISnmpOID.OID_ETHERNET_DUPLEX, new BITEMethod(BITEType.DISK_MMC, FUNC_ETH_DUPLEX));
        map.put(ISnmpOID.OID_ETHERNET_IP, new BITEMethod(BITEType.DISK_MMC, FUNC_ETH_IP));

        map.put(ISnmpOID.OID_USB_STATUS, new BITEMethod(BITEType.USB, FUNC_DEFAULT));
        map.put(ISnmpOID.OID_SD_STATUS, new BITEMethod(BITEType.SD, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_WIFI_MODULE, new BITEMethod(BITEType.TEST_WIFI, FUNC_DEFAULT));
        map.put(ISnmpOID.OID_WIFI_RF_MODULE_STATE, new BITEMethod(BITEType.TEST_WIFI, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_BT_MODULE, new BITEMethod(BITEType.TEST_BLUETOOTH, FUNC_DEFAULT));
        map.put(ISnmpOID.OID_BT_RF_MODULE_STATE, new BITEMethod(BITEType.TEST_BLUETOOTH, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_RFID_MODULE, new BITEMethod(BITEType.TEST_RFID, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_LCD_MODULE, new BITEMethod(BITEType.TEST_LCD, FUNC_DEFAULT));
        map.put(ISnmpOID.OID_BACKLIGHT_STATE, new BITEMethod(BITEType.TEST_LCD, FUNC_DEFAULT));

        map.put(ISnmpOID.OID_HARDKEY_LED1, new BITEMethod(BITEType.TEST_LED, NUMBER_ONE));
        map.put(ISnmpOID.OID_HARDKEY_LED2, new BITEMethod(BITEType.TEST_LED, NUMBER_TWO));
        map.put(ISnmpOID.OID_USB_PORT_LED, new BITEMethod(BITEType.TEST_LED, NUMBER_THREE));


    }

    private String getValue(BITEType type, int functionID) {
        try {
            switch (type) {
                case CPU_USAGE:
                    errorCode = hwtestService.get_cpu(functionID, buffer);
                    break;
                case CPU_TEMPERATURE:
                    errorCode = hwtestService.get_temperature(buffer);
                    break;
                case POWER_VOLTAGE:
//                    errorCode = hwtestService.get_power(functionID, buffer);
                    break;
                case TEST_AUDIO_OUT_PUT:
                    errorCode = hwtestService.test_audio_chip(buffer);
                    break;
                case TEST_AUDIO_HEADSET:
                    errorCode = hwtestService.test_audio_headset(buffer);
                    break;
                case AUDIO_CHIP:
//                    errorCode = hwtestService.get_audio(buffer);
                    break;
                case NTSC_SIGNAL:
//                    errorCode = hwtestService.get_ntsc(buffer);
                    break;
                case MEMORY:
                    errorCode = hwtestService.get_memory(functionID, buffer);
                    break;
                case DISK_MMC:
                    errorCode = hwtestService.get_disk_mmc(functionID, buffer);
                    break;
                case DISK_SSD:
                    errorCode = hwtestService.get_disk_ssd(functionID, buffer);
                    break;
//                case SD_CARD:
//                    errorCode = hwtestService.get_sdcard(functionID, buffer);
//                    break;
                case ETHERNET:
                    errorCode = hwtestService.get_ethernet(functionID, buffer);
                    break;
                case USB:
//                    errorCode = hwtestService.get_usb(functionID, buffer);
                    break;
                case SD:
//                    errorCode = hwtestService.get_sdcard(functionID, buffer);
                    break;
                case TEST_WIFI:
                    errorCode = hwtestService.test_wifi(buffer);
                    break;
                case TEST_BLUETOOTH:
                    errorCode = hwtestService.test_bluetooth(buffer);
                    break;
                case TEST_RFID:
                    errorCode = hwtestService.test_rfid(FUNC_DEFAULT, buffer);
                    break;
                case TEST_LCD:
                    errorCode = hwtestService.test_lcd(buffer);
                    break;
                case TEST_LED:
                    errorCode = hwtestService.get_led(functionID, buffer);
                    break;
                default:
                    return null;
            }
            if (errorCode != 0) {
                Log.e(TAG, "get BITE value failed [type:" + type + "]!");
                return null;
            }
            return getBufferValue(buffer);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getLEDState(int position) {
        try {
            errorCode = hwtestService.get_led(position, buffer);

            if (errorCode != 0) {
                Log.e(TAG, "get LED state failed! position:" + position);
            }
            return Integer.parseInt(getBufferValue(buffer)) == 0;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setLEDState(int position, boolean lightOn) {
        try {
            int mode = lightOn ? LED_ON : LED_OFF;
            errorCode = hwtestService.set_led(position, mode);
            if (errorCode != 0) {
                Log.e(TAG, "set LED state failed! position:" + position + " to " + (lightOn ? "on" : "off"));
            }
            return errorCode == 0;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getBufferValue(byte[] buffer) {
        int length = buffer[0];
        return new String(buffer, 1, length);
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    String getIpAddress() {
        WifiManager wifimanage = (WifiManager) BaseApplication.getContext().getSystemService(Context.WIFI_SERVICE);// 获取WifiManager
        // 检查wifi是否开启
        if (!wifimanage.isWifiEnabled()) {
            wifimanage.setWifiEnabled(true);
        }
        WifiInfo wifiinfo = wifimanage.getConnectionInfo();
        DhcpInfo dhcpInfo = wifimanage.getDhcpInfo();
        String ip = intToIp(wifiinfo.getIpAddress());
        return ip;

    }

    /**
     * 获取的int转为真正的ip地址
     *
     * @param i
     * @return
     */
    private String intToIp(int i) {
        // TODO Auto-generated method stub
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }

    @Override
    public Variable getValue(String oid) {
        BITEMethod method = map.get(oid);
        return new OctetString(getValue(method.type, method.funcID));
    }

    /**
     * public int getCpuUsage() {
     * return Integer.parseInt(getValue(BITEType.CPU_USAGE, FUNC_DEFAULT));
     * }
     *
     * public int getCpuTemperature() {
     * return Integer.parseInt(getValue(BITEType.CPU_TEMPERATURE,
     * FUNC_DEFAULT));
     * }
     *
     * public int getPowerVoltage() {
     * return Integer.parseInt(getValue(BITEType.POWER_VOLTAGE, FUNC_DEFAULT));
     * }
     *
     * public String getAudio() {
     * return getValue(BITEType.AUDIO_CHIP, FUNC_DEFAULT);
     * }
     *
     * public long getTotalMemory() {
     * return Long.parseLong(getValue(BITEType.MEMORY, FUNC_TOTAL));
     * }
     *
     * public long getUsedMemory() {
     * return Long.parseLong(getValue(BITEType.MEMORY, FUNC_USED));
     * }
     *
     * public long getAvailableMemory() {
     * return Long.parseLong(getValue(BITEType.MEMORY, FUNC_AVAILABLE));
     * }
     *
     * public long getEMMCTotalCapacity() {
     * return Long.parseLong(getValue(BITEType.DISK_MMC, FUNC_TOTAL));
     * }
     *
     * public long getEMMCUsedCapacity() {
     * return Long.parseLong(getValue(BITEType.DISK_MMC, FUNC_USED));
     * }
     *
     * public long getEMMCAvailableCapacity() {
     * return Long.parseLong(getValue(BITEType.DISK_MMC, FUNC_AVAILABLE));
     * }
     *
     * public long getSSDTotalCapacity() {
     * return Long.parseLong(getValue(BITEType.DISK_SSD, FUNC_TOTAL));
     * }
     *
     * public long getSSDUsedCapacity() {
     * return Long.parseLong(getValue(BITEType.DISK_SSD, FUNC_USED));
     * }
     *
     * public long getSSDAvailableCapacity() {
     * return Long.parseLong(getValue(BITEType.DISK_SSD, FUNC_AVAILABLE));
     * }
     *
     * public boolean getEthernetLinkState() {
     * return Integer.parseInt(getValue(BITEType.ETHERNET, FUNC_ETH_LINK)) == 0;
     * }
     *
     * public String getEthernetSpeed() {
     * return getValue(BITEType.ETHERNET, FUNC_ETH_SPEED) + " M";
     * }
     *
     * public String getEthernetDuplex() {
     * return getValue(BITEType.ETHERNET, FUNC_ETH_DUPLEX);
     * }
     *
     * public String getEthernetIPAddress() {
     * return getValue(BITEType.ETHERNET, FUNC_ETH_IP);
     * }
     *
     * public boolean getUSBState() {
     * return Integer.parseInt(getValue(BITEType.USB, FUNC_DEFAULT)) == 0;
     * }
     *
     * public boolean getSDCardState() {
     * return Integer.parseInt(getValue(BITEType.SD_CARD, FUNC_DEFAULT)) == 0;
     * }
     */
}
