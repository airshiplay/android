package com.airshiplay.framework.text;

import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class TextView extends android.widget.TextView {
	private Logger log = LoggerFactory.getLogger(TextView.class);

	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		log.debug("onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		log.debug("onMeasure width=%d,height=%d", getWidth(), getHeight());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		log.debug("onSizeChanged w=%d,h=%d,oldw=%d,oldh=%d", new Object[] { w,
				h, oldw, oldh });
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		log.debug("onTextChanged");
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		log.debug("onDraw");
		super.onDraw(canvas);
	}
}
