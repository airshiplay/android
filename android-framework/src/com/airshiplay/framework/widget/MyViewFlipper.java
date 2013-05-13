package com.airshiplay.framework.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;
import android.widget.RemoteViews.RemoteView;

@RemoteView
public class MyViewFlipper extends ViewAnimator {
	  private static final String TAG = "ViewFlipper";
	    private static final boolean LOGD = false;

	    private static final int DEFAULT_INTERVAL = 3000;

	    private int mFlipInterval = DEFAULT_INTERVAL;
	    private boolean mAutoStart = false;

	    private boolean mRunning = false;
	    private boolean mStarted = false;
	    private boolean mVisible = false;
	    private boolean mUserPresent = true;

	    public MyViewFlipper(Context context) {
	        super(context);
	    }

	    public MyViewFlipper(Context context, AttributeSet attrs) {
	        super(context, attrs);

//	        TypedArray a = context.obtainStyledAttributes(attrs,
//	                com.android.internal.R.styleable.ViewFlipper);
//	        mFlipInterval = a.getInt(
//	                com.android.internal.R.styleable.ViewFlipper_flipInterval, DEFAULT_INTERVAL);
//	        mAutoStart = a.getBoolean(
//	                com.android.internal.R.styleable.ViewFlipper_autoStart, false);
//	        a.recycle();
	    }

	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            final String action = intent.getAction();
	            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
	                mUserPresent = false;
	                updateRunning();
	            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
	                mUserPresent = true;
	                updateRunning(false);
	            }
	        }
	    };

	    @Override
	    protected void onAttachedToWindow() {
	        super.onAttachedToWindow();

	        // Listen for broadcasts related to user-presence
	        final IntentFilter filter = new IntentFilter();
	        filter.addAction(Intent.ACTION_SCREEN_OFF);
	        filter.addAction(Intent.ACTION_USER_PRESENT);
	        getContext().registerReceiver(mReceiver, filter);

	        if (mAutoStart) {
	            // Automatically start when requested
	            startFlipping();
	        }
	    }

	    @Override
	    protected void onDetachedFromWindow() {
	        super.onDetachedFromWindow();
	        mVisible = false;

	        getContext().unregisterReceiver(mReceiver);
	        updateRunning();
	    }

	    @Override
	    protected void onWindowVisibilityChanged(int visibility) {
	        super.onWindowVisibilityChanged(visibility);
	        mVisible = visibility == VISIBLE;
	        updateRunning(false);
	    }

	    /**
	     * How long to wait before flipping to the next view
	     *
	     * @param milliseconds
	     *            time in milliseconds
	     */
	    //@android.view.RemotableViewMethod
	    public void setFlipInterval(int milliseconds) {
	        mFlipInterval = milliseconds;
	    }

	    /**
	     * Start a timer to cycle through child views
	     */
	    public void startFlipping() {
	        mStarted = true;
	        updateRunning();
	    }

	    /**
	     * No more flips
	     */
	    public void stopFlipping() {
	        mStarted = false;
	        updateRunning();
	    }

	    @Override
	    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
	        super.onInitializeAccessibilityEvent(event);
	        event.setClassName(ViewFlipper.class.getName());
	    }

	    @Override
	    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
	        super.onInitializeAccessibilityNodeInfo(info);
	        info.setClassName(ViewFlipper.class.getName());
	    }

	    /**
	     * Internal method to start or stop dispatching flip {@link Message} based
	     * on {@link #mRunning} and {@link #mVisible} state.
	     */
	    private void updateRunning() {
	        updateRunning(true);
	    }

	    /**
	     * Internal method to start or stop dispatching flip {@link Message} based
	     * on {@link #mRunning} and {@link #mVisible} state.
	     *
	     * @param flipNow Determines whether or not to execute the animation now, in
	     *            addition to queuing future flips. If omitted, defaults to
	     *            true.
	     */
	    private void updateRunning(boolean flipNow) {
	        boolean running = mVisible && mStarted && mUserPresent;
	        if (running != mRunning) {
	            if (running) {
	                //showOnly(mWhichChild, flipNow);
	                Message msg = mHandler.obtainMessage(FLIP_MSG);
	                mHandler.sendMessageDelayed(msg, mFlipInterval);
	            } else {
	                mHandler.removeMessages(FLIP_MSG);
	            }
	            mRunning = running;
	        }
	        if (LOGD) {
	            Log.d(TAG, "updateRunning() mVisible=" + mVisible + ", mStarted=" + mStarted
	                    + ", mUserPresent=" + mUserPresent + ", mRunning=" + mRunning);
	        }
	    }
	    void showOnly(int childIndex, boolean animate) {}
	    /**
	     * Returns true if the child views are flipping.
	     */
	    public boolean isFlipping() {
	        return mStarted;
	    }

	    /**
	     * Set if this view automatically calls {@link #startFlipping()} when it
	     * becomes attached to a window.
	     */
	    public void setAutoStart(boolean autoStart) {
	        mAutoStart = autoStart;
	    }

	    /**
	     * Returns true if this view automatically calls {@link #startFlipping()}
	     * when it becomes attached to a window.
	     */
	    public boolean isAutoStart() {
	        return mAutoStart;
	    }

	    private final int FLIP_MSG = 1;

	    private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            if (msg.what == FLIP_MSG) {
	                if (mRunning) {
	                    showNext();
	                    msg = obtainMessage(FLIP_MSG);
	                    sendMessageDelayed(msg, mFlipInterval);
	                }
	            }
	        }
	    };
}
