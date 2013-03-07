package com.airshiplay.mobile.android.framework.sliding;

import com.airshiplay.mobile.android.framework.R;
import com.airshiplay.mobile.android.framework.util.Devices;
import com.airshiplay.mobile.android.framework.util.SystemUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author airshiplay
 * @Create Date 2013-3-7
 * @version 1.0
 * @since 1.0
 */
public class SlidingLayout extends ViewGroup implements
		GestureDetector.OnGestureListener {
	public static final int DURATION = 500;
	protected Animation mAnimation = null;
	protected Animation.AnimationListener mCloseListener = null;
	protected View mContent = null;
	protected GestureDetector mDetector = new GestureDetector(this);
	protected SlidingLayout.SidebarListener mListener = null;
	protected Animation.AnimationListener mOpenListener = null;
	protected boolean mOpened = false;
	protected boolean mPressed = false;
	protected View mSideBar = null;
	protected int mSidebarWidth = 0;

	public SlidingLayout(Context context) {
		super(context);
	}

	public SlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void closeSidebar() {
		if (this.mOpened)
			toggleSidebar();
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean bool = false;
		if (!isOpening())
			bool = super.dispatchTouchEvent(ev);
		if (bool)
			return bool;
		if (!this.mDetector.onTouchEvent(ev)) {
			bool = super.dispatchTouchEvent(ev);
		} else {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				int i = (int) ev.getX();
				int j = (int) ev.getY();
				if ((this.mContent.getLeft() < i)
						&& (this.mContent.getRight() > i)
						&& (this.mContent.getTop() < j)
						&& (this.mContent.getBottom() > j))
					closeSidebar();
			}
			bool = true;
		}
		return bool;
	}

	public void firstShow() {
		this.mSideBar.setVisibility(View.VISIBLE);
		this.mOpened = true;
		requestLayout();
		if (this.mListener != null)
			this.mListener.onSidebarOpened();
	}

	public boolean isOpening() {
		return this.mOpened;
	}

	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec,
			int parentHeightMeasureSpec) {
		if (this.mSideBar == child) {
			super.measureChild(child, View.MeasureSpec.makeMeasureSpec(
					(int) (0.9D * getMeasuredWidth()),
					View.MeasureSpec.getMode(parentWidthMeasureSpec)),
					parentHeightMeasureSpec);
			return;
		}
		super.measureChild(child, parentWidthMeasureSpec,
				parentHeightMeasureSpec);
	}

	@Override
	public boolean onDown(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		if ((this.mContent.getLeft() < x) && (this.mContent.getRight() > x)
				&& (this.mContent.getTop() + SystemUtils.dp2px(42.0F) < y)
				&& (this.mContent.getBottom() > y))
			return true;
		return false;
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		this.mSideBar = findViewById(R.id.sidebar);
		this.mContent = findViewById(R.id.main);
		if (this.mSideBar == null)
			throw new NullPointerException("no view id = animation_sidebar");
		if (this.mContent == null)
			throw new NullPointerException("no view id = animation_content");
		this.mOpenListener = new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mSideBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mContent.clearAnimation();
				mOpened = true;
				requestLayout();
				if (mListener != null)
					mListener.onSidebarOpened();
			}

		};
		this.mCloseListener = new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mContent.clearAnimation();
				mSideBar.setVisibility(View.INVISIBLE);
				mOpened = false;
				requestLayout();
				if (mListener != null)
					mListener.onSidebarClosed();
			}
		};
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		boolean bool = true;
		if ((velocityX > 0.0F) && (Math.abs(velocityX) > Math.abs(velocityY))) {
			closeSidebar();
			return bool;
		}// MotionEvent e1, MotionEvent e2, float velocityX, float velocityY
		int x = (int) e1.getX();
		int y = (int) e1.getY();
		if ((this.mContent.getLeft() >= x) || (this.mContent.getRight() <= x)
				|| (this.mContent.getTop() >= y)
				|| (this.mContent.getBottom() <= y))
			bool = false;
		return bool;
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean bool = false;
		if (!isOpening())
			return bool;
		int action = ev.getAction();
		if ((action == MotionEvent.ACTION_UP)
				|| (action == MotionEvent.ACTION_DOWN)) {
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			if ((this.mContent.getLeft() < x) && (this.mContent.getRight() > x)
					&& (this.mContent.getTop() < y)
					&& (this.mContent.getBottom() > y)) {//触点位于content时
				if (action == MotionEvent.ACTION_DOWN)
					this.mPressed = true;
				if ((this.mPressed) && (action == MotionEvent.ACTION_UP)
						&& (this.mListener != null)) {
					this.mPressed = false;
					bool = this.mListener.onContentTouchedWhenOpening();
				} else {
					this.mPressed = false;
				}
			}
		}
		return bool;
	}

	protected void onLayout(boolean changed, int l, int t,
			int r, int b) {
		this.mSideBar.layout(r - this.mSidebarWidth, 0, r,
				0 + this.mSideBar.getMeasuredHeight());
		if (this.mOpened) {
			this.mContent.layout(l - this.mSidebarWidth, 0, r
					- this.mSidebarWidth, b);
			return;
		}
		this.mContent.layout(l, 0, r, b);
	}

	public void onLongPress(MotionEvent e) {
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		super.measureChildren(widthMeasureSpec, heightMeasureSpec);
		this.mSidebarWidth = (13 * Devices.size[0] / 20);
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		boolean bool = false;
		if ((e1 == null) || (e2 == null))
			return bool;

		int x = (int) e1.getX();
		int y = (int) e1.getY();
		if ((this.mContent.getLeft() < x) && (this.mContent.getRight() > x)
				&& (this.mContent.getTop() < y)
				&& (this.mContent.getBottom() > y))//触点位于content时
			bool = true;
		return bool;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public void openSidebar() {
		if (!this.mOpened)
			toggleSidebar();
	}

	public void resetLayout() {
		this.mContent.clearAnimation();
		this.mSideBar.setVisibility(4);
		this.mOpened = false;
		requestLayout();
		if (this.mListener != null)
			this.mListener.onSidebarClosed();
	}

	public void setListener(SlidingLayout.SidebarListener sidebarListener) {
		this.mListener = sidebarListener;
	}

	public void toggleSidebar() {
		if (this.mContent.getAnimation() != null)
			return;
		if (this.mOpened) {
			this.mAnimation = new TranslateAnimation(0.0F, this.mSidebarWidth,
					0.0F, 0.0F);
			this.mAnimation.setAnimationListener(this.mCloseListener);
		} else {
			this.mAnimation = new TranslateAnimation(0.0F, -this.mSidebarWidth,
					0.0F, 0.0F);
			this.mAnimation.setAnimationListener(this.mOpenListener);
		}
		this.mAnimation.setDuration(500L);
		this.mAnimation.setFillAfter(true);
		this.mAnimation.setFillEnabled(true);
		this.mContent.startAnimation(this.mAnimation);
	}

	public abstract interface SidebarListener {
		public abstract boolean onContentTouchedWhenOpening();

		public abstract void onSidebarClosed();

		public abstract void onSidebarOpened();
	}
}
