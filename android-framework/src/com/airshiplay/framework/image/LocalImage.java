package com.airshiplay.framework.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.airshiplay.mobile.util.ScreenUtil;

public class LocalImage implements IImageCapturer {
	protected static ImageCache webImageCache;
	private int defaultImage = 0;
	private String filePath;
	private int height = 100;
	private int sampleSize;
	private int width = 140;

	public LocalImage(String filepath) {
		this.filePath = filepath;
	}

	/**
	 * @param filepath
	 * @param width unit dp
	 * @param height unit dp
	 */
	public LocalImage(String filepath, int width, int height) {
		this.filePath = filepath;
		this.width = width;
		this.height = height;
	}

	@Override
	public Bitmap get(Context context) {
		if (webImageCache == null)
			return null;
		return webImageCache.getBitmapFromMemory(filePath);
	}

	@Override
	public String getCacheKey() {
		return this.filePath;
	}

	@Override
	public void recycle() {
		Bitmap bitmap = webImageCache.getBitmapFromMemory(filePath);
		if (bitmap != null) {
			webImageCache.remove(filePath);
			bitmap.recycle();
		}
	}

	@Override
	public Bitmap request(Context context) {
		if (webImageCache == null)
			webImageCache = ImageCache.getImageCache(context);
		Bitmap bitmap = null;
		if (this.filePath != null) {
			bitmap = getImageResource(context, filePath);
			if (bitmap != null) {
				webImageCache.cacheBitmapToMemory(filePath, bitmap);
			}
		}
		return bitmap;
	}

	public Bitmap getImageResource(Context context, String filepath) {
		this.width = ScreenUtil.dp2px(context, width);
		this.height = ScreenUtil.dp2px(context, height);
		return getSketchPicture(context, filepath);
	}

	private Bitmap getSketchPicture(Context context, String filepath) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filepath, options);
			int hSample = (int) Math.ceil(options.outHeight / this.height);
			int wSample = (int) Math.ceil(options.outWidth / this.width);
			if ((hSample > 1) || (wSample > 1)) {
				if (hSample > wSample)
					options.inSampleSize = hSample;
				else
					options.inSampleSize = wSample;
			} else if (sampleSize != 0) {
				options.inSampleSize = sampleSize;
			}
			options.inJustDecodeBounds = false;
			return BitmapLoader.getBitmapFromDisk(filepath, options);

		} catch (Exception localException) {
			return BitmapFactory.decodeResource(context.getResources(), defaultImage);
		}
	}

	public int getSampleSize() {
		return this.sampleSize;
	}

	public int getDefaultImage() {
		return this.defaultImage;
	}

	public LocalImage setDefaultImage(int defaultResId) {
		this.defaultImage = defaultResId;
		return this;
	}

	public LocalImage setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
		return this;
	}
}