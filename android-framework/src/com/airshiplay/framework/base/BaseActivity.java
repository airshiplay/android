package com.airshiplay.framework.base;

import android.app.Activity;
import android.os.Bundle;

import com.airshiplay.framework.R;
import com.airshiplay.framework.application.FWApplication;
import com.airshiplay.framework.event.SystemEvent;
import com.airshiplay.framework.event.SystemEvent.EventTypeData;

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
