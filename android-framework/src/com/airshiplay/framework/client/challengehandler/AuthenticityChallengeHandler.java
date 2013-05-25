package com.airshiplay.framework.client.challengehandler;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airshiplay.framework.client.FWUtils;
import com.airshiplay.framework.client.api.challengehandler.WLChallengeHandler;

public class AuthenticityChallengeHandler extends WLChallengeHandler {
	private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";
	private static final String AUTH_FAIL_ID = "WLClient.authFailure";
	private static final String INIT_FAILURE_TITLE_ID = "WLClient.wlclientInitFailure";
	private static final String CLOSE_ID = "WLClient.close";

	public AuthenticityChallengeHandler(String realm) {
		super(realm);
	}

	public void handleSuccess(JSONObject identity) {
	}

	public void handleFailure(JSONObject error) {
		// ResourceBundle bundle =
		// ResourceBundle.getBundle("com/worklight/wlclient/messages",
		// Locale.getDefault());
		//
		// Context ctx = WLClient.getInstance().getContext();
		// Intent intent = new Intent(ctx, UIActivity.class);
		//
		// intent.putExtra("action", "exit");
		//
		// intent.putExtra("dialogue_message",
		// bundle.getString("WLClient.authFailure"));
		//
		// intent.putExtra("dialogue_title",
		// bundle.getString("WLClient.wlclientInitFailure"));
		//
		// intent.putExtra("positive_button_text",
		// bundle.getString("WLClient.close"));
		//
		// ctx.startActivity(intent);
	}

	public void handleChallenge(JSONObject challenge) {
		try {
			String challengeData = challenge.getString("WL-Challenge-Data");
			int index = challengeData.indexOf("+");
			String allArgs = challengeData.substring(index + 1);
			String arg0 = challengeData.substring(0, index);

			List<String> args = Arrays.asList(allArgs.split("-"));
			JSONArray jsonArgs = new JSONArray(args);

			JSONArray data = new JSONArray();
			data.put(0, arg0);
			data.put(1, jsonArgs);

			// submitChallengeAnswer(SecurityUtils.hashDataFromJSON(WLClient.getInstance().getContext(),
			// data));
		} catch (JSONException e) {
			FWUtils.debug(e.getMessage());
			// } catch (UnsupportedEncodingException e) {
			// WLUtils.debug(e.getMessage());
		}
	}
}