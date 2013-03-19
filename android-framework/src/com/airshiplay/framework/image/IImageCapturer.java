/**
 * 
 */
package com.airshiplay.framework.image;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author airshiplay
 * @Create Date 2013-3-10
 * @version 1.0
 * @since 1.0
 */
public interface IImageCapturer {


	public abstract Bitmap get(Context context);

	public abstract String getCacheKey();

	public abstract void recycle();

	public abstract Bitmap request(Context context);
}
