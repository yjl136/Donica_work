package cn.donica.slcd.ble.utils;

import cn.donica.slcd.ble.SlcdApplication;

public class AppTool {

    public synchronized static int getSerial() {
        int serial = SerialCache.getSerial(SlcdApplication.getAppContext());
        serial++;
        if (serial == 256) {
            serial = 1;
        }
        SerialCache.saveSerial(SlcdApplication.getAppContext(), serial);
        return serial;
    }


}
