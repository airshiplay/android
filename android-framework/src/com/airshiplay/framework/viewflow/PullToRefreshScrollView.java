/**
 * 
 */
package com.airshiplay.framework.viewflow;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airshiplay.framework.R;
import com.airshiplay.framework.widget.PullToRefreshListView.P2RListViewStateListener;

/**
 * @author airshiplay
 * @Create Date 2013-3-5
 * @version 1.0
 * @since 1.0
 */
public class PullToRefreshScrollView extends ViewFlowScrollView {

	protected static final int RATIO = 2;
	protected static final int RELEASE_TO_REFRESH = 0;
	protected static final int PULL_TO_REFRESH = 1;
	protected static final int REFRESHING = 2;
	protected static final int DONE = 3;
	protected RotateAnimation mAnimation = null;
	protected ImageView mArrowImageView = null;
	protected boolean mCanReturn = false;
	protected Context mContext = null;
	protected int mHeadContentHeight = 0;
	protected int mHeadContentWidth = 0;
	protected LinearLayout mHeadView = null;
	protected LayoutInflater mInflater = null;
	protected boolean mIsBack = false;
	protected boolean mIsPullAble = true;
	protected boolean mIsRecored = false;
	protected float mLastMotionX = 0;
	protected float mLastMotionY = 0;
	protected TextView mLastUpdatedTextView = null;
	protected LinearLayout mLayout = null;
	protected P2RScrollViewStateListener mListener = null;
	protected ProgressBar mProgressBar = null;
	protected int mRefreshState = DONE;
	protected RotateAnimation mReverseAnimation = null;
	protected int mStartY = 0;
	protected TextView mTipsTextView = null;

	public PullToRefreshScrollView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullToRefreshScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
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
		addView(this.mHeadView);
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
		setListViewOverScrollModeNever();
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

	public void addChild(View view) {
		this.mLayout.addView(view);
	}

	public void addChild(View view, int index) {
		this.mLayout.addView(view, index);
	}

	/**
	 * 请求加载数据
	 */
	protected void onLoading() {
		if (this.mListener != null) {
			this.mListener
					.onStateChanged(P2RListViewStateListener.STATE_LOADING);
		}
	}

	/**
	 * pull，请求加载数据
	 */
	protected void onPull() {
		if (this.mListener != null)
			this.mListener.onStateChanged(P2RListViewStateListener.STATE_PULL);
	}

	/**
	 * 恢复，加载数据
	 */
	protected void onRecover() {
		if (this.mListener != null)
			this.mListener
					.onStateChanged(P2RListViewStateListener.STATE_RECOVER);
	}

	/**
	 * 数据加载完成
	 */
	public void onLoadingComplete() {
		this.mRefreshState = DONE;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		mLastUpdatedTextView.setText(String.valueOf(this.mContext
				.getString(R.string.overefresh_update))
				+ simpleDateFormat.format(new Date()));
		changeHeaderViewState();
		onRecover();
	}

	/**
	 * change HeaderView state
	 */
	protected void changeHeaderViewState() {

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}

	protected void setListViewOverScrollModeNever() {
		if (android.os.Build.VERSION.SDK_INT >= 9)
			setOverScrollMode(OVER_SCROLL_NEVER);
	}

	public void setListener(
			P2RScrollViewStateListener p2RScrollViewStateListener) {
		this.mListener = p2RScrollViewStateListener;
	}

	public abstract interface P2RScrollViewStateListener {
		public static final int STATE_LOADING = 1;
		public static final int STATE_PULL = 0;
		public static final int STATE_RECOVER = 2;

		public abstract void onStateChanged(int viewState);
	}
}
