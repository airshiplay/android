/**
 * 
 */
package com.airshiplay.framework.util;

import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * @author airshiplay
 * @Create Date 2013-2-24
 * @version 1.0
 * @since 1.0
 */
public class TelephoneUtil {

	public static boolean isNetworkAvailable(Context context) {
		try {
			NetworkInfo networkInfo = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE))
					.getActiveNetworkInfo();
			return (networkInfo != null)
					&& (networkInfo.isAvailable() && networkInfo.isConnected());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isWifiEnable(Context context) {
		try {
			NetworkInfo networkInfo = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE))
					.getActiveNetworkInfo();
			return (networkInfo != null)
					&& (networkInfo.isAvailable() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int getAndroidSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	public static int getCacheSize(Context context) {
		return 1048576 * ((ActivityManager) context
				.getSystemService("activity")).getMemoryClass() / 8;
	}

	public static String getDevice() {
		return Build.DEVICE;
	}

	public static float getDisplayDensity(Activity activity) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(localDisplayMetrics);
		return localDisplayMetrics.density;
	}

	public static float getDisplayDensity(Context context) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay().getMetrics(localDisplayMetrics);
		return localDisplayMetrics.density;
	}

	public static int getDisplayHeight(Activity activity) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(localDisplayMetrics);
		return localDisplayMetrics.heightPixels;
	}

	public static int getDisplayHeight(Context context) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay().getMetrics(localDisplayMetrics);
		return localDisplayMetrics.heightPixels;
	}

	public static int getDisplayWidth(Activity activity) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(localDisplayMetrics);
		return localDisplayMetrics.widthPixels;
	}

	public static int getDisplayWidth(Context context) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay().getMetrics(localDisplayMetrics);
		return localDisplayMetrics.widthPixels;
	}

	public static String getIMEI(Context context) {
		String str = ((TelephonyManager) context.getSystemService("phone"))
				.getDeviceId();
		if (str == null)
			str = "";
		return str;
	}

	public static String getIMSI(Context context) {
		String str = ((TelephonyManager) context.getSystemService("phone"))
				.getSubscriberId();
		if (str == null)
			str = "";
		return str;
	}

	public static String getLocalMacAddress(Context context) {
		return ((WifiManager) context.getSystemService("wifi"))
				.getConnectionInfo().getMacAddress();
	}

	public static String getNetWorkName(Context context) {
		return getNetworkType(context).toLowerCase();
	}

	public static String getNetworkOperatorName(Activity activity) {
		return ((TelephonyManager) activity.getSystemService("phone"))
				.getNetworkOperatorName();
	}

	public static String getNetworkOperatorName(Context context) {
		return ((TelephonyManager) context.getSystemService("phone"))
				.getNetworkOperatorName();
	}

	public static String getPhoneLanguage() {
		String str = Locale.getDefault().getLanguage();
		if (str == null)
			str = "";
		return str;
	}

	public static String getPhoneType() {
		String str = Build.MODEL;
		if (str != null)
			str = str.replace(" ", "");
		return str.trim();
	}

	public static String getProduct() {
		return Build.PRODUCT;
	}

	/**
	 * @param context
	 * @param unit
	 *            The unit to convert from.
	 * @param value
	 *            value The value to apply the unit to.
	 * @return The complex floating point value multiplied by the appropriate
	 *         metrics depending on its unit.
	 */
	public static int getRawSize(Context context, int unit, float value) {
		Resources resources;
		if (context == null)
			resources = Resources.getSystem();
		else
			resources = context.getResources();
		return (int) TypedValue.applyDimension(unit, value,
				resources.getDisplayMetrics());
	}

	public static String getResolution(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels + "x" + dm.heightPixels;
	}

	public static String getResolution(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels + "x" + dm.heightPixels;
	}

	@SuppressWarnings("deprecation")
	public static String getSDKVersion() {
		return Build.VERSION.SDK;
	}

	public static String getSDKVersionName() {
		return Build.VERSION.RELEASE;
	}

	public static String getServiceName(Context context) {
		if (getNetworkType(context).equals("wifi"))
			return "wifi";
		if (isConnectChinaMobile(context))
			return "mobile";
		if (isConnectChinaUnicom(context))
			return "unicom";
		if (isConnectChinaTelecom(context))
			return "telecom";
		return "";
	}

	public static String getType() {
		return Build.TYPE;
	}

	public static String getUserAgent() {
		return getPhoneType();
	}

	public static String getAccessPointType(Context context) {
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE"))
			return networkInfo.getExtraInfo();
		return null;
	}

	public static String getCurrentApnProxy(Context context) {
		Cursor cur = null;
		try {
			Uri uri = Uri.parse("content://telephony/carriers/preferapn");
			cur = context.getContentResolver().query(uri, null, null, null,
					null);
			if ((cur != null) && (cur.moveToFirst())) {
				String str = cur.getString(cur.getColumnIndex("proxy"));
				return str;
			}
		} finally {
			if (cur != null)
				cur.close();
		}
		return null;
	}

	public static String getNetworkType(Context context) {
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if ((networkInfo != null) && (networkInfo.isAvailable())) {
			if (networkInfo.getTypeName().toLowerCase().equals("wifi"))
				return "wifi";
			return networkInfo.getExtraInfo().toLowerCase();

		}
		return "wifi not available";
	}

	public static String getProxyIp(String paramString, Context context) {
		if (paramString == null)
			return null;
		Cursor cur = null;
		try {
			Uri uri = Uri.parse("content://telephony/carriers");
			cur = context.getContentResolver().query(uri, null, null, null,
					null);
			String id;
			do {
				if (cur != null) {
					boolean bool = cur.moveToNext();
					if (bool)
						;
				} else {
					return null;
				}
				id = cur.getString(cur.getColumnIndex("_id"));
			} while (!paramString.trim().equals(id));
			return cur.getString(cur.getColumnIndex("proxy"));
		} finally {
			if (cur != null)
				cur.close();
		}

	}

	public static WifiInfo getWifiStatus(Context context) {
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.getConnectionInfo();
	}

	public static boolean isCmwap(Context context) {
		if ((!isConnectChinaMobile(context)) || (!isMobileType(context)))
			return false;
		String str = getCurrentApnProxy(context);
		if (str == null)
			return false;
		return (str.equals("10.0.0.172")) || (str.equals("010.000.000.172"));
	}

	public static boolean isConnectChinaMobile(Context context) {
		String str = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
		if (str != null)
			return (str.startsWith("46000")) || (str.startsWith("46002"));
		return false;
	}

	public static boolean isConnectChinaTelecom(Context context) {
		String str = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
		if (str != null)
			return str.startsWith("46003");
		return false;
	}

	public static boolean isConnectChinaUnicom(Context context) {
		String str = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
		if (str != null)
			return str.startsWith("46001");
		return false;
	}

	public static boolean isMobileType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null)
			return false;
		return networkInfo.getTypeName().equalsIgnoreCase("mobile");
	}

	public static boolean isUniwap(Context context) {
		if ((!isConnectChinaUnicom(context)) || (!isMobileType(context)))
			return false;
		String str = getCurrentApnProxy(context);
		if (str == null)
			return false;
		return (str.equals("10.0.0.172")) || (str.equals("010.000.000.172"));
	}

	public static void shotVibratePhone(Context context) {
		((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
				.vibrate(new long[] { 800L, 50L, 400L, 30L }, -1);
	}

	public static boolean isSdcardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static long getAvailableExternalMemorySize() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			StatFs statFs = new StatFs(Environment
					.getExternalStorageDirectory().getPath());
			return statFs.getBlockSize() * statFs.getAvailableBlocks();
		}
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_REMOVED)) {
			return -1L;
		}
		return 0L;
	}

	public static long getAvailableInternalMemorySize() {
		StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
	    return statFs.getBlockSize() * statFs.getAvailableBlocks();
	}
}
