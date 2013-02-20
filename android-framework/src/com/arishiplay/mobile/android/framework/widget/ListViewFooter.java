/**
 * 
 */
package com.arishiplay.mobile.android.framework.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * @author airshiplay
 * @Create Date 2013-2-11
 * @version 1.0
 * @since 1.0
 */
public class ListViewFooter {
	View mFooterView;

	public ListViewFooter(LayoutInflater paramLayoutInflater) {
		View localView = paramLayoutInflater.inflate(2130903110, null);
		this.mFooterView = localView;
		showFooterViewWaiting();
	}

	public View getFooterView() {
		return this.mFooterView;
	}

	public void hideFooterView() {
		this.mFooterView.setVisibility(8);
	}

	public void showFooterViewError(View.OnClickListener paramOnClickListener) {
		this.mFooterView.setVisibility(0);
		this.mFooterView.findViewById(2131230942).setVisibility(4);
		this.mFooterView.findViewById(2131230922).setVisibility(0);
		this.mFooterView.findViewById(2131230922).setOnClickListener(
				paramOnClickListener);
	}

	public void showFooterViewError(String paramString,
			View.OnClickListener paramOnClickListener) {
		this.mFooterView.setVisibility(0);
		this.mFooterView.findViewById(2131230942).setVisibility(4);
		TextView localTextView = (TextView) this.mFooterView
				.findViewById(2131230922);
		localTextView.setVisibility(0);
		localTextView.setOnClickListener(paramOnClickListener);
		localTextView.setText(paramString);
	}

	public void showFooterViewWaiting() {
		this.mFooterView.setVisibility(0);
		this.mFooterView.findViewById(2131230942).setVisibility(0);
		this.mFooterView.findViewById(2131230922).setVisibility(4);
	}
}
