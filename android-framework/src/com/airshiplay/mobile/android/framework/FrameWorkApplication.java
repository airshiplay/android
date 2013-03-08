package com.airshiplay.mobile.android.framework;

import android.app.Application;
import android.view.WindowManager;

import com.airshiplay.mobile.android.framework.util.Devices;

/**
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public class FrameWorkApplication extends Application {
	private WindowManager.LayoutParams winLayoutParams = new WindowManager.LayoutParams();
	private static FrameWorkApplication instance;

	public static FrameWorkApplication getInstance() {
		return instance;
	}

	public final WindowManager.LayoutParams getWinLayoutParams() {
		return this.winLayoutParams;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Devices.init(this);
	}
}
