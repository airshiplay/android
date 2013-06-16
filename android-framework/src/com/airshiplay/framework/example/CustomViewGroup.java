package com.airshiplay.framework.example;

import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.Scroller;

public class CustomViewGroup extends ViewGroup {
	private static Logger log = LoggerFactory.getLogger(CustomViewGroup.class);
	private static final int SNAP_VELOCITY = 300;
	private static final int INVALID_SCREEN = -1;
	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	private int mTouchSlop;
	private boolean mIsScrolling;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;
	private Scroller mScroller;
	private boolean mFirstLayout = true;
	private int mTouchState;
	private float mLastMotionX;

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
		mTouchSlop = vc.getScaledTouchSlop();
		mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
		mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
		mScroller = new Scroller(getContext());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		log.debug("onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		if (mFirstLayout) {
			mScroller.startScroll(0, 0, width / getChildCount(), 0, 0);
			mFirstLayout = false;
		}
	}

	@Override
	public void computeScroll() {
		log.debug("computeScroll CurrX=%d,CurrY=%d", mScroller.getCurrX(),
				mScroller.getCurrY());
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		log.debug("onScrollChanged l=%d,t=%d", l, t);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		log.debug("onLayout");
		int childLeft = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
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
		case MotionEvent.ACTION_DOWN: {
			mLastMotionX = ev.getX();
			break;
		}
		}

		// In general, we don't want to intercept touch events. They should be
		// handled by the child view.
		return false;
	}

	private int calculateDistanceX(MotionEvent ev) {
		return (int) Math.abs(ev.getX() - mLastMotionX);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		log.debug("onTouchEvent: action=%d", ev.getAction());
		final int action = ev.getAction();
		final float x = ev.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			// Remember where the motion event started
			mLastMotionX = x;

			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;

			break;
		case MotionEvent.ACTION_MOVE:
			final int deltaX = (int) (mLastMotionX - x);

			boolean xMoved = Math.abs(deltaX) > mTouchSlop;

			if (xMoved) {
				// Scroll if the user moved far enough along the X axis
				mTouchState = TOUCH_STATE_SCROLLING;

				if (mTouchState == TOUCH_STATE_SCROLLING) {
					// Scroll to follow the motion event

					mLastMotionX = x;

					final int scrollX = getScrollX();
					if (deltaX < 0) {
						if (scrollX > 0) {
							scrollBy(Math.max(-scrollX, deltaX), 0);
						}
					} else if (deltaX > 0) {
						final int availableToScroll = getChildAt(
								getChildCount() - 1).getRight()
								- scrollX - getWidth();
						if (availableToScroll > 0) {
							scrollBy(Math.min(availableToScroll, deltaX), 0);
						}
					}
					return true;
				}
				break;

			}
			// Here we actually handle the touch event (e.g. if the action is
			// ACTION_MOVE,
			// scroll this container).
			// This method will only be called if the touch event was
			// intercepted in
			// onInterceptTouchEvent
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int offset = getChildCount() * getWidth();
		if (getScrollX() <= 0)
			canvas.translate(500, 0);
		super.dispatchDraw(canvas);
		if (getScrollX() <= 0)
			canvas.translate(-500, 0);
	}
}