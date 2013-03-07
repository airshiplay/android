package com.airshiplay.mobile.android.framework.example.sliding;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.airshiplay.mobile.android.framework.R;
import com.airshiplay.mobile.android.framework.widget.SlidingLayout;

public class SlidingHandler implements View.OnClickListener, SlidingLayout.SidebarListener {
	private Activity main = null;
	private String b = null;
	private SlidingLayout d = null;
	private boolean e = false;
	private boolean f = false;
	private boolean g = true;
	  private View c = null;
	public SlidingHandler(Activity paramActivity, SlidingLayout paramSlidingLayout) {
		this.main = paramActivity;
		this.d = paramSlidingLayout;
		this.d.setListener(this);
		LinearLayout localLinearLayout1 = (LinearLayout) this.main.findViewById(R.id.layout_content);
		this.c = this.main.findViewById(2131231088);
	}

	@Override
	public boolean onContentTouchedWhenOpening() {
		return false;
	}

	@Override
	public void onSidebarClosed() {

	}

	@Override
	public void onSidebarOpened() {

	}

	@Override
	public void onClick(View v) {

	}

}
