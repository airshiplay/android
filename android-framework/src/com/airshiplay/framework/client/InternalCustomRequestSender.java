 package com.airshiplay.framework.client;
 
 import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;

import com.airshiplay.framework.client.api.WLClient;
import com.airshiplay.framework.client.api.WLErrorCode;
import com.airshiplay.framework.client.api.WLFailResponse;
import com.airshiplay.framework.client.api.WLRequestOptions;
import com.airshiplay.framework.client.api.WLResponse;
import com.airshiplay.framework.client.api.WLResponseListener;
 
 class InternalCustomRequestSender
   implements Runnable
 {
   WLResponseListener listener;
   int requestTimeoutInMilliseconds;
   HttpRequestBase httpRequest;
 
   protected InternalCustomRequestSender(HttpRequestBase httpRequest, int requestTimeoutInMilliseconds, WLResponseListener listener)
   {
     this.httpRequest = httpRequest;
     this.requestTimeoutInMilliseconds = requestTimeoutInMilliseconds;
     this.listener = listener;
   }
 
   public void run()
   {
     WLClient client = WLClient.getInstance();
     HttpClient httpClient = AsynchronousRequestSender.getHttpClient();
 
     if (this.requestTimeoutInMilliseconds > 0) {
       HttpConnectionParams.setSoTimeout(httpClient.getParams(), this.requestTimeoutInMilliseconds);
       HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), this.requestTimeoutInMilliseconds);
     }
 
     WLResponse response = null;
     try
     {
       HttpResponse httpResponse = httpClient.execute(this.httpRequest, client.GetHttpContext());
       response = new WLResponse(httpResponse);
     } catch (SocketTimeoutException e) {
       this.listener.onFailure(new WLFailResponse(WLErrorCode.REQUEST_TIMEOUT, WLErrorCode.REQUEST_TIMEOUT.getDescription(), new WLRequestOptions()));
       return;
     } catch (ConnectTimeoutException e) {
       this.listener.onFailure(new WLFailResponse(WLErrorCode.UNRESPONSIVE_HOST, WLErrorCode.UNRESPONSIVE_HOST.getDescription(), new WLRequestOptions()));
       return;
     } catch (Exception e) {
       this.listener.onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, WLErrorCode.UNEXPECTED_ERROR.getDescription(), new WLRequestOptions()));
       return;
     }
 
     this.listener.onSuccess(response);
   }
 }
