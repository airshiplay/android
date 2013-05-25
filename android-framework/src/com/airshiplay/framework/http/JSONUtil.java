/**
 * 
 */
package com.airshiplay.framework.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

/**
 * @author airshiplay
 * @Create Date 2013-2-24
 * @version 1.0
 * @since 1.0
 */
public class JSONUtil {
	public static String get(String url) throws ClientProtocolException,
			IOException, JSONException {
		HttpGet httpGet = new HttpGet(url);
		HttpConnectionParams.setConnectionTimeout(httpGet.getParams(),30000);
		HttpConnectionParams.setSoTimeout(httpGet.getParams(), 30000);
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
		String result = null;
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(httpResponse.getEntity());
		} 
		return result;
	}

	/**
	 * @param mPath
	 * @param object
	 * @return
	 */
	public static String getContentFromFile(String path, Object object) {
		return null;
	}
}
