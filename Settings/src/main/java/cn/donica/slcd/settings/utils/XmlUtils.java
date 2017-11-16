package cn.donica.slcd.settings.utils;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-11-15 08:53
 * Describe:
 */

public class XmlUtils {
    public final static String CONFIG_DIR = "/mnt/sata/system";
    public final static String CONFIG_PATH = "/mnt/sata/system/config.xml";
    public final static String TAG = "config";
    public final static String SEAT_BACK_TAG = "seatback";

    /**
     * 获取
     *
     * @return
     */
    public static int getSeatBack() {
        int value = 0;
        try {
            FileInputStream is = new FileInputStream(CONFIG_PATH);
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(is, "UTF-8");
            int event = pullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = pullParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (SEAT_BACK_TAG.equals(nodeName)) {
                            value = Integer.valueOf(pullParser.nextText());
                            break;
                        }
                }
                event = pullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    /**
     * 保存配置值
     *
     * @return
     */
    public static void saveConfigValue(Context context, int value) {
        try {
            File dir = new File(CONFIG_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(CONFIG_PATH);
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            XmlSerializer xs = Xml.newSerializer();
            xs.setOutput(os, "UTF-8");
            xs.startDocument("UTF-8", true);
            xs.startTag(null, TAG);
            xs.startTag(null, SEAT_BACK_TAG);
            xs.text(String.valueOf(value));
            xs.endTag(null, SEAT_BACK_TAG);
            xs.endTag(null, TAG);
            xs.endDocument();
            os.flush();
            os.close();
            Log.d("XmlUtils", "sucess!!");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("XmlUtils", "failed!!");
        }
    }
}
