package com.airshiplay.framework.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class Devices {
	public static float density;
	public static int[] size;

	public static void init(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		density = dm.density;
		size = new int[2];
		size[0] = dm.widthPixels;
		size[1] = dm.heightPixels;
	}
}
