package com.airshiplay.framework.skin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.airshiplay.mobile.util.AppUtil;

/**
 * @author airshiplay
 * @Create 2013-6-16
 * @version 1.0
 * @since 1.0
 */
public class AppInfo {
	Resources resouces;
	int versionCode;
	String versionName;
	String packageName;
	String fileName;
	String filePath;
	String appName;
	Drawable appIcon;

	public AppInfo() {
	}

	public static AppInfo getInstance(Context context, PackageInfo pkgInfo) {
		AppInfo appInfo = new AppInfo();
		appInfo.packageName = pkgInfo.packageName;
		appInfo.versionCode = pkgInfo.versionCode;
		appInfo.versionName = pkgInfo.versionName;
		appInfo.appIcon = pkgInfo.applicationInfo.loadIcon(context
				.getPackageManager());
		appInfo.appName = pkgInfo.applicationInfo.loadLabel(
				context.getPackageManager()).toString();
		return appInfo;
	}

	public static Resources getResource(Context context, String archiveFilePath) {
		if (!archiveFilePath.toLowerCase().endsWith(".apk")
				|| !( new File(archiveFilePath)).exists())
			return null;
		String pkgParserStr = "android.content.pm.PackageParser";
		String assetManagerStr = "android.content.res.AssetManager";
		try {
			// ////////////////
			Class<?> pkgParserCls = Class.forName(pkgParserStr);
			Constructor<?> pkgParserCon = (pkgParserCls)
					.getConstructor(new Class[] { String.class });
			Object pkgParser = (pkgParserCon)
					.newInstance(new Object[] { archiveFilePath });
			DisplayMetrics dm = new DisplayMetrics();
			dm.setToDefaults();
			Method parsePkgzMethod = pkgParserCls.getDeclaredMethod(
					"parsePackage", new Class[] { File.class, String.class,
							DisplayMetrics.class, Integer.TYPE });
			Object resultPackage = parsePkgzMethod.invoke(pkgParser, new File(
					archiveFilePath), archiveFilePath, dm, Integer.valueOf(0));
			Field applicationInfoField = resultPackage.getClass()
					.getDeclaredField("applicationInfo");
			ApplicationInfo applicationInfo = (ApplicationInfo) applicationInfoField
					.get(resultPackage);
			// /////////////////
			Class<?> assetManagerCls = Class.forName(assetManagerStr);
			Object assertManager = assetManagerCls.newInstance();
			Method addAssetPathMethod = assetManagerCls.getDeclaredMethod(
					"addAssetPath", new Class[] { String.class });
			addAssetPathMethod.invoke(assertManager,
					new Object[] { archiveFilePath });
			Resources localResources = context.getResources();
			Constructor<Resources> resourcesConstructor = Resources.class
					.getConstructor(new Class[] { assertManager.getClass(),
							localResources.getDisplayMetrics().getClass(),
							localResources.getConfiguration().getClass() });
			Resources res=resourcesConstructor.newInstance(new Object[] {
					assertManager, localResources.getDisplayMetrics(),
					localResources.getConfiguration() });

			return res;
		} catch (Exception e) {
		}
		return null;
	}

	public static AppInfo getInstance(Context context, String archiveFilePath) {
		File apkFile = null;
		if (!archiveFilePath.toLowerCase().endsWith(".apk")
				|| !(apkFile = new File(archiveFilePath)).exists())
			return null;
		PackageInfo pkgInfo = AppUtil.getPackageInfo(context, archiveFilePath);
		pkgInfo.applicationInfo.publicSourceDir = archiveFilePath;
		pkgInfo.applicationInfo.sourceDir = archiveFilePath;
		AppInfo apkInfo = new AppInfo();
		apkInfo.filePath = apkFile.getParent();
		apkInfo.fileName = apkFile.getName();
		return getInstance(context, pkgInfo);
	}
}
