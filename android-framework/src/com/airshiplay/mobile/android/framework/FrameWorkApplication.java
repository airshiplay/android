package com.airshiplay.mobile.android.framework;

import com.airshiplay.mobile.android.framework.util.Devices;

import android.app.Application;

public class FrameWorkApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Devices.init(this);
	}
}
