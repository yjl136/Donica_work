/**
 *
 */
package cn.donica.slcd.dmanager.snmp.scalar;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Luke Huang
 */
public class MemoryScalar {
    private static final String TAG = MemoryScalar.class.getName();

    private Map<String, Long> memoryInfo = new HashMap<String, Long>();

    public enum MemoryType {
        TOTAL("Total Memory"), USED("Used Memory"), AVAILABLE("Available Memory");

        public String type;

        private MemoryType(String type) {
            this.type = type;
        }
    }

    public MemoryScalar() {
        readMemoryInfo();
    }

    /**
     * Get the memory size (Unit: B) of all types(Total, Used, Available).
     * Available memory includes free memory, buffered memory, and cached
     * memory.
     *
     * @param type memory type
     * @return the memory size of specific type. The unit is Byte(B).
     */
    public long getMemorySize(MemoryType type) {
        long size = 0;
        switch (type) {
            case TOTAL:
                size = memoryInfo.get("MemTotal");
                break;

            case USED:
                size = memoryInfo.get("MemUsed");
                break;

            case AVAILABLE:
                size = memoryInfo.get("MemAvailable");
                break;
        }
        return size << 10;
    }

    private void readMemoryInfo() {
        try {
            FileReader fileReader = new FileReader(ISystemFile.MEMORY_INFO_FILE);
            BufferedReader reader = new BufferedReader(fileReader, ISystemFile.DEFAULT_READ_BUFFER_SIZE);
            String lineContent;
            while ((lineContent = reader.readLine()) != null) {
                Log.d(TAG, lineContent);
                String[] contents = lineContent.trim().substring(0, lineContent.trim().length() - 3).split(":\\s+", 2);
                if (contents.length > 1) {
                    // The unit is KB here
                    memoryInfo.put(contents[0].trim(), Long.valueOf(contents[1].trim()));
                }
            }
            memoryInfo.put("MemAvailable", memoryInfo.get("MemFree") + memoryInfo.get("Buffers") + memoryInfo.get("Cached"));
            memoryInfo.put("MemUsed", memoryInfo.get("MemTotal") - memoryInfo.get("MemAvailable"));
            Log.i(TAG, "Total Memory =" + memoryInfo.get("MemTotal") + " KB");
            Log.i(TAG, "Available Memory =" + memoryInfo.get("MemAvailable") + " KB");
            Log.i(TAG, "Used Memory =" + memoryInfo.get("MemUsed") + " KB");
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read memory information file " + ISystemFile.MEMORY_INFO_FILE);
            e.printStackTrace();
        }
    }
}
