package com.airshiplay.framework.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.http.Header;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.airshiplay.framework.client.api.WLClient;
import com.airshiplay.framework.client.api.WLErrorCode;
import com.airshiplay.framework.client.api.WLFailResponse;
import com.airshiplay.framework.client.api.WLRequestOptions;
import com.airshiplay.framework.client.api.WLResponse;
import com.airshiplay.framework.client.api.challengehandler.ChallengeHandler;
import com.airshiplay.framework.client.api.challengehandler.WLChallengeHandler;

public class FWRequest {
	private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";
	private static final String AUTH_FAIL_ID = "WLClient.authFailure";
	private static final String ERROR_ID = "WLClient.error";
	private static final String CLOSE_BUTTON_ID = "WLClient.close";
	private static final String ACCESS_DENIED_ID = "WLClient.accessDenied";
	private FWRequestListener requestListener;
	private HttpPost postRequest;
	private HttpContext httpContext;
	private WLRequestOptions requestOptions;
	private FWConfig config;
	private Context context;
	private String requestURL = null;

	private HashMap<String, Object> wlAnswers = new HashMap<String, Object>();

	public FWRequest(FWRequestListener wlRequestListener, HttpContext httpContext, WLRequestOptions wlRequestData, FWConfig wlConfig, Context context) {
		this.requestListener = wlRequestListener;
		this.httpContext = httpContext;
		this.requestOptions = wlRequestData;
		this.config = wlConfig;
		this.context = context;
	}

	public void makeRequest(String requestPath) {
		makeRequest(requestPath, false);
	}

	public void makeRequest(String requestPath, boolean isFullPath) {
		this.requestURL = null;
		if (!isFullPath)
			this.requestURL = new StringBuilder().append(this.config.getAppURL().toExternalForm()).append(requestPath).toString();
		else {
			this.requestURL = new StringBuilder().append(this.config.getRootURL()).append("/").append(requestPath).toString();
		}

		sendRequest(this.requestURL);
	}

	private void sendRequest(String requestURL) {
		this.postRequest = new HttpPost(requestURL);

		addHeaders(this.config, this.postRequest);
		addExtraHeaders(this.postRequest);
		addParams(this.requestOptions, this.postRequest);
		WLClient.getInstance().addGlobalHeadersToRequest(this.postRequest);
		addExpectedAnswers(this.postRequest);
		try {
			AsynchronousRequestSender.getInstance().sendRequestAsync(this);
		} catch (Exception e) {
			FWUtils.error(e.getMessage(), e);
			triggerUnexpectedOnFailure(e);
		}
	}

	public void requestFinished(WLResponse response) {
		try {
			checkResponseForSuccesses(response);
		} catch (Exception e) {
			triggerUnexpectedOnFailure(e);
			return;
		}
		boolean isContainChallenges;
		try {
			isContainChallenges = checkResponseForChallenges(response);
		} catch (Exception e) {
			triggerUnexpectedOnFailure(e);
			return;
		}
		if (!isContainChallenges)
			if (response.getStatus() == 200) {
				try {
					JSONObject responseJson = response.getResponseJSON();
					if (responseJson != null) {
						Object subscriptionStateObj = responseJson.get("notificationSubscriptionState");

						if (subscriptionStateObj != null) {
							String token = null;
							JSONArray eventSources = null;

							JSONObject subscriptionState = (JSONObject) subscriptionStateObj;
							// try
							// {
							// Class.forName("com.google.android.gcm.GCMRegistrar");
							//
							// WLClient client = WLClient.getInstance();
							// WLPush push = client.getPush();
							// try
							// {
							// token = (String)subscriptionState.get("token");
							// eventSources =
							// (JSONArray)subscriptionState.get("eventSources");
							// }
							// catch (JSONException e)
							// {
							// }
							// push.clearSubscribedEventSources();
							// if ((eventSources != null) &&
							// (eventSources.length() > 0)) {
							// push.updateSubscribedEventSources(eventSources);
							// }
							// push.updateToken(token);
							// } catch (ClassNotFoundException e) {
							// WLUtils.error("Push notification will not work because GCMRegistrar class is not available. Check if gcm.jar is available in the path.");
							// }
						}
					}
				} catch (JSONException e) {
				}
				processSuccessResponse(response);
			} else {
				processFailureResponse(response);
			}
	}

	private void triggerUnexpectedOnFailure(Exception e) {
		getRequestListener().onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, e.getMessage(), getOptions()));
	}

	private void addHeaders(FWConfig config, HttpPost postRequest) {
		postRequest.addHeader("X-Requested-With", "XMLHttpRequest");
		postRequest.addHeader("x-wl-app-version", config.getApplicationVersion());
	}

	private void addExtraHeaders(HttpPost postRequest) {
		ArrayList extraHeaders = this.requestOptions.getHeaders();
		if (extraHeaders == null) {
			return;
		}
		for (Iterator iterator = extraHeaders.iterator(); iterator.hasNext();) {
			Header header = (Header) iterator.next();
			postRequest.addHeader(header);
		}
	}

	private void addParams(WLRequestOptions requestOptions, HttpPost postRequest) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		if ((requestOptions.getParameters() != null) && (!requestOptions.getParameters().isEmpty())) {
			for (String paramName : requestOptions.getParameters().keySet()) {
				params.add(new BasicNameValuePair(paramName, (String) requestOptions.getParameters().get(paramName)));
			}
		}
		params.add(new BasicNameValuePair("isAjaxRequest", "true"));

		params.add(new BasicNameValuePair("x", String.valueOf(Math.random())));

		UrlEncodedFormEntity encodedFormEntity = null;
		try {
			encodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			FWUtils.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		postRequest.setEntity(encodedFormEntity);
	}

	private void addExpectedAnswers(HttpPost postRequest) {
		if (this.wlAnswers.isEmpty()) {
			return;
		}

		JSONObject allAnswers = new JSONObject();

		for (Map.Entry<String, Object> entry : this.wlAnswers.entrySet()) {
			String realm = (String) entry.getKey();
			Object answer = entry.getValue();

			if (answer == null) {
				return;
			}
			try {
				allAnswers.accumulate(realm, answer);
			} catch (JSONException e) {
				FWUtils.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}

		String strJSON = allAnswers.toString();
		postRequest.addHeader("Authorization", strJSON);

		this.wlAnswers.clear();
	}

	public FWRequestListener getRequestListener() {
		return this.requestListener;
	}

	public HttpPost getPostRequest() {
		return this.postRequest;
	}

	public HttpContext getHttpContext() {
		return this.httpContext;
	}

	public WLRequestOptions getOptions() {
		return this.requestOptions;
	}

	public FWConfig getConfig() {
		return this.config;
	}

	public Context getContext() {
		return this.context;
	}

	private void setExpectedAnswers(List<String> realms) {
		for (Iterator<String> iterator = realms.iterator(); iterator.hasNext();) {
			String realm = iterator.next();
			this.wlAnswers.put(realm, null);
		}
	}

	public void submitAnswer(String realm, Object answer) {
		this.wlAnswers.put(realm, answer);
		resendIfNeeded();
	}

	private void resendIfNeeded() {
		boolean resend = true;
		Iterator<Object> i$;
		if (this.wlAnswers == null) {
			resend = true;
		} else {
			Collection<Object> answers = this.wlAnswers.values();
			for (i$ = answers.iterator(); i$.hasNext();) {
				Object obj = i$.next();
				if (obj == null) {
					resend = false;
					break;
				}
			}
		}

		if (resend)
			resendRequest();
	}

	public void removeExpectedAnswer(String realm) {
		this.wlAnswers.remove(realm);
		resendIfNeeded();
	}

	private void checkResponseForSuccesses(WLResponse response) {
		JSONObject responseJSON = response.getResponseJSON();

		if (responseJSON == null) {
			return;
		}
		try {
			if (!responseJSON.has("WL-Authentication-Success")) {
				return;
			}

			JSONObject successes = responseJSON.getJSONObject("WL-Authentication-Success");

			if (successes != null) {
				JSONArray realms = successes.names();

				for (int i = 0; i < successes.length(); i++) {
					String realm = realms.getString(i);
					JSONObject success = successes.getJSONObject(realm);

					WLChallengeHandler handler = WLClient.getInstance().getWLChallengeHandler(realm);

					if (handler != null) {
						handler.handleSuccess(success);
						handler.releaseWaitingList();
					}
				}
			}
		} catch (Exception e) {
			triggerUnexpectedOnFailure(e);
		}
	}

	private boolean checkResponseForChallenges(WLResponse response) {
		boolean containsChallenges = false;

		ResourceBundle bundle = ResourceBundle.getBundle("com/worklight/wlclient/messages", Locale.getDefault());
		if (isWl401(response)) {
			JSONObject responseJSON = response.getResponseJSON();

			List<String> realmNames = new ArrayList<String>();
			try {
				JSONObject challenges;
				challenges = responseJSON.getJSONObject("challenges");
				JSONArray realms = challenges.names();

				for (int i = 0; i < realms.length(); i++) {
					realmNames.add(realms.getString(i));
				}

				setExpectedAnswers(realmNames);

				for (String realm : realmNames) {
					JSONObject challenge = challenges.getJSONObject(realm);

					WLChallengeHandler handler = WLClient.getInstance().getWLChallengeHandler(realm);

					if (handler == null) {
						FWUtils.error(new StringBuilder().append("Application will exit, because unexpected challenge handler arrived while using realm ").append(realm)
								.append(". Register the challenge handler using registerChallengeHandler().").toString());

						showErrorDialogue(bundle.getString("WLClient.error"), bundle.getString(AUTH_FAIL_ID), bundle.getString("WLClient.close"));
					} else {
						handler.startHandleChallenge(this, challenge);
					}
				}
			} catch (JSONException e) {

				FWUtils.debug(new StringBuilder().append("Wrong JSON arrived when processing a challenge in a 401 response. With ").append(e.getMessage()).toString(), e);
			}

			containsChallenges = true;
		} else if (isWl403(response)) {
			JSONObject responseJSON = response.getResponseJSON();
			try {
				JSONObject challenges = responseJSON.getJSONObject("WL-Authentication-Failure");
				JSONArray realms = challenges.names();

				for (int i = 0; i < realms.length(); i++) {
					String realm = realms.getString(i);
					JSONObject challenge = challenges.getJSONObject(realm);
					WLChallengeHandler handler = WLClient.getInstance().getWLChallengeHandler(realm);

					if (handler != null) {
						handler.handleFailure(challenge);
						handler.clearWaitingList();
					} else {
						StringBuilder b = new StringBuilder(bundle.getString("WLClient.accessDenied"));

						String reason = challenge.getString("reason");
						if (reason != null) {
							b.append(new StringBuilder().append("\nReason: ").append(reason).toString());
						}
						FWUtils.error(new StringBuilder()
								.append("The application will exit, because connect to Worklight server failed due to missing challenge handler to handle ").append(realm)
								.toString());

						// showErrorDialogue(bundle.getString("WLClient.error"),
						// b.toString(), bundle.getString("WLClient.close"));
					}
				}
			} catch (JSONException e) {
				FWUtils.debug(new StringBuilder().append("Wrong JSON arrived when processing a challenge in a 403 response. with ").append(e.getMessage()).toString(), e);
			}

			containsChallenges = true;
		} else {
			containsChallenges = handleCustomChallenges(response);
		}

		return containsChallenges;
	}

	private void showErrorDialogue(String string, String string2, String string3) {

	}

	private boolean isWl401(WLResponse response) {
		if (response.getStatus() == 401) {
			Header challengesHeader = response.getHeader("WWW-Authenticate");
			if ((challengesHeader != null) && (challengesHeader.getValue().equalsIgnoreCase("WL-Composite-Challenge"))) {
				return true;
			}
		}
		return false;
	}

	private boolean isWl403(WLResponse response) {
		try {
			return (response.getStatus() == 403) && (response.getResponseJSON() != null) && (response.getResponseJSON().get("WL-Authentication-Failure") != null);
		} catch (JSONException e) {
			FWUtils.error(e.getMessage(), e);
		}

		return false;
	}

	private void processSuccessResponse(WLResponse response) {
		try {
			FWCookieManager.handleResponseHeaders(response.getHeaders(), new URI(this.requestURL));
		} catch (URISyntaxException e) {
			triggerUnexpectedOnFailure(e);
			return;
		}

		this.requestListener.onSuccess(response);
	}

	private void processFailureResponse(WLResponse response) {
		try {
			FWCookieManager.handleResponseHeaders(response.getHeaders(), new URI(this.requestURL));
		} catch (URISyntaxException e) {
			triggerUnexpectedOnFailure(e);
			return;
		}

		WLFailResponse failResponse = new WLFailResponse(response);
		failResponse.setOptions(this.requestOptions);
		this.requestListener.onFailure(failResponse);
	}

	private boolean handleCustomChallenges(WLResponse response) {
		boolean containsChallenges = false;

		ChallengeHandler handler = WLClient.getInstance().getChallengeHandler(response);
		if (handler != null) {
			handler.startHandleChallenge(this, response);
			containsChallenges = true;
		}

		return containsChallenges;
	}

	private void resendRequest() {
		if (this.requestURL != null)
			sendRequest(this.requestURL);
		else
			FWUtils.debug("resendRequest failed: requestURL is null.");
	}
}
