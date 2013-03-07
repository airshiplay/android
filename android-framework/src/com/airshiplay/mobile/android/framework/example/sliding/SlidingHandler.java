package com.airshiplay.mobile.android.framework.example.sliding;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.airshiplay.mobile.android.framework.R;
import com.airshiplay.mobile.android.framework.sliding.SlidingLayout;

/**
 * @author airshiplay
 * @Create Date 2013-3-7
 * @version 1.0
 * @since 1.0
 */
public class SlidingHandler implements View.OnClickListener,
		SlidingLayout.SidebarListener {
	private Activity main = null;
	private SlidingLayout mSlidingLayout = null;
	private boolean close = true;

	public SlidingHandler(Activity activity, SlidingLayout slidingLayout) {
		this.main = activity;
		this.mSlidingLayout = slidingLayout;
		this.mSlidingLayout.setListener(this);
		//对sidebar进行处理
		LinearLayout sideview = (LinearLayout) this.main
				.findViewById(R.id.layout_content);
	}

	@Override
	public boolean onContentTouchedWhenOpening() {
		return false;
	}

	@Override
	public void onSidebarClosed() {
		this.close = true;
	}

	@Override
	public void onSidebarOpened() {
		this.close = false;
	}

	@Override
	public void onClick(View v) {
		if (this.close)
			return;
	}

}
