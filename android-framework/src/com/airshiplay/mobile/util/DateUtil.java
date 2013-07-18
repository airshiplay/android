package com.airshiplay.mobile.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return 返回yyyy-MM-dd HH:mm:ss格式时间
	 */
	public static String format(Date date) {
		return getDateFormat().format(date);
	}
}
