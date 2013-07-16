package com.airshiplay.mobile.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 * @变更记录 2013-7-16 上午9:43:09 lig
	 * 
	 */
	public static String format(Date date) {
		return getDateFormat().format(date);
	}
}
