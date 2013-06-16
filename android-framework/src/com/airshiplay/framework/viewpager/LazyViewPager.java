package com.airshiplay.framework.viewpager;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author airshiplay
 * @Create 2013-6-1
 * @version 1.0
 * @since 1.0
 */
public class LazyViewPager extends ViewGroup {
	private static final String TAG = "LazyViewPager";
	private static final boolean DEBUG = false;
	private static final int DEFAULT_OFFSCREEN_PAGES = 0;
	private static final int INVALID_POINTER = -1;
	private static final int MAX_SETTLE_DURATION = 600;
	public static final int SCROLL_STATE_DRAGGING = 1;
	public static final int SCROLL_STATE_IDLE = 0;
	public static final int SCROLL_STATE_SETTLING = 2;
	private static final boolean USE_CACHE = false;
	private static final Interpolator sInterpolator = new Interpolator() {
		public final float getInterpolation(float paramFloat) {
			float f = paramFloat - 1.0F;
			return 1.0F + f * (f * f);
		}
	};
	ArrayList listenerList = new ArrayList();
	private int mActivePointerId = -1;
	private PagerAdapter mAdapter;
	private float mBaseLineFlingVelocity;
	private int mChildHeightMeasureSpec;
	private int mChildWidthMeasureSpec;
	private int mCurItem;
	private long mFakeDragBeginTime;
	private boolean mFakeDragging;
	private boolean mFirstLayout = true;
	private float mFlingVelocityInfluence;
	private boolean mInLayout;
	private float mInitialMotionX;
	private boolean mIsBeingDragged;
	private boolean mIsUnableToDrag;
	private final ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();
	private float mLastMotionX;
	private float mLastMotionY;
	private EdgeEffectCompat mLeftEdge;
	private Drawable mMarginDrawable;
	private int mMaximumVelocity;
	private int mMinimumVelocity;
	private int mOffscreenPageLimit = 0;
	private LazyViewPager.OnPageChangeListener mOnPageChangeListener;
	private int mPageMargin;
	private boolean mPopulatePending;
	private Parcelable mRestoredAdapterState = null;
	private ClassLoader mRestoredClassLoader = null;
	private int mRestoredCurItem = -1;
	private EdgeEffectCompat mRightEdge;
	private int mScrollState = 0;
	private Scroller mScroller;
	private boolean mScrolling;
	private boolean mScrollingCacheEnabled;
	private int mTouchSlop;
	private VelocityTracker mVelocityTracker;

	public LazyViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViewPager();
	}

	public LazyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViewPager();
	}

	public LazyViewPager(Context context) {
		super(context);
		initViewPager();
	}

	void initViewPager() {
		setWillNotDraw(false);
		setDescendantFocusability(262144);
		setFocusable(true);
		Context context = getContext();
		this.mScroller = new Scroller(context, sInterpolator);
		ViewConfiguration localViewConfiguration = ViewConfiguration
				.get(context);
		this.mTouchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(localViewConfiguration);
		this.mMinimumVelocity = localViewConfiguration
				.getScaledMinimumFlingVelocity();
		this.mMaximumVelocity = localViewConfiguration
				.getScaledMaximumFlingVelocity();
		this.mLeftEdge = new EdgeEffectCompat(context);
		this.mRightEdge = new EdgeEffectCompat(context);
		this.mBaseLineFlingVelocity = (2500.0F * context.getResources()
				.getDisplayMetrics().density);
		this.mFlingVelocityInfluence = 0.4F;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

	public abstract interface OnPageChangeListener {
		public abstract void onPageScrollStateChanged(int paramInt);

		public abstract void onPageScrolled(int paramInt1, float paramFloat,
				int paramInt2);

		public abstract void onPageSelected(int position);
	}
}
