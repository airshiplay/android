package com.arishiplay.mobile.android.framework.util;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class Log {
	private String tag;

	public static class LogFactory {
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

	public Log(String tag) {
		this.tag = tag;
	}

	public void debug(String msg) {
		android.util.Log.d(tag, msg);
	}
}
