package cn.donica.slcd.dmanager.snmp.trap;

import android.util.Log;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;

import cn.donica.slcd.dmanager.snmp.ISnmpOID;
import cn.donica.slcd.dmanager.utils.AppCommonUtil;

/**
 * Created by liangmingjie on 2015/12/10.
 */
public class LogRecordTrap {

    private static final String TAG = "LogRecordTrap";

    private Address targetAddress;


    public static final int DEFAULT_VERSION = SnmpConstants.version2c;
    public static final long DEFAULT_TIMEOUT = 3 * 1000L;
    public static final int DEFAULT_RETRY = 3;

    private Snmp snmp = null;
    private CommunityTarget target = null;


    public void sendRecord(final String packageName, final String appVersionName, final String location, final String tag, final String logContent, final String status) throws IOException {
        System.out.println("----&gt; 初始 Trap 的IP和端口 &lt;----");
        target = createTarget4Trap("udp:192.168.23.2/8087");
        final TransportMapping transport = new DefaultUdpTransportMapping();
        new Thread(new Runnable() {
            @Override
            public void run() {
                snmp = new Snmp(transport);
                try {
                    transport.listen();
                    PDU pdu = new PDU();
                    String deviceID = getDeviceID();
                    String ip = AppCommonUtil.getStaticIP();
                    String logTime = AppCommonUtil.getCurrentTime();
                    String trapID = AppCommonUtil.getMD5Str(logTime + deviceID + logContent);

                    // TODO init object id
                    /**
                     * @param logTime 日志记录时间，以SLCD为准  native
                     * @param packageName 应用包名 remote
                     * @param appVerisonName 应用版本名字 remote
                     * @param location 日志触发的代码位置 remote
                     * @param trapId 唯一的日志标识 native
                     * @param deviceID 设备ID，由以太网的MAC地址和座位号结合。可能因实际装机不严谨情况，座位号会有重复情况 native
                     * @param IP 设备的静态IP地址 native
                     * @param tag 日志的标签，默认值有l为launch（启动日志）,s为status（部件的状态日志）,m为maintain（维护日志，一般为机务人员对关键模块操作的日志记录） remote
                     * @param logContent 日志的具体内容 remote
                     * @param status 状态信息，默认为null,一般为部件的开启状态，例如WiFi开关，蓝牙开关 remote
                     */
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TIME), new OctetString(logTime)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_PACKAGENAME), new OctetString(packageName)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_APP_VERSION_NAME), new OctetString(appVersionName)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_LOCATION), new OctetString(location)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_DEVICE_ID), new OctetString(deviceID)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_IP_ADDRESS), new OctetString(ip)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_TYPE), new OctetString(tag)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_STATUS), new OctetString(status)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_CONTENT), new OctetString(logContent)));
                    pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TRAP_ID), new OctetString(trapID)));
                    pdu.setType(PDU.TRAP);
                    ResponseEvent responseEvent = snmp.send(pdu, target);
                    if (responseEvent != null && responseEvent.getResponse() != null) {
                        Vector<? extends VariableBinding> vector = responseEvent.getResponse().getVariableBindings();
                        for (int i = 0; i < vector.size(); i++) {
                            VariableBinding variableBinding = vector.elementAt(i);
                            Log.d(TAG, variableBinding.getOid().toString() + " : " + variableBinding.getVariable().toString());
                        }
                    }
//                    snmp.send(pdu, target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        /////////////////////////////////////////////////

//        PDU pdu = new PDU();
//
//        String deviceID = getDeviceID();
//        String ip = AppCommonUtil.getStaticIP();
//        String logTime = AppCommonUtil.getCurrentTime();
//        String trapID = AppCommonUtil.getMD5Str(logTime + deviceID + logContent);
//
//        // TODO init object id
//        /**
//         * @param logTime 日志记录时间，以SLCD为准  native
//         * @param packageName 应用包名 remote
//         * @param appVerisonName 应用版本名字 remote
//         * @param location 日志触发的代码位置 remote
//         * @param trapId 唯一的日志标识 native
//         * @param deviceID 设备ID，由以太网的MAC地址和座位号结合。可能因实际装机不严谨情况，座位号会有重复情况 native
//         * @param IP 设备的静态IP地址 native
//         * @param tag 日志的标签，默认值有l为launch（启动日志）,s为status（部件的状态日志）,m为maintain（维护日志，一般为机务人员对关键模块操作的日志记录） remote
//         * @param logContent 日志的具体内容 remote
//         * @param status 状态信息，默认为null,一般为部件的开启状态，例如WiFi开关，蓝牙开关 remote
//         */
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TIME), new OctetString(logTime)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_PACKAGENAME), new OctetString(packageName)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_APP_VERSION_NAME), new OctetString(appVersionName)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_LOCATION), new OctetString(location)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_DEVICE_ID), new OctetString(deviceID)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_IP_ADDRESS), new OctetString(ip)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_TYPE), new OctetString(tag)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_STATUS), new OctetString(status)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_CONTENT), new OctetString(logContent)));
//        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TRAP_ID), new OctetString(trapID)));
//        pdu.setType(PDU.TRAP);
//        snmp.send(pdu, target);
        System.out.println("----&gt; Trap Send END &lt;----");
        Log.d(TAG, "接收参数成功");
        // send pdu

    }

    /**
     * 创建对象communityarget
     *
     * @return CommunityTarget
     */
    public static CommunityTarget createTarget4Trap(String address) {
        CommunityTarget target = new CommunityTarget();
        target.setAddress(GenericAddress.parse(address));
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /**
     * 设备ID由以太网卡Mac地址和SCLD座位号组成。
     * SLCD座位号因人工配置原因，不一定做到唯一性，但以太网卡Mac是唯一的，所以设备ID也是唯一的。
     *
     * @return 设备的ID号
     */
    public static String getDeviceID() {
        String deviceID = "192.168.2.99:" + AppCommonUtil.getSeatPosition();
        return deviceID;
    }
}
