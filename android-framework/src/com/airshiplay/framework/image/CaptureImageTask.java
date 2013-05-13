/**
 * 
 */
package com.airshiplay.framework.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

/**
 * Thread Capture Image，（local file or remote http）
 * 
 * @author airshiplay
 * @Create Date 2013-3-10
 * @version 1.0
 * @since 1.0
 */
public class CaptureImageTask implements Runnable {
	private static final int BITMAP_READY = 0;
	private Context context;
	private IImageCapturer image;
	private OnCompleteHandler onCompleteHandler;

	public CaptureImageTask(Context context, IImageCapturer iImageCapturer) {
		this.image = iImageCapturer;
		this.context = context;
	}

	public void complete(Bitmap bitmap) {
		if ((this.onCompleteHandler != null) && (bitmap != null))
			this.onCompleteHandler.obtainMessage(BITMAP_READY, bitmap)
					.sendToTarget();
	}

	public IImageCapturer getImage() {
		return this.image;
	}

	@Override
	public void run() {
		if (this.image != null) {
			Bitmap bitmap = this.image.request(this.context);
			complete(bitmap);
			this.context = null;
		}
	}

	/**
	 * 
	 */
	public void cancel() {
		this.onCompleteHandler.cancel();
	}

	public class OnCompleteHandler extends Handler {
		private boolean cancelled = false;

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
		this.onCompleteHandler = onCompleteHandler;
	}
}
