package com.airshiplay.framework.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.airshiplay.framework.client.api.WLResponseListener;

public class AsynchronousRequestSender {
	private static final ExecutorService pool = Executors.newFixedThreadPool(6);
	private static AsynchronousRequestSender sender;

	public static synchronized AsynchronousRequestSender getInstance() {
		if (sender != null) {
			return sender;
		}

		sender = new AsynchronousRequestSender();
		return sender;
	}

	public static void releaseHttpClient() {
		HttpClientFactory.releaseInstance();
	}

	public static HttpClient getHttpClient() {
		return HttpClientFactory.getInstance(null);
	}

	public void sendRequestAsync(FWRequest request) {
		pool.execute(new InternalRequestSender(request));
	}

	public void sendCustomRequestAsync(HttpRequestBase httpRequest,
			int requestTimeoutInMilliseconds, WLResponseListener listener) {
		pool.execute(new InternalCustomRequestSender(httpRequest,
				requestTimeoutInMilliseconds, listener));
	}
}
