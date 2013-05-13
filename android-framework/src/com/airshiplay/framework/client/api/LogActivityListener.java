 package com.airshiplay.framework.client.api;
 
 import com.airshiplay.framework.client.FWRequestListener;
import com.airshiplay.framework.client.FWUtils;
 
 class LogActivityListener
   implements FWRequestListener
 {
   public void onFailure(WLFailResponse failResponse)
   {
     FWUtils.error("Activity will not be logged in Worklight server using logActivity() because of " + failResponse.getErrorMsg());
   }
 
   public void onSuccess(WLResponse response)
   {
     FWUtils.debug("logActivity success. Response from server is " + response.toString());
   }
 }
