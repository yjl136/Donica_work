/**
 *
 */
package cn.donica.slcd.dmanager.snmp.trap;

import android.util.Log;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import java.io.IOException;
import java.util.Vector;

import cn.donica.slcd.dmanager.utils.PropertiesUtil;

/**
 * @author Luke Huang
 */
public class SnmpTrapReceiver {
    private static final String TAG = "SnmpTrapReceiver";
    private Snmp snmp;
    private Address address;
    private MultiThreadedMessageDispatcher dispatcher;
    private ThreadPool threadPool;

    private void init() throws IOException {
        threadPool = ThreadPool.create("snmp trap", 2);
        dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
        address = GenericAddress.parse((new PropertiesUtil()).getProperty("snmp.trap.address"));

        TransportMapping<?> transport;
        if (address instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) address);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) address);
        }
        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.addCommandResponder(new TrapResponder());
        Log.d(TAG, "start listening ... ...");
        snmp.listen();
    }

    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class TrapResponder implements CommandResponder {
        @Override
        public void processPdu(CommandResponderEvent event) {
            // parse response
            if (event != null && event.getPDU() != null) {
                Vector<? extends VariableBinding> vector = event.getPDU().getVariableBindings();
                for (int i = 0; i < vector.size(); i++) {
                    VariableBinding variableBinding = vector.elementAt(i);
                    System.out.println(variableBinding.getOid() + " : " + variableBinding.getVariable());
                }
            }
        }
    }
}
