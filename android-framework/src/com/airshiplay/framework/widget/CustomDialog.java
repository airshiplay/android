package com.airshiplay.framework.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

/**
 * @author airshiplay
 * @Create 2013-6-22
 * @version 1.0
 * @since 1.0
 */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public CustomDialog(Context context) {
		super(context);
	}

	public static class Builder extends AlertDialog.Builder {
		private AlertDialog.Builder builder;

		public Builder(Context context) {
			super(context);
			// builder = new AlertDialog.Builder(context);
		}
	}
}
