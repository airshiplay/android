package com.airshiplay.framework.example;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {
	private int mTouchSlop;
	private boolean mIsScrolling;

	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewGroup(Context context) {
		super(context);
		
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

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Here we actually handle the touch event (e.g. if the action is
		// ACTION_MOVE,
		// scroll this container).
		// This method will only be called if the touch event was intercepted in
		// onInterceptTouchEvent
		
		
	}
}
