/**
 * 
 */
package com.airshiplay.mobile.util;

import java.io.File;
import java.util.Iterator;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

/**
 * android应用相关工具类
 * 
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public class AppUtil {
	/**
	 * 应用图标
	 * 
	 * @param context
	 * @return
	 */
	public static Drawable getAppIcon(Context context) {
		return context.getApplicationInfo().loadIcon(context.getPackageManager());
	}

	/**
	 * 应用名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
	}

	/**
	 * 应用包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		return context.getPackageName();
	}

	/**
	 * 运行中的应用进程ID PID
	 * 
	 * @param context
	 * @param processName
	 *            进程名
	 * @return
	 */
	public static int getPid(Context context, String processName) {
		Iterator<ActivityManager.RunningAppProcessInfo> itr = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses().iterator();
		ActivityManager.RunningAppProcessInfo runningAppProcessInfo;
		do {
			if (!itr.hasNext())
				return 0;
			runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) itr.next();
		} while (!processName.equals(runningAppProcessInfo.processName));
		return runningAppProcessInfo.pid;
	}

	/**
	 * 应用版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 16384).versionCode;
			return versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 应用版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 16384).versionName;
			return versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 未安装APK信息
	 * 
	 * @param context
	 * @param archiveFilePath
	 *            apk路径
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context, String archiveFilePath) {
		PackageInfo pkgInfo = context.getPackageManager().getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
		if (pkgInfo.applicationInfo != null) {
			pkgInfo.applicationInfo.sourceDir = archiveFilePath;
			pkgInfo.applicationInfo.publicSourceDir = archiveFilePath;
		}
		return pkgInfo;
	}

	/**
	 * 是否为系统应用
	 * 
	 * @param context
	 * @param packageName
	 *            应用包名
	 * @return
	 */
	public static boolean isSystemApplication(Context context, String packageName) {
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo packageInfo = manager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
			if (new File("/data/app/" + packageInfo.packageName + ".apk").exists()) {
				return true;
			}
			if (packageInfo.versionName != null && packageInfo.applicationInfo.uid > 10000) {
				return true;
			}
			if ((packageInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) {
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
