package com.airshiplay.framework.download;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageTextView extends LinearLayout implements ViewState {
	private ImageView mImage;
	private TextView mText;
	private int orientation = VERTICAL;
	private int gravity = Gravity.CENTER;
	private Context mContext;

	public ImageTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ImageTextView(Context context) {
		super(context);
		init(context);
	}

	void init(Context context) {
		mContext = context;
		this.setOrientation(orientation);
		this.setGravity(gravity);
		mImage = new ImageView(context);
		mImage.setImageResource(android.R.drawable.ic_media_pause);
		addView(mImage, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		mText = new TextView(context);
		mText.setText(android.R.string.ok);
		addView(mText, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
	}

	@Override
	public ViewState findViewStateWithTag(Object tag) {
		View view = this.findViewWithTag(tag);
		if (view != null)
			if (view instanceof ViewState) {
				return (ViewState) view;
			}
		return null;
	}

	@Override
	public void updateState(int taskState) {

	}

	@Override
	public void updateProgressChange(int percent, long loadSize) {

	}

}
