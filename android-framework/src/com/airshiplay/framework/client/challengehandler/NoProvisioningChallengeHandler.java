package com.airshiplay.framework.client.challengehandler;

import org.json.JSONException;
import org.json.JSONObject;

import com.airshiplay.framework.client.api.challengehandler.BaseDeviceAuthChallengeHandler;

public class NoProvisioningChallengeHandler extends BaseDeviceAuthChallengeHandler
{
  public NoProvisioningChallengeHandler(String realm)
  {
    super(realm);
  }

  public void handleChallenge(JSONObject challenge)
  {
    try
    {
      getDeviceAuthDataAsync(challenge.getString("token"));
    }
    catch (JSONException e) {
      throw new RuntimeException("Error retrieving device data - JSON error");
    } catch (Exception e) {
      throw new RuntimeException("Error retrieving device data");
    }
  }

  protected void onDeviceAuthDataReady(JSONObject data)
    throws JSONException
  {
    JSONObject answer = new JSONObject();

    answer.put("ID", data);

    submitChallengeAnswer(answer);
  }

  public void handleFailure(JSONObject error)
  {
  }

  public void handleSuccess(JSONObject identity)
  {
  }
}