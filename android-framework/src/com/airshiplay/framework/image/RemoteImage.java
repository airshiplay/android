/**
 * 
 */
package com.airshiplay.framework.image;

import com.airshiplay.framework.util.BitmapUtil;
import com.airshiplay.framework.util.ScreenUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

/**
 * @author airshiplay
 * @Create Date 2013-3-10
 * @version 1.0
 * @since 1.0
 */
public class RemoteImage implements IImageCapturer {

	protected static ImageCache imageCache;
	protected boolean mAllowStop;
	protected Integer mCornerPixel;
	protected boolean mCut;
	protected boolean mRotate;
	protected boolean mRoundCorner;
	/** unit px */
	protected float mWidth;
	/** unit px */
	protected float mHeight;
	protected String url;

	public RemoteImage(String url) {
		this.url = url;
	}

	@Override
	public Bitmap get(Context context) {
		if (imageCache == null)
			return null;
		return imageCache.getBitmapFromMemory(this.url);
	}

	@Override
	public String getCacheKey() {
		return this.url;
	}

	@Override
	public void recycle() {
		Bitmap localBitmap = imageCache.getBitmapFromMemory(this.url);
		if (localBitmap != null) {
			imageCache.remove(this.url);
			localBitmap.recycle();
		}
	}

	@Override
	public Bitmap request(Context context) {
		if (imageCache == null) {
			imageCache = ImageCache.getImageCache(context);
		}
		if (TextUtils.isEmpty(this.url))
			return null;

		Bitmap bitmap = imageCache.getBitmapFromDisk(this.url, this.mWidth,
				this.mHeight);
		if (bitmap == null) {
			bitmap = BitmapLoader.downloadBitmap(this.url, this.mWidth,
					this.mHeight);
		}
		if (bitmap != null) {
			bitmap = setRoundCorner(bitmap);
			bitmap = getRotateImage(bitmap);
			bitmap = cutImageCenter(bitmap);
			if (bitmap != null) {
				imageCache.cacheBitmapToMemory(this.url, bitmap);
			}
		}
		if (this.mAllowStop)
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
			}
		return bitmap;
	}

	public static RemoteImage getListIcon(String url) {
		RemoteImage remoteImage = new RemoteImage(url);
		remoteImage.mWidth = ScreenUtil.dp2px(48);
		remoteImage.mHeight = ScreenUtil.dp2px(48);
		remoteImage.mRoundCorner = true;
		return remoteImage;
	}

	/**
	 * width fixed,screen width
	 * 
	 * @param url
	 * @return
	 */
	public static RemoteImage getScreenShot(String url) {
		return getScreenShot(url, false);
	}

	public static RemoteImage getScreenShot(String url, boolean halfWidth) {
		RemoteImage remoteImage = new RemoteImage(url);
		if (halfWidth)
			remoteImage.mWidth = ScreenUtil.resolutionXY[0] / 2;
		else {
			remoteImage.mWidth = ScreenUtil.resolutionXY[0];
		}
		remoteImage.mHeight = 1;
		return remoteImage;
	}

	/**
	 * Height 48dp fixed
	 * 
	 * @param url
	 * @return
	 */
	public static RemoteImage getWebIcon(String url) {
		return getWebIcon(url, false, false, false);
	}

	public static RemoteImage getWebIcon(String url, boolean screenHeight) {
		return getWebIcon(url, screenHeight, false, false);
	}

	public static RemoteImage getWebIcon(String url, boolean screenHeight,
			boolean allowStop, boolean cut) {
		RemoteImage remoteImage = new RemoteImage(url);
		if (screenHeight) {
			remoteImage.mHeight = ScreenUtil.resolutionXY[1];
		} else {
			remoteImage.mHeight = ScreenUtil.dp2px(48);
		}
		remoteImage.mWidth = 1;
		remoteImage.mAllowStop = allowStop;
		remoteImage.mCut = cut;
		return remoteImage;
	}

	/**
	 * 对超出屏幕的图片采用居中截取,把多余的边界抛弃
	 * 
	 * @param bitmap
	 * @return
	 */
	private Bitmap cutImageCenter(Bitmap bitmap) {
		if (!mCut)
			return bitmap;
		Bitmap resultBitmap = null;
		try {
			int outW = bitmap.getWidth() - ScreenUtil.resolutionXY[0];
			int outH = bitmap.getHeight() - ScreenUtil.resolutionXY[1];
			if ((outW > 0) && (outH > 0)) {
				resultBitmap = Bitmap.createBitmap(bitmap, outW / 2, outH / 2,
						ScreenUtil.resolutionXY[0], ScreenUtil.resolutionXY[1]);
				bitmap.recycle();
			} else if (outW > 0) {
				resultBitmap = Bitmap.createBitmap(bitmap, outW / 2, 0,
						ScreenUtil.resolutionXY[0], bitmap.getHeight());
				bitmap.recycle();
			} else if (outH > 0) {
				resultBitmap = Bitmap.createBitmap(bitmap, 0, outH / 2,
						bitmap.getWidth(), ScreenUtil.resolutionXY[1]);
				bitmap.recycle();
			} else {
				resultBitmap = bitmap;
			}
		} catch (OutOfMemoryError localOutOfMemoryError) {
			resultBitmap = bitmap;
		}
		return resultBitmap;
	}

	/**
	 * 图片旋转90
	 * 
	 * @param bitmap
	 * @return
	 */
	private Bitmap getRotateImage(Bitmap bitmap) {
		if (!mRotate || null == bitmap)
			return bitmap;
		return BitmapUtil.getVerticalImage(bitmap);
	}

	/**
	 * 图片增加圆角
	 * 
	 * @param bitmap
	 * @return
	 */
	private Bitmap setRoundCorner(Bitmap bitmap) {
		Bitmap resultBitmap = null;
		if (!mRoundCorner) {
			return bitmap;
		}
		if (mCornerPixel == null)
			resultBitmap = BitmapUtil.toRoundCorner(bitmap);
		else {
			resultBitmap = BitmapUtil.toRoundCorner(bitmap,
					mCornerPixel.intValue());
		}
		return resultBitmap;
	}

	public String getUrl() {
		return this.url;
	}

	public void setAllowStop(boolean allowStop) {
		this.mAllowStop = allowStop;
	}

	public void setHeight(float height) {
		this.mHeight = height;
	}

	public void setRotate(boolean rotate) {
		this.mRotate = rotate;
	}

	public void setRoundCorner(boolean roundCorner) {
		this.mRoundCorner = roundCorner;
	}

	public void setRoundCornerPixel(Integer roundCornerPixel) {
		this.mCornerPixel = roundCornerPixel;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setWidth(float width) {
		this.mWidth = width;
	}
}
