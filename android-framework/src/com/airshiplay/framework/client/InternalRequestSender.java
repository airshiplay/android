package com.airshiplay.framework.client;

import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;

import android.os.Build;

import com.airshiplay.framework.client.api.WLErrorCode;
import com.airshiplay.framework.client.api.WLFailResponse;
import com.airshiplay.framework.client.api.WLResponse;

class InternalRequestSender implements Runnable {
	FWRequest request;

	protected InternalRequestSender(FWRequest request) {
		this.request = request;
	}

	public void run() {
		FWUtils.debug("Sending request " + this.request.getPostRequest().getURI());
		WLResponse response = null;
		try {
			HttpClient httpClient = HttpClientFactory.getInstance(this.request.getConfig());
			setUserAgentHeader(httpClient);
			setConnectionTimeout(httpClient);
			FWCookieManager.addCookies(this.request);
			addInstanceAuthHeader();
			HttpResponse httpResponse = httpClient.execute(this.request.getPostRequest(), this.request.getHttpContext());

			response = new WLResponse(httpResponse);
			response.setOptions(this.request.getOptions());
		} catch (SocketTimeoutException e) {
			this.request.getRequestListener().onFailure(new WLFailResponse(WLErrorCode.REQUEST_TIMEOUT, WLErrorCode.REQUEST_TIMEOUT.getDescription(), this.request.getOptions()));
			return;
		} catch (ConnectTimeoutException e) {
			this.request.getRequestListener().onFailure(
					new WLFailResponse(WLErrorCode.UNRESPONSIVE_HOST, WLErrorCode.UNRESPONSIVE_HOST.getDescription(), this.request.getOptions()));
			return;
		} catch (Exception e) {
			this.request.getRequestListener().onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, WLErrorCode.UNEXPECTED_ERROR.getDescription(), this.request.getOptions()));
			return;
		}

		this.request.requestFinished(response);
	}

	private void setConnectionTimeout(HttpClient httpClient) {
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), this.request.getOptions().getTimeout());
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), this.request.getOptions().getTimeout());
	}

	private void setUserAgentHeader(HttpClient httpClient) {
		String userAgent = (String) httpClient.getParams().getParameter("http.useragent");
		String strWLAgent = " WLNativeAPI(" + Build.DEVICE + "; " + Build.DISPLAY + "; " + Build.MODEL + "; SDK " + Build.VERSION.SDK + "; Android " + Build.VERSION.RELEASE + ")";

		if ((userAgent != null) && (userAgent.indexOf("WLNativeAPI(") < 0))
			httpClient.getParams().setParameter("http.useragent", userAgent + strWLAgent);
		else if (userAgent == null)
			httpClient.getParams().setParameter("http.useragent", strWLAgent);
	}

	private void addInstanceAuthHeader() {
		if (!FWUtils.isStringEmpty(FWCookieManager.getInstanceAuthId())) {
			if (this.request.getPostRequest().getHeaders("WL-Instance-Id") != null) {
				this.request.getPostRequest().removeHeaders("WL-Instance-Id");
			}
			this.request.getPostRequest().addHeader("WL-Instance-Id", FWCookieManager.getInstanceAuthId());
		}
	}
}
