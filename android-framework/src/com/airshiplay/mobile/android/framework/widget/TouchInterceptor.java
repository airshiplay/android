/**
 * 
 */
package com.airshiplay.mobile.android.framework.widget;

import com.airshiplay.mobile.android.framework.R;

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

	public TouchInterceptor(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public TouchInterceptor(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		this.mTempRect = new Rect();
		this.mTouchSlop = ViewConfiguration.get(paramContext)
				.getScaledTouchSlop();
		if (paramAttributeSet != null) {
			TypedArray a = getContext().obtainStyledAttributes(
					paramAttributeSet, R.styleable.TouchInterceptor, 0, 0);
			this.mItemHeightNormal = a.getDimensionPixelSize(0, 0);
			this.mItemHeightExpanded = a.getDimensionPixelSize(1,
					mItemHeightNormal);
			this.grabberId = a.getResourceId(2, 0);
			this.dragndropBackgroundColor = a.getColor(3, 1719697536);
			this.mRemoveMode = a.getInt(4, 0);
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
