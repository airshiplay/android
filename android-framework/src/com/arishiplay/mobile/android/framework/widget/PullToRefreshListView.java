/**
 * 
 */
package com.arishiplay.mobile.android.framework.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author airshiplay
 * @Create Date 2013-2-11
 * @version 1.0
 * @since 1.0
 */
public class PullToRefreshListView extends ListView {

	protected static final int PULL_TO_REFRESH = 1;
	protected static final int REFRESHING = 2;
	protected static final int DONE = 3;
	protected static final int RATIO = 2;

	protected static final int RELEASE_TO_REFRESH = 0;
	protected RotateAnimation mAnimation = null;
	protected ImageView mArrowImageView = null;
	protected Context mContext = null;
	protected int mHeadContentHeight;
	protected int mHeadContentWidth;
	protected LinearLayout mHeadView = null;
	protected LayoutInflater mInflater = null;
	protected boolean mIsBack;
	protected boolean mIsPullAble = true;
	protected boolean mIsRecored;
	protected float mLastMotionX;
	protected float mLastMotionY;
	protected TextView mLastUpdatedTextView = null;
	protected P2RListViewStateListener mListener = null;
	protected ProgressBar mProgressBar = null;
	protected int mRefreshState = DONE;
	protected RotateAnimation mReverseAnimation = null;
	protected int mStartY;
	protected TextView mTipsTextView = null;

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshListView(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (this.mIsPullAble) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				onTouochDown(ev);
				break;
			case MotionEvent.ACTION_UP:
				onTouchUp(ev);
				break;
			case MotionEvent.ACTION_MOVE:
				onTouchMove(ev);
				break;
			default:
				return super.dispatchTouchEvent(ev);
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * @param ev
	 */
	private void onTouchMove(MotionEvent ev) {

	}

	/**
	 * @param ev
	 */
	private void onTouchUp(MotionEvent ev) {
		switch (this.mRefreshState) {
		case REFRESHING:
			this.mIsRecored = false;
			this.mIsBack = false;
			break;
		case DONE:
			onRecover();
			break;
		case PULL_TO_REFRESH:
			this.mRefreshState = DONE;
			changeHeaderViewState();
			onRecover();
			break;
		case RELEASE_TO_REFRESH:
			this.mRefreshState = REFRESHING;
			changeHeaderViewState();
			onLoading();
			break;
		default:
			break;
		}
	}

	/**
	 * @param ev
	 */
	private void onTouochDown(MotionEvent ev) {
		this.mLastMotionX = ev.getX();
		this.mLastMotionY = ev.getY();
	}

	protected void changeHeaderViewState() {
		switch (this.mRefreshState) {
		case RELEASE_TO_REFRESH:
			this.mArrowImageView.setVisibility(View.VISIBLE);
			this.mProgressBar.setVisibility(View.GONE);
			this.mTipsTextView.setVisibility(View.VISIBLE);
			this.mLastUpdatedTextView.setVisibility(View.VISIBLE);
			this.mArrowImageView.clearAnimation();
			this.mArrowImageView.startAnimation(this.mAnimation);
			this.mTipsTextView.setText(2131297171);
			break;
		case PULL_TO_REFRESH:
			this.mProgressBar.setVisibility(View.GONE);
			this.mTipsTextView.setVisibility(View.VISIBLE);
			this.mLastUpdatedTextView.setVisibility(View.VISIBLE);
			this.mArrowImageView.clearAnimation();
			this.mArrowImageView.setVisibility(View.VISIBLE);
			if (this.mIsBack) {
				this.mIsBack = false;
				this.mArrowImageView.clearAnimation();
				this.mArrowImageView.startAnimation(this.mReverseAnimation);
				this.mTipsTextView.setText("");
			}
			break;

		case REFRESHING:
			this.mProgressBar.setVisibility(View.GONE);
			this.mTipsTextView.setVisibility(View.VISIBLE);
			this.mLastUpdatedTextView.setVisibility(View.VISIBLE);
			this.mArrowImageView.clearAnimation();
			this.mArrowImageView.setVisibility(View.VISIBLE);
			if (this.mIsBack) {
				this.mIsBack = false;
				this.mArrowImageView.clearAnimation();
				this.mArrowImageView.startAnimation(this.mReverseAnimation);
				this.mTipsTextView.setText("");
			}else{
				this.mHeadView.setPadding(0, 0, 0, 0);
		        this.mProgressBar.setVisibility(View.VISIBLE);
		        this.mArrowImageView.clearAnimation();
		        this.mArrowImageView.setVisibility(View.GONE);
		        this.mTipsTextView.setText(2131297173);
		        this.mLastUpdatedTextView.setVisibility(View.VISIBLE);
			}
			break;
		case DONE:
			this.mProgressBar.setVisibility(View.GONE);
			this.mTipsTextView.setVisibility(View.VISIBLE);
			this.mLastUpdatedTextView.setVisibility(View.VISIBLE);
			this.mArrowImageView.clearAnimation();
			this.mArrowImageView.setVisibility(View.VISIBLE);
			if (this.mIsBack) {
				this.mIsBack = false;
				this.mArrowImageView.clearAnimation();
				this.mArrowImageView.startAnimation(this.mReverseAnimation);
				this.mTipsTextView.setText("");
			} else {
				this.mHeadView.setPadding(0, this.mHeadContentHeight * -1, 0, 0);
				this.mProgressBar.setVisibility(View.GONE);
				this.mArrowImageView.clearAnimation();
				this.mArrowImageView.setImageResource(2130837971);
				this.mTipsTextView.setText("");
				this.mLastUpdatedTextView.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
	}

	public void onAttached() {
		if (DONE != this.mRefreshState) {
			if (REFRESHING != this.mRefreshState) {
				this.mRefreshState = DONE;
				changeHeaderViewState();
				onRecover();
			}
		}
	}

	protected void onLoading() {
		if (this.mListener != null) {
			this.mListener
					.onStateChanged(P2RListViewStateListener.STATE_LOADING);
		}
	}

	public void onLoadingComplete() {
		this.mRefreshState = DONE;
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				"MM-dd HH:mm");
		Date localDate = new Date();
		String str1 = localSimpleDateFormat.format(localDate);
		TextView localTextView = this.mLastUpdatedTextView;
		String str2 = String.valueOf(this.mContext.getString(2131297172));
		String str3 = str2 + str1;
		localTextView.setText(str3);
		changeHeaderViewState();
		onRecover();
	}

	protected void onPull() {
		if (this.mListener != null)
			this.mListener.onStateChanged(P2RListViewStateListener.STATE_PULL);
	}

	protected void onRecover() {
		if (this.mListener != null)
			this.mListener
					.onStateChanged(P2RListViewStateListener.STATE_RECOVER);
	}

	public void setListener(
			P2RListViewStateListener paramP2RListViewStateListener) {
		this.mListener = paramP2RListViewStateListener;
	}

	public void setPullEnable(boolean paramBoolean) {
		this.mIsPullAble = paramBoolean;
	}

	public void setRecover() {
		this.mRefreshState = 3;
		changeHeaderViewState();
		onRecover();
	}

	public void setRefreshTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		Date date = new Date();
		String str3 = String.valueOf(this.mContext.getString(2131297172))
				+ simpleDateFormat.format(date);
		this.mLastUpdatedTextView.setText(str3);
	}

	public abstract interface P2RListViewStateListener {
		public static final int STATE_LOADING = 1;
		public static final int STATE_PULL = 0;
		public static final int STATE_RECOVER = 2;

		public abstract void onStateChanged(int paramInt);
	}
}
