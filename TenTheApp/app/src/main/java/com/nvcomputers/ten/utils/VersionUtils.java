package com.nvcomputers.ten.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class VersionUtils {

    public static int getAppVersion(Context context) {
        return getAppPackageInfo(context).versionCode;
    }

    public static String getAppVersionName(Context context) {
        return getAppPackageInfo(context).versionName;
    }

    private static PackageInfo getAppPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
