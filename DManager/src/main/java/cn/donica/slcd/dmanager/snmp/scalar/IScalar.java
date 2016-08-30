/**
 *
 */
package cn.donica.slcd.dmanager.snmp.scalar;

import org.snmp4j.smi.Variable;

/**
 * @author Luke Huang
 */
public interface IScalar {
    public Variable getValue(String oid);
}
