package com.airshiplay.mobile.android.framework.example.sliding;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.airshiplay.mobile.android.framework.R;
import com.airshiplay.mobile.android.framework.widget.SlidingLayout;

public class SlidingLayoutDemo extends Activity {
	private SlidingLayout mSlidingLayout;
	private View content;
	private View mSideBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidinglayout);
		content=getWindow().findViewById(android.R.id.content);
		initContentView();
		setContentView();
	}
	void initContentView(){
		mSlidingLayout =(SlidingLayout) findViewById(R.id.slidinglayout);
		mSideBar =findViewById(R.id.sidebar);
	}
	void setContentView(){
		new SlidingHandler(this, mSlidingLayout);
		mSlidingLayout.firstShow();
	}
}
