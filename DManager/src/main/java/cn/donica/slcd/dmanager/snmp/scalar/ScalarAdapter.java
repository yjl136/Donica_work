/**
 *
 */
package cn.donica.slcd.dmanager.snmp.scalar;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import cn.donica.slcd.dmanager.snmp.ISnmpOID;

/**
 * @author Luke Huang
 */
public class ScalarAdapter {
    private static final String TAG = "ScalarAdapter";
    private static Map<String, ScalarType> map = new HashMap<String, ScalarType>();

    public enum ScalarType {
        APPLICATION, SYSTEM
    }

    static {
//		map.put(ISnmpOID.OID_SNMPD_STATE, ScalarType.APPLICATION);
//		map.put(ISnmpOID.OID_SNMPD_RUNNINGTIME, ScalarType.APPLICATION);
//		map.put(ISnmpOID.OID_CMA_STATE, ScalarType.APPLICATION);
//		map.put(ISnmpOID.OID_CMA_RUNNINGTIME, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_LAUNCHER_STATE, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_LAUNCHER_RUNNINGTIME, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_UPGRADE_STATE, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_UPGRADE_RUNNINGTIME, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_SETTINGS_STATE, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_SETTINGS_RUNNINGTIME, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_OAM_STATE, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_OAM_RUNNINGTIME, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_MESSAGE_SERVICE_STATE, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_MESSAGE_SERVICE_RUNNINGTIME, ScalarType.APPLICATION);
        map.put(ISnmpOID.OID_CPU_TEMPERATURE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_CPU_IDLE_RATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_CPU_USAGE_RATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_AUDIO_OUTPUT, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_AUDIO_CHIP_ID, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_HEADPHONE_STATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_AD_MOUNT_STATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_NTSC_SIGNAL_STATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_MEMORY_TOTAL, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_MEMORY_AVAILABLE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_MEMORY_USED, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_SSD_TOTAL_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_SSD_USED_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_SSD_AVAILABLE_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_SSD_STATUS, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_EMMC_TOTAL_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_EMMC_USED_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_EMMC_AVAILABLE_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_EMMC_STATUS, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_SD_TOTAL_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_SD_USED_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_SD_AVAILABLE_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_SD_STATUS, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_MODULE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_RF_MODULE_STATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_IP, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_DHCP, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_NETMASK, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_GATEWAY, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_DNS1, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_DNS2, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_WIFI_MAC, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_BT_MODULE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_BT_RF_MODULE_STATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_RFID_MODULE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_RFID_PAIR_STATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_LCD_MODULE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_BACKLIGHT_STATE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_CPU_POWER_MODULE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_POWER_VOLTAGE_VCC, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_POWER_VOLTAGE_V1, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_POWER_VOLTAGE_V2, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_POWER_VOLTAGE_V3, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_CONN_STATUS, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_SPEED, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_DUPLEX, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_IP, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_DHCP, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_MAC, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_NETMASK, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_GATEWAY, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_DNS1, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_ETHERNET_DNS2, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_USB_TOTAL_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_USB_USED_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_USB_AVAILABLE_SIZE, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_USB_STATUS, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_HARDKEY_LED1, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_HARDKEY_LED2, ScalarType.SYSTEM);
        map.put(ISnmpOID.OID_USB_PORT_LED, ScalarType.SYSTEM);

    }

    public static IScalar getScalar(Context context, String oid) {
        Log.d(TAG, "oid is" + oid);
        switch (map.get(oid)) {
            case APPLICATION:
                Log.d(TAG, "return new ApplicationScalar(context)");
                return new ApplicationScalar(context);
            default:
                Log.d(TAG, "return new SystemScalar();");
                return new SystemScalar();
        }
    }
}
