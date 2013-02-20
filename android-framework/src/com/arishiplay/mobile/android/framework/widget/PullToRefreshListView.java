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
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arishiplay.mobile.android.framework.R;

/**
 * @author airshiplay
 * @Create Date 2013-2-11
 * @version 1.0
 * @since 1.0
 */
public class PullToRefreshListView extends ListView {
	/** 松开可以刷新 */
	protected static final int RELEASE_TO_REFRESH = 0;
	/** 下拉可以刷新 */
	protected static final int PULL_TO_REFRESH = 1;
	/** 刷新中 */
	protected static final int REFRESHING = 2;
	/** 刷新完成 */
	protected static final int DONE = 3;
	protected static final int RATIO = 2;

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
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	protected void init(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mHeadView = (LinearLayout) this.mInflater.inflate(
				R.layout.overefresh_head, null);
		this.mArrowImageView = (ImageView) this.mHeadView
				.findViewById(R.id.arrowImageView);
		this.mArrowImageView.setMinimumWidth(70);
		this.mArrowImageView.setMinimumHeight(50);
		this.mProgressBar = (ProgressBar) this.mHeadView
				.findViewById(R.id.progressBar);
		this.mTipsTextView = (TextView) this.mHeadView
				.findViewById(R.id.tipsTextView);
		this.mLastUpdatedTextView = (TextView) this.mHeadView
				.findViewById(R.id.lastUpdatedTextView);
		measureView(this.mHeadView);
		this.mHeadContentHeight = this.mHeadView.getMeasuredHeight();
		this.mHeadContentWidth = this.mHeadView.getMeasuredWidth();
		this.mHeadView.setPadding(0, this.mHeadContentHeight * -1, 0, 0);
		this.mHeadView.invalidate();
		addHeaderView(this.mHeadView, null, false);
		this.mAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.mAnimation.setInterpolator(new LinearInterpolator());
		this.mAnimation.setDuration(250L);
		this.mAnimation.setFillAfter(true);

		this.mReverseAnimation = new RotateAnimation(-180, -360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.mReverseAnimation.setInterpolator(new LinearInterpolator());
		this.mReverseAnimation.setDuration(200L);
		this.mReverseAnimation.setFillAfter(true);
		this.mRefreshState = DONE;
		setListViewOverScrollModeNever();
	}

	protected void setListViewOverScrollModeNever() {
		if (android.os.Build.VERSION.SDK_INT >= 9)
			setOverScrollMode(OVER_SCROLL_NEVER);
	}

	protected void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null)
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int childHeightSpec = 0;
		if (p.height > 0)
			childHeightSpec = View.MeasureSpec.makeMeasureSpec(p.height,
					MeasureSpec.EXACTLY);
		else
			childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		child.measure(childWidthSpec, childHeightSpec);
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
		int y = (int) ev.getY();
		if ((!this.mIsRecored) && (getFirstVisiblePosition() == 0)) {
			if (Math.abs(ev.getY() - this.mLastMotionY) > Math.abs(ev.getX()
					- this.mLastMotionX))// 垂直 大于水平移动位置,开始记录计算
			{
				this.mIsRecored = true;
				this.mStartY = y;
			}
		}
		this.mLastMotionX = ev.getX();
		this.mLastMotionY = ev.getY();
		if ((REFRESHING != this.mRefreshState) && (this.mIsRecored)) {
			if (this.mRefreshState == RELEASE_TO_REFRESH) {
				setSelection(0);
				if ((y - this.mStartY) / 2 >= this.mHeadContentHeight
						|| y - this.mStartY <= 0) {
					if (y - this.mStartY > 0)
						return;
					this.mRefreshState = DONE;
					changeHeaderViewState();
					return;
				}
				this.mRefreshState = PULL_TO_REFRESH;
				changeHeaderViewState();
			}
			if (PULL_TO_REFRESH == this.mRefreshState) {
				setSelection(0);
				if ((y - this.mStartY) / 2 < this.mHeadContentHeight) {
					if (y - this.mStartY <= 0) {
						this.mRefreshState = DONE;
						changeHeaderViewState();
					}
					return;
				}
				this.mRefreshState = RELEASE_TO_REFRESH;
				this.mIsBack = false;
				changeHeaderViewState();
			}
		}// //////////
		if (DONE == this.mRefreshState) {
			if (y - this.mStartY > 0) {
				this.mRefreshState = PULL_TO_REFRESH;
				changeHeaderViewState();
				onPull();
			}
		}
		if (PULL_TO_REFRESH == this.mRefreshState) {
			this.mHeadView.setPadding(0, this.mHeadContentHeight * -1
					+ (y - this.mStartY) / 2, 0, 0);
		}
		if (this.mRefreshState == RELEASE_TO_REFRESH) {
			this.mHeadView.setPadding(0, (y - this.mStartY) / 2
					- this.mHeadContentHeight, 0, 0);
		}

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
			this.mTipsTextView.setText(R.string.overefresh_releasetorefresh);
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
			} else {
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
				this.mHeadView
						.setPadding(0, this.mHeadContentHeight * -1, 0, 0);
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
		if (DONE != this.mRefreshState && REFRESHING != this.mRefreshState) {
			this.mRefreshState = DONE;
			changeHeaderViewState();
			onRecover();
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
		String str2 = String.valueOf(this.mContext
				.getString(R.string.overefresh_update));
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
		this.mLastUpdatedTextView.setText(String.valueOf(this.mContext
				.getString(R.string.overefresh_update))
				+ simpleDateFormat.format(new Date()));
	}

	public abstract interface P2RListViewStateListener {
		public static final int STATE_LOADING = 1;
		public static final int STATE_PULL = 0;
		public static final int STATE_RECOVER = 2;

		public abstract void onStateChanged(int state);
	}
}
