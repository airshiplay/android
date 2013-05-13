/**
 * 
 */
package com.airshiplay.framework.data.remote;

import com.airshiplay.framework.http.JSONUtil;

import android.text.TextUtils;

/**
 * @author airshiplay
 * @Create Date 2013-2-24
 * @version 1.0
 * @since 1.0
 */
public class NetFetcherImp implements Runnable {
	boolean isErrorRequest = false;
	ICallback mCallback;
	Throwable mErrorThrowable;
	IFetchHandler mFetchHandler;
	String mUrl;
	String responseContent;

	@Override
	public void run() {
		try {
			this.responseContent = doRequest();
			if (this.mFetchHandler != null) {
				this.mFetchHandler.sendCompletedMessage(this.responseContent);
			}
			requestCallback(0);
		} catch (Exception e) {
			e.printStackTrace();
			if (this.mFetchHandler != null)
				this.mFetchHandler.sendErrorMessage(e);
			requestCallback(65436);
		}
	}

	/**
	 * @param i
	 */
	private void requestCallback(int i) {
		if (this.mCallback != null) {
			this.mCallback.callback(i, this.responseContent);
		}
	}

	/**
	 * @return
	 */
	private String doRequest() throws Exception {
		boolean bool = TextUtils.isEmpty(this.mUrl);
		if (bool)
			return "";
		return JSONUtil.get(mUrl);
	}

	public void setCallback(ICallback mCallback) {
		this.mCallback = mCallback;
	}

	public void setHandler(IFetchHandler handler) {
		this.mFetchHandler = handler;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	/**
	 * 异步网络请求数据
	 */
	public void request() {
		new Thread(this).start();
	}

	/**
	 * 同步网络请求数据
	 */
	public void requestSync() {
		try {
			this.responseContent = doRequest();
			if (this.mFetchHandler != null) {
				mFetchHandler.sendCompletedMessage(responseContent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.mFetchHandler.sendErrorMessage(e);
			this.isErrorRequest = true;
		}
	}

	public abstract interface ICallback {
		public abstract void callback(int code, String responseContent);
	}

	public abstract interface IFetchHandler {
		public abstract void sendCompletedMessage(String responseContent);

		public abstract void sendErrorMessage(Throwable tr);
	}
}
