package com.airshiplay.framework;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.widget.TextView;

import com.airshiplay.mobile.event.SystemEvent;
import com.airshiplay.mobile.event.SystemEvent.EventTypeData;
import com.airshiplay.mobile.log.Logger;
import com.airshiplay.mobile.log.LoggerFactory;
import com.airshiplay.mobile.util.BitmapUtil;
import com.airshiplay.mobile.util.DrawableUtil;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class MainUI extends Activity implements SystemEvent.EventListener, View.OnClickListener {
	private static final Logger log = LoggerFactory.getLogger(MainUI.class);
	private TextView text;
	private TextView text1;
	private Drawable background;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.debug("onCreate");
		setContentView(R.layout.main);
		text = (TextView) findViewById(R.id.text);
		text.setOnClickListener(this);
		text1 = (TextView) findViewById(R.id.text1);
		text1.setOnClickListener(this);
		getFilesDir();
		getClass().getClassLoader();
		getClassLoader();
		getBaseContext();
		getApplicationContext();
		getPackageResourcePath();
		Thread.currentThread().getContextClassLoader();
		XmlPullParser parser;
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new FileReader("/sdcard/loading_page.xml"));
			// TypedValue outValue= new TypedValue();
			// getResources().getValue(R.xml.loading_page, outValue, true);
			// parser = getResources().getXml(R.xml.loading_page);
			StringBuffer buffer = new StringBuffer();
			XmlResourceParser  xmlResource;
			// int eventType = parser.getEventType();
			// while (eventType != XmlPullParser.END_DOCUMENT) {
			// if(eventType == XmlPullParser.START_DOCUMENT) {
			// System.out.println("Start document");
			// } else if(eventType == XmlPullParser.START_TAG) {
			// System.out.println("Start tag "+parser.getName());
			// buffer.append("<"+parser.getName());
			// for(int i=0;i<parser.getAttributeCount();i++){
			// buffer.append("  "+parser.getAttributeName(i));
			// buffer.append("=\""+parser.getAttributeValue(i)+"\"");
			// }buffer.append(">");
			// } else if(eventType == XmlPullParser.END_TAG) {
			// System.out.println("End tag "+parser.getName());
			// buffer.append("</"+parser.getName()+">");
			// } else if(eventType == XmlPullParser.TEXT) {
			// System.out.println("Text "+parser.getText());
			// buffer.append(parser.getText());
			// }
			// eventType = parser.next();
			// }
			// System.out.println("End document");
			System.out.println(buffer);
			View v = LayoutInflater.from(getBaseContext()).inflate(parser, null);
			System.out.println(v);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onEvent(int eventType, EventTypeData eventTypeData) {

	}

	public void onClickStart(View v) {
		startActivity(new Intent().setClass(getApplicationContext(), PullToRefreshUI.class));
	}

	@Override
	public void onClick(View v) {
		Drawable d = v.getBackground();
		Drawable c = getResources().getDrawable(R.drawable.test_main_btn_bg);
		if (background != null) {
			v.setBackground(DrawableUtil.getNewDrawable(background));
			return;
		}
		Bitmap bit = BitmapUtil.getBitmap("/sdcard/test_main_btn_bg_pressed.9.png");
		DrawableUtil.getDrawable(bit, getResources());
		background = DrawableUtil.getDrawable(bit, getResources(), R.drawable.test_main_btn_bg_pressed);
		v.setBackground(background);
	}

}
