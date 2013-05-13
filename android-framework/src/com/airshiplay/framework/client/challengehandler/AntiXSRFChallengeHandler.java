package com.airshiplay.framework.client.challengehandler;

import org.json.JSONException;
import org.json.JSONObject;

import com.airshiplay.framework.client.FWUtils;
import com.airshiplay.framework.client.api.WLClient;
import com.airshiplay.framework.client.api.challengehandler.WLChallengeHandler;

public class AntiXSRFChallengeHandler extends WLChallengeHandler {
	private static final String PROTOCOL_ERROR_MESSAGE = "Application will exit because wrong JSON arrived when processing it from AntiXSRFChallengeHandler with ";

	public AntiXSRFChallengeHandler(String realm) {
		super(realm);
	}

	public void handleSuccess(JSONObject identity) {
	}

	public void handleFailure(JSONObject error) {
	}

	public void handleChallenge(JSONObject challenge) {
		String headerValue;
		try {
			headerValue = challenge.getString("WL-Instance-Id");
		} catch (JSONException e) {
			FWUtils.error("Application will exit because wrong JSON arrived when processing it from AntiXSRFChallengeHandler with " + e.getMessage(), e);
			throw new RuntimeException("Application will exit because wrong JSON arrived when processing it from AntiXSRFChallengeHandler with ");
		}

		WLClient.getInstance().addGlobalHeader("WL-Instance-Id", headerValue);

		submitChallengeAnswer(null);
	}
}