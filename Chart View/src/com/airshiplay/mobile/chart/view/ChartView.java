package com.airshiplay.mobile.chart.view;

import com.airshiplay.mobile.chart.shape.BaseShape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author airshiplay
 * @Create Date 2013-2-3
 * @version 1.0
 * @since 1.0
 */
public class ChartView extends View {
	private ShapeDrawable mShapeDrawable;

	public ChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChartView(Context context) {
		super(context);
	}

	public ChartView(Context context, BaseShape shape) {
		super(context);
		init(shape);
	}

	void init(BaseShape shape) {
		mShapeDrawable = new ShapeDrawable(shape);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mShapeDrawable.getShape().resize(w, h);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		mShapeDrawable.draw(canvas);
	}
}
