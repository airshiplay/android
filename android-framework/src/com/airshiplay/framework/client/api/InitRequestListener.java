package com.airshiplay.framework.client.api;

import com.airshiplay.framework.client.FWRequestListener;

class InitRequestListener implements FWRequestListener {
	public void onFailure(WLFailResponse failResponse) {
		failResponse.getOptions().getResponseListener().onFailure(failResponse);
	}

	public void onSuccess(WLResponse response) {
		WLClient.getInstance().setInitialized(true);
		response.getOptions().getResponseListener().onSuccess(response);
	}
}
