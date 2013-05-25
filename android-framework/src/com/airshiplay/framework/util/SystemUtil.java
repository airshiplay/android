package com.airshiplay.framework.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;


public class SystemUtil {
	public static File getBaseStorePath(Context context) {
		File file = getExternalBaseStorePath();
		if (file != null)
			return file;
		return context.getDir("framework", Context.MODE_PRIVATE);
	}

	public static File getExternalBaseStorePath() {
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
			File f = Environment.getExternalStoragePublicDirectory("framework");
			if (!f.exists())
				f.mkdirs();
			return f;
		}
		return null;
	}
}
