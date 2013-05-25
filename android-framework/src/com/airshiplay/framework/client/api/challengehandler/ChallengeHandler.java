 package com.airshiplay.framework.client.api.challengehandler;
 
 import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.airshiplay.framework.client.AsynchronousRequestSender;
import com.airshiplay.framework.client.FWUtils;
import com.airshiplay.framework.client.api.WLClient;
import com.airshiplay.framework.client.api.WLProcedureInvocationData;
import com.airshiplay.framework.client.api.WLRequestOptions;
import com.airshiplay.framework.client.api.WLResponse;
import com.airshiplay.framework.client.api.WLResponseListener;
 
 public abstract class ChallengeHandler extends BaseChallengeHandler<WLResponse>
   implements WLResponseListener
 {
   public ChallengeHandler(String realm)
   {
     super(realm);
   }
 
   protected void submitSuccess(WLResponse response)
   {
     synchronized (this)
     {
       this.activeRequest.removeExpectedAnswer(getRealm());
 
       this.activeRequest = null;
 
       releaseWaitingList();
     }
   }
 
   public abstract boolean isCustomResponse(WLResponse paramWLResponse);
 
   protected void submitLoginForm(String requestURL, Map<String, String> requestParameters, Map<String, String> requestHeaders, int requestTimeoutInMilliseconds, String requestMethod)
   {
     FWUtils.debug("Request [login]");
 
     WLClient client = WLClient.getInstance();
     String url = null;
 
     if ((requestURL.indexOf("http") == 0) && (requestURL.indexOf(":") > 0)) {
       url = requestURL;
     } else {
       String extForm = client.getConfig().getAppURL().toExternalForm();
 
       if ((extForm.charAt(extForm.length() - 1) == '/') && (requestURL.length() > 0) && (requestURL.charAt(0) == '/')) {
         requestURL = requestURL.substring(1);
       }
       url = extForm + requestURL;
     }
 
     HttpRequestBase httpRequest = null;
 
     if (requestMethod.equalsIgnoreCase("post")) {
       httpRequest = new HttpPost(url);
       addPostParams((HttpPost)httpRequest, requestParameters);
     } else if (requestMethod.equalsIgnoreCase("get")) {
       httpRequest = new HttpGet(url);
       addGetParams((HttpGet)httpRequest, requestParameters);
     } else {
       throw new RuntimeException("CustomChallengeHandler.submitLoginForm: invalid request method.");
     }
 
     httpRequest.addHeader("x-wl-app-version", client.getConfig().getApplicationVersion());
 
     if (requestHeaders != null) {
       for (String headerName : requestHeaders.keySet()) {
         httpRequest.addHeader(headerName, (String)requestHeaders.get(headerName));
       }
     }
 
     AsynchronousRequestSender.getInstance().sendCustomRequestAsync(httpRequest, requestTimeoutInMilliseconds, this);
   }
 
   public void submitAdapterAuthentication(WLProcedureInvocationData invocationData, WLRequestOptions requestOptions) {
     if (requestOptions == null) {
       requestOptions = new WLRequestOptions();
     }
     requestOptions.setFromChallenge(true);
     WLClient.getInstance().sendInvoke(invocationData, this, requestOptions);
   }
 
   private void addPostParams(HttpPost postRequest, Map<String, String> requestParams) {
     List params = new ArrayList();
     params.add(new BasicNameValuePair("isAjaxRequest", "true"));
 
     if (requestParams != null) {
       for (String paramName : requestParams.keySet()) {
         params.add(new BasicNameValuePair(paramName, (String)requestParams.get(paramName)));
       }
     }
 
     UrlEncodedFormEntity encodedFormEntity = null;
     try {
       encodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
     } catch (UnsupportedEncodingException e) {
       FWUtils.error(e.getMessage(), e);
       throw new RuntimeException(e);
     }
 
     postRequest.setEntity(encodedFormEntity);
   }
 
   private void addGetParams(HttpGet getRequest, Map<String, String> requestParams) {
     HttpParams httpParams = new BasicHttpParams();
     httpParams.setParameter("isAjaxRequest", "true");
 
     for (String paramName : requestParams.keySet()) {
       httpParams.setParameter(paramName, requestParams.get(paramName));
     }
 
     getRequest.setParams(httpParams);
   }
 }