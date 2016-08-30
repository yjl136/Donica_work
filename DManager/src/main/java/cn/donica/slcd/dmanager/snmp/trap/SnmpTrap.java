/**
 *
 */

package cn.donica.slcd.dmanager.snmp.trap;

import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import cn.donica.slcd.dmanager.snmp.ISnmpOID;
import cn.donica.slcd.dmanager.utils.PropertiesUtil;

/**
 * @author Luke Huang
 */
public class SnmpTrap {
    private static final String TAG = "SnmpTrap";
    private static final int CATEGORY_SLCD = 1;
    private Snmp snmp;
    private Address targetAddress;

    public void init() throws IOException {
        targetAddress = GenericAddress.parse((new PropertiesUtil()).getProperty("snmp.agent.address"));
        TransportMapping<?> transportMapping = new DefaultUdpTransportMapping();
        snmp = new Snmp(transportMapping);
        transportMapping.listen();
    }

    public void sendTrapMessage() throws IOException {
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
        long trapID = 111;
        String mac = "00:00:00:00:00:00";
        String ip = "192.168.2.102";
        // TODO init object id
        // logTime  Log时间
        // packageName 包名
        // appVername app版本名字
        // appVerCode app版本号
        // tag  log的tag信息，包含类名和方法名，方便定位出现错误的地方
        // level 保存告警信息的等级，有i,w,e三个等级,i为普通信息(info),w为警告信息(warn),e为错误信息（error）
        // type  log的类别，有0,1,2,3,4,5六种。0--未知类别（无法判断哪种类别）1--系统告警 2--数据库告警 3—网络告警 4--处理错误告警 5--服务质量告警
        // logContent log的具体文本内容
        // trapId 唯一的ID识别号
        // ipAddress 设备IP地址
        // deviceId  设备唯一ID号
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TIME), new TimeTicks(SystemClock.elapsedRealtime() / 100)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_PACKAGENAME), new OctetString("DManager")));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_APP_VERSION_NAME), new OctetString("1")));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_APP_VERSION_CODE), new OctetString("1.0")));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TAG), new UnsignedInteger32(1)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_LEVEL), new UnsignedInteger32(1)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TYPE), new UnsignedInteger32(CATEGORY_SLCD)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_CONTENT), new OctetString("SnmpTrap")));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_TRAP_ID), new Gauge32(trapID)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_IP_ADDRESS), new OctetString(ip)));
        pdu.add(new VariableBinding(new OID(ISnmpOID.OID_DEVICE_ID), new OctetString(mac)));
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
}
