package cn.donica.slcd.dmanager.utils;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import cn.donica.slcd.dmanager.crash.BaseApplication;

/**
 * Created by liangmingjie on 2015/12/16.
 */
public class AppInfoUtil {

    private String appname = "";
    private String pname = "";
    private String versionName = "";
    private int versionCode = 0;
    private Drawable icon;

    private void prettyPrint() {

    }

    private ArrayList<AppInfoUtil> getPackages() {
        ArrayList<AppInfoUtil> apps = getInstalledApps();
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    private ArrayList<AppInfoUtil> getInstalledApps() {
        ArrayList<AppInfoUtil> res = new ArrayList<AppInfoUtil>();
        List<PackageInfo> packs = BaseApplication.getContext().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (p.versionName == null) {
                continue;
            }
            AppInfoUtil newInfo = new AppInfoUtil();
            newInfo.appname = p.applicationInfo.loadLabel(BaseApplication.getContext().getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(BaseApplication.getContext().getPackageManager());
            res.add(newInfo);
        }
        return res;
    }
}
