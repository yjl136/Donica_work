package cn.donica.slcd.dmanager.snmp.trap;

import android.os.RemoteException;
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
import cn.donica.slcd.dmanager.utils.EthernetUtil;
import cn.donica.slcd.dmanager.utils.PropertiesUtil;

/**
 * Created by liangmingjie on 2015/12/11.
 */
public class LogWarnTrap {
    public static String customTagPrefix = "Donica"; // 自定义Tag的前缀，可以是作者名
    private static final String TAG = "LogRecordTrap";
    private static final int CATEGORY_SLCD = 1;
    private Snmp snmp;
    private Address targetAddress;


    /**
     * @param level      应用的告警的级别，有i,w,e三种.其中i为info(普通信息)，w为warn（警告信息），e为error（错误信息）
     * @param category   报警信息的分类，有0和1两种。0为软件类信息software，1为硬件类信息hardware
     * @param type       告警信息的类别，有0,1,2,3，4，5六种。
     *                   	0--未知类别（无法判断哪种类别）
     *                   	1--系统告警
     *                   	2--数据库告警
     *                   	3--网络告警
     *                   	4--处理错误告警
     *                   	5--服务质量告警
     * @param logContent 告警信息的具体内容
     * @param location   告警信息的触发代码位置
     * @throws RemoteException
     */
    public void sendWarn(String level, int category, int type, String logContent, String location) throws IOException {
        targetAddress = GenericAddress.parse((new PropertiesUtil()).getProperty("snmp.agent.address"));
        TransportMapping<?> transportMapping = new DefaultUdpTransportMapping();
        snmp = new Snmp(transportMapping);
        transportMapping.listen();
        // initialize target from target address
        CommunityTarget target = new CommunityTarget();
        target.setAddress(targetAddress);
        // set snmp version
        target.setVersion(SnmpConstants.version2c);
        // set retry times
        target.setRetries(2);
        // set timeout
        target.setTimeout(3000);
        // initialize PDU
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
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_LOCATION), new OctetString(location)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TRAP_ID), new OctetString(trapID)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_DEVICE_ID), new OctetString(deviceID)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_IP_ADDRESS), new OctetString(ip)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LOG_CONTENT), new OctetString(logContent)));
        pdu.setType(PDU.TRAP);

        // send pdu
        ResponseEvent responseEvent = snmp.send(pdu, target);
        if (responseEvent != null && responseEvent.getResponse() != null) {
            Vector<? extends VariableBinding> vector = responseEvent.getResponse().getVariableBindings();
            for (int i = 0; i < vector.size(); i++) {
                VariableBinding variableBinding = vector.elementAt(i);
                Log.d(TAG, variableBinding.getOid().toString() + " : " + variableBinding.getVariable().toString());
            }
        }
    }

    /**
     * 设备ID由以太网卡Mac地址和SCLD座位号组成。
     * SLCD座位号因人工配置原因，不一定做到唯一性，但以太网卡Mac是唯一的，所以设备ID也是唯一的。
     *
     * @return 设备的ID号
     */
    public static String getDeviceID() {
        String deviceID = EthernetUtil.getMacAddress() + ":" + AppCommonUtil.getSeatPosition();
        return deviceID;
    }
}
