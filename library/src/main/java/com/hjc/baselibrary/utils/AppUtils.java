package com.hjc.baselibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * 跟App相关的辅助类
 */
@SuppressWarnings("WeakerAccess")
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("AppUtils cannot be instantiated");
    }

    /**
     * 得到设备号
     * @param context
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = tm.getSubscriberId();
            if (imsi == null || imsi.length() < 1) {
                imsi = tm.getDeviceId();
            }

            if (imsi == null || imsi.length() < 1) {
//				imsi = getMobileMacAddr(ctx);
            }

            return imsi;
        } catch (Exception ex) {
        }
        return "";
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用的版本号
     *
     * @param context 上下文
     * @param pkgName 应用包名
     * @return 应用版本号
     */
    public static int getVersionCode(Context context, String pkgName) {
        if (StringUtils.isEmpty(pkgName)) {
            return 0;
        }

        int versionCode = 0;

        try {
            versionCode = context.getPackageManager().getPackageInfo(pkgName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
