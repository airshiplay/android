/**
 * 
 */
package com.airshiplay.mobile.viewflow;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airshiplay.framework.R;

/**
 * @author airshiplay
 * @Create Date 2013-3-4
 * @version 1.0
 * @since 1.0
 */
public class BitmapFlowIndicator extends View implements FlowIndicator,
		Animation.AnimationListener {
	private int currentScroll;
	private int fadeOutTime;
	private int flowWidth;
	private FadeTimer timer;
	private ViewFlow viewFlow;
	private Animation animation;
	public Animation.AnimationListener animationListener = this;
	protected Bitmap inactiveBitmap;
	protected Bitmap activeBitmap;
	protected int activeRes;
	protected Context mContext;
	protected final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	protected int inactiveRes;
	protected int perHeight;
	protected int perWidth;

	private int lastScroll = -1;

	public BitmapFlowIndicator(Context context) {
		super(context);
		this.mContext = context;
	}

	public BitmapFlowIndicator(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		this.mContext = context;
		TypedArray a = context.obtainStyledAttributes(paramAttributeSet,
				R.styleable.BitmapFlowIndicator);
		this.inactiveRes = a.getResourceId(
				R.styleable.BitmapFlowIndicator_inactiveRes,
				R.drawable.viewflow_indicator_inactive);
		this.activeRes = a.getResourceId(
				R.styleable.BitmapFlowIndicator_activeRes,
				R.drawable.viewflow_indicator_active);
		this.fadeOutTime = a.getInt(
				R.styleable.BitmapFlowIndicator_fadeOutTime, 0);
		Resources resources = context.getResources();
		this.inactiveBitmap = BitmapFactory.decodeResource(resources,
				this.inactiveRes);
		this.perWidth = this.inactiveBitmap.getWidth();
		this.perHeight = this.inactiveBitmap.getHeight();
		this.activeBitmap = BitmapFactory.decodeResource(resources,
				this.activeRes);
		a.recycle();
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// We were told how big to be
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		// Measure the height
		else {
			result = (int) (2 * this.perHeight + getPaddingTop()
					+ getPaddingBottom() + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// We were told how big to be
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		// Calculate the width according the views count
		else {
			int count = 3;
			if (viewFlow != null) {
				count = viewFlow.getViewsCount();
			}
			result = (int) (getPaddingLeft() + getPaddingRight()
					+ (count * this.perWidth) + (count - 1) * this.perWidth / 2 + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private void resetTimer() {
		if (this.fadeOutTime > 0) {
			if ((this.timer != null) && (this.timer._run))
				this.timer.resetTimer();
			this.timer = new FadeTimer();
			this.timer.execute(new Void[0]);
		}
	}

	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		int count = 1;
		if (viewFlow != null) {
			count = viewFlow.getViewsCount();
		}
		float bitmapSeparation = this.perWidth * 3 / 2;
		int leftPadding = getPaddingLeft();
		for (int iLoop = 0; iLoop < count; iLoop++) {
			canvas.drawBitmap(inactiveBitmap, getPaddingLeft()
					+ bitmapSeparation * iLoop, getPaddingTop(), mPaint);
		}

		float cx = 0;
		if (flowWidth != 0) {
			// Draw the filled circle according to the current scroll
			cx = currentScroll * bitmapSeparation / flowWidth;
		}
		canvas.drawBitmap(activeBitmap, leftPadding + cx, getPaddingTop(),
				this.mPaint);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	public void onScrolled(int h, int v, int oldh, int oldv) {
		setVisibility(View.VISIBLE);
		resetTimer();
		this.currentScroll = h;
		this.flowWidth = this.viewFlow.getWidth();
		invalidate();
	}

	public void onSwitched(View view, int position) {
	}

	public void setBitmapRes(int activeResid, int inactiveResid) {
		Resources resources = this.mContext.getResources();
		this.inactiveBitmap = BitmapFactory.decodeResource(resources,
				inactiveResid);
		this.perWidth = this.inactiveBitmap.getWidth();
		this.perHeight = this.inactiveBitmap.getHeight();
		this.activeBitmap = BitmapFactory
				.decodeResource(resources, activeResid);
	}

	public void setViewFlow(ViewFlow viewFlow) {
		resetTimer();
		this.viewFlow = viewFlow;
		this.flowWidth = this.viewFlow.getWidth();
		invalidate();
	}

	class FadeTimer extends AsyncTask<Void, Void, Void> {
		private boolean _run = true;
		private int timer = 0;

		private FadeTimer() {
		}

		protected Void doInBackground(Void... params) {
			while (true) {
				if (!_run)
					return null;
				try {
					Thread.sleep(1);
					this.timer++;
					if (this.timer == BitmapFlowIndicator.this.fadeOutTime) {
						this._run = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		protected void onPostExecute(Void result) {
			animation = AnimationUtils.loadAnimation(getContext(),
					android.R.anim.fade_out);
			animation.setAnimationListener(animationListener);
			startAnimation(animation);
		}

		public void resetTimer() {
			this.timer = 0;
		}

	}

	public void onAnimationEnd(Animation animation) {
		setVisibility(View.GONE);
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}
