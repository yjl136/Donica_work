package cn.donica.slcd.ble.ota;

import android.content.Context;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-11-01 09:10
 * Describe:
 */

public class OTAManager {
    public final static String PROTOCOL_DEFAULT = "http";
    public final static String SERVER_DEFAULT = "192.168.2.69";
    public final static int PORT_DEFAULT = 80;

    /**
     * 获取服务端BuildPropParser
     *
     * @param configURL
     * @param context
     * @return
     */
    public static BuildPropParser getTargetPackagePropertyList(URL configURL, Context context) {
        try {
            URL url = configURL;
            url.openConnection();
            InputStream reader = url.openStream();
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buffer = new byte[153600];
            int totalBufRead = 0;
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[153600];
                totalBufRead += bytesRead;
            }
            reader.close();
            BuildPropParser parser = new BuildPropParser(writer, context);
            return parser;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 比较服务器版本和本地版本
     *
     * @param parser
     * @return
     */
    public static boolean compareLocalVersionToServer(BuildPropParser parser) {
        if (parser == null) {
            return false;
        }
        String localNumVersion = Build.VERSION.INCREMENTAL;
        Long buildutc = Build.TIME;
        Long remoteBuildUTC = (Long.parseLong(parser.getProp("ro.build.date.utc"))) * 1000;
        boolean upgrade = false;
        upgrade = remoteBuildUTC > buildutc;
        return upgrade;
    }

    /**
     * 用于检测指定URL是否可以用
     *
     * @param url
     * @return
     */
    public static boolean checkURLOK(URL url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return
     */
    public static URL getBuildPropURL() throws MalformedURLException {
        String buildconfigAddr = new String(Build.PRODUCT + "/" + "build.prop");
        URL buildpropURL = new URL(PROTOCOL_DEFAULT, SERVER_DEFAULT, PORT_DEFAULT, buildconfigAddr);
        return buildpropURL;
    }

    /**
     * 是否系统升级
     *
     * @return
     */
    public static boolean checkVersion(Context context) {
        boolean upgrade = false;
        try {
            if (checkURLOK(getBuildPropURL())) {
                //网络可以使用
                BuildPropParser parser = getTargetPackagePropertyList(getBuildPropURL(), context);
                upgrade = compareLocalVersionToServer(parser);
            } else {
                //网络不可以用
                upgrade = false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            return upgrade;
        }
    }
}
