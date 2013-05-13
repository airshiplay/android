package com.airshiplay.framework.client.api;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import com.airshiplay.framework.client.FWUtils;

public class WLFailResponse extends WLResponse {
	private static final String JSON_KEY_ERROR_MSG = "errorMsg";
	private static final String JSON_KEY_ERROR_CODE = "errorCode";
	protected WLErrorCode errorCode;
	protected String errorMsg;

	public WLFailResponse(HttpResponse httpResponse) {
		super(httpResponse);
		parseErrorFromResponse();
	}

	public WLFailResponse(WLResponse wlResponse) {
		super(wlResponse);
		parseErrorFromResponse();
	}

	public WLFailResponse(WLErrorCode errorCode, String errorMsg, WLRequestOptions requestOptions) {
		super(500, "", requestOptions);
		setErrorCode(errorCode);
		setErrorMsg(errorMsg);
	}

	public WLErrorCode getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(WLErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		if (this.errorMsg == null) {
			return this.errorCode.getDescription();
		}
		return this.errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	private void parseErrorFromResponse() {
		if ((getResponseText() != null) && (getResponseText().length() > 0))
			try {
				JSONObject jsonResponse = FWUtils.convertStringToJSON(getResponseText());

				this.errorCode = WLErrorCode.UNEXPECTED_ERROR;
				if (jsonResponse.has(JSON_KEY_ERROR_CODE)) {
					this.errorCode = WLErrorCode.valueOf(jsonResponse.getString(JSON_KEY_ERROR_CODE));
				}

				if (jsonResponse.has(JSON_KEY_ERROR_MSG))
					this.errorMsg = jsonResponse.getString(JSON_KEY_ERROR_MSG);
			} catch (Exception e) {
				FWUtils.debug(String.format("Additional error information is not available for the current response and response text is: %s", new Object[] { getResponseText() }),
						e);

				this.errorCode = WLErrorCode.UNEXPECTED_ERROR;
				if (getStatus() >= 500)
					this.errorCode = WLErrorCode.UNRESPONSIVE_HOST;
				else if (getStatus() >= 408)
					this.errorCode = WLErrorCode.REQUEST_TIMEOUT;
				else if (getStatus() >= 404)
					this.errorCode = WLErrorCode.REQUEST_SERVICE_NOT_FOUND;
			}
	}

	public String toString() {
		return super.toString() + " WLFailResponse [errorMsg=" + this.errorMsg + ", errorCode=" + this.errorCode + "]";
	}
}