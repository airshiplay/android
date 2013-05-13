package com.airshiplay.framework;

import com.airshiplay.framework.image.FWImageView;
import com.airshiplay.framework.image.RemoteImage;

import android.app.Activity;
import android.os.Bundle;
import android.sax.RootElement;
import android.widget.ImageView;

public class ImageActivity extends Activity {

	private ImageView img;
	private FWImageView tool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		img = new ImageView(this);
		tool = new FWImageView(img);
		setContentView(img);
		tool.setImage(RemoteImage.getListIcon("http://172.16.17.160:8181/xml/11.jpg"));
	}
}
