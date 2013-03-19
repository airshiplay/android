/**
 * 
 */
package com.airshiplay.framework.image;

import com.airshiplay.framework.image.CaptureImageTask.OnCompleteHandler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * @author airshiplay
 * @Create Date 2013-3-10
 * @version 1.0
 * @since 1.0
 */
public class CaptureImageTask implements Runnable {

	/**
	 * @param localContext3
	 * @param paramIImageCapturer
	 */
	public CaptureImageTask(Context localContext3,
			IImageCapturer paramIImageCapturer) {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	public void cancel() {
		// TODO Auto-generated method stub

	}

	public class OnCompleteHandler extends Handler {
		private boolean cancelled;

		public void cancel() {
			this.cancelled = true;
		}

		public void handleMessage(Message paramMessage) {
			if (this.cancelled)
				return;
			onComplete();
		}

		public void onComplete() {
		}
	}

	/**
	 * @param onCompleteHandler
	 */
	public void setOnCompleteHandler(OnCompleteHandler onCompleteHandler) {
		// TODO Auto-generated method stub
		
	}
}
