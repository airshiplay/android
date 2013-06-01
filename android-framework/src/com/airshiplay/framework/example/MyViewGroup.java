package com.airshiplay.framework.example;

import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {
	private static Logger log = LoggerFactory.getLogger(MyViewGroup.class);
	private int mTouchSlop;
	private boolean mIsScrolling;
	private int mSlop;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;

	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyViewGroup(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		ViewConfiguration vc = ViewConfiguration.get(context);
		mSlop = vc.getScaledTouchSlop();
		mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
		mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		/*
		 * This method JUST determines whether we want to intercept the motion.
		 * If we return true, onTouchEvent will be called and we do the actual
		 * scrolling there.
		 */

		final int action = MotionEventCompat.getActionMasked(ev);
		log.debug("onInterceptTouchEvent: action=%d", action);
		// Always handle the case of the touch gesture being complete.
		if (action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			// Release the scroll.
			mIsScrolling = false;
			return false; // Do not intercept touch event, let the child handle
							// it
		}

		switch (action) {
		case MotionEvent.ACTION_MOVE: {
			if (mIsScrolling) {
				// We're currently scrolling, so yes, intercept the
				// touch event!
				return true;
			}

			// If the user has dragged her finger horizontally more than
			// the touch slop, start the scroll

			// left as an exercise for the reader
			final int xDiff = calculateDistanceX(ev);

			// Touch slop should be calculated using ViewConfiguration
			// constants.
			if (xDiff > mTouchSlop) {
				// Start scrolling!
				mIsScrolling = true;
				return true;
			}
			break;
		}
		}

		// In general, we don't want to intercept touch events. They should be
		// handled by the child view.
		return false;
	}

	private int calculateDistanceX(MotionEvent ev) {
		return 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		log.debug("onTouchEvent: action=%d", ev.getAction());
		// Here we actually handle the touch event (e.g. if the action is
		// ACTION_MOVE,
		// scroll this container).
		// This method will only be called if the touch event was intercepted in
		// onInterceptTouchEvent
		return true;
	}
}
