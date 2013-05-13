package com.airshiplay.framework.client.api;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.airshiplay.framework.client.FWUtils;
import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

public class WLResponse {
	private Logger log = LoggerFactory.getLogger(WLResponse.class);
	protected int status;
	protected WLRequestOptions requestOptions;
	protected String responseText;
	private Header[] headers;
	private HttpResponse httpResponseCache;
	private JSONObject responseJSON;

	public WLResponse(HttpResponse httpResponse) {
		this.status = httpResponse.getStatusLine().getStatusCode();
		this.headers = httpResponse.getAllHeaders();
		this.httpResponseCache = httpResponse;
		try {
			this.responseText = FWUtils.convertStreamToString(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			log.error("Response from Worklight server failed because could not read text from response " + e.getMessage(), e);
		}

		responseTextToJSON(this.responseText);
	}

	public WLResponse(WLResponse response) {
		this.status = response.status;
		this.requestOptions = response.requestOptions;
		this.responseText = response.responseText;
		this.responseJSON = response.responseJSON;
		this.httpResponseCache = response.httpResponseCache;
	}

	protected WLResponse(int status, String responseText, WLRequestOptions requestOptions) {
		this.status = status;
		this.requestOptions = requestOptions;
		this.responseText = responseText;

		responseTextToJSON(responseText);
	}

	private void responseTextToJSON(String responseText) {
		int firstIndex = this.responseText.indexOf('{');
		int lastIndex = this.responseText.lastIndexOf('}');

		if ((firstIndex == -1) || (lastIndex == -1)) {
			this.responseJSON = null;
			return;
		}

		String jsonText = this.responseText.substring(firstIndex, lastIndex + 1);
		try {
			this.responseJSON = new JSONObject(jsonText);
		} catch (JSONException e) {
			log.error("Response from Worklight server failed because could not read JSON from response with text " + jsonText, e);
			this.responseJSON = null;
		}
	}

	public int getStatus() {
		return this.status;
	}

	public Object getInvocationContext() {
		return this.requestOptions.getInvocationContext();
	}

	public String getResponseText() {
		return this.responseText;
	}

	public void setInvocationContext(Object invocationContext) {
		this.requestOptions.setInvocationContext(invocationContext);
	}

	public WLRequestOptions getOptions() {
		return this.requestOptions;
	}

	public void setOptions(WLRequestOptions wlOptions) {
		this.requestOptions = wlOptions;
	}

	public Header getHeader(String text) {
		for (int i = 0; i < this.headers.length; i++) {
			if (this.headers[i].getName().equalsIgnoreCase(text)) {
				return this.headers[i];
			}
		}

		return null;
	}

	public Header[] getHeaders() {
		return this.headers;
	}

	public void setResponseHeader(String name, String value) {
		this.httpResponseCache.setHeader(name, value);
		this.headers = this.httpResponseCache.getAllHeaders();
	}

	public JSONObject getResponseJSON() {
		return this.responseJSON;
	}

	public String toString() {
		return "WLResponse [invocationContext=" + this.requestOptions.getInvocationContext() + ", responseText=" + this.responseText + ", status=" + this.status + "]";
	}
}