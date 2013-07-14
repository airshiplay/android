package com.airshiplay.mobile.client.api;

import com.airshiplay.mobile.client.MobileRequestListener;

/**
 * @author airshiplay
 * @Create 2013-7-14
 * @version 1.0
 * @since 1.0
 */
class InitRequestListener implements MobileRequestListener {

	@Override
	public void onSuccess(MobileResponse response) {
		response.getOptions().getResponseListener().onSuccess(response);
	}

	@Override
	public void onFailure(MobileFailResponse response) {
		response.getOptions().getResponseListener().onFailure(response);
	}

}
