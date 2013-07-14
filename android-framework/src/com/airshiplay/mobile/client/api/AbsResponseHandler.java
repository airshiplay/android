package com.airshiplay.mobile.client.api;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * @author airshiplay
 * @Create 2013-6-30
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsResponseHandler<T> extends Handler implements
		MobileResponseListener {
	private Class<T> cls;
	private int errorCode;
	private T t;

	public AbsResponseHandler(Class<T> c) {
		this.cls = c;
	}

	@Override
	public void onSuccess(MobileResponse response) {
		try {
			t = new Gson().fromJson(response.getResponseText(), cls);
			sendEmptyMessage(0);
			return;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		this.errorCode = MobileHttpError.EC_ParseException;
		sendEmptyMessage(1);
	}

	@Override
	public void onFailure(MobileFailResponse response) {
		errorCode=response.getErrorCode();
		sendEmptyMessage(1);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 0:
			onHandlerSuccess(t);
			break;
		case 1:
			onHandlerFailure(errorCode);
			break;
		default:
			break;
		}
	}

	public abstract void onHandlerSuccess(T t);

	public abstract void onHandlerFailure(int type);
}