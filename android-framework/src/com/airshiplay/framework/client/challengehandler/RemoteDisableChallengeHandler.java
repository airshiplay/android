 package com.airshiplay.framework.client.challengehandler;
 
 import org.json.JSONException;
import org.json.JSONObject;

import com.airshiplay.framework.client.FWUtils;
import com.airshiplay.framework.client.api.WLClient;
import com.airshiplay.framework.client.api.challengehandler.WLChallengeHandler;
 
 public class RemoteDisableChallengeHandler extends WLChallengeHandler
 {
   private static final String NOTIFY_MESAGE = "NOTIFY";
   private static final String PROTOCOL_MESSAGE_TYPE = "messageType";
   private static final String PROTOCOL_MESSAGE_ID = "messageId";
   private static final String PROTOCOL_MESSAGE = "message";
   private static final String PROTOCOL_DOWNLOAD_LINK = "downloadLink";
   private static final String NOTIFICATION_TITLE_ID = "WLClient.notificationTitle";
   private static final String APPLICATION_DISABLED_ID = "WLClient.applicationDenied";
   private static final String CLOSE_ID = "WLClient.close";
   private static final String GET_NEW_VERSION_ID = "WLClient.getNewVersion";
   private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";
   private static final String PROTOCOL_ERROR_MESSAGE = "Protocol Error - could not parse JSON object";
 
   public RemoteDisableChallengeHandler(String realm)
   {
     super(realm);
   }
 
   public void handleSuccess(JSONObject identity)
   {
   }
 
   public void handleFailure(JSONObject error)
   {
     try
     {
       String message = error.getString("message");
 
       String downloadLink = error.getString("downloadLink");
 
       createAndShowDisableDialogue(message, downloadLink);
     }
     catch (JSONException e)
     {
       FWUtils.error("Protocol Error - could not parse JSON object", e);
       throw new RuntimeException("Protocol Error - could not parse JSON object");
     }
   }
 
   private void createAndShowDisableDialogue(String message, String downloadLink)
   {
//     ResourceBundle bundle = ResourceBundle.getBundle("com/worklight/wlclient/messages", Locale.getDefault());
// 
//     Context ctx = WLClient.getInstance().getContext();
//     Intent intent = new Intent(ctx, UIActivity.class);
// 
//     intent.putExtra("action", "wl_remoteDisableRealm");
// 
//     intent.putExtra("dialogue_message", message);
// 
//     intent.putExtra("dialogue_title", bundle.getString("WLClient.applicationDenied"));
// 
//     intent.putExtra("positive_button_text", bundle.getString("WLClient.close"));
// 
//     if ((downloadLink != null) && (downloadLink.length() != 0) && (!downloadLink.equalsIgnoreCase("null")))
//     {
//       intent.putExtra("download_link", downloadLink);
// 
//       intent.putExtra("neutral_button_text", bundle.getString("WLClient.getNewVersion"));
//     }
// 
//     ctx.startActivity(intent);
   }
 
   public void handleChallenge(JSONObject challenge)
   {
     try
     {
       String message = challenge.getString("message");
       String messageId = challenge.getString("messageId");
       String messageType = challenge.getString("messageType");
 
       String storedMessageId = FWUtils.readWLPref(WLClient.getInstance().getContext(), "messageId");
 
       if (isDisplayMessageDialogue(storedMessageId, messageId, messageType))
       {
         createAndShowMessageDialogue(message, messageId);
 
         FWUtils.writeWLPref(WLClient.getInstance().getContext(), "messageId", messageId);
       }
       else
       {
         submitChallengeAnswer(messageId);
       }
     }
     catch (JSONException e)
     {
       FWUtils.error("Protocol Error - could not parse JSON object", e);
       throw new RuntimeException("Protocol Error - could not parse JSON object");
     }
   }
 
   private void createAndShowMessageDialogue(String message, String messageId)
   {
		// ResourceBundle bundle =
		// ResourceBundle.getBundle("com/worklight/wlclient/messages",
		// Locale.getDefault());
		//
		// Context ctx = WLClient.getInstance().getContext();
		// Intent intent = new Intent(ctx, UIActivity.class);
		//
		// intent.putExtra("action", "notify");
		//
		// intent.putExtra("dialogue_message", message);
		//
		// intent.putExtra("dialogue_title",
		// bundle.getString("WLClient.notificationTitle"));
		//
		// intent.putExtra("positive_button_text",
		// bundle.getString("WLClient.close"));
		//
		// ctx.startActivity(intent);
   }
 
   private boolean isDisplayMessageDialogue(String storedMessageId, String messageId, String messageType)
   {
     if ((messageType == null) || (!messageType.equalsIgnoreCase("NOTIFY")))
     {
       return true;
     }
 
     if ((storedMessageId == null) || (!storedMessageId.equalsIgnoreCase(messageId)))
     {
       return true;
     }
 
     return false;
   }
 }