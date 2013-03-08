package com.airshiplay.mobile.android.framework.application;

import android.app.Application;
import android.view.WindowManager;

import com.airshiplay.mobile.android.framework.util.Devices;

/**
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public class FWApplication extends Application {
	private WindowManager.LayoutParams winLayoutParams = new WindowManager.LayoutParams();
	private static FWApplication instance;

	public static FWApplication getInstance() {
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
