package com.airshiplay.mobile.client.api;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @author airshiplay
 * @Create 2013-6-30
 * @version 1.0
 * @since 1.0
 */
public class MobileResponse {
	protected int status;
	protected MobileRequestOptions requestOptions;
	protected String responseText;
	private Header[] headers;
	private HttpResponse httpResponseCache;

	public MobileResponse(HttpResponse httpResponse) {
		this.status = httpResponse.getStatusLine().getStatusCode();
		this.headers = httpResponse.getAllHeaders();
		this.httpResponseCache = httpResponse;
		try {
			this.responseText = EntityUtils.toString(httpResponse.getEntity(),
					HTTP.UTF_8);
		} catch (Exception e) {
			
		}
	}

	public MobileResponse(MobileResponse response) {
		this.status = response.status;
		this.requestOptions = response.requestOptions;
		this.responseText = response.responseText;
		this.httpResponseCache = response.httpResponseCache;
	}

	protected MobileResponse(int status, String responseText,
			MobileRequestOptions requestOptions) {
		this.status = status;
		this.requestOptions = requestOptions;
		this.responseText = responseText;
	}

	public int getStatus() {
		return this.status;
	}

	public Header[] getHeaders() {
		return this.headers;
	}

	public String getResponseText() {
		return responseText;
	}

	public MobileRequestOptions getOptions() {
		return this.requestOptions;
	}

	public void setOptions(MobileRequestOptions options) {
		this.requestOptions = options;
	}

}