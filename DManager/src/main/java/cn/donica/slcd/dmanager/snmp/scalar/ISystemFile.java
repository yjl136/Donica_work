/**
 *
 */
package cn.donica.slcd.dmanager.snmp.scalar;

/**
 * @author Luke Huang
 */
public interface ISystemFile {
    public static final String VERSION_FILE = "/proc/version";
    public static final String MEMORY_INFO_FILE = "/proc/meminfo";
    public static final String CPU_INFO_FILE = "/proc/cpuinfo";
    public static final String STATUS_FILE = "/proc/stat";

    public static final int DEFAULT_READ_BUFFER_SIZE = 1024;
}
