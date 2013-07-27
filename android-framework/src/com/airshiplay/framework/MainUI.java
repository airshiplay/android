package com.airshiplay.framework;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
public class MainUI extends Activity implements SystemEvent.EventListener,
		View.OnClickListener {
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

	}

	@Override
	public void onEvent(int eventType, EventTypeData eventTypeData) {

	}
	public void onClickStart(View v) {
		startActivity(new Intent().setClass(getApplicationContext(), PullToRefreshUI.class));
	}
	
	@Override
	public void onClick(View v) {
		Drawable d=v.getBackground();
		Drawable c=getResources().getDrawable(R.drawable.test_main_btn_bg);
		if (background != null) {
			v.setBackground(DrawableUtil.getNewDrawable(background));
			return;
		}
		Bitmap bit = BitmapUtil
				.getBitmap("/sdcard/test_main_btn_bg_pressed.9.png");
		DrawableUtil.getDrawable(bit, getResources());
		background = DrawableUtil.getDrawable(bit, getResources(),
				R.drawable.test_main_btn_bg_pressed);
		v.setBackground(background);
	}
}
