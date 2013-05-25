package com.airshiplay.framework.client.api.challengehandler;

import org.json.JSONObject;

public abstract class WLChallengeHandler extends BaseChallengeHandler<JSONObject> {
	public WLChallengeHandler(String realm) {
		super(realm);
	}

	public void submitChallengeAnswer(Object answer) {
		if (answer == null) {
			this.activeRequest.removeExpectedAnswer(getRealm());
		} else {
			this.activeRequest.submitAnswer(getRealm(), answer);
		}

		this.activeRequest = null;
	}

	public abstract void handleSuccess(JSONObject paramJSONObject);

	public abstract void handleFailure(JSONObject paramJSONObject);
}