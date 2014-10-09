package me.isming.meizitu.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import me.isming.meizitu.App;


/**
 * Created by sam on 14-7-19.
 * AppInfo Util
 */
public class AppInfoUtil {
    private static int sVersionCode;
    private static String sVersionName;

    private static final String DEFAULT = "test";
    private static final String KEY_CHANNEL = "UMENG_CHANNEL";

    /**
     * 获取版本号
     * @return
     */
    public static int getVersionCode() {
        try {
            if (sVersionCode == 0) {
                PackageManager manager = App.getContext().getPackageManager();
                PackageInfo info = manager.getPackageInfo(App.getContext().getPackageName(), 0);
                sVersionCode = info.versionCode;
            }
        } catch (Exception e) {
        }
        return sVersionCode;
    }

    /**
     * 获取版本名
     * @return
     */
    public static String getVersionName() {
        try {
            if (android.text.TextUtils.isEmpty(sVersionName)) {
                PackageManager manager = App.getContext().getPackageManager();
                PackageInfo info = manager.getPackageInfo(App.getContext().getPackageName(), 0);
                sVersionName = info.versionName;
            }
        } catch (Exception e) {
        }
        return sVersionName;
    }

    /**
     * 获取渠道
     * @return
     */
    public static String getChannel() {
        if (App.getContext() == null) {
            return DEFAULT;
        }
        try {
            ApplicationInfo appInfo = App.getContext().getPackageManager().getApplicationInfo(
                    App.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String channel = appInfo != null && appInfo.metaData != null ? appInfo.metaData.getString(KEY_CHANNEL) : null;
            if (channel == null) {
                return DEFAULT;
            }
            return channel;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return DEFAULT;
    }


    public static String getDiqu() {
        if (App.getContext() == null) {
            return "hk";
        }
        try {
            ApplicationInfo appInfo = App.getContext().getPackageManager().getApplicationInfo(
                    App.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String channel = appInfo != null && appInfo.metaData != null ? appInfo.metaData.getString("DIQU") : null;
            if (channel == null) {
                return "hk";
            }
            return channel;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "hk";
    }


}
