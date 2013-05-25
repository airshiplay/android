/**
 * 
 */
package com.airshiplay.framework.image;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Image Capturer Interface from Local or Remote
 * 
 * @author airshiplay
 * @Create Date 2013-3-10
 * @version 1.0
 * @since 1.0
 */
public interface IImageCapturer {

	public abstract Bitmap get(Context context);

	public abstract String getCacheKey();

	public abstract void recycle();

	/**
	 * request load bitmap（local or，remote）
	 * 
	 * @param context
	 * @return
	 */
	public abstract Bitmap request(Context context);
}
