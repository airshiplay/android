package com.airshiplay.mobile.client;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.airshiplay.framework.util.AppUtil;
import com.airshiplay.framework.util.MobileResource;

/**
 * <p>
 * res/*.xml文件实例<br/>
 * &ltstring name="MobileServerProtocol"&gthttp&lt/string&gt<br/>
 * &ltstring name="MobileServerHost"&gt172.0.0.1&lt/string&gt<br/>
 * &ltstring name="MobileServerPort"&gt8080&lt/string&gt<br/>
 * &ltstring name="MobileServerContext"&gt/mobile&lt/string&gt<br/>
 * &ltstring
 * name="MobileServerURL"&gthttp://172.0.0.1:8080/mobile/api&lt/string&gt<br/>
 * </p>
 * 
 * @author airshiplay
 * @Create 2013-6-30
 * @version 1.0
 * @since 1.0
 */
public class MobileConfig {
	private Context mContext;

	public MobileConfig() {
	}

	public MobileConfig(Context context) {
		this.mContext = context;
	}

	public MobileConfig(Activity context) {
		this(context.getApplication());
	}

	public String getDefaultRootUrl() {
		String port = ":" + getPort();
		String context = (TextUtils.isEmpty(getContext()))
				|| (getContext().equals("/")) ? "" : getContext();
		String url = String.format("%s://%s%s%s", getProtocol(), getHost(),
				port, context);
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	public String getRootURL() {
		String result = MobileResource.getInstance(mContext).getString(
				"MobileServerURL");
		if ("".equals(result)) {
			result = getDefaultRootUrl();
		}
		return result;
	}

	public String getProtocol() {
		String protocol = MobileResource.getInstance(mContext).getString(
				"MobileServerProtocol");
		if (!"".equals(protocol))
			return protocol;
		return "http";
	}

	public String getHost() {
		String host = MobileResource.getInstance(mContext).getString(
				"MobileServerHost");
		if (!"".equals(host))
			return host;
		return "172.0.0.1";
	}

	public String getPort() {
		String port = MobileResource.getInstance(mContext).getString(
				"MobileServerPort");
		if (!"".equals(port))
			return port;
		return "8080";
	}

	public String getContext() {
		String context = MobileResource.getInstance(mContext).getString(
				"MobileServerContext");
		if (!"".equals(context))
			return context;
		return "/";
	}

	public URL getAppURL() {
		try {
			return new URL(getRootURL() + "/apps/services/api/");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not parse URL." + e.getMessage(),
					e);
		}
	}

	public String getApplicationVersion() {
		return AppUtil.getVersionName(mContext);
	}

}
