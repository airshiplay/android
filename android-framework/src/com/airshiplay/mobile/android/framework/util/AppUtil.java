/**
 * 
 */
package com.airshiplay.mobile.android.framework.util;

import java.util.Iterator;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public class AppUtil {
	public static Drawable getAppIcon(Context context) {
		return context.getApplicationInfo().loadIcon(
				context.getPackageManager());
	}

	public static String getAppName(Context context) {
		return context.getApplicationInfo()
				.loadLabel(context.getPackageManager()).toString();
	}

	public static String getPackageName(Context context) {
		return context.getPackageName();
	}

	public static int getPid(Context context, String processName) {
		Iterator<ActivityManager.RunningAppProcessInfo> itr = ((ActivityManager) context
				.getSystemService("activity")).getRunningAppProcesses()
				.iterator();
		ActivityManager.RunningAppProcessInfo runningAppProcessInfo;
		do {
			if (!itr.hasNext())
				return 0;
			runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) itr
					.next();
		} while (!processName.equals(runningAppProcessInfo.processName));
		return runningAppProcessInfo.pid;
	}

	public static int getVersionCode(Context context) {
		try {
			int versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 16384).versionCode;
			return versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getVersionName(Context context) {
		try {
			String versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 16384).versionName;
			return versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
