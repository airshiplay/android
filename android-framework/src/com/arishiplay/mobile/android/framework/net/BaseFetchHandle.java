/**
 * 
 */
package com.arishiplay.mobile.android.framework.net;

import com.arishiplay.mobile.android.framework.data.NetFetcherImp;

import android.os.Handler;
import android.os.Message;

/**
 * @author airshiplay
 * @Create Date 2013-2-24
 * @version 1.0
 * @param <T>
 * @since 1.0
 */
public abstract class BaseFetchHandle<T> implements NetFetcherImp.IFetchHandler {
	private Handler mHandler;

	public BaseFetchHandle() {
		this.mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					onSuccess((T) msg.obj);
					break;
				case -99:
				case -100:
				default:
					onFailure((Throwable) msg.obj);
				}
			}
		};
	}

	/**
	 * 后台解析，网络请求返回的数据 ;解析成功后发送 {@link #sendSuccessMessage(Object)}
	 * 
	 * @param value
	 *            网络请求 返回的字符串
	 * @throws Exception
	 */
	protected abstract void onReceivedData(String value) throws Exception;

	/**
	 * handler 失败
	 * 
	 * @param tr
	 */
	protected abstract void onFailure(Throwable tr);

	/**
	 * handler成功 数据处理
	 * 
	 * @param t
	 */
	protected abstract void onSuccess(T t);

	@Override
	public void sendCompletedMessage(String value) {
		try {
			onReceivedData(value);
		} catch (Exception e) {
			sendErrorMessage(e);
		}
	}

	@Override
	public void sendErrorMessage(Throwable throwable) {
		sendErrorMessage(65436, throwable);
	}

	public void sendErrorMessage(int what, Throwable throwable) {
		Message message = this.mHandler.obtainMessage(what, throwable);
		this.mHandler.sendMessage(message);
	}

	public void sendSuccessMessage(T t) {
		Message message = this.mHandler.obtainMessage(0, t);
		this.mHandler.sendMessage(message);
	}
}
