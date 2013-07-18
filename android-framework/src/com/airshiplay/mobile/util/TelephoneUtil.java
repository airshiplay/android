/**
 * 
 */
package com.airshiplay.mobile.util;

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
 * android电话相关工具类
 * 
 * @author airshiplay
 * @Create Date 2013-2-24
 * @version 1.0
 * @since 1.0
 */
public class TelephoneUtil {

	/**
	 * 
	 * This method requires the caller to hold the permission
	 * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.
	 */
	public static boolean isNetworkAvailable(Context context) {
		try {
			NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
					.getActiveNetworkInfo();
			return (networkInfo != null) && (networkInfo.isAvailable() && networkInfo.isConnected());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * This method requires the caller to hold the permission
	 * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.
	 */
	public static boolean isWifiEnable(Context context) {
		try {
			NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
					.getActiveNetworkInfo();
			return (networkInfo != null) && (networkInfo.isAvailable() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int getAndroidSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * single application memory max size limit
	 * 
	 * @param context
	 * @return Unit KB
	 */
	public static int getCacheSize(Context context) {
		return 1024 * ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}

	public static String getDevice() {
		return Build.DEVICE;
	}

	public static float getDisplayDensity(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	public static float getDisplayDensity(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	public static int getDisplayHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public static int getDisplayHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public static int getDisplayWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getDisplayWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * the IMEI for GSM and the MEID or ESN for CDMA phones
	 * <p>
	 * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
	 * READ_PHONE_STATE}
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if (str == null)
			str = "";
		return str;
	}

	/**
	 * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
	 * READ_PHONE_STATE}
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context) {
		String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
		if (str == null)
			str = "";
		return str;
	}

	public static String getLocalMacAddress(Context context) {
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
	}

	public static String getNetWorkName(Context context) {
		return getNetworkType(context).toLowerCase();
	}

	/**
	 * Returns the alphabetic name of current registered operator.
	 * <p>
	 * Availability: Only when user is registered to a network. Result may be
	 * unreliable on CDMA networks (use {@link TelephonyManager#getPhoneType()}
	 * to determine if on a CDMA network).
	 */
	public static String getNetworkOperatorName(Activity activity) {
		return ((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
	}

	public static String getNetworkOperatorName(Context context) {
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
	}

	/**
	 * Returns the language code for this {@code Locale} or the empty string if
	 * no language was set.
	 */
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

	public static String getManufacturer() {
		return Build.MANUFACTURER;
	}

	public static String getProduct() {
		return Build.PRODUCT;
	}

	/**
	 * @param context
	 * @param unit
	 *            The unit to convert from. {@link TypedValue#TYPE_DIMENSION}.
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
		return (int) TypedValue.applyDimension(unit, value, resources.getDisplayMetrics());
	}

	public static String getResolution(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels + "x" + dm.heightPixels;
	}

	public static String getResolution(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
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
		NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE"))
			return networkInfo.getExtraInfo();
		return null;
	}

	public static String getCurrentApnProxy(Context context) {
		Cursor cur = null;
		try {
			Uri uri = Uri.parse("content://telephony/carriers/preferapn");
			cur = context.getContentResolver().query(uri, null, null, null, null);
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
		NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
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
			cur = context.getContentResolver().query(uri, null, null, null, null);
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
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
	}

	public static boolean isCmwap(Context context) {
		if ((!isConnectChinaMobile(context)) || (!isMobileType(context)))
			return false;
		String str = getCurrentApnProxy(context);
		if (str == null)
			return false;
		return (str.equals("10.0.0.172")) || (str.equals("010.000.000.172"));
	}

	/**
	 * China Mobile Network
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectChinaMobile(Context context) {
		// MCC+MNC (mobile country code + mobile network code)
		String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
		if (str != null)
			return (str.startsWith("46000")) || (str.startsWith("46002"));
		return false;
	}

	/**
	 * China Telecom Network
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectChinaTelecom(Context context) {
		String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
		if (str != null)
			return str.startsWith("46003");
		return false;
	}

	/**
	 * China Unicom Network
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectChinaUnicom(Context context) {
		String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
		if (str != null)
			return str.startsWith("46001");
		return false;
	}

	/**
	 * Current Network Type（Is Mobile）
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null)
			return false;
		return networkInfo.getTypeName().equalsIgnoreCase("mobile");
	}

	/**
	 * Network Access point Uniwap
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isUniwap(Context context) {
		if ((!isConnectChinaUnicom(context)) || (!isMobileType(context)))
			return false;
		String str = getCurrentApnProxy(context);
		if (str == null)
			return false;
		return (str.equals("10.0.0.172")) || (str.equals("010.000.000.172"));
	}

	public static void shotVibratePhone(Context context) {
		((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[] { 800L, 50L, 400L, 30L }, -1);
	}

	public static boolean isSdcardExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static long getAvailableExternalMemorySize() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			return statFs.getBlockSize() * statFs.getAvailableBlocks();
		}
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
			return -1L;
		}
		return 0L;
	}

	public static long getAvailableInternalMemorySize() {
		StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
		return statFs.getBlockSize() * statFs.getAvailableBlocks();
	}
}
