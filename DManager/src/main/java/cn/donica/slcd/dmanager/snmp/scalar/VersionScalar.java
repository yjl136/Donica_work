package cn.donica.slcd.dmanager.snmp.scalar;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VersionScalar {
    private static final String TAG = VersionScalar.class.getName();

    public enum VersionType {
        SYSTEM("System Version"), KERNEL("Kernel Version"), FIRMWARE("Firmware Version"), MODEL("Model Version");

        public String type;

        private VersionType(String type) {
            this.type = type;
        }
    }

    public String getVersion(VersionType type) {
        String version = null;
        switch (type) {
            case SYSTEM:
                version = Build.DISPLAY;
                break;

            case KERNEL:
                version = getKernelVersion();
                break;

            case FIRMWARE:
                version = Build.VERSION.RELEASE;
                break;

            case MODEL:
                version = Build.MODEL;
                break;
        }
        return version;
    }

    /**
     * @return 内核版本
     */
    private String getKernelVersion() {
        String version = null;
        try {
            FileReader fileReader = new FileReader(ISystemFile.VERSION_FILE);
            BufferedReader reader = new BufferedReader(fileReader, ISystemFile.DEFAULT_READ_BUFFER_SIZE);
            String lineContent = reader.readLine();
            String[] contents = lineContent.split("\\s+");
            if (contents.length > 2) {
                version = contents[2];
            }
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            Log.e(TAG, "failed to read version file " + ISystemFile.VERSION_FILE + " : " + e.getMessage());
            e.printStackTrace();
        }
        return version;
    }
}
