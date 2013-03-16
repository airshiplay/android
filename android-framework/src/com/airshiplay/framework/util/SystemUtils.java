package com.airshiplay.framework.util;

public class SystemUtils {
	public static int dp2px(float dp) {
		return (int) (0.5f + Devices.density * dp);
	}
}
