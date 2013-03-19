/**
 * 
 */
package com.airshiplay.framework.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

/**
 * @author airshiplay
 * @Create Date 2013-3-10
 * @version 1.0
 * @since 1.0
 */
public class SharedPreferencesDB {
	private static SharedPreferencesDB sharedPreferencesDB = null;
	private Context mContext;
	private SharedPreferences prefs = null;

	protected SharedPreferencesDB(Context context) {
		this.mContext = context;
		try {
			this.prefs = context.createPackageContext(context.getPackageName(),
					Context.CONTEXT_IGNORE_SECURITY).getSharedPreferences(
					"mc_forum.prefs",
					Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
			return;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static SharedPreferencesDB getInstance(Context context) {
		if (sharedPreferencesDB == null)
			sharedPreferencesDB = new SharedPreferencesDB(context);
		return sharedPreferencesDB;
	}

	public static void newInstance(Context context) {
		sharedPreferencesDB = new SharedPreferencesDB(context);
	}
}
