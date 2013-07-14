package com.airshiplay.mobile.client.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.webkit.CookieSyncManager;

import com.airshiplay.mobile.client.AsynchronousRequestSender;
import com.airshiplay.mobile.client.MobileConfig;
import com.airshiplay.mobile.client.MobileCookieManager;
import com.airshiplay.mobile.client.MobileRequest;

/**
 * @author airshiplay
 * @Create 2013-6-29
 * @version 1.0
 * @since 1.0
 */
public class MobileClient {

	private static MobileClient mobileClient;
	private MobileConfig config;
	private HttpContext httpContext;
	private Context context;
	private Hashtable<String, String> globalHeaders;

	private MobileClient(Context context) {
		this.config = new MobileConfig(context);
		this.httpContext = new BasicHttpContext();
		this.context = context;
		this.globalHeaders = new Hashtable<String, String>();
	}

	/**
	 * @param context
	 * @return
	 */
	public static MobileClient createInstance(Context context) {
		if (mobileClient != null) {
			releaseInstance();
		}
		mobileClient = new MobileClient(context);
		CookieSyncManager.createInstance(context);
		return mobileClient;
	}

	/**
	 * @return
	 */
	public static MobileClient getInstance() {
		if (mobileClient == null) {
			throw new RuntimeException(
					"MobileClient object has not been created.");
		}
		return mobileClient;
	}

	private MobileConfig getConfig() {
		return config;
	}

	/**
	 * @param responseListener
	 */
	public void connect(MobileResponseListener responseListener) {
		MobileCookieManager.clearCookies();

		MobileRequestOptions requestOptions = new MobileRequestOptions();
		requestOptions.addParameter("action", "test");
		requestOptions.setResponseListener(responseListener);

		InitRequestListener initRequestListener = new InitRequestListener();
		MobileRequest initRequest = new MobileRequest(initRequestListener,
				this.httpContext, requestOptions, this.config, this.context);
		initRequest.makeRequest("connect");
	}

	private static void releaseInstance() {
	}

	public void sendInvoke(MobileResponseListener responseListener) {

	}

	public void addGlobalHeader(String headerName, String value) {
		this.globalHeaders.put(headerName, value);
	}

	public void removeGlobalHeader(String headerName) {
		this.globalHeaders.remove(headerName);
	}

	public void addGlobalHeadersToRequest(HttpPost postRequest) {
		for (Map.Entry<String, String> entry : this.globalHeaders.entrySet())
			postRequest.addHeader(entry.getKey(), entry.getValue());
	}

	// /////////////////////////////////////////////////////////////
	public void execute(String requestURL,
			Map<String, String> requestParameters,
			Map<String, String> requestHeaders,
			int requestTimeoutInMilliseconds, String requestMethod,
			MobileResponseListener responseListener) {

		MobileClient client = MobileClient.getInstance();
		String url = null;

		if ((requestURL.indexOf("http") == 0) && (requestURL.indexOf(":") > 0)) {
			url = requestURL;
		} else {
			String extForm = client.getConfig().getAppURL().toExternalForm();

			if ((extForm.charAt(extForm.length() - 1) == '/')
					&& (requestURL.length() > 0)
					&& (requestURL.charAt(0) == '/')) {
				requestURL = requestURL.substring(1);
			}
			url = extForm + requestURL;
		}

		HttpRequestBase httpRequest = null;

		if (requestMethod.equalsIgnoreCase("post")) {
			httpRequest = new HttpPost(url);
			addPostParams((HttpPost) httpRequest, requestParameters);
		} else if (requestMethod.equalsIgnoreCase("get")) {
			httpRequest = new HttpGet(url);
			addGetParams((HttpGet) httpRequest, requestParameters);
		} else {
			throw new RuntimeException(
					"CustomChallengeHandler.submitLoginForm: invalid request method.");
		}

		httpRequest.addHeader("x-mobile-app-version", client.getConfig()
				.getApplicationVersion());

		if (requestHeaders != null) {
			for (String headerName : requestHeaders.keySet()) {
				httpRequest.addHeader(headerName,
						(String) requestHeaders.get(headerName));
			}
		}

		AsynchronousRequestSender.getInstance().sendCustomRequestAsync(
				httpRequest, requestTimeoutInMilliseconds, responseListener);
	}

	private void addPostParams(HttpPost postRequest,
			Map<String, String> requestParams) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("isAjaxRequest", "true"));
		if (requestParams != null) {
			for (String paramName : requestParams.keySet()) {
				params.add(new BasicNameValuePair(paramName,
						requestParams.get(paramName)));
			}
		}
		UrlEncodedFormEntity encodedFormEntity = null;
		try {
			encodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		postRequest.setEntity(encodedFormEntity);
	}

	private void addGetParams(HttpGet getRequest,
			Map<String, String> requestParams) {
		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter("isAjaxRequest", "true");
		for (String paramName : requestParams.keySet()) {
			httpParams.setParameter(paramName, requestParams.get(paramName));
		}
		getRequest.setParams(httpParams);
	}

	public HttpContext getHttpContext() {
		return httpContext;
	}
}