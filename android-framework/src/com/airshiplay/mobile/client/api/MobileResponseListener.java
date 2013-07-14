package com.airshiplay.mobile.client.api;



/**
 * @author airshiplay
 * @Create 2013-6-29
 * @version 1.0
 * @since 1.0
 */
public interface MobileResponseListener {
	public void onSuccess(MobileResponse response);

	public void onFailure(MobileFailResponse response);
}
