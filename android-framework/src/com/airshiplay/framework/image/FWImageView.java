/**
 * 
 */
package com.airshiplay.framework.image;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.airshiplay.framework.util.TelephoneUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

/**
 * @author airshiplay
 * @Create Date 2013-3-10
 * @version 1.0
 * @since 1.0
 */
public class FWImageView {
	public static final int KEY = 110;
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);
	private boolean SaveFlowMode;
	protected CaptureImageTask currentTask;
	private OnSetImageFinished finishedHandler;
	private boolean isRemoved;
	boolean mCanAnimation;
	private IImageCapturer mCapturer;
	protected Context mContext;
	protected Drawable mDefaultDrawable;
	private boolean mForce;

	protected WeakReference<ImageView> mImageViewRef;
	private boolean mOnlyLoadCache;

	/**
	 * @param imageView
	 */
	public FWImageView(ImageView imageView) {
		imageView.setTag(this);
		mContext = imageView.getContext().getApplicationContext();
		mImageViewRef = new WeakReference<ImageView>(imageView);
	}

	public FWImageView(ImageView imageView, Integer defaultDrawableResid) {
		mContext = imageView.getContext().getApplicationContext();
		imageView.setTag(this);
		mImageViewRef = new WeakReference<ImageView>(imageView);
		if (defaultDrawableResid != null) {
			mDefaultDrawable = mContext.getResources().getDrawable(
					defaultDrawableResid.intValue());
			imageView.setImageDrawable(mDefaultDrawable);
		}
	}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}

	private void setImage(Bitmap bitmap, boolean animate) {
		ImageView imageView = (ImageView) mImageViewRef.get();
		if (imageView == null)
			return;
		if (animate) {
			AlphaAnimation animation = new AlphaAnimation(0, 1);
			imageView.setImageBitmap(bitmap);
			imageView.setAnimation(animation);
			animation.setDuration(300L);
			animation.start();
		} else
			imageView.setImageBitmap(bitmap);
		if (finishedHandler != null)
			finishedHandler.onSetImageFinished();

	}

	public ImageView getImageView() {
		return (ImageView) mImageViewRef.get();
	}

	public void setDefautlImage() {
		if (mDefaultDrawable == null)
			return;
		ImageView imageView = (ImageView) mImageViewRef.get();
		if (imageView != null) {
			imageView.setImageDrawable(mDefaultDrawable);
		}

	}

	public void setFinishedHandler(OnSetImageFinished onSetImageFinished) {
		finishedHandler = onSetImageFinished;
	}

	public void setForceImage(boolean paramBoolean) {
		mForce = paramBoolean;
	}

	public void setImage() {
		if (mCapturer != null) {
			IImageCapturer localIImageCapturer = mCapturer;
			boolean bool = mOnlyLoadCache;
			setImage(localIImageCapturer, bool);
		}
	}

	public void setImage(IImageCapturer iImageCapturer) {
		setImage(iImageCapturer, false);
	}

	public void setImage(IImageCapturer iImageCapturer,
			OnSetImageFinished onSetImageFinished) {
		finishedHandler = onSetImageFinished;
		setImage(iImageCapturer, false);
	}

	public void setImage(IImageCapturer iImageCapturer, boolean onlyLoadCache) {
		setImage(iImageCapturer, onlyLoadCache, false);
	}

	public void setImage(IImageCapturer iImageCapturer, boolean onlyLoadCache,
			boolean animate) {
		if (iImageCapturer == null)
			return;
		if (!canLoad(mContext)) {
			setDefautlImage();
		} else {
			isRemoved = false;
			mCapturer = iImageCapturer;
			mOnlyLoadCache = onlyLoadCache;
			Bitmap bitmap = mCapturer.get(mContext);
			if ((bitmap != null) && (!bitmap.isRecycled())) {
				setImage(bitmap, animate);
			} else {
				setDefautlImage();
				if (!onlyLoadCache) {
					if (currentTask != null) {
						currentTask.cancel();
						currentTask = null;
					}
					currentTask = new CaptureImageTask(mContext, mCapturer);
					currentTask
							.setOnCompleteHandler(currentTask.new OnCompleteHandler() {

								@Override
								public void onComplete() {
									Bitmap bitmap = mCapturer.get(mContext);
									if (bitmap != null)
										setImage(bitmap, true);
									else
										setDefautlImage();
								}
							});
					threadPool.execute(currentTask);
				}
			}
		}

	}

	/**
	 * @param localContext1
	 * @return
	 */
	private boolean canLoad(Context context) {
		if (mForce)
			return true;
		if ((!TelephoneUtil.isWifiEnable(mContext)) && (SaveFlowMode))
			return false;
		else
			return true;
	}

	public void setImage(String url) {
		RemoteImage remoteImage = RemoteImage.getListIcon(url);
		setImage(remoteImage);
	}

	public void setImage(String url, boolean onlyLoadCache) {
		RemoteImage remoteImage = RemoteImage.getListIcon(url);
		setImage(remoteImage, onlyLoadCache);
	}

	public void setImageResource(int paramInt) {
		ImageView imageView = (ImageView) mImageViewRef.get();
		if (imageView != null) {
			mCapturer = null;
			imageView.setImageResource(paramInt);
		}
	}

	public abstract interface OnSetImageFinished {
		public abstract void onSetImageFinished();
	}
}
