package com.airshiplay.mobile.client;

import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;

import android.os.Build;

import com.airshiplay.mobile.client.api.MobileFailResponse;
import com.airshiplay.mobile.client.api.MobileHttpError;
import com.airshiplay.mobile.client.api.MobileResponse;

/**
 * @author airshiplay
 * @Create 2013-7-14
 * @version 1.0
 * @since 1.0
 */
class InternalRequestSender implements Runnable {
	MobileRequest request;

	protected InternalRequestSender(MobileRequest request) {
		this.request = request;
	}

	public void run() {
		MobileResponse response = null;
		try {
			HttpClient httpClient = HttpClientFactory.getInstance(this.request
					.getConfig());
			setUserAgentHeader(httpClient);
			setConnectionTimeout(httpClient);
			MobileCookieManager.addCookies(this.request);
			HttpResponse httpResponse = httpClient.execute(
					this.request.getPostRequest(),
					this.request.getHttpContext());

			response = new MobileResponse(httpResponse);
			response.setOptions(this.request.getOptions());
		} catch (SocketTimeoutException e) {
			this.request.getRequestListener().onFailure(
					new MobileFailResponse(MobileHttpError.EC_SocketTimeout, e
							.getMessage(), this.request.getOptions()));
			return;
		} catch (ConnectTimeoutException e) {
			this.request.getRequestListener().onFailure(
					new MobileFailResponse(MobileHttpError.EC_ConnectTimeout, e
							.getMessage(), this.request.getOptions()));
			return;
		} catch (Exception e) {
			this.request.getRequestListener().onFailure(
					new MobileFailResponse(MobileHttpError.EC_Exception, e
							.getMessage(), this.request.getOptions()));
			return;
		}
		this.request.requestFinished(response);
	}

	/**
	 * @param httpClient
	 */
	private void setConnectionTimeout(HttpClient httpClient) {
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), this.request
				.getOptions().getTimeout());
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				this.request.getOptions().getTimeout());
	}

	/**
	 * @param httpClient
	 */
	private void setUserAgentHeader(HttpClient httpClient) {
		String userAgent = (String) httpClient.getParams().getParameter(
				"http.useragent");
		@SuppressWarnings("deprecation")
		String strWLAgent = " MobileNativeAPI(" + Build.DEVICE + "; "
				+ Build.DISPLAY + "; " + Build.MODEL + "; SDK "
				+ Build.VERSION.SDK + "; Android " + Build.VERSION.RELEASE
				+ ")";

		if ((userAgent != null) && (userAgent.indexOf("MobileNativeAPI(") < 0))
			httpClient.getParams().setParameter("http.useragent",
					userAgent + strWLAgent);
		else if (userAgent == null)
			httpClient.getParams().setParameter("http.useragent", strWLAgent);
	}
}
