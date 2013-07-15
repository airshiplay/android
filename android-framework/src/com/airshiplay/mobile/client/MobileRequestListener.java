package com.airshiplay.mobile.client;

import com.airshiplay.mobile.client.api.MobileFailResponse;
import com.airshiplay.mobile.client.api.MobileResponse;

/**
 * @author airshiplay
 * @Create 2013-7-14
 * @version 1.0
 * @since 1.0
 */
public abstract interface MobileRequestListener {
	public abstract void onSuccess(MobileResponse response);

	public abstract void onFailure(MobileFailResponse response);
}
