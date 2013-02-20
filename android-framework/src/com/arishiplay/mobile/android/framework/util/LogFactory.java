/**
 * 
 */
package com.arishiplay.mobile.android.framework.util;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author airshiplay
 * @Create Date 2013-2-14
 * @version 1.0
 * @since 1.0
 */
public class LogFactory {
	@SuppressWarnings("rawtypes")
	public static Map<Class, WeakReference<Log>> map = new HashMap<Class, WeakReference<Log>>();

	public static Log getLog(@SuppressWarnings("rawtypes") Class cls) {
		return getLog(cls, cls.getSimpleName());
	}

	public static Log getLog(@SuppressWarnings("rawtypes") Class cls, String tag) {
		WeakReference<Log> weak = map.get(cls);
		Log log = null;
		if (weak == null) {
			log = new Log(tag);
			map.put(cls, new WeakReference<Log>(log));
		} else {
			log = weak.get();
			if (log == null) {
				log = new Log(tag);
				map.put(cls, new WeakReference<Log>(log));
			}
		}
		return log;
	}
}
