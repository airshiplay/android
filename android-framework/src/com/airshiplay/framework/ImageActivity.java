package com.airshiplay.framework;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.airshiplay.framework.image.FWImageView;
import com.airshiplay.framework.image.RemoteImage;

public class ImageActivity extends Activity implements OnClickListener {

	private ImageView img;
	private FWImageView tool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView scroll = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		scroll.addView(layout);
		setContentView(scroll);
		tool = new FWImageView(new ImageView(this),android.R.drawable.star_big_off);
		layout.addView(tool.getImageView(), new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		tool.getImageView().setBackgroundColor(Color.BLUE);
		tool.getImageView().setOnClickListener(this);
		tool.setImage(RemoteImage
				.getWebIcon("http://172.16.17.160:8181/xml/22.jpg",true));
		
		tool = new FWImageView(new ImageView(this),android.R.drawable.star_big_off);
		layout.addView(tool.getImageView(), new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		tool.getImageView().setBackgroundColor(Color.RED);
		tool.getImageView().setOnClickListener(this);
		tool.setImage(RemoteImage
				.getScreenShot("http://172.16.17.160:8181/xml/33.jpg",true));
		
		
		tool = new FWImageView(new ImageView(this),android.R.drawable.star_big_off);
		layout.addView(tool.getImageView(), new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		tool.getImageView().setBackgroundColor(Color.WHITE);
		tool.getImageView().setOnClickListener(this);
		tool.setImage(RemoteImage
				.getListIcon("http://172.16.17.160:8181/xml/11.jpg"));
	}

	@Override
	public void onClick(View v) {
		FWImageView fw = (FWImageView) v.getTag();
		int width = ((ImageView) v).getWidth();
		int height = ((ImageView) v).getHeight();
		Toast.makeText(this, width + "*" + height, Toast.LENGTH_LONG).show();
	}
}
