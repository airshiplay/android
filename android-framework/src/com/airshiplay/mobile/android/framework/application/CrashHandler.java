/**
 * 
 */
package com.airshiplay.mobile.android.framework.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import com.airshiplay.mobile.android.framework.util.AppUtil;

/**
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
	private static final String CRASH_REPORTER_EXTENSION = ".cr";
	public static final boolean DEBUG = true;
	private static CrashHandler INSTANCE;
	private static final String STACK_TRACE = "STACK_TRACE";
	public static final String TAG = "CrashHandler";
	private static final String VERSION_CODE = "versionCode";
	private static final String VERSION_NAME = "versionName";
	private Context mContext;
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private Properties mDeviceCrashInfo = new Properties();

	private String[] getCrashReportFiles(Context context) {
		return context.getFilesDir().list(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(CRASH_REPORTER_EXTENSION);
			}
		});
	}

	public static CrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}

	private boolean handleException(final Throwable paramThrowable) {
		if (paramThrowable == null)
			return true;
		StringWriter localStringWriter = new StringWriter();
		PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
		final StringBuffer localStringBuffer = localStringWriter.getBuffer();
		paramThrowable.printStackTrace(localPrintWriter);
		new Thread() {
			public void run() {
				// new
				// FeedBackServiceImpl().feedBack(CrashHandler.this.mContext,
				// paramThrowable.toString(),
				// localStringBuffer.toString(), 3, "1.0.4");
				Process.killProcess(AppUtil.getPid(CrashHandler.this.mContext,
						AppUtil.getPackageName(CrashHandler.this.mContext)));
				Process.killProcess(Process.myPid());
			}
		}.start();
		return false;
	}

	private void postReport(File paramFile) {
	}

	private String saveCrashInfoToFile(Throwable paramThrowable) {
		StringWriter localStringWriter = new StringWriter();
		PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
		paramThrowable.printStackTrace(localPrintWriter);
		for (Throwable localThrowable = paramThrowable.getCause(); localThrowable != null; localThrowable = localThrowable
				.getCause())
			localThrowable.printStackTrace(localPrintWriter);
		String str1 = localStringWriter.toString();
		localPrintWriter.close();
		this.mDeviceCrashInfo.put(STACK_TRACE, str1);
		try {
			long l = System.currentTimeMillis();
			String str2 = "crash-" + l + CRASH_REPORTER_EXTENSION;
			FileOutputStream localFileOutputStream = this.mContext
					.openFileOutput(str2, 0);
			this.mDeviceCrashInfo.store(localFileOutputStream, "");
			localFileOutputStream.flush();
			localFileOutputStream.close();
			return str2;
		} catch (Exception localException) {
			Log.e(TAG, "an error occured while writing report file...",
					localException);
		}
		return null;
	}

	private void sendCrashReportsToServer(Context context) {
		String[] files = getCrashReportFiles(context);
		if ((files != null) && (files.length > 0)) {
			TreeSet<String> treeSet = new TreeSet<String>();
			treeSet.addAll(Arrays.asList(files));
			Iterator<String> itr = treeSet.iterator();
			while (itr.hasNext()) {
				String str = (String) itr.next();
				File file = new File(context.getFilesDir(), str);
				postReport(file);
				file.delete();
			}
		}
	}

	public void collectCrashDeviceInfo(Context context) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 1);
			if (packageInfo != null) {
				if (packageInfo.versionName == null) {
					mDeviceCrashInfo.put(VERSION_NAME, "not set");
				} else {
					mDeviceCrashInfo.put(VERSION_NAME, packageInfo.versionName);
					mDeviceCrashInfo.put(VERSION_CODE,
							Integer.valueOf(packageInfo.versionCode));
				}
			}
			Field[] fields = Build.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					Field field = fields[0];
					field.setAccessible(true);
					this.mDeviceCrashInfo.put(field.getName(), field.get(null));
					Log.d(TAG, field.getName() + " : " + field.get(null));
				} catch (Exception e) {
					Log.e(TAG, "Error while collect crash info", e);
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);

		}
	}

	public void init(Context context) {
		this.mContext = context;
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(this.mContext);
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		if ((!handleException(ex)) && (this.mDefaultHandler != null)) {
			this.mDefaultHandler.uncaughtException(thread, ex);
			return;
		}
		try {
			Thread.sleep(3000L);
			System.exit(10);
			return;
		} catch (InterruptedException e) {
			Log.e(TAG, "Error : ", e);
		}
	}
}