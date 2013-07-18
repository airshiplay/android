package com.airshiplay.mobile.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * @author airshiplay
 * @Create 2013-7-14
 * @version 1.0
 * @since 1.0
 */
public class SystemUtil {
	/**
	 * 当SK卡存在时，返回/sdcard/framework路径；当SK卡不存在时，返回应用的数据存储路径
	 * 
	 * @param context
	 * @return
	 */
	public static File getBaseStorePath(Context context) {
		return getBaseStorePath(context, "framework");
	}

	public static File getBaseStorePath(Context context, String name) {
		File file = getExternalBaseStorePath(name);
		if (file != null)
			return file;
		return context.getDir(name, Context.MODE_PRIVATE);
	}

	/**
	 * SD卡 存在时，返回/sdcard/framework存储路径
	 * 
	 * @return
	 */
	public static File getExternalBaseStorePath() {
		return getExternalBaseStorePath("framework");
	}

	public static File getExternalBaseStorePath(String name) {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		if (mExternalStorageAvailable && mExternalStorageWriteable) {
			File f = Environment.getExternalStoragePublicDirectory(name);
			if (!f.exists())
				f.mkdirs();
			return f;
		}
		return null;
	}
}
