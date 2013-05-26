package com.airshiplay.framework.adapter;

import android.view.View;

public interface IListViewFooter {
	public View getFooterView();

	public void hideFooterView();

	public void showFooterViewError(View.OnClickListener onClickListener);

	public void showFooterViewError(String text,
			View.OnClickListener onClickListener);

	public void showFooterViewWaiting();
}
