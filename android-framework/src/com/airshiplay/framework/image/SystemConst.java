package com.airshiplay.framework.image;

import java.io.File;

import android.os.Environment;

public class SystemConst {

	public static String IMAGE_DIR = Environment
			.getExternalStoragePublicDirectory("dd").getAbsolutePath()
			+ File.separator;
	static {
		File file = Environment.getExternalStoragePublicDirectory("dd");
		if (!file.exists())
			file.mkdirs();
	}
}
