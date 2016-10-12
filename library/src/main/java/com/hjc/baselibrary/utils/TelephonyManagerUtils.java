package com.hjc.baselibrary.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * 获取手机SIM卡信息的工具类 需要权限：android.permission.READ_PHONE_STATE
 *
 * @author mingsong.zhang
 * @date 20120627
 */
public class TelephonyManagerUtils {

	private TelephonyManagerUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("TelephonyManagerUtils cannot be instantiated");
	}

	/**
	 * 获取设备唯一ID
	 *
	 * @param context context
	 * @return 设备唯一ID
	 */
	public static String getUniqeId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 获取IMSI
	 * 需要权限：android.permission.READ_PHONE_STATE
	 *
	 * @return null if device ID is not available.
	 */
	public static String getImsi(Context context) {

		String imsi = (String) SPUtils.get(context, "imsi", "");
		if (StringUtils.isEmpty(imsi)) {
			TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			imsi = telMgr.getSubscriberId();
			SPUtils.put(context, "imsi", imsi);

		}
		return imsi;
	}

	/**
	 * 唯一的设备ID：<br/>
	 * 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID<br/>
	 * 需要权限：android.permission.READ_PHONE_STATE
	 *
	 * @return null if device ID is not available.
	 */
	public static String getImei(Context context) {

		String imei = (String) SPUtils.get(context, "imei", "");
		if (StringUtils.isEmpty(imei)) {
			TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			imei = telMgr.getDeviceId();
			SPUtils.put(context, "imei", imei);

		}
		return imei;
	}

	/**
	 * 获取mac地址 需要权限：android.permission.READ_PHONE_STATE
	 *
	 * @return null if device ID is not available.
	 */
	public static String getMacAddress(Context context) {

		String mac = (String) SPUtils.get(context, "mac", "");
		if (StringUtils.isEmpty(mac)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				mac = getMacAddrOnTarget23();
			} else {
				// tencent过滤了获取服务WIFI_SERVICE，必须获取app的context来获取
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				mac = wifiManager.getConnectionInfo().getMacAddress();
			}
			SPUtils.put(context, "mac", mac);

		}
		return mac;
	}


	private static String getMacAddrOnTarget23() {
		try {
			List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface nif : all) {
				if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

				byte[] macBytes = nif.getHardwareAddress();
				if (macBytes == null) {
					return "";
				}

				StringBuilder res1 = new StringBuilder();
				for (byte b : macBytes) {
					res1.append(Integer.toHexString(b & 0xFF) + ":");
				}

				if (res1.length() > 0) {
					res1.deleteCharAt(res1.length() - 1);
				}
				return res1.toString();
			}
		} catch (Exception ex) {
		}
		return "02:00:00:00:00:00";
	}

	/**
	 * 获取运营商信息
	 *
	 * @return null if device ID is not available.
	 */
	public static String getProvidersName(Context context) {

		String providersName = (String) SPUtils.get(context, "providersName", "");
		if (StringUtils.isEmpty(providersName)) {
			TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			// 返回唯一的用户ID;就是这张卡的编号神马的
			String IMSI = telMgr.getSubscriberId();
			// 可能为null
			if (StringUtils.isNotEmpty(IMSI)) {
				// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
				if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
					providersName = "cmcc";
				} else if (IMSI.startsWith("46001")) {
					// 联通
					providersName = "cucc";
				} else if (IMSI.startsWith("46003")) {
					// 电信
					providersName = "ctcc";
				}
			}

			return providersName;

		}
		return providersName;
	}

	/**
	 * 获取手机号码
	 *
	 * @param context
	 * @return
	 */
	public static String getMobileNumber(Context context) {
		TelephonyManager tmManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String telString = tmManager.getLine1Number();
		if (StringUtils.isEmpty(telString))
			telString = "";
		return telString;
	}

	/**
	 * 获取ram总内存大小
	 */
	public static float getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		float initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
		}
		// Byte转换为GB，内存大小规格化
		return initial_memory / (1024 * 1024 * 1024);
	}

	/**
	 * 获取应用ID
     */
	public static String getAndroidAppId(Context context) {
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	/**
	 * 获取基站信息
     */
	public static GsmCellLocation getCellLocation(Context context) {
        // 判断权限
        int resultLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int resultPhoneState = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (resultLocation != PackageManager.PERMISSION_GRANTED
                || resultPhoneState != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CellLocation cellLocation = tm.getCellLocation();

        if (cellLocation instanceof GsmCellLocation) {
            return (GsmCellLocation)cellLocation;
        }

        // 兼容电信基站信息
        if (cellLocation instanceof CdmaCellLocation) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation)cellLocation;
            GsmCellLocation gsl = new GsmCellLocation();
            int lac = cdmaCellLocation.getNetworkId();
            int cid = cdmaCellLocation.getBaseStationId();
            gsl.setLacAndCid(lac, cid);

            return gsl;
        }

		return null;
	}
}
