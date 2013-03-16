/**
 * 
 */
package com.airshiplay.framework.viewflow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author airshiplay
 * @Create Date 2013-3-4
 * @version 1.0
 * @since 1.0
 */
public class ViewFlowAdapter<T> extends BaseAdapter implements TitleProvider,
		IgnoreRectProvider {
	protected Context mContext;
	int[] mTitleResIds;
	protected View[] mViews;

	public ViewFlowAdapter(Context context, int[] titleResIds) {
		this.mContext = context;
		this.mTitleResIds = titleResIds;
		this.mViews = new View[titleResIds.length];
	}

	public ViewFlowAdapter(Context context, int[] titleResIds,
			View[] views) {
		this.mContext = context;
		this.mTitleResIds = titleResIds;
		this.mViews = views;
	}

	@Override
	public int getCount() {
		return this.mTitleResIds.length;
	}

	@Override
	public T getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mViews[position];
	}

	@Override
	public View getIgnoreRectView(int paramInt) {
		return null;
	}

	@Override
	public String getTitle(int position) {
		return mContext.getString(mTitleResIds[position]);
	}

}
