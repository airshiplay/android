package com.airshiplay.mobile.log;

import java.io.FileNotFoundException;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.airshiplay.mobile.log.impl.AndroidLoggerFactory;
import com.airshiplay.mobile.log.impl.FileLoggerFactory;
import com.airshiplay.mobile.log.impl.NOPLoggerFactory;

/**
 * 
 * 使用前 必须在{@link Application#onCreate()}中初始化，
 * {@link LoggerFactory#init(Context)}
 * 
 * <pre>
 * private Logger log = LoggerFactory.getLogger(MainActivity.class);
 * log.debug(&quot;msg&quot;);
 * </pre>
 * 
 * 注意：Application中使用log，必须在 初始化 {@link LoggerFactory#init(Context)}
 * 后，对成员变量log的初始化，即 log={@link LoggerFactory#getLogger(Class)}
 * 
 * @author airshiplay
 * @version 1.0
 * @since 1.0 2013-4-1
 */
public class LoggerFactory {
	static Context mContext;
	static final int UNINITIALIZED = 0;
	static final int SUCCESSFUL_INITILIZATION = 3;
	static final int NOP_FALLBACK_INITILIZATION = 4;

	static int INITIALIZATION_STATE = UNINITIALIZED;
	static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
	static ILoggerFactory ANDROID_FACTORY;

	public static void init(Context context) {
		mContext = context.getApplicationContext();
	}

	/**
	 * Return a logger named according to the name parameter using the
	 * statically bound {@link ILoggerFactory} instance.
	 * 
	 * @param name
	 *            The name of the logger.
	 * @return logger
	 */
	public static Logger getLogger(String name) {
		ILoggerFactory iLoggerFactory = getILoggerFactory();
		return iLoggerFactory.getLogger(name);
	}

	/**
	 * Return a logger named corresponding to the class passed as parameter,
	 * using the statically bound {@link ILoggerFactory} instance.
	 * 
	 * @param clazz
	 *            the returned logger will be named after clazz
	 * @return logger
	 */
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class clazz) {
		return getLogger(clazz.getName());
	}

	private static ILoggerFactory getILoggerFactory() {
		if (INITIALIZATION_STATE == UNINITIALIZED && ANDROID_FACTORY == null) {
			performInitialization();
		}
		switch (INITIALIZATION_STATE) {
		case SUCCESSFUL_INITILIZATION:
			return ANDROID_FACTORY;
		}
		return NOP_FALLBACK_FACTORY;
	}

	private final static void performInitialization() {
		try {
			boolean debug = (mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
			if (debug) {
				getAnroidLoggerFactory();
				INITIALIZATION_STATE = SUCCESSFUL_INITILIZATION;
				Log.i("LoggerFactory", "debug log model");
				return;
			} else {
				int logMode = mContext.createPackageContext("com.airshiplay.framework.log", Context.CONTEXT_IGNORE_SECURITY)
						.getSharedPreferences("framework", Context.MODE_MULTI_PROCESS + Context.MODE_WORLD_READABLE).getInt("logmode", 0);
				switch (logMode) {
				case 0:
					INITIALIZATION_STATE = NOP_FALLBACK_INITILIZATION;
					Log.i("LoggerFactory", "no log model--");
					return;
				default:
					getFileLoggerFactory();
					INITIALIZATION_STATE = SUCCESSFUL_INITILIZATION;
					Log.i("LoggerFactory", "file log model");
					return;
				}
			}

		} catch (Exception e) {
			INITIALIZATION_STATE = NOP_FALLBACK_INITILIZATION;
			Log.i("LoggerFactory", "no log model");
			return;
		}

	}

	private static synchronized ILoggerFactory getAnroidLoggerFactory() {
		if (ANDROID_FACTORY == null)
			ANDROID_FACTORY = new AndroidLoggerFactory();
		return ANDROID_FACTORY;
	}

	private static synchronized ILoggerFactory getFileLoggerFactory() throws FileNotFoundException {
		if (ANDROID_FACTORY == null)
			ANDROID_FACTORY = new FileLoggerFactory(mContext);
		return ANDROID_FACTORY;
	}
}
