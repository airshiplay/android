package com.airshiplay.framework.client.api;

public abstract interface WLResponseListener {
	public abstract void onSuccess(WLResponse paramWLResponse);

	public abstract void onFailure(WLFailResponse paramWLFailResponse);
}
