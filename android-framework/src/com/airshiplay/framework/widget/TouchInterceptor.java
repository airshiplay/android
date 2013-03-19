/**
 * 
 */
package com.airshiplay.framework.widget;

import com.airshiplay.framework.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author airshiplay
 * @Create Date 2013-3-7
 * @version 1.0
 * @since 1.0
 */
public class TouchInterceptor extends ListView {
	public static final int FLING = 0;
	public static final int SLIDE_LEFT = 2;
	public static final int SLIDE_RIGHT = 1;
	private int dragndropBackgroundColor;
	private int grabberId;
	private int mCoordOffset;
	private Bitmap mDragBitmap;
	private DragListener mDragListener;
	private int mDragPoint;
	private int mDragPos;
	private ImageView mDragView;
	private DropListener mDropListener;
	private int mFirstDragPos;
	private GestureDetector mGestureDetector;
	private int mHeight;
	private int mItemHeightExpanded;
	private int mItemHeightNormal;
	private int mLowerBound;
	private RemoveListener mRemoveListener;
	private int mRemoveMode;
	private Rect mTempRect;
	private final int mTouchSlop;
	private int mUpperBound;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowParams;

	public TouchInterceptor(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchInterceptor(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mTempRect = new Rect();
		this.mTouchSlop = ViewConfiguration.get(context)
				.getScaledTouchSlop();
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(
					attrs, R.styleable.TouchInterceptor, 0, 0);
			this.mItemHeightNormal = a.getDimensionPixelSize(R.styleable.TouchInterceptor_normal_height, 0);
			this.mItemHeightExpanded = a.getDimensionPixelSize(R.styleable.TouchInterceptor_expanded_height,
					mItemHeightNormal);
			this.grabberId = a.getResourceId(R.styleable.TouchInterceptor_grabber, 0);
			this.dragndropBackgroundColor = a.getColor(R.styleable.TouchInterceptor_dragndrop_background, 1719697536);
			this.mRemoveMode = a.getInt(R.styleable.TouchInterceptor_remove_mode, 0);
			a.recycle();
		}
	}

	public void setDragListener(DragListener paramDragListener) {
		this.mDragListener = paramDragListener;
	}

	public void setDropListener(DropListener paramDropListener) {
		this.mDropListener = paramDropListener;
	}

	public void setRemoveListener(RemoveListener paramRemoveListener) {
		this.mRemoveListener = paramRemoveListener;
	}

	public abstract interface DragListener {
		public abstract void drag(int paramInt1, int paramInt2);
	}

	public abstract interface DropListener {
		public abstract void drop(int paramInt1, int paramInt2);
	}

	public abstract interface RemoveListener {
		public abstract void remove(int paramInt);
	}
}
