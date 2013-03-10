/**
 * 
 */
package com.airshiplay.mobile.android.framework.image;

import android.content.Context;
import android.graphics.Bitmap;

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
	protected float mHeight;
	protected boolean mRotate;
	protected boolean mRoundCorner;
	protected float mWidth;
	protected String url;

	public RemoteImage(String url) {
		this.url = url;
	}

	public static RemoteImage getListIcon(String url) {
		return null;
	}

	private Bitmap cutImageCenter(Bitmap bitmap) {
		return null;
	}

	private Bitmap getRotateImage(Bitmap bitmap) {
		return null;
	}

	public static RemoteImage getScreenShot(String url) {
		return getScreenShot(url, false);
	}

	public static RemoteImage getScreenShot(String url, boolean halfWidth) {
		return null;
	}

	public static RemoteImage getWebIcon(String url) {
		return getWebIcon(url, false, false, false);
	}

	public static RemoteImage getWebIcon(String url, boolean halfHeight) {
		return getWebIcon(url, halfHeight, false, false);
	}

	public static RemoteImage getWebIcon(String url, boolean halfHeight,
			boolean allowStop, boolean cut) {
		return null;
	}

	private Bitmap setRoundCorner(Bitmap bitmap) {
		return null;
	}

	@Override
	public Bitmap get(Context context) {
		return null;
	}

	@Override
	public String getCacheKey() {
		return null;
	}

	@Override
	public void recycle() {

	}

	@Override
	public Bitmap request(Context context) {
		return null;
	}

	public void setAllowStop(boolean paramBoolean) {
		this.mAllowStop = paramBoolean;
	}

	public void setHeight(float paramFloat) {
		this.mHeight = paramFloat;
	}

	public void setRotate(boolean paramBoolean) {
		this.mRotate = paramBoolean;
	}

	public void setRoundCorner(boolean paramBoolean) {
		this.mRoundCorner = paramBoolean;
	}

	public void setRoundCornerPixel(Integer paramInteger) {
		this.mCornerPixel = paramInteger;
	}

	public void setUrl(String paramString) {
		this.url = paramString;
	}

	public void setWidth(float paramFloat) {
		this.mWidth = paramFloat;
	}
}
