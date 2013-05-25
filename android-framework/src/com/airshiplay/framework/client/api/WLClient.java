package com.airshiplay.framework.client.api;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.airshiplay.framework.client.AsynchronousRequestSender;
import com.airshiplay.framework.client.FWConfig;
import com.airshiplay.framework.client.FWCookieManager;
import com.airshiplay.framework.client.FWRequest;
import com.airshiplay.framework.client.FWRequestListener;
import com.airshiplay.framework.client.FWUtils;
import com.airshiplay.framework.client.api.challengehandler.BaseChallengeHandler;
import com.airshiplay.framework.client.api.challengehandler.ChallengeHandler;
import com.airshiplay.framework.client.api.challengehandler.WLChallengeHandler;
import com.airshiplay.framework.client.challengehandler.AntiXSRFChallengeHandler;
import com.airshiplay.framework.client.challengehandler.AuthenticityChallengeHandler;
import com.airshiplay.framework.client.challengehandler.NoProvisioningChallengeHandler;
import com.airshiplay.framework.client.challengehandler.RemoteDisableChallengeHandler;
import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

public class WLClient {
	private static Logger log = LoggerFactory.getLogger(WLClient.class);
	public static final String WL_INSTANCE_ID = "WL-Instance-Id";
	private static final String INIT_REQUEST_PATH = "login";
	private static final String INVOKE_PROCEDURE_REQUEST_PATH = "query";
	private static final String SEND_INVOKE_PROCEDURE_REQUEST_PATH = "invoke";
	private static final String LOG_ACTIVITY_REQUEST_PATH = "logactivity";
	private static final String AUTHENTICATE_REQUEST_PATH = "authenticate";
	public static final String KEY_TIME_OUT = "timeout";
	public static final String KEY_INVOKE_PROCEDURE_INVOCATION_CONTEXT = "invokcationContext";
	public static final String KEY_LISTENER = "listener";
	private static final String ANTI_XSRF_REALM = "wl_antiXSRFRealm";
	private static final String NO_PROVISIONING_REALM = "wl_deviceNoProvisioningRealm";
	private static final String REMOTE_DISABLE_REALM = "wl_remoteDisableRealm";
	private static final String AUTHENTICITY_REALM = "wl_authenticityRealm";
	private static final String CHALLENGE_HANDLER_NULL_ERROR = "Cannot register 'null' challenge handler";
	private static final String NO_REALM_REGISTER_ERROR = "Application will exit because the challengeHandler parameter for registerChallengeHandler (challengeHandler) has a null realm property. Call this API with a valid reference to challenge handler.";
	private static final String INVOKE_PROCEDURE_INIT_ERROR = "invokeProcedure() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.";
	private static final String LOG_ACTIVITY_INIT_ERROR = "logActivity() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.";
	private static final String INVOKE_PROCEDURE_RUN_ERROR = "Error during invocation of remote procedure, because responseListener parameter can't be null.";
	private static WLClient wlClient = null;
	private boolean isInitialized;
	private HttpContext httpContext;
	private FWConfig config;
	private Context context;
	private Hashtable<String, BaseChallengeHandler> chMap;
	private Hashtable<String, String> globalHeaders;

	public FWConfig getConfig() {
		return this.config;
	}

	public Context getContext() {
		return this.context;
	}

	public HttpContext GetHttpContext() {
		return this.httpContext;
	}

	private WLClient(Context context) {
		this.config = new FWConfig(context);
		this.httpContext = new BasicHttpContext();
		this.context = context;

		this.chMap = new Hashtable<String, BaseChallengeHandler>();

		this.globalHeaders = new Hashtable<String, String>();

		registerDefaultChallengeHandlers();
	}

	public static WLClient createInstance(Context context) {
		if (wlClient != null) {
			log.debug("WLClient has already been created.");
			releaseInstance();
		}

		wlClient = new WLClient(context);
		CookieSyncManager.createInstance(context);
		return wlClient;
	}

	public static WLClient getInstance() {
		if (wlClient == null) {
			throw new RuntimeException("WLClient object has not been created. You must call WLClient.createInstance first.");
		}
		return wlClient;
	}

	private static void releaseInstance() {
		AsynchronousRequestSender.releaseHttpClient();
		wlClient = null;
	}

	/** @deprecated */
	public void init(WLResponseListener responseListener) {
		connect(responseListener);
	}

	public void connect(WLResponseListener responseListener) {
		FWCookieManager.clearCookies();

		if (updateCookiesFromWebView()) {
			String instanceId = FWUtils.readWLPref(getContext(), WL_INSTANCE_ID);
			if (!FWUtils.isStringEmpty(instanceId)) {
				getInstance().addGlobalHeader(WL_INSTANCE_ID, instanceId);
			}
		}

		WLRequestOptions requestOptions = new WLRequestOptions();
		requestOptions.addParameter("action", "test");
		requestOptions.setResponseListener(responseListener);

		InitRequestListener initRequestListener = new InitRequestListener();
		FWRequest initRequest = new FWRequest(initRequestListener, this.httpContext, requestOptions, this.config, this.context);
		initRequest.makeRequest(INIT_REQUEST_PATH);
	}

	public void checkForNotifications() {
		if (!this.isInitialized) {
			return;
		}

		FWRequestListener listener = new FWRequestListener() {
			public void onSuccess(WLResponse wlResponse) {
			}

			public void onFailure(WLFailResponse wlFailResponse) {
			}
		};
		FWRequest checkNotificationRequest = new FWRequest(listener, this.httpContext, new WLRequestOptions(), this.config, this.context);

		checkNotificationRequest.makeRequest(AUTHENTICATE_REQUEST_PATH);
	}

	public void addGlobalHeader(String headerName, String value) {
		this.globalHeaders.put(headerName, value);
	}

	public void removeGlobalHeader(String headerName) {
		this.globalHeaders.remove(headerName);
	}

	public void addGlobalHeadersToRequest(HttpPost postRequest) {
		for (Map.Entry entry : this.globalHeaders.entrySet())
			postRequest.addHeader((String) entry.getKey(), (String) entry.getValue());
	}

	public void invokeProcedure(WLProcedureInvocationData invocationData, WLResponseListener responseListener, WLRequestOptions requestOptions) {
		if (!this.isInitialized) {
			log.error("invokeProcedure() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.");
			return;
		}

		if (responseListener == null) {
			throw new IllegalArgumentException("Error during invocation of remote procedure, because responseListener parameter can't be null.");
		}

		if (requestOptions == null) {
			requestOptions = new WLRequestOptions();
		}
		requestOptions.setResponseListener(responseListener);

		requestOptions.addParameters(invocationData.getInvocationDataMap());
		InvokeProcedureRequestListener invokeProcedureInternalListener = new InvokeProcedureRequestListener();

		FWRequest invokeProcedureWLRequest = new FWRequest(invokeProcedureInternalListener, this.httpContext, requestOptions, this.config, this.context);

		invokeProcedureWLRequest.makeRequest(INVOKE_PROCEDURE_REQUEST_PATH);
	}

	public void sendInvoke(WLProcedureInvocationData invocationData, WLResponseListener responseListener, WLRequestOptions requestOptions) {
		if (responseListener == null) {
			throw new IllegalArgumentException("Error during invocation of remote procedure, because responseListener parameter can't be null.");
		}

		if (requestOptions == null) {
			requestOptions = new WLRequestOptions();
		}
		requestOptions.setResponseListener(responseListener);

		requestOptions.addParameters(invocationData.getInvocationDataMap());
		InvokeProcedureRequestListener invokeProcedureInternalListener = new InvokeProcedureRequestListener();

		FWRequest invokeProcedureWLRequest = new FWRequest(invokeProcedureInternalListener, this.httpContext, requestOptions, this.config, this.context);

		invokeProcedureWLRequest.makeRequest(SEND_INVOKE_PROCEDURE_REQUEST_PATH, true);
	}

	public void invokeProcedure(WLProcedureInvocationData invocationData, WLResponseListener responseListener) {
		invokeProcedure(invocationData, responseListener, null);
	}

	public void logActivity(String activityType) {
		if (!this.isInitialized) {
			log.error("logActivity() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.");
			return;
		}

		if (activityType == null) {
			throw new RuntimeException("ActivityType cannot be null");
		}

		WLRequestOptions requestOptions = new WLRequestOptions();
		requestOptions.addParameter("activity", activityType);
		LogActivityListener logActivityListener = new LogActivityListener();

		FWRequest logActivityWLRequest = new FWRequest(logActivityListener, this.httpContext, requestOptions, this.config, this.context);

		logActivityWLRequest.makeRequest(LOG_ACTIVITY_REQUEST_PATH);
	}

	protected void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	private boolean updateCookiesFromWebView() {
		String wlServerUrl = getWLServerURL();
		String cookiesString = CookieManager.getInstance().getCookie(wlServerUrl);

		if (!FWUtils.isStringEmpty(cookiesString)) {
			FWCookieManager.setCookies(cookiesString, wlServerUrl);
			Set<Cookie> cookies = FWCookieManager.getCookies();

			if (cookies != null) {
				CookieStore cookieStore = new BasicCookieStore();
				for (Cookie cookie : cookies) {
					cookieStore.addCookie(cookie);
				}
				this.httpContext.setAttribute("http.cookie-store", cookieStore);
				return true;
			}
			log.debug("No Cookies found for url " + this.config.getAppURL().getHost() + " in WebView.");
			return false;
		}

		return false;
	}

	private void registerDefaultChallengeHandlers() {
		registerChallengeHandler(new AntiXSRFChallengeHandler(ANTI_XSRF_REALM));

		registerChallengeHandler(new NoProvisioningChallengeHandler("wl_deviceNoProvisioningRealm"));

		registerChallengeHandler(new RemoteDisableChallengeHandler("wl_remoteDisableRealm"));

		registerChallengeHandler(new AuthenticityChallengeHandler("wl_authenticityRealm"));
	}

	public void registerChallengeHandler(BaseChallengeHandler challengeHandler) {
		if (challengeHandler == null) {
			log.error("Cannot register 'null' challenge handler");
			throw new RuntimeException("Cannot register 'null' challenge handler");
		}

		String realm = challengeHandler.getRealm();
		if (realm == null) {
			log.error("Application will exit because the challengeHandler parameter for registerChallengeHandler (challengeHandler) has a null realm property. Call this API with a valid reference to challenge handler.");
			throw new RuntimeException(
					"Application will exit because the challengeHandler parameter for registerChallengeHandler (challengeHandler) has a null realm property. Call this API with a valid reference to challenge handler.");
		}
		this.chMap.put(realm, challengeHandler);
	}

	public WLChallengeHandler getWLChallengeHandler(String realm) {
		if (realm == null) {
			return null;
		}

		BaseChallengeHandler handler = this.chMap.get(realm);

		if ((handler != null) && ((handler instanceof WLChallengeHandler))) {
			return (WLChallengeHandler) handler;
		}

		return null;
	}

	public ChallengeHandler getChallengeHandler(WLResponse response) {
		Iterator iter = this.chMap.entrySet().iterator();

		while (iter.hasNext()) {
			BaseChallengeHandler handler = (BaseChallengeHandler) ((Map.Entry) iter.next()).getValue();
			if ((handler instanceof ChallengeHandler)) {
				ChallengeHandler cl = (ChallengeHandler) handler;
				if (cl.isCustomResponse(response)) {
					return cl;
				}
			}
		}

		return null;
	}

	private String getWLServerURL() {
		String context = getConfig().getContext();
		String host = getConfig().getAppURL().getHost();
		String serverUrl = (context != null) && (context.trim().length() > 1) ? host + context : host;
		return serverUrl;
	}
}
