package com.airshiplay.framework.client.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.airshiplay.framework.client.FWUtils;

public class WLProcedureInvocationResult extends WLResponse
{
  private static final String JSON_KEY_IS_SUCCESSFUL = "isSuccessful";
  private JSONObject jsonResult;

  public WLProcedureInvocationResult(WLResponse response)
  {
    super(response);
  }

  public boolean isSuccessful() {
    boolean isSuccessful = false;
    try {
      isSuccessful = getResult().getBoolean("isSuccessful");
    }
    catch (JSONException e) {
    }
    return isSuccessful;
  }

  public JSONObject getResult() throws JSONException {
    if (this.jsonResult == null) {
      this.jsonResult = FWUtils.convertStringToJSON(this.responseText);
    }
    return this.jsonResult;
  }

  public String toString()
  {
    return "WLProcedureInvocationResult [isSuccessful=" + isSuccessful() + ", result=" + this.jsonResult + "]";
  }
}