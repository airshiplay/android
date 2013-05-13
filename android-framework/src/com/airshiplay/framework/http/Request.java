package com.airshiplay.framework.http;


public class Request {
	public static <T> void request(final String url, final String httpMethod, final RequestListener<T> listener) {
		new Thread() {
			public void run() {
				//NetUtils.getStringByApacheGet(url, null);
				T t=null;
				listener.onComplete(t);
			}
		}.start();
	}
}
