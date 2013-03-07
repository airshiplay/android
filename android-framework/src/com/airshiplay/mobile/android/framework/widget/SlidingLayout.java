package com.airshiplay.mobile.android.framework.widget;

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

public class SlidingLayout extends ViewGroup implements GestureDetector.OnGestureListener {
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

	public SlidingLayout(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
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
			if (ev.getAction() == 0) {
				int i = (int) ev.getX();
				int j = (int) ev.getY();
				if ((this.mContent.getLeft() < i) && (this.mContent.getRight() > i) && (this.mContent.getTop() < j) && (this.mContent.getBottom() > j))
					closeSidebar();
			}
			bool = true;
		}
		return bool;
	}

	public void firstShow() {
		this.mSideBar.setVisibility(0);
		this.mOpened = true;
		requestLayout();
		if (this.mListener != null)
			this.mListener.onSidebarOpened();
	}

	public boolean isOpening() {
		return this.mOpened;
	}

	protected void measureChild(View paramView, int paramInt1, int paramInt2) {
		if (this.mSideBar == paramView) {
			int i = View.MeasureSpec.getMode(paramInt1);
			super.measureChild(paramView, View.MeasureSpec.makeMeasureSpec((int) (0.9D * getMeasuredWidth()), i), paramInt2);
			return;
		}
		super.measureChild(paramView, paramInt1, paramInt2);
	}

	public boolean onDown(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		if ((this.mContent.getLeft() < x) && (this.mContent.getRight() > x) && 
				(this.mContent.getTop() + SystemUtils.dp2px(42.0F) < y) && (this.mContent.getBottom() > y))
			return true;
		return false;
	}

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

	public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		boolean bool = true;
		if ((paramFloat1 > 0.0F) && (Math.abs(paramFloat1) > Math.abs(paramFloat2))) {
			closeSidebar();
			return bool;
		}
		int i = (int) paramMotionEvent1.getX();
		int j = (int) paramMotionEvent1.getY();
		if ((this.mContent.getLeft() >= i) || (this.mContent.getRight() <= i) || (this.mContent.getTop() >= j) || (this.mContent.getBottom() <= j))
			bool = false;
		return bool;
	}

	public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
		boolean bool = false;
		if (!isOpening())
			return bool;
		int i = paramMotionEvent.getAction();
		if ((i == 1) || (i == 0)) {
			int j = (int) paramMotionEvent.getX();
			int k = (int) paramMotionEvent.getY();
			if ((this.mContent.getLeft() < j) && (this.mContent.getRight() > j) && (this.mContent.getTop() < k) && (this.mContent.getBottom() > k)) {
				if (i == 0)
					this.mPressed = true;
				if ((this.mPressed) && (i == 1) && (this.mListener != null)) {
					this.mPressed = false;
					bool = this.mListener.onContentTouchedWhenOpening();
				} else {
					this.mPressed = false;
				}
			}
		}
		return bool;
	}

	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		this.mSideBar.layout(paramInt3 - this.mSidebarWidth, 0, paramInt3, 0 + this.mSideBar.getMeasuredHeight());
		if (this.mOpened) {
			this.mContent.layout(paramInt1 - this.mSidebarWidth, 0, paramInt3 - this.mSidebarWidth, paramInt4);
			return;
		}
		this.mContent.layout(paramInt1, 0, paramInt3, paramInt4);

	}

	public void onLongPress(MotionEvent paramMotionEvent) {
	}

	public void onMeasure(int paramInt1, int paramInt2) {
		super.onMeasure(paramInt1, paramInt2);
		super.measureChildren(paramInt1, paramInt2);
		this.mSidebarWidth = (13 * Devices.size[0] / 20);
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		boolean bool = false;
		if ((e1 == null) || (e2 == null))
			return bool;

		int i = (int) e1.getX();
		int j = (int) e1.getY();
		if ((this.mContent.getLeft() < i) && (this.mContent.getRight() > i) && (this.mContent.getTop() < j) && (this.mContent.getBottom() > j))
			bool = true;
		return bool;
	}

	public void onShowPress(MotionEvent paramMotionEvent) {
	}

	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
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

	public void setListener(SlidingLayout.SidebarListener paramSidebarListener) {
		this.mListener = paramSidebarListener;
	}

	public void toggleSidebar() {
		if (this.mContent.getAnimation() != null)
			return;
		if (this.mOpened) {
			this.mAnimation = new TranslateAnimation(0.0F, this.mSidebarWidth, 0.0F, 0.0F);
			this.mAnimation.setAnimationListener(this.mCloseListener);
		} else {
			this.mAnimation = new TranslateAnimation(0.0F, -this.mSidebarWidth, 0.0F, 0.0F);
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
