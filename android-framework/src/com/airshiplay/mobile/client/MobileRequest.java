package com.airshiplay.mobile.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.util.Log;

import com.airshiplay.mobile.client.api.MobileClient;
import com.airshiplay.mobile.client.api.MobileFailResponse;
import com.airshiplay.mobile.client.api.MobileHttpError;
import com.airshiplay.mobile.client.api.MobileRequestOptions;
import com.airshiplay.mobile.client.api.MobileResponse;

/**
 * @author airshiplay
 * @Create 2013-6-30
 * @version 1.0
 * @since 1.0
 */
public class MobileRequest {
	private MobileRequestListener requestListener;
	private HttpPost postRequest;
	private HttpContext httpContext;
	private Context context;
	private String requestURL = null;
	private MobileConfig config;
	private MobileRequestOptions requestOptions;

	public MobileRequest(MobileRequestListener initRequestListener,
			HttpContext httpContext, MobileRequestOptions mRequestData,
			MobileConfig mConfig, Context context) {
		this.requestListener = initRequestListener;
		this.httpContext = httpContext;
		this.requestOptions = mRequestData;
		this.config = mConfig;
		this.context = context;
	}

	public void makeRequest(String requestPath) {
		makeRequest(requestPath, false);
	}

	public void makeRequest(String requestPath, boolean isFullPath) {
		this.requestURL = null;
		if (!isFullPath)
			this.requestURL = new StringBuilder()
					.append(this.config.getAppURL().toExternalForm())
					.append(requestPath).toString();
		else {
			this.requestURL = new StringBuilder()
					.append(this.config.getRootURL()).append("/")
					.append(requestPath).toString();
		}

		sendRequest(this.requestURL);
	}

	private void sendRequest(String requestURL) {
		this.postRequest = new HttpPost(requestURL);

		addHeaders(this.config, this.postRequest);
		addExtraHeaders(this.postRequest);
		addParams(this.requestOptions, this.postRequest);
		MobileClient.getInstance().addGlobalHeadersToRequest(this.postRequest);
		try {
			AsynchronousRequestSender.getInstance().sendRequestAsync(this);
		} catch (Exception e) {
			triggerUnexpectedOnFailure(e);
		}
	}

	private void triggerUnexpectedOnFailure(Exception e) {
		requestListener.onFailure(new MobileFailResponse(
				MobileHttpError.EC_RequestException, e.getMessage(),
				requestOptions));
	}

	private void addParams(MobileRequestOptions requestOptions,
			HttpPost postRequest) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		if ((requestOptions.getParameters() != null)
				&& (!requestOptions.getParameters().isEmpty())) {
			for (String paramName : requestOptions.getParameters().keySet()) {
				params.add(new BasicNameValuePair(paramName,
						(String) requestOptions.getParameters().get(paramName)));
			}
		}
		params.add(new BasicNameValuePair("isAjaxRequest", "true"));

		params.add(new BasicNameValuePair("x", String.valueOf(Math.random())));

		UrlEncodedFormEntity encodedFormEntity = null;
		try {
			encodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e("MobileRequest", e.getMessage());
			throw new RuntimeException(e);
		}
		postRequest.setEntity(encodedFormEntity);
	}

	private void addExtraHeaders(HttpPost postRequest) {
		ArrayList<Header> extraHeaders = this.requestOptions.getHeaders();
		if (extraHeaders == null) {
			return;
		}
		for (Iterator<Header> iterator = extraHeaders.iterator(); iterator
				.hasNext();) {
			Header header = (Header) iterator.next();
			postRequest.addHeader(header);
		}
	}

	private void addHeaders(MobileConfig config2, HttpPost postRequest2) {
	}

	public MobileRequestListener getRequestListener() {
		return requestListener;
	}

	public void setRequestListener(MobileRequestListener responseListener) {
		this.requestListener = responseListener;
	}

	public HttpPost getPostRequest() {
		return postRequest;
	}

	public void setPostRequest(HttpPost postRequest) {
		this.postRequest = postRequest;
	}

	public HttpContext getHttpContext() {
		return httpContext;
	}

	public void setHttpContext(HttpContext httpContext) {
		this.httpContext = httpContext;
	}

	public MobileConfig getConfig() {
		return config;
	}

	public Context getContext() {
		return this.context;
	}

	public MobileRequestOptions getOptions() {
		return requestOptions;
	}

	public void requestFinished(MobileResponse response) {
		if (response.getStatus() == MobileHttpError.SC_OK) {
			requestListener.onSuccess(response);
		}

	}

}
