/**
 * 
 */
package com.airshiplay.framework.image;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.airshiplay.framework.util.BitmapUtil;

public class ImageCache {
	private static final String DISK_CACHE_PATH = "/web_image_cache/";
	private static final int MAX_SIZE = 30;
	private static String diskCachePath;
	private static ImageCache instance;
	private static boolean selfPath = true;
	private boolean diskCacheEnabled;

	protected LruCache<String, WeakReference<Bitmap>> memoryCache;

	private ImageCache(Context context) {
		this.memoryCache = new LruCache<String, WeakReference<Bitmap>>(MAX_SIZE);
		if (selfPath) {
			String baseDir = String.valueOf(context.getApplicationContext()
					.getCacheDir().getAbsolutePath());
			diskCachePath = baseDir + DISK_CACHE_PATH;
		}
		File file = new File(diskCachePath);
		file.mkdirs();
		this.diskCacheEnabled = file.exists();
	}

	public static ImageCache getImageCache(Context context) {
		if (instance == null)
			instance = new ImageCache(context);
		return instance;
	}

	public Bitmap getBitmapFromMemory(String url) {
		String str = BitmapLoader.getKeyByUrl(url);
		WeakReference<Bitmap> weakReference = (this.memoryCache).get(str);
		Bitmap bitmap = null;
		if (weakReference != null) {
			bitmap = (Bitmap) weakReference.get();
			if (bitmap != null) {
				if (bitmap.isRecycled())
					return null;
			} else {
				this.memoryCache.remove(str);
			}
		}
		return bitmap;
	}

	public void remove(String url) {
		if (url == null)
			return;
		String key = BitmapLoader.getKeyByUrl(url);
		this.memoryCache.remove(key);
		File file = new File(diskCachePath, key);
		if ((file.exists()) && (file.isFile()))
			file.delete();
	}

	public void cacheBitmapToMemory(String url, Bitmap bitmap) {
		if (url == null)
			return;
		String key = BitmapLoader.getKeyByUrl(url);
		memoryCache.put(key, new WeakReference<Bitmap>(bitmap));
	}

	private String getFilePath(String url) {
		StringBuilder buffer = new StringBuilder(String.valueOf(diskCachePath));
		buffer.append(BitmapLoader.getKeyByUrl(url));
		return buffer.toString();
	}

	public static void setCachePath(String diskPath) {
		if (TextUtils.isEmpty(diskPath))
			return;
		diskCachePath = diskPath;
		selfPath = true;
	}

	public Bitmap getBitmapFromDisk(String url) {
		if (TextUtils.isEmpty(url))
			return null;

		Bitmap bitmap = null;
		if (diskCacheEnabled) {
			String str = getFilePath(url);
			if (new File(str).exists())
				bitmap = BitmapLoader.getBitmapFromDisk(str);
		}
		return bitmap;

	}

	public void clear() {
		this.memoryCache.evictAll();
		File file = new File(diskCachePath);
		if(file.exists()){
			
		}
	}

	public Bitmap getBitmapFromDisk(String url, float scale) {
		if (TextUtils.isEmpty(url))
			return null;
		Bitmap bitmap = null;
		if (diskCacheEnabled) {
			String filePath = getFilePath(url);
			if (new File(filePath).exists()) {
				BitmapFactory.Options options = BitmapUtil
						.getScaleBitmapFactoryOption(scale);
				bitmap = BitmapLoader.getBitmapFromDisk(filePath, options);
			}
		}
		return bitmap;

	}

	public Bitmap getBitmapFromDisk(String url, float mWidth, float mHeight) {
		Bitmap bitmap = null;
		if (this.diskCacheEnabled) {
			String ilePath = getFilePath(url);
			if (new File(ilePath).exists())
				bitmap = BitmapLoader.getBitmapFromDisk(ilePath, mWidth,
						mHeight);
		}
		return bitmap;
	}

}
