package com.airshiplay.mobile.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;

import com.airshiplay.mobile.log.Logger;
import com.airshiplay.mobile.log.LoggerFactory;

/**
 * @author airshiplay
 * @Create 2013-7-14
 * @version 1.0
 * @since 1.0
 */
public class MobileCookieManager {
	private static Logger log = LoggerFactory
			.getLogger(MobileCookieManager.class);
	private static Set<Cookie> cookies;

	/**
	 * @param request
	 */
	public static void addCookies(MobileRequest request) {
		if ((cookies != null) && (!cookies.isEmpty())) {
			CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
			List<Cookie> cookies = new ArrayList<Cookie>();
			cookies.addAll(getCookies());
			List<Header> cookieHeader = cookieSpecBase.formatCookies(cookies);
			request.getPostRequest().setHeader((Header) cookieHeader.get(0));
		}
	}

	public static void clearCookies() {
		if (cookies != null)
			cookies.clear();
	}

	/**
	 * @param cookiesString
	 * @param domain
	 */
	public static void setCookies(String cookiesString, String domain) {
		if ((cookiesString == null) || (cookiesString.length() == 0)) {
			log.debug(String
					.format("setCookies() can't parse cookieString which is null or empty.",
							new Object[0]));
			return;
		}
		if ((domain == null) || (domain.length() == 0)) {
			log.debug(String
					.format("setCookies() can't create cookies with a null or empty domain.",
							new Object[0]));
			return;
		}

		HashSet<Cookie> cookieSet = new HashSet<Cookie>();

		String[] cookies = cookiesString.split(";");
		for (int i = 0; i < cookies.length; i++) {
			String[] keyValue = cookies[i].trim().split("=");
			if (keyValue.length == 2) {
				BasicClientCookie cookie = new BasicClientCookie(
						keyValue[0].trim(), keyValue[1].trim());
				cookie.setDomain(domain);
				cookieSet.add(cookie);
			} else {
				log.debug("setCookies() can't parse cookie %s.", cookies[i]);
			}
		}
		MobileCookieManager.cookies = cookieSet;
	}

	public static Set<Cookie> getCookies() {
		return cookies;
	}

	/**
	 * @param headers
	 * @param uri
	 */
	public static void handleResponseHeaders(Header[] headers, URI uri) {
		if (cookies == null) {
			cookies = new HashSet<Cookie>();
		}
		CookieOrigin origin = new CookieOrigin(uri.getHost(), uri.getPort(),
				"/apps/services", false);
		CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
		for (int i = 0; i < headers.length; i++)
			if (headers[i].getName().toLowerCase().equals("set-cookie")) {
				List<Cookie> theCookies;
				try {
					theCookies = cookieSpecBase.parse(headers[i], origin);
				} catch (MalformedCookieException e) {
					log.error(
							new StringBuilder()
									.append("Response ")
									.append(uri != null ? uri.getPath() : "")
									.append(" from Mobile server failed because cookies could not be extracted from http header ")
									.append(headers[i].getName())
									.append(" with ").append(e.getMessage())
									.toString(), e);
					throw new RuntimeException(e);
				}
				cookies.addAll(theCookies);
			}
	}

}
