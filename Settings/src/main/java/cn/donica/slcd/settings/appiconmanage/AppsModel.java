package cn.donica.slcd.settings.appiconmanage;

import android.graphics.drawable.Drawable;

public class AppsModel {
    /**
     * 程序图标
     */
    private Drawable appIcon;
    /**
     * 主入口activity名称
     */
    private String activityName;
    /**
     * 包名
     */
    private String packageName;

    /**
     * 应用名
     */
    private CharSequence appName;

    public CharSequence getAppName() {
        return appName;
    }

    public void setAppName(CharSequence appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getActivityName() {
        return activityName;
    }

    /**
     * 保存程序Activity名称
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getPackageName() {
        return packageName;
    }

    /**
     * 保存程序包名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}
