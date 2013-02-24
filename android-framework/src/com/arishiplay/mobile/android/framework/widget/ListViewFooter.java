/**
 * 
 */
package com.arishiplay.mobile.android.framework.widget;

import com.arishiplay.mobile.android.framework.R;

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
		this.mFooterView = paramLayoutInflater.inflate(R.layout.footer_wait,
				null);
		showFooterViewWaiting();
	}

	public View getFooterView() {
		return this.mFooterView;
	}

	public void hideFooterView() {
		this.mFooterView.setVisibility(View.GONE);
	}

	public void showFooterViewError(View.OnClickListener paramOnClickListener) {
		this.mFooterView.setVisibility(View.VISIBLE);
		this.mFooterView.findViewById(R.id.wait).setVisibility(View.INVISIBLE);
		this.mFooterView.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
		this.mFooterView.findViewById(R.id.refresh).setOnClickListener(
				paramOnClickListener);
	}

	public void showFooterViewError(String paramString,
			View.OnClickListener paramOnClickListener) {
		this.mFooterView.setVisibility(View.VISIBLE);
		this.mFooterView.findViewById(R.id.wait).setVisibility(View.INVISIBLE);
		TextView localTextView = (TextView) this.mFooterView
				.findViewById(R.id.refresh);
		localTextView.setVisibility(View.VISIBLE);
		localTextView.setOnClickListener(paramOnClickListener);
		localTextView.setText(paramString);
	}

	public void showFooterViewWaiting() {
		this.mFooterView.setVisibility(View.VISIBLE);
		this.mFooterView.findViewById(R.id.wait).setVisibility(View.VISIBLE);
		this.mFooterView.findViewById(R.id.refresh).setVisibility(
				View.INVISIBLE);
	}
}
