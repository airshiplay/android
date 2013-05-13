package com.airshiplay.framework.client.api.challengehandler;

import org.json.JSONObject;

public abstract class BaseProvisioningChallengeHandler extends BaseDeviceAuthChallengeHandler {
	public BaseProvisioningChallengeHandler(String realm) {
		super(realm);
	}

	protected abstract JSONObject createJsonCsr(String paramString1, String paramString2, String paramString3);

	protected void submitCsr(JSONObject csr) {
	}

	protected boolean isCertificateChallengeResponse(JSONObject challenge) {
		return false;
	}

	protected void onDeviceAuthDataReady(JSONObject data) {
	}
}
