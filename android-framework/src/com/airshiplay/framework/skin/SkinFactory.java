package com.airshiplay.framework.skin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import com.airshiplay.mobile.util.DrawableUtil;
import com.airshiplay.mobile.util.ScreenUtil;

/**
 * 
 * zip资源包目录结构 res/drawable; res/drawable-hdpi; res/drawable-xhdpi;
 * 
 * @author airshiplay
 * @Create 2013-7-28
 * @version 1.0
 * @since 1.0
 */
public class SkinFactory {
	private static SkinFactory factory;
	private String zipPath;
	private String resourcePath;
	private Context mContext;
	private Resources resources;
	SparseArray<SoftReference<Drawable>> cache = new SparseArray<SoftReference<Drawable>>();

	public static SkinFactory getInstance(Context context) {
		if (factory == null) {
			factory = new SkinFactory(context);
			factory.resources = context.getResources();
		}
		return factory;
	}

	public SkinFactory(Context context) {
		this.mContext = context.getApplicationContext();
	}

	/**
	 * @param path
	 *            ZIP文件资源包路径
	 * @throws IOException
	 * @throws ZipException
	 */
	public void init(String path) throws ZipException, IOException {
		zipPath = path;
		File file = new File(path);
		if (!file.exists())
			return;
		ZipFile zip = new ZipFile(file);
	}

	/**
	 * @param resId
	 * @return
	 */
	public Drawable getDrawable(int resId) {
		SoftReference<Drawable> reference = cache.get(resId);
		if (reference != null) {
			Drawable drawable = reference.get();
			if (drawable != null)
				return DrawableUtil.getNewDrawable(drawable);
			cache.remove(resId);
		}
		String s = (resId + "");
		int index = s.lastIndexOf(".");
		if (index == -1)
			return null;
		String prefix = s.substring(index);
		index = prefix.indexOf("selector");
		if (index != -1) {
			prefix = prefix.substring(0, index);
		}
		if (ScreenUtil.density == 2) {
			String path = resourcePath + "/res/drawable-xhdpi";
			Drawable drawable = DrawableUtil.getDrawable(path, prefix, mContext.getResources());
			if (drawable != null) {
				cache.put(resId, new SoftReference<Drawable>(drawable));
				return drawable;
			}
		}
		Drawable drawable = DrawableUtil.getDrawable(resourcePath + "/res/drawable-hdpi", prefix, mContext.getResources());
		if (drawable != null) {
			cache.put(resId, new SoftReference<Drawable>(drawable));
		}
		return drawable;
	}

	public int getColor(int resId) {
		resources.getColor(resId);
		return 0;
	}

	public ColorStateList getColorStateList(int resId) {
		XmlPullParser parser;
		try {
			String s = (resId + "");
			int index = s.lastIndexOf(".");
			if (index == -1)
				return null;
			String prefix = s.substring(index);
			parser = XmlPullParserFactory.newInstance().newPullParser();
			String filename = resourcePath + "/" + prefix + ".xml";
			parser.setInput(new FileReader(filename));
			return ColorStateList.createFromXml(resources, parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resources.getColorStateList(resId);
	}
}
