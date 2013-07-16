package com.airshiplay.framework.activity;

import android.app.Activity;
import android.os.Bundle;

import com.airshiplay.framework.R;
import com.airshiplay.mobile.application.MobileApplication;
import com.airshiplay.mobile.event.SystemEvent;
import com.airshiplay.mobile.event.SystemEvent.EventTypeData;

public abstract class BaseActivity extends Activity implements SystemEvent.EventListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setWindowAnimations(R.style.ActivityAnimationRightInLeftOut);
		SystemEvent.addListener(SystemEvent.EVENT_TYPE_RESTART, this);
		MobileApplication.getInstance().addAcitvity(this);
	}

	@Override
	public void onEvent(int eventType, EventTypeData eventTypeData) {
		switch (eventType) {
		case SystemEvent.EVENT_TYPE_RESTART:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MobileApplication.getInstance().remove(this);
	}
}
