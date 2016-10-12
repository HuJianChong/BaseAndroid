package com.hjc.baselibrary.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

/**
 * 手机硬件参数数据类，欢迎界面获取手机硬件参数存入SP中 便于其他地方使用以及定时做上传
 *
 * @author blueming.wu
 * @date 2012-10-22
 */
public class DeviceConfig {

	/** 手机屏幕宽高（分辨率） */
	private static int w;
	private static int h;
    public static String deviceId;
    public static String imsi;
    public static String imei;
    public static String mac;
	public static String net = "wifi";
	public static String operator;
	public static String osVersion;
	public static String mb;
	public static String ram;
	public static String country;
	public static int StatusBarHeight;

	public static int getDeviceWidth() {
		return w;
	}

	public static int getDeviceHeight() {
		return h;
	}

	public static String getCountry() {
		return country;
	}

	/**
	 * 得到屏幕尺寸数据存入DeviceConfig
	 */
	public static void initScreenSize(Context context) {
		if (w == 0 || h == 0) {
			Logger.e("手机分辨率w：" + w + "  h:" + h);
			reinstallScreenSize(context);
		}
	}

	/**
	 * 重新得到屏幕尺寸数据存入DeviceConfig
	 */
	public static void reinstallScreenSize(Context context) {
		DisplayMetrics size = new DisplayMetrics();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(size);
		w = size.widthPixels;
		h = size.heightPixels;
		Logger.e("reinstallScreenSize 手机分辨率w：" + w + "  h:" + h);
	}

	/**
	 * 网络发生变化，重新获取网络状态
	 *
	 * @Title: resetNetStatus
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void resetNetStatus(Context context) {
		net = NetWorkUtil.getNetworkType(context);
		Logger.e("The net type has changed：" + net);
	}

	/**
	 *
	 * @Title: initDeviceData
	 * @Description: 初始化设备基础数据
	 * @param @param context 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void initDeviceData(Context context) {
		if (StringUtils.isEmpty(ram))
			ram = TelephonyManagerUtils.getTotalMemory() + "G";

		net = NetWorkUtil.getNetworkType(context);

		if (StatusBarHeight == 0)
			StatusBarHeight = getStatusBarHeight(context);

		if (StringUtils.isEmpty(country)) {
			country = getOsLocaleLanguage(context);
			Logger.e("deviceinfo, country:" + country);
		}
	}

	/**
	 * 必须授权了Manifest.permission.READ_PHONE_STATE
	 * @param context context
     */
	public static void initDataWithPermission(Context context){
		if (StringUtils.isEmpty(deviceId))
			deviceId = TelephonyManagerUtils.getUniqeId(context);
		if (StringUtils.isEmpty(imsi))
			imsi = TelephonyManagerUtils.getImsi(context);
		if (StringUtils.isEmpty(imei))
			imei = TelephonyManagerUtils.getImei(context);
		if (StringUtils.isEmpty(mac))
			mac = TelephonyManagerUtils.getMacAddress(context);
		if (StringUtils.isEmpty(operator))
			operator = TelephonyManagerUtils.getProvidersName(context);
		if (StringUtils.isEmpty(mb))
			mb = TelephonyManagerUtils.getMobileNumber(context);
	}

	// 获取手机状态栏高度
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 获取当前系统的语言环境
	 *
	 * @Title: getOsLocale
	 * @return String 返回类型
	 * @return
	 * @throws
	 */
	public static String getOsLocaleLanguage(Context context) {
		return context.getResources().getConfiguration().locale.toString();
	}

}
