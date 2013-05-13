package com.airshiplay.framework.client;

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.SSLCertificateSocketFactory;

class HttpClientFactory {
	private static DefaultHttpClient client;
	private static final int SOCKET_OPERATION_TIMEOUT = 60000;

	public static synchronized DefaultHttpClient getInstance(FWConfig config) throws RuntimeException {
		if (client != null) {
			return client;
		}

		if (config == null) {
			return null;
		}

		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, SOCKET_OPERATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_OPERATION_TIMEOUT);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		HttpClientParams.setRedirecting(params, false);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		String protocol = config.getProtocol();
		int port = Integer.valueOf(config.getPort()).intValue();

		if (protocol.equalsIgnoreCase("http")) {
			schemeRegistry.register(new Scheme(protocol, PlainSocketFactory.getSocketFactory(), port));
		} else if (protocol.equalsIgnoreCase("https")) {
			schemeRegistry.register(new Scheme(protocol, SSLCertificateSocketFactory.getHttpSocketFactory(60000, null), port));
		} else {
			throw new RuntimeException("HttpClientFactory: Can't create HttpClient with protocol " + protocol);
		}

		ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

		client = new DefaultHttpClient(manager, params);
		return client;
	}

	static void releaseInstance() {
		client = null;
	}
}
