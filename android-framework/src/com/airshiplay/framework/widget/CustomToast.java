package com.airshiplay.framework.widget;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * @author airshiplay
 * @Create 2013-6-22
 * @version 1.0
 * @since 1.0
 */
public class CustomToast extends Toast {
	private static Toast single = null;

	public CustomToast(Context context) {
		super(context);
	}

	public static Toast makeSingleText(Context context, CharSequence text,
			int duration) {
		if (single == null) {
			single = makeText(context, text, duration);
		}
		return single;
	}

	public static Toast makeSingleText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		return makeSingleText(context, context.getResources().getText(resId),
				duration);
	}

	public static Toast makeText(Context context, CharSequence text,
			int duration) {
		return Toast.makeText(context, text, duration);
	}

	public static Toast makeText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId),
				duration);
	}

	public static class Builder {
		private Context mContext;
		private Toast toast;

		public Builder(Context context) {
			this.mContext = context;
			this.toast = new Toast(mContext);
		}

		public Builder setGravity(int gravity) {
			toast.setGravity(gravity, 0, 0);
			return this;
		}

		public Builder setText(CharSequence text) {
			toast.setText(text);
			return this;
		}

		public Builder setText(int resId) {
			toast.setText(resId);
			return this;
		}

		public Builder setDuration(int duration) {
			toast.setDuration(duration);
			return this;
		}

		public void show() {
			toast.show();
		}
	}
}
