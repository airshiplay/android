/**
 * 
 */
package com.airshiplay.mobile.android.framework.wheel;

import com.airshiplay.mobile.android.framework.util.FWResource;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.widget.Scroller;

/**
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public class WheelView extends View {

	private GestureDetector gestureDetector;
	private Scroller scroller;
	private FWResource fwResource;
	private GestureDetector.SimpleOnGestureListener gestureListener;

	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	public WheelView(Context context) {
		super(context);
		initData(context);
	}

	private void initData(Context context) {
		this.gestureDetector = new GestureDetector(context,
				this.gestureListener);
		this.gestureDetector.setIsLongpressEnabled(false);
		this.scroller = new Scroller(context);
		this.fwResource = FWResource.getInstance(context);
	}
}
