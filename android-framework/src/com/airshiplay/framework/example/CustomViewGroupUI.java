package com.airshiplay.framework.example;

import com.airshiplay.framework.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class CustomViewGroupUI extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_viewgroup_ui);
		new LinearLayout(this);
	}
}
