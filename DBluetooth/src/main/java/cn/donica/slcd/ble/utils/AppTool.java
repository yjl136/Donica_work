package cn.donica.slcd.ble.utils;

import cn.donica.slcd.ble.MainApplication;

public class AppTool {

    public synchronized static int getSerial() {
        int serial = SerialCache.getSerial(MainApplication.getAppContext());
        serial++;
        if (serial == 256) {
            serial = 1;
        }
        SerialCache.saveSerial(MainApplication.getAppContext(), serial);
        return serial;
    }


}
