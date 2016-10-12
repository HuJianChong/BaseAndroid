package com.hjc.baselibrary.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * 跟App相关的辅助类
 */
public class AppUtils {

	private static final String TAG = "AppUtils";

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("AppUtils cannot be instantiated");
	}

	/**
	 * 判断
	 * @param context context
	 * @param packageName	packageName
     * @return
     */
	public static boolean isBackground(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.i(TAG, appProcess.processName);
					return true;
				}else{
					Log.i(TAG, appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 得到设备号
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getDeviceId(Context ctx) {
		try {
			TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
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
	 * 得到设置mark值
	 * 
	 * @param context
	 * @return
	 */
	public static int getDeviceMark(Context context) {
		String temp = "";
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			temp = tm.getDeviceId();
			if (temp == null || temp.length() < 1) {
				temp = "temp";
			}
		} catch (Exception ex) {
			temp = "temp";
		}

		String imsi = "and" + temp;
		int hashValue = imsi.hashCode();
		return Math.abs(hashValue);
	}

	public static String getAppName(Context mContext, String defaultAppName)
	{
		String appName;
		try{
			PackageInfo pkg = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			appName = pkg.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
		}
		catch (Exception e)
		{
			appName = defaultAppName;
			e.printStackTrace();
		}
		return appName;
	}
	/**
	 * 得到app的版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionInit = "01.00.00";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			String versionName = pi.versionName;

			if (versionName == null || versionName.length() <= 0) {
				versionName = versionInit;
			}

			return versionName;
		} catch (Exception e) {
		}
		return versionInit;
	}

	/**
	 * 得到APP版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersinNumber(Context context) {
		int versionInit = 0;

		if (context == null) {
			return versionInit;
		}

		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			int versionCode = pi.versionCode;

			if (versionCode <= 0) {
				return versionInit;
			}

			return versionCode;
		} catch (Exception e) {
		}
		return versionInit;
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
