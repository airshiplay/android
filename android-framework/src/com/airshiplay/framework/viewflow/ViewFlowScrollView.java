/**
 * 
 */
package com.airshiplay.framework.viewflow;

import com.airshiplay.mobile.viewflow.ViewFlow;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author airshiplay
 * @Create Date 2013-3-5
 * @version 1.0
 * @since 1.0
 */
public class ViewFlowScrollView extends ScrollView {
	private String tag = "ViewFlowScroller";
	private ViewFlow mViewFlow;

	public ViewFlowScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ViewFlowScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewFlowScrollView(Context context) {
		super(context);
	}

	private RectF getRectF(View view) {
		if (null == view)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		RectF rectF = new RectF();
		rectF.left = location[0];
		rectF.right = location[0] + view.getWidth();
		rectF.top = location[1];
		rectF.bottom = location[1] + view.getHeight();
		return rectF;
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			if (null == mViewFlow)
				return super.onInterceptTouchEvent(ev);
			try {
				RectF rectF = getRectF(mViewFlow);
				if (rectF != null) {
					boolean contain = rectF
							.contains(ev.getRawX(), ev.getRawY());
					if (contain) {
						contain = this.mViewFlow.isScrolling();
						if (contain)
							return false;
						return super.onInterceptTouchEvent(ev);
					}
				}
			} catch (IllegalArgumentException e) {
				return super.onInterceptTouchEvent(ev);
			}
		} catch (Exception e) {
			return super.onInterceptTouchEvent(ev);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			return super.onTouchEvent(ev);
		} catch (IllegalArgumentException e) {
		}
		return false;
	}

	public void setViewFlow(ViewFlow viewFlow) {
		this.mViewFlow = viewFlow;
	}
}