package com.airshiplay.framework.client.api;

import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class WLProcedureInvocationResponse extends WLProcedureInvocationResult
{
  @Deprecated
  public WLProcedureInvocationResponse(WLResponse response)
  {
    super(response);
  }

  @Deprecated
  public JSONObject getJSONResult()
    throws JSONException
  {
    return super.getResult();
  }
}