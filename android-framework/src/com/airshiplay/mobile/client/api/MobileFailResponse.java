package com.airshiplay.mobile.client.api;

import org.apache.http.HttpResponse;

/**
 * @author airshiplay
 * @Create 2013-6-30
 * @version 1.0
 * @since 1.0
 */
public class MobileFailResponse extends MobileResponse {
	protected int errorCode;
	protected String errorMsg;

	public MobileFailResponse(int errorCode, String errorMsg,
			MobileRequestOptions requestOptions) {
		super(errorCode, errorMsg, requestOptions);
		setErrorCode(errorCode);
		setErrorMsg(errorMsg);
	}

	public MobileFailResponse(HttpResponse httpResponse) {
		super(httpResponse);
	}

	public MobileFailResponse(MobileResponse response) {
		super(response);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
