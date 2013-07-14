package com.airshiplay.mobile.client.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;


public class MobileRequestOptions {
	private Map<String, String> parameters = new HashMap<String, String>();
	private int timeout = 10000;
	private Object invocationContext;
	private MobileResponseListener responseListener;
	private ArrayList<Header> headers = new ArrayList<Header>();
	private boolean fromChallenge;

	public int getTimeout() {
		return this.timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Object getInvocationContext() {
		return this.invocationContext;
	}

	public void setInvocationContext(Object invocationContext) {
		this.invocationContext = invocationContext;
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}

	public MobileResponseListener getResponseListener() {
		return this.responseListener;
	}

	protected void setResponseListener(MobileResponseListener responseListener) {
		this.responseListener = responseListener;
	}

	public void addParameters(Map<String, String> parameters) {
		this.parameters.putAll(parameters);
	}

	public void addParameter(String name, String value) {
		this.parameters.put(name, value);
	}

	public String getParameter(String name) {
		return this.parameters != null ? (String) this.parameters.get(name) : "";
	}

	public void addHeader(Header header) {
		if (header == null) {
			return;
		}
		this.headers.add(header);
	}

	public void addHeader(String name, String value) {
		addHeader(new BasicHeader(name, value));
	}

	public void setHeaders(ArrayList<Header> extraHeaders) {
		this.headers = extraHeaders;
	}

	public ArrayList<Header> getHeaders() {
		return this.headers;
	}

	public boolean isFromChallenge() {
		return this.fromChallenge;
	}

	public void setFromChallenge(boolean flag) {
		this.fromChallenge = flag;
	}
}
