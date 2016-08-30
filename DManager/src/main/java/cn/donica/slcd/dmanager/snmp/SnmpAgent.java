/**
 *
 */
package cn.donica.slcd.dmanager.snmp;

import android.content.Context;
import android.util.Log;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import cn.donica.slcd.dmanager.snmp.scalar.IScalar;
import cn.donica.slcd.dmanager.snmp.scalar.ScalarAdapter;
import cn.donica.slcd.dmanager.utils.PropertiesUtil;

/**
 * SNMP agent worked on SLCD is to process device built-in test functionality
 * via SNMP.
 *
 * @author Luke Huang
 */
public class SnmpAgent implements CommandResponder {
    private static final String TAG = SnmpAgent.class.getName();
    private Snmp snmp;
    private boolean started = false;
    private Context context;

    public SnmpAgent(Context context) {
        this.context = context;
    }

    public boolean isStarted() {
        return started;
    }

    public void start() {
        if (!started) {
            try {
                //getProperty("snmp.agent.address")取出assets文件夹里的config.properties中key对应的value值
                //此为udp:0.0.0.0/161
                String address = (new PropertiesUtil()).getProperty("snmp.agent.address");
                //开启一个UDP通信
                TransportMapping<UdpAddress> transportMapping = new DefaultUdpTransportMapping((UdpAddress) GenericAddress.parse(address));
                snmp = new Snmp(transportMapping);
                snmp.addCommandResponder(this);
                //开启监听
                snmp.listen();
                started = true;
                Log.i(TAG, "start to listen ");
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.w(TAG, "agent is already started");
        }
    }

    public void stop() {
        try {
            if (snmp != null) {
                snmp.close();
            }
            started = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processPdu(CommandResponderEvent event) {
        Log.i(TAG, "community name" + new String(event.getSecurityName()));
        PDU pdu = event.getPDU();
        if (pdu == null) {
            Log.w(TAG, "pdu is null");
        } else {
            Log.i(TAG, "recv pdu:" + pdu.toString());
            switch (pdu.getType()) {
                case PDU.GET:
                case PDU.GETNEXT:
                    Log.i(TAG, "pdu type:" + pdu.getType());
                    Log.i(TAG, "prepare response for pdu");
                    pdu.setType(PDU.RESPONSE);
                    Vector<?> variableBindings = pdu.getVariableBindings();
                    for (int i = 0; i < variableBindings.size(); i++) {
                        VariableBinding vb = (VariableBinding) variableBindings.get(i);
                        pdu.set(i, new VariableBinding(vb.getOid(), getValue(vb.getOid().toString())));
                    }
                    try {
                        // send response
                        event.getMessageDispatcher().returnResponsePdu(
                                event.getMessageProcessingModel(),
                                event.getSecurityModel(), event.getSecurityName(), event.getSecurityLevel(), pdu,
                                event.getMaxSizeResponsePDU(), event.getStateReference(), new StatusInformation()
                        );
                    } catch (MessageException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                    break;
            }
        }
    }

    private Variable getValue(String oid) {
//        return new OctetString("Hi,Handsome Boy!");
        IScalar scalar = ScalarAdapter.getScalar(context, oid);
        return scalar.getValue(oid);
    }
}
