package com.airshiplay.mobile.util;

import java.util.Map;
import java.util.Set;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;

public class SharedPreferencesUtil {
	private static final String name = "framework.prefs";
	private SharedPreferences prefs;
	private static SharedPreferencesUtil instance;
	@SuppressWarnings("unused")
	private Context mContext;

	/**
	 * @param context
	 * @param name
	 */
	@SuppressLint({ "WorldWriteableFiles", "WorldReadableFiles" })
	public SharedPreferencesUtil(Context context, String name) {
		mContext = context;
		try {
			prefs = context.createPackageContext(context.getPackageName(),
					Context.CONTEXT_IGNORE_SECURITY).getSharedPreferences(
					StringUtils.isEmpty(name) ? SharedPreferencesUtil.name
							: name,
					Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE
							+ Context.MODE_MULTI_PROCESS);
			return;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		prefs = context.getSharedPreferences(
				StringUtils.isEmpty(name) ? SharedPreferencesUtil.name : name,
				Context.MODE_WORLD_WRITEABLE + Context.MODE_MULTI_PROCESS);
	}

	public static SharedPreferencesUtil getInstance(Context context) {
		return getInstance(context, name);
	}

	public static SharedPreferencesUtil getInstance(Context context, String name) {
		if (instance == null) {
			instance = new SharedPreferencesUtil(context, name);
		}
		return instance;
	}

	public static SharedPreferencesUtil getNewInstance(Context context) {
		return getNewInstance(context, name);
	}

	/**
	 * @param context
	 * @param name
	 * @return
	 */
	public static SharedPreferencesUtil getNewInstance(Context context,
			String name) {
		instance = new SharedPreferencesUtil(context, name);
		return instance;
	}

	public String getString(String key) {
		return prefs.getString(key, null);
	}

	public Map<String, ?> getAll() {
		return prefs.getAll();
	}

	public String getString(String key, String defValue) {
		return prefs.getString(key, defValue);
	}

	public Set<String> getStringSet(String key, Set<String> defValues) {
		return prefs.getStringSet(key, defValues);
	}

	public int getInt(String key, int defValue) {
		return prefs.getInt(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return prefs.getLong(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return prefs.getFloat(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return prefs.getBoolean(key, defValue);
	}

	public boolean contains(String key) {
		return prefs.contains(key);
	}

	private Editor edit() {
		return prefs.edit();
	}

	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		prefs.registerOnSharedPreferenceChangeListener(listener);
	}

	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
	}

	public boolean putString(String key, String value) {
		return edit().putString(key, value).commit();
	}

	public SharedPreferencesUtil putStringl(String key, String value) {
		putString(key, value);
		return instance;
	}

	public boolean putStringSet(String key, Set<String> values) {
		return edit().putStringSet(key, values).commit();
	}

	public SharedPreferencesUtil putStringSetl(String key, Set<String> values) {
		putStringSet(key, values);
		return instance;
	}

	public boolean putInt(String key, int value) {
		return edit().putInt(key, value).commit();
	}

	public SharedPreferencesUtil putIntl(String key, int value) {
		putInt(key, value);
		return instance;
	}

	public boolean putLong(String key, long value) {
		return edit().putLong(key, value).commit();
	}

	public SharedPreferencesUtil putLongl(String key, long value) {
		putLong(key, value);
		return instance;
	}

	public boolean putFloat(String key, float value) {
		return edit().putFloat(key, value).commit();
	}

	public SharedPreferencesUtil putFloatl(String key, float value) {
		putFloat(key, value);
		return instance;
	}

	public boolean putBoolean(String key, boolean value) {
		return edit().putBoolean(key, value).commit();
	}

	public SharedPreferencesUtil putBooleanl(String key, boolean value) {
		putBoolean(key, value);
		return instance;
	}

	public boolean remove(String key) {
		return edit().remove(key).commit();
	}

	public boolean clear() {
		return edit().clear().commit();
	}

	public void apply() {
		edit().apply();
	}

	public static boolean getBoolean(Context mCtx, String key, boolean defValue) {
		return SharedPreferencesUtil.getInstance(mCtx)
				.getBoolean(key, defValue);
	}
}
