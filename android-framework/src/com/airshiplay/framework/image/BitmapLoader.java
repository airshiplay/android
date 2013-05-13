/**
 * 
 */
package com.airshiplay.framework.image;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.airshiplay.framework.util.ScreenUtil;

/**
 * @author airshiplay
 * @Create Date 2013-3-18
 * @version 1.0
 * @since 1.0
 */
public class BitmapLoader {

	public static Bitmap downloadBitmap(String url, float mWidth, float mHeight) {
		Bitmap bitmap = null;
		if (!TextUtils.isEmpty(url)) {
			if (mWidth > 0 && mHeight > 0) {
				String filePath = downloadBitmap(url);
				if (!TextUtils.isEmpty(filePath))
					bitmap = getBitmapFromDisk(filePath, mWidth, mHeight);
			}
		}
		return bitmap;
	}

	/**
	 * @param url
	 * @return 下载文件后保存的路径
	 */
	public static String downloadBitmap(String url) {
		StringBuffer buffer = new StringBuffer((String) SystemConst.IMAGE_DIR);
		buffer.append(getKeyByUrl(url));
		if (downloadBitmap(url, buffer.toString()))
			return buffer.toString();
		return null;
	}

	public static boolean downloadBitmap(String urls, String saveFilePath) {
		HttpURLConnection urlConnection = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			URL url = new URL(urls);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream());
			out = new FileOutputStream(saveFilePath);
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}

		return false;
	}

	public static String getKeyByUrl(String url) {
		boolean bool = TextUtils.isEmpty(url);
		String str = null;
		if (!bool)
			str = String.valueOf(url.replaceAll("[.:/,%?&=]", "+")
					.replaceAll("[+]+", "+").hashCode());
		return str;
	}

	public static Bitmap getBitmapFromDisk(String loadFilePath) {
		return null;
	}

	public static Bitmap getBitmapFromDisk(String filepath, float width,
			float height) {
		Bitmap bitmap = null;
		if (TextUtils.isEmpty(filepath))
			return null;
		try {
			if (width > 0 && height > 0) {
				bitmap = BitmapFactory.decodeFile(filepath,
						getOptions(filepath, width, height));
			} else {
				bitmap = BitmapFactory.decodeFile(filepath);
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			new File(filepath).delete();
		}
		return bitmap;
	}

	public static Bitmap getBitmapFromDisk(String filepath,
			BitmapFactory.Options options) {
		try {
			return BitmapFactory.decodeFile(filepath, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			new File(filepath).delete();
		}
		return null;
	}

	/**
	 * @param filepath
	 *            complete path name for the file to be decoded.
	 * @param width
	 *            dp <0 不做处理，
	 * @param height
	 *            dp
	 * @return
	 */
	public static BitmapFactory.Options getOptions(String filepath,
			float width, float height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// true 不分配空间，解码，仅计算图片的原始高度、宽度
		BitmapFactory.decodeFile(filepath, options);
		if ((width > 0) || (height > 0)) {
			if ((width != 1) || (height <= 1)) {
				float w = ScreenUtil.dp2px(width);
				if (options.outWidth > w) {
					options.inDensity = 240;
					options.inTargetDensity = (int) (options.inDensity * w / options.outWidth);
					options.inScaled = true;
				}
			}
			float h = ScreenUtil.dp2px(height);
			if (options.outHeight > h) {
				options.inDensity = 240;
				options.inTargetDensity = (int) (options.inDensity * h / options.outHeight);
				options.inScaled = true;
			}
		}
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		return options;

	}
}
