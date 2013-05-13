package com.airshiplay.framework.client;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class FWConfig {
	public static final String WL_CLIENT_PROPS_NAME = "wlclient.properties";
	public static final String WL_X_VERSION_HEADER = "x-wl-app-version";
	public static final String ENABLE_SETTINGS = "enableSettings";
	public static final String WL_APP_VERSION = "wlAppVersion";
	public static final String WL_APP_ID = "wlAppId";
	public static final String WL_SERVER_HOST = "fwServerHost";
	public static final String WL_SERVER_PROTOCOL = "fwServerProtocol";
	public static final String WL_SERVER_PORT = "fwServerPort";
	public static final String WL_SERVER_CONTEXT = "fwServerContext";
	private static final String WL_ENVIRONMENT = "fwEnvironment";
	public static final String WL_GCM_SENDER = "GcmSenderId";
	public static final String WL_WEB_RESOURCES_UNPACKD_SIZE = "webResourcesSize";
	private static final String IGNORED_FILE_EXTENSIONS = "ignoredFileExtensions";
	private static final String WL_PREFS = "WLPrefs";
	private static final String WL_TEST_WEB_RESOURCES_CHECKSUM = "testWebResourcesChecksum";
	private Properties wlProperties = new Properties();
	private SharedPreferences prefs = null;

	public FWConfig(Context context) {
		try {
			this.wlProperties.load(context.getAssets().open("wlclient.properties"));
			this.prefs = context.getSharedPreferences(WL_PREFS, 0);
		} catch (IOException e) {
			throw new RuntimeException("WLConfig(): Can't load wlclient.properties file");
		}
	}

	public FWConfig(Activity context) {
		this(context.getApplication());
	}

	public URL getAppURL() {
		try {
			return new URL(getRootURL() + "/apps/services/api/" + getAppId() + "/" + getWLEnvironment() + "/");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not parse URL; check assets/wlclient.properties. " + e.getMessage(), e);
		}
	}

	public String getAppId() {
		return getPropertyOrPref("wlAppId", "appIdPref");
	}

	public String getWLEnvironment() {
		String result = this.wlProperties.getProperty(WL_ENVIRONMENT, null);
		if (result == null) {
			result = "android";
		}
		return result;
	}

	public String getApplicationVersion() {
		return getPropertyOrPref("wlAppVersion", "appVersionPref");
	}

	public String getDefaultRootUrl() {
		String port = ":" + getPort();
		String context = (FWUtils.isStringEmpty(getContext())) || (getContext().equals("/")) ? "" : getContext();
		String url = String.format("%s://%s%s%s", new Object[] { getProtocol(), getHost(), port, context });
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	public String getRootURL() {
		String result = this.prefs.getString("WLServerURL", null);
		if (result == null) {
			result = getDefaultRootUrl();
		}
		return result;
	}

	public String[] getMediaExtensions() {
		String mediaExtentionStr = (String) this.wlProperties.get(IGNORED_FILE_EXTENSIONS);
		if (mediaExtentionStr != null) {
			String[] mediaExtentionArr = mediaExtentionStr.replaceAll(" ", "").split(",");
			return mediaExtentionArr;
		}
		return null;
	}

	public String getGCMSender() {
		String wlGCMSender = this.wlProperties.getProperty("GcmSenderId");
		if (wlGCMSender != null) {
			return wlGCMSender.trim();
		}
		return wlGCMSender;
	}

	public String getMainFilePath() {
		return getAppId() + ".html";
	}

	public String getSettingsFlag() {
		return (String) this.wlProperties.get("enableSettings");
	}

	public String getTestWebResourcesChecksumFlag() {
		return (String) this.wlProperties.get(WL_TEST_WEB_RESOURCES_CHECKSUM);
	}

	public String getProtocol() {
		return (String) this.wlProperties.get("wlServerProtocol");
	}

	public String getHost() {
		return (String) this.wlProperties.get("wlServerHost");
	}

	public String getPort() {
		return (String) this.wlProperties.get("wlServerPort");
	}

	public String getContext() {
		return (String) this.wlProperties.get("wlServerContext");
	}

	private String getPropertyOrPref(String propertyKey, String prefKey) {
		String result = this.prefs.getString(prefKey, null);
		if (result == null) {
			result = (String) this.wlProperties.get(propertyKey);
		}
		return result;
	}

	public String getWebResourcesUnpackedSize() {
		return (String) this.wlProperties.get("webResourcesSize");
	}
}