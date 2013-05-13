package com.airshiplay.framework.util;

import java.util.Map;
import java.util.Set;

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

	public SharedPreferencesUtil(Context context) {
		mContext = context;
		try {
			prefs = context.createPackageContext(context.getPackageName(), Context.CONTEXT_IGNORE_SECURITY)
					.getSharedPreferences(name,
					Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE+Context.MODE_MULTI_PROCESS);
			return;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		prefs = context.getSharedPreferences(name, Context.MODE_WORLD_WRITEABLE+Context.MODE_MULTI_PROCESS);
	}

	public static SharedPreferencesUtil getInstance(Context context) {
		if (instance == null) {
			instance = new SharedPreferencesUtil(context);
		}
		return instance;
	}

	public static SharedPreferencesUtil getNewInstance(Context context) {
		instance = new SharedPreferencesUtil(context);
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

	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		prefs.registerOnSharedPreferenceChangeListener(listener);
	}

	public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
	}

	public boolean putString(String key, String value) {
		return edit().putString(key, value).commit();
	}

	public boolean putStringSet(String key, Set<String> values) {
		return edit().putStringSet(key, values).commit();
	}

	public boolean putInt(String key, int value) {
		return edit().putInt(key, value).commit();
	}

	public boolean putLong(String key, long value) {
		return edit().putLong(key, value).commit();
	}

	public boolean putFloat(String key, float value) {
		return edit().putFloat(key, value).commit();
	}

	public boolean putBoolean(String key, boolean value) {
		return edit().putBoolean(key, value).commit();
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
}
