package com.airshiplay.framework.client.api.challengehandler;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;

import com.airshiplay.framework.client.api.WLClient;

public abstract class BaseDeviceAuthChallengeHandler extends WLChallengeHandler {
	public BaseDeviceAuthChallengeHandler(String realm) {
		super(realm);
	}

	protected void getDeviceAuthDataAsync(String token) throws Exception, JSONException {
		JSONObject payload = new JSONObject();
		JSONObject appData = new JSONObject();
		JSONObject deviceData = new JSONObject();

		WLClient client = WLClient.getInstance();

		appData.put("id", client.getConfig().getAppId());
		appData.put("version", client.getConfig().getApplicationVersion());


		String android_id = "uuid";

		deviceData.put("id", android_id);
		deviceData.put("os", Build.VERSION.RELEASE);
		deviceData.put("model", Build.MODEL);
		deviceData.put("environment", "Android");

		payload.put("app", appData);

		payload.put("device", deviceData);

		payload.put("token", token);

		onDeviceAuthDataReady(payload);
	}

	protected abstract void onDeviceAuthDataReady(JSONObject paramJSONObject) throws JSONException;
}