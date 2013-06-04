package com.airshiplay.framework.example;

import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

public class CustomViewGroup extends ViewGroup {
	private static Logger log = LoggerFactory.getLogger(CustomViewGroup.class);
	private int mTouchSlop;
	private boolean mIsScrolling;
	private int mSlop;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;

	public CustomViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CustomViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomViewGroup(Context context) {
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
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//		// MeasureSpec.EXACTLY,MATCH_PARENT,精确尺寸
//		// MeasureSpec.AT_MOST,WRAP_CONTENT,最大尺寸 //MeasureSpec.UNSPECIFIED
//		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		
		
		
		setMeasuredDimension(
				getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
				getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
		
		 final int measuredWidth = getMeasuredWidth();
	        final int maxGutterSize = measuredWidth / 10;

	        // Children are just made to fill our space.
	        int childWidthSize = measuredWidth - getPaddingLeft() - getPaddingRight();
	        int childHeightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

	        /*
	         * Make sure all children have been properly measured. Decor views first.
	         * Right now we cheat and make this less complicated by assuming decor
	         * views won't intersect. We will pin to edges based on gravity.
	         */
	        int size = getChildCount();
	        for (int i = 0; i < size; ++i) {
	            final View child = getChildAt(i);
	            if (child.getVisibility() != GONE) {
	                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
	                if (lp != null ) {}
	            }
	        }


	        // Page views next.
	        size = getChildCount();
	        for (int i = 0; i < size; ++i) {
	            final View child = getChildAt(i);
	            if (child.getVisibility() != GONE) {}
	        }
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
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
