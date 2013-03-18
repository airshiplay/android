/**
 * 
 */
package com.airshiplay.framework.util;

import java.io.File;
import java.io.IOException;

import com.airshiplay.framework.image.BitmapLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Environment;

/**
 * @author airshiplay
 * @Create Date 2013-3-18
 * @version 1.0
 * @since 1.0
 */
public class BitmapUtil {
	public static final Bitmap.CompressFormat COMPRESS_FORMAT;
	public static String SHARE_PIC_FILENAME;
	public static String TEMP_SCALE_PIC_FILENAME;
	public static int height;
	public static int scaleHeight = 320;
	public static int scaleWidth = 240;
	public static int width;

	static {
		height = 800;
		width = 600;
		COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
		File file = Environment.getExternalStorageDirectory();
		SHARE_PIC_FILENAME = file + "/framework/temp/Img20110317.jpg";
		TEMP_SCALE_PIC_FILENAME = file + "/framework/temp/Tmp20110317.jpg";
	}

	public static Bitmap composeBitmap(Bitmap bitmap1, Bitmap bitmap2) {
		Paint paint = null;
		Bitmap.Config localConfig = bitmap1.getConfig();
		Bitmap localBitmap = Bitmap.createBitmap(height, width, localConfig);
		if ((bitmap1 != null) && (bitmap2 != null)) {
			Canvas canvas = new Canvas(localBitmap);
			int k = height / 2;
			int m = bitmap1.getWidth();
			float f1 = (k - m) / 2;
			int n = width;
			int i1 = bitmap1.getHeight();
			float f2 = (n - i1) / 2;
			canvas.drawBitmap(bitmap1, f1, f2, paint);
			int i2 = height * 3 / 2;
			int i3 = bitmap2.getWidth();
			float f3 = (i2 - i3) / 2;
			int i4 = width;
			int i5 = bitmap2.getHeight();
			float f4 = (i4 - i5) / 2;
			canvas.drawBitmap(bitmap2, f3, f4, paint);
			canvas.save(31);
			canvas.restore();
		}
		return localBitmap;
	}

	private static File createFile(String filePath) {
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			if (file.getParentFile().mkdirs())
				try {
					if (!file.createNewFile())
						return null;
					return file;
				} catch (IOException e) {
				}
		} else {
			try {
				if (file.exists())
					file.delete();
				if (!file.createNewFile()) {
					return null;
				}
				return file;
			} catch (IOException e) {
			}
		}
		return null;
	}

	public static float getBalance(int h, int w, float height, float width) {
		return Math.min(h / height, w / width);
	}

	public static BitmapFactory.Options getScaleBitmapFactoryOption(float scale) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = (int) (1 / scale);
		options.inPurgeable = true;
		options.inInputShareable = true;
		return options;
	}

	public static void postScale(String loadFilePath, String saveFilePath,
			int h, int w) {
		Bitmap bitmap = BitmapLoader.getBitmapFromDisk(loadFilePath);
		if (bitmap != null)
			saveBitmap(zoomImage(bitmap, w, h), saveFilePath);
	}

	public static boolean saveBitmap(Bitmap paramBitmap, String paramString) {
		return false;
	}

	public static Bitmap toRoundCorner(Bitmap paramBitmap) {
		return toRoundCorner(paramBitmap, 10);
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int radius) {
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				try {
					Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
					Bitmap localBitmap = Bitmap.createBitmap(bitmap.getWidth(),
							bitmap.getHeight(), localConfig);
					Canvas canvas = new Canvas(localBitmap);
					Paint paint = new Paint();
					Rect rect = new Rect(0, 0, bitmap.getWidth(),
							bitmap.getHeight());
					RectF localRectF = new RectF(rect);
					paint.setAntiAlias(true);
					canvas.drawARGB(0, 0, 0, 0);
					paint.setColor(-12434878);
					canvas.drawRoundRect(localRectF, radius, radius, paint);
					paint.setXfermode(new PorterDuffXfermode(
							PorterDuff.Mode.SRC_IN));
					canvas.drawBitmap(bitmap, rect, rect, paint);
					bitmap.recycle();
					return localBitmap;
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	public static Bitmap zoomImage(Bitmap bitmap) {
		if (bitmap != null) {
			float scale = getBalance(width, height, bitmap.getHeight(),
					bitmap.getWidth());
			Matrix localMatrix = new Matrix();
			localMatrix.postScale(scale, scale);
			try {
				Bitmap localBitmap1 = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), localMatrix,
						true);
				return localBitmap1;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Bitmap zoomImage(Bitmap bitmap, int w, int h) {
		if (bitmap != null) {
			float scale = getBalance(h, w, bitmap.getHeight(),
					bitmap.getWidth());
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			try {
				Bitmap bmap = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				if (bitmap != null)
					bitmap.recycle();
				return bmap;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Bitmap getVerticalImage(Bitmap bitmap) {
		return rotate(bitmap, 90);
	}

	private static Bitmap rotate(Bitmap bitmap, int rotate) {
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				try {
					Matrix matrix = new Matrix();
					matrix.setRotate(rotate);
					Bitmap bmap = Bitmap
							.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
									bitmap.getHeight(), matrix, true);
					return bmap;
				} catch (Exception e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();

				}
			}
		}
		return null;
	}

	public static Bitmap scaleImage(Bitmap bitmap, float scale) {
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				try {
					Matrix matrix = new Matrix();
					matrix.postScale(scale, scale);
					Bitmap bmap = Bitmap
							.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
									bitmap.getHeight(), matrix, true);
					return bmap;
				} catch (Exception e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
