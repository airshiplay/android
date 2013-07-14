package com.airshiplay.mobile.client;

import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;

import com.airshiplay.mobile.client.api.MobileClient;
import com.airshiplay.mobile.client.api.MobileFailResponse;
import com.airshiplay.mobile.client.api.MobileHttpError;
import com.airshiplay.mobile.client.api.MobileRequestOptions;
import com.airshiplay.mobile.client.api.MobileResponse;
import com.airshiplay.mobile.client.api.MobileResponseListener;

/**
 * @author airshiplay
 * @Create 2013-6-30
 * @version 1.0
 * @since 1.0
 */
class InternalCustomRequestSender implements Runnable {
	MobileResponseListener listener;
	int requestTimeoutInMilliseconds;
	HttpRequestBase httpRequest;

	protected InternalCustomRequestSender(HttpRequestBase httpRequest,
			int requestTimeoutInMilliseconds, MobileResponseListener listener) {
		this.httpRequest = httpRequest;
		this.requestTimeoutInMilliseconds = requestTimeoutInMilliseconds;
		this.listener = listener;
	}

	public void run() {
		MobileClient client = MobileClient.getInstance();
		HttpClient httpClient = AsynchronousRequestSender.getHttpClient();

		if (this.requestTimeoutInMilliseconds > 0) {
			HttpConnectionParams.setSoTimeout(httpClient.getParams(),
					this.requestTimeoutInMilliseconds);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					this.requestTimeoutInMilliseconds);
		}

		MobileResponse response = null;
		try {
			HttpResponse httpResponse = httpClient.execute(this.httpRequest,
					client.getHttpContext());
			response = new MobileResponse(httpResponse);
		} catch (SocketTimeoutException e) {
			this.listener.onFailure(new MobileFailResponse(
					MobileHttpError.EC_SocketTimeout, e.getMessage(),
					new MobileRequestOptions()));
			return;
		} catch (ConnectTimeoutException e) {
			this.listener.onFailure(new MobileFailResponse(
					MobileHttpError.EC_ConnectTimeout, e.getMessage(),
					new MobileRequestOptions()));
			return;
		} catch (Exception e) {
			this.listener.onFailure(new MobileFailResponse(
					MobileHttpError.EC_Exception, e.getMessage(),
					new MobileRequestOptions()));
			return;
		}

		this.listener.onSuccess(response);
	}
}
