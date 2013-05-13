package com.airshiplay.framework.client.challengehandler;

import org.json.JSONObject;

import com.airshiplay.framework.client.api.challengehandler.BaseProvisioningChallengeHandler;

public class AutoProvisioningChallengeHandler extends BaseProvisioningChallengeHandler {
	public AutoProvisioningChallengeHandler(String realm) {
		super(realm);
	}

	public void handleChallenge(JSONObject challenge) {
	}

	protected JSONObject createJsonCsr(String provisioningEntity, String realm, String customPayload) {
		return null;
	}

	public void handleFailure(JSONObject error) {
	}

	public void handleSuccess(JSONObject identity) {
	}
}