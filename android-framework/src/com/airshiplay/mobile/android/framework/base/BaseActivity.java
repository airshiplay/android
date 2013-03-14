package com.airshiplay.mobile.android.framework.base;

import android.app.Activity;
import android.os.Bundle;

import com.airshiplay.mobile.android.framework.R;
import com.airshiplay.mobile.android.framework.application.FWApplication;
import com.airshiplay.mobile.android.framework.event.SystemEvent;
import com.airshiplay.mobile.android.framework.event.SystemEvent.EventTypeData;

public abstract class BaseActivity extends Activity implements SystemEvent.EventListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setWindowAnimations(R.style.ActivityAnimation1);
		SystemEvent.addListener(SystemEvent.EVENT_TYPE_RESTART, this);
		FWApplication.getInstance().addAcitvity(this);
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
		FWApplication.getInstance().remove(this);
	}
}
