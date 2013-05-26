package com.airshiplay.framework.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;
import com.airshiplay.framework.widget.ListViewFooter;
import com.airshiplay.framework.widget.LoadingView;

public abstract class AbsListViewAdapter<T> extends BaseAdapter implements
		AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
	/** 请求数据中 0 */
	public static final int STATUS_LOADING = 0;
	/** 没有数据被显示 1 */
	public static final int STATUS_LIST_ISNULL = 1;
	/** 请求数据不成功（网络异常等） 2 */
	public static final int STATUS_ERROR = 2;
	/** pull请求数据中 3 */
	public static final int STATUS_PULL_LOADING = 3;
	/** 255 */
	public static final int STATUS_PENDDING = 255;
	/** 准备好，可以请求加载数据等操作 254 */
	public static final int STATUS_READY = 254;
	protected final Logger log = LoggerFactory
			.getLogger(AbsListViewAdapter.class);
	protected List<T> mObjects;
	protected ListView mListView;
	protected Context mContext;
	protected Object[] requests;
	protected IListViewFooter mFootView;
	protected ILoadingView mLoadView;

	int mCurrentStatus;

	public AbsListViewAdapter(Context context) {
		this(context, null);
	}

	public AbsListViewAdapter(Context context, ListView listView,
			Object... request) {
		init(context, listView, request);
	}

	private void init(Context context, ListView listView, Object... request) {
		this.mContext = context;
		this.mListView = listView;
		this.requests = request;
		mListView.setAdapter(this);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public T getItem(int position) {
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
			break;
		case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
			break;
		case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			break;
		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	protected abstract void onDataItemClick(View view, int position);

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		onDataItemClick(view, position);
	}

}
