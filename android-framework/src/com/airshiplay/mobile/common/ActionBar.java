package com.airshiplay.mobile.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class ActionBar {
	public static void closeInputMethod(Activity activity) {
		if ((activity != null) && (activity.getCurrentFocus() != null)) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive())
				inputMethodManager.hideSoftInputFromWindow(activity
						.getCurrentFocus().getWindowToken(), 0);
		}
	}

	private static void initBackBtn(final Activity activity) {
		Button backBtn = (Button) activity.findViewById(2131230809);
		if (backBtn != null)
			backBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					ActionBar.closeInputMethod(activity);
					activity.finish();
				}
			});
	}

	private static void initBackBtn(final Activity activity,
			final OnBackListener onBackListener) {
		Button localButton = (Button) activity.findViewById(2131230809);
		if (localButton != null)
			localButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					ActionBar.closeInputMethod(activity);
					if (onBackListener != null)
						onBackListener.onBackFinish();
				}
			});
	}

	public static void initRightBtn(Activity activity, int resId,
			View.OnClickListener onClickListener) {
		Button button = (Button) activity.findViewById(2131230811);
		if (button != null) {
			button.setText(activity.getString(resId));
			button.setOnClickListener(onClickListener);
		}
	}

	public static void initRightBtn(Activity activity, String text, int resid,
			View.OnClickListener onClickListener) {
		Button button = (Button) activity.findViewById(2131230811);
		if (button != null) {
			button.setText(text);
			button.setBackgroundResource(resid);
			button.setOnClickListener(onClickListener);
		}
	}

	public static void initTitle(Activity activity, int resId) {
		initBackBtn(activity);
		initTitleStr(activity, resId);
	}

	public static void initTitle(Activity activity, int resId,
			OnBackListener onBackListener) {
		initBackBtn(activity, onBackListener);
		initTitleStr(activity, resId);
	}

	public static void initTitle(Activity activity, String text) {
		initBackBtn(activity);
		initTitleStr(activity, text);
	}

	public static void initTitle(Activity activity, String text,
			OnBackListener onBackListener) {
		initBackBtn(activity, onBackListener);
		initTitleStr(activity, text);
	}

	/**
	 * @param activity
	 * @param resId
	 *            Resource id for the string
	 */
	private static void initTitleStr(Activity activity, int resId) {
		TextView textView = (TextView) activity.findViewById(2131230810);
		if (textView != null)
			textView.setText(activity.getString(resId));
	}

	private static void initTitleStr(Activity activity, String text) {
		TextView textView = (TextView) activity.findViewById(2131230810);
		if ((textView != null) && (text != null))
			textView.setText(text);
	}

	public static void setBackBtnVisibility(Activity activity, int visibility) {
		Button localButton = (Button) activity.findViewById(2131230809);
		if (localButton != null)
			localButton.setVisibility(visibility);
	}

	public static abstract interface OnBackListener {
		public abstract void onBackFinish();
	}
}
