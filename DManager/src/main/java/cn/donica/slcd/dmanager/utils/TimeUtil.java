/**
 *
 */
package cn.donica.slcd.dmanager.utils;


/**
 * @author Luke Huang
 */
public class TimeUtil {
    /**
     * get time stamp (seconds)
     *
     * @return Time stamp
     */
    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
