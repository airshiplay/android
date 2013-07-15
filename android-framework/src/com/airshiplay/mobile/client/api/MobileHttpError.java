package com.airshiplay.mobile.client.api;

import org.apache.http.HttpStatus;

/**
 * @author airshiplay
 * @Create 2013-6-30
 * @version 1.0
 * @since 1.0
 */
public interface MobileHttpError extends HttpStatus {

	public static final int EC_SocketTimeout = -1000;
	public static final int EC_ConnectTimeout = -1001;
	public static final int EC_Exception = -1002;
	public static final int EC_RequestException = -1003;
	public static final int EC_ParseException = -1004;
}
