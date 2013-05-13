 package com.airshiplay.framework.client.api;
 
 import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.airshiplay.framework.client.FWUtils;
 
 public final class WLProcedureInvocationFailResponse extends WLFailResponse
 {
   private static final String JSON_KEY_ERROR_MESSAGES = "errors";
   private JSONObject jsonResult;
 
   public WLProcedureInvocationFailResponse(HttpResponse httpResponse)
   {
     super(httpResponse);
   }
 
   public WLProcedureInvocationFailResponse(WLResponse response) {
     super(response);
   }
 
   public WLProcedureInvocationFailResponse(WLFailResponse response) {
     super(response);
     setErrorCode(response.getErrorCode());
     setErrorMsg(response.getErrorMsg());
   }
 
   public List<String> getProcedureInvocationErrors() {
     List errors = new ArrayList();
     try {
       if ((getResult() != null) && (getResult().get("errors") != null)) {
         errors = FWUtils.convertJSONArrayToList(getResult().getJSONArray("errors"));
       }
     }
     catch (JSONException e)
     {
     }
     return errors;
   }
 
   public JSONObject getResult() throws JSONException {
     if ((this.jsonResult == null) && (this.responseText != null) && (this.responseText.length() > 0)) {
       this.jsonResult = FWUtils.convertStringToJSON(this.responseText);
     }
     return this.jsonResult;
   }
 
   @Deprecated
   public JSONObject getJSONResult() throws JSONException {
     return getResult();
   }
 }