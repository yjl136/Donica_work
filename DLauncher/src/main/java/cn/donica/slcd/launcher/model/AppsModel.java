package cn.donica.slcd.launcher.model;

import android.graphics.Bitmap;

public class AppsModel {
    //程序图标
    private Bitmap activity_Icon;
    //activity名称
    private String activity_Name;
    //包名
    private String package_Name;

    private CharSequence app_name;

    public CharSequence getApp_name() {
        return app_name;
    }

    public void setApp_name(CharSequence activityText) {
        app_name = activityText;
    }

    public Bitmap getActivity_Icon() {
        return activity_Icon;
    }

    public void setActivity_Icon(Bitmap activityIcon) {
        activity_Icon = activityIcon;
    }

    public String getActivity_Name() {
        return activity_Name;
    }

    /**
     * 保存程序Activity名称
     */
    public void setActivity_Name(String activityName) {
        activity_Name = activityName;
    }

    public String getPackage_Name() {
        return package_Name;
    }

    /**
     * 保存程序包名
     */
    public void setPackage_Name(String packageName) {
        package_Name = packageName;
    }

}
