 package com.airshiplay.framework.client.api;
 
 import java.util.HashMap;
 import java.util.Map;
 import org.json.JSONArray;
 import org.json.JSONException;
 
 public class WLProcedureInvocationData
 {
   private Object[] parameters;
   private String adapter;
   private String procedure;
 
   public WLProcedureInvocationData(String adapter, String procedure)
   {
     this.adapter = adapter;
     this.procedure = procedure;
   }
 
   public void setParameters(Object[] parameters)
   {
     this.parameters = parameters;
   }
 
   public Map<String, String> getInvocationDataMap()
   {
     Map invocationData = new HashMap();
 
     JSONArray jsonParams = new JSONArray();
     if (this.parameters != null) {
       for (int i = 0; i < this.parameters.length; i++) {
         try {
           jsonParams.put(i, this.parameters[i]);
         } catch (JSONException e) {
           throw new RuntimeException(e);
         }
       }
     }
     invocationData.put("adapter", this.adapter);
     invocationData.put("procedure", this.procedure);
     invocationData.put("parameters", jsonParams.toString());
     return invocationData;
   }
 }