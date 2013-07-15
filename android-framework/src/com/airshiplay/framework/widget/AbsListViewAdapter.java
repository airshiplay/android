/**
 * 
 */
package com.airshiplay.framework.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.airshiplay.framework.widget.PullToRefreshListView.P2RListViewStateListener;
import com.airshiplay.mobile.log.Logger;
import com.airshiplay.mobile.log.LoggerFactory;
import com.airshiplay.mobile.util.TelephoneUtil;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

/**
 * @author airshiplay
 * @Create Date 2013-2-11
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsListViewAdapter<T> extends BaseAdapter implements
		AbsListView.OnScrollListener, Filterable {
	private static final Logger log = LoggerFactory.getLogger(AbsListViewAdapter.class);
	/**
	 * Contains the list of objects that represent the data of this
	 * ArrayAdapter. The content of this list is referred to as "the array" in
	 * the documentation.
	 */
	protected List<T> mObjects;

	/**
	 * Lock used to modify the content of {@link #mObjects}. Any write operation
	 * performed on the array should be synchronized on this lock. This lock is
	 * also used by the filter (see {@link #getFilter()} to make a synchronized
	 * copy of the original array of data.
	 */
	protected final Object mLock = new Object();

	/**
	 * The resource indicating what views to inflate to display the content of
	 * this array adapter.
	 */
	private int mResource;

	/**
	 * The resource indicating what views to inflate to display the content of
	 * this array adapter in a drop down widget.
	 */
	private int mDropDownResource;

	/**
	 * If the inflated resource is not a TextView, {@link #mFieldId} is used to
	 * find a TextView inside the inflated views hierarchy. This field must
	 * contain the identifier that matches the one defined in the resource file.
	 */
	private int mFieldId = 0;

	/**
	 * Indicates whether or not {@link #notifyDataSetChanged()} must be called
	 * whenever {@link #mObjects} is modified.
	 */
	private boolean mNotifyOnChange = true;

	protected Context mContext;

	// A copy of the original mObjects array, initialized from and then used
	// instead as soon as
	// the mFilter ArrayFilter is used. mObjects will then only contain the
	// filtered values.
	protected ArrayList<T> mOriginalValues;
	protected Filter mFilter;

	protected LayoutInflater mInflater;

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
	protected ListView mListView;
	protected boolean isFilter;
	protected boolean isLastPage;
	protected boolean isLocalMode;
	protected boolean mAddFooter;
	int mCurrentStatus;
	private View.OnClickListener mErrorClickLisenter;
	protected ListViewFooter mFootView;
	protected LoadingView mLoadView;
	protected String mNoDataTip;
	Handler mOnScrollChangedHandle;
	protected boolean mOnScrolleIdle;
	protected String mUrl;

	private int mIndex;

	public AbsListViewAdapter(Context context, ListView listview, String url) {
		init(context, listview, url);
	}

	private void init(Context context, ListView listview, String url) {
		mContext = context;
		mListView = listview;
		mUrl = url;
		// ///////////

		mObjects = new ArrayList<T>();
		mInflater = LayoutInflater.from(mContext);
		if (mListView == null)
			throw new NullPointerException("listview is null");
		if ((this.mListView instanceof PullToRefreshListView)) {
			((PullToRefreshListView) this.mListView)
					.setListener(new PullToRefreshListView.P2RListViewStateListener() {

						@Override
						public void onStateChanged(int state) {
							onListViewStateChanged(state);
						}
					});
		}
		mListView.setAdapter(this);
		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(new AbsListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position;
				if ((mListView.getHeaderViewsCount() > 0)
						|| (mListView.getFooterViewsCount() > 0)) {
					index = position - mListView.getHeaderViewsCount();
				}
				if ((index >= 0) && (index < mObjects.size())) {
					onDataItemClick(view, index);
				}
			}
		});
		mErrorClickLisenter = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				request();
			}
		};
		mOnScrollChangedHandle = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (mOnScrolleIdle)
					notifyDataSetChanged();
			};
		};

		addFooterView();

		this.mLoadView = new LoadingView(mInflater);
		this.mLoadView.setEmptyView(mListView);

	}

	protected void onDataItemClick(View view, int position) {
	}

	protected void addFooterView() {
		if (!this.mAddFooter) {
			if (this.mFootView == null) {
				this.mFootView = new ListViewFooter(this.mInflater);
			}
			this.mFootView.hideFooterView();
			this.mListView.addFooterView(this.mFootView.getFooterView());
			this.mAddFooter = true;
		}
	}

	protected void changeRequestStatus(int status) {
		mCurrentStatus = status;
		switch (mCurrentStatus) {
		case STATUS_LOADING:
			if (this.mObjects.isEmpty())
				this.mLoadView.showViewLoading();
			else
				this.mFootView.showFooterViewWaiting();
			break;
		case STATUS_ERROR:
			if (this.mObjects.isEmpty()) {
				this.mLoadView.showLoadFailed(mErrorClickLisenter);
			} else {
				this.mFootView.showFooterViewError(mErrorClickLisenter);
			}
			break;
		case STATUS_PENDDING:

			break;
		case STATUS_PULL_LOADING:

			break;
		case STATUS_READY:

			break;

		default:
			this.mFootView.hideFooterView();
			break;
		}
	}

	public void appendData(List<T> list, boolean last, int index) {
		log.debug("append data");
		if (this.mCurrentStatus == STATUS_PULL_LOADING) {
			((PullToRefreshListView) this.mListView).onLoadingComplete();
			reset();
		}
		this.mIndex = index;
		appendData(list, last);

		if (this.mIndex != 0) {

		} else if ((this.mListView instanceof PullToRefreshListView)) {
			((PullToRefreshListView) this.mListView).setRefreshTime();
		}
	}

	/**
	 * @param list
	 * @param last
	 *            true=所有数据加载完成，不再请求数据
	 */
	public void appendData(List<T> list, boolean last) {
		if (list == null) {
			log.debug("append data status-error");
			changeRequestStatus(STATUS_ERROR);
			return;
		}
		this.isLastPage = last;
		if (!list.isEmpty()) {
			addAll(list);
		}
		if (this.isLastPage)
			removeFootView();
		if (this.mObjects.isEmpty()) {
			onDataEmpty();
			changeRequestStatus(STATUS_LIST_ISNULL);
		} else
			changeRequestStatus(-1);
	}

	public void appendDataByManual(List<T> list, boolean last, int index) {
		this.mIndex = index;
		appendData(list, last);
	}

	/**
	 * 加载数据为空，自定义处理
	 */
	protected void onDataEmpty() {

	}

	/**
	 * Adds the specified object at the end of the array.
	 * 
	 * @param object
	 *            The object to add at the end of the array.
	 */
	public void add(T object) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.add(object);
			} else {
				mObjects.add(object);
			}
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Adds the specified Collection at the end of the array.
	 * 
	 * @param collection
	 *            The Collection to add at the end of the array.
	 */
	public void addAll(Collection<? extends T> collection) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.addAll(collection);
			} else {
				mObjects.addAll(collection);
			}
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Adds the specified items at the end of the array.
	 * 
	 * @param items
	 *            The items to add at the end of the array.
	 */
	public void addAll(T... items) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				Collections.addAll(mOriginalValues, items);
			} else {
				Collections.addAll(mObjects, items);
			}
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Inserts the specified object at the specified index in the array.
	 * 
	 * @param object
	 *            The object to insert into the array.
	 * @param index
	 *            The index at which the object must be inserted.
	 */
	public void insert(T object, int index) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.add(index, object);
			} else {
				mObjects.add(index, object);
			}
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Removes the specified object from the array.
	 * 
	 * @param object
	 *            The object to remove.
	 */
	public void remove(T object) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.remove(object);
			} else {
				mObjects.remove(object);
			}
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Remove all elements from the list.
	 */
	public void clear() {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.clear();
			} else {
				mObjects.clear();
			}
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Sorts the content of this adapter using the specified comparator.
	 * 
	 * @param comparator
	 *            The comparator used to sort the objects contained in this
	 *            adapter.
	 */
	public void sort(Comparator<? super T> comparator) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				Collections.sort(mOriginalValues, comparator);
			} else {
				Collections.sort(mObjects, comparator);
			}
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mNotifyOnChange = true;
	}

	/**
	 * Control whether methods that change the list ({@link #add},
	 * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
	 * {@link #notifyDataSetChanged}. If set to false, caller must manually call
	 * notifyDataSetChanged() to have the changes reflected in the attached
	 * view.
	 * 
	 * The default is true, and calling notifyDataSetChanged() resets the flag
	 * to true.
	 * 
	 * @param notifyOnChange
	 *            if true, modifications to the list will automatically call
	 *            {@link #notifyDataSetChanged}
	 */
	public void setNotifyOnChange(boolean notifyOnChange) {
		mNotifyOnChange = notifyOnChange;
	}

	/**
	 * Returns the context associated with this array adapter. The context is
	 * used to create views from the resource passed to the constructor.
	 * 
	 * @return The Context associated with this adapter.
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCount() {
		return mObjects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public T getItem(int position) {
		return mObjects.get(position);
	}

	/**
	 * Returns the position of the specified item in the array.
	 * 
	 * @param item
	 *            The item to retrieve the position of.
	 * 
	 * @return The position of the specified item.
	 */
	public int getPosition(T item) {
		return mObjects.indexOf(item);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getItemId(int position) {
		return position;
	}

	public void removeFootView() {
		if ((this.mAddFooter) && (this.mFootView != null)
				&& (this.mFootView.getFooterView() != null)) {
			ListView localListView = this.mListView;
			View localView = this.mFootView.getFooterView();
			localListView.removeFooterView(localView);
			this.mAddFooter = false;
		}
	}

	public void request() {
		log.debug("request");
		if ((this.isLastPage) && (!this.isLocalMode))
			return;
		changeRequestStatus(STATUS_LOADING);
		doRequest();

	}

	protected void doRefreshRequest() {
		if (this.isLocalMode) {
			doRequest("");
			return;
		}
		if (TextUtils.isEmpty(this.mUrl)) {
			changeRequestStatus(-1);
			return;
		}
		doRequest(this.mUrl);
	}

	protected void doRequest() {
		if (this.isLocalMode) {
			doRequest("");
			return;
		}
		if (TextUtils.isEmpty(this.mUrl)) {
			changeRequestStatus(STATUS_ERROR);
			return;
		}
		doRequest(this.mUrl);
	}

	public void request(String url) {
		if (TextUtils.isEmpty(url))
			return;
		this.mUrl = url;
		reset();
		request();
	}

	public void notifyRequestError() {
		if (STATUS_PULL_LOADING == this.mCurrentStatus) {
			changeRequestStatus(-1);
			((PullToRefreshListView) this.mListView).setRecover();
		} else {
			changeRequestStatus(STATUS_ERROR);
		}
	}

	public void reset() {
		this.mObjects.clear();
		this.mIndex = 0;
		this.isLastPage = false;
		this.mCurrentStatus = -1;
		notifyDataSetChanged();
	}

	protected void doRequest(String url) {
	}

	public void setNullTip(String noDataTip) {
		this.mNoDataTip = noDataTip;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	protected void setOnScrollIdleView(Object holder, T paramObject2,
			int position) {
	}

	protected void setOnScrollViewContent(Object holder, T paramObject2,
			int position) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (mCurrentStatus != STATUS_LOADING) {
			if ((STATUS_PULL_LOADING != mCurrentStatus)
					&& (this.mCurrentStatus != -1) && (totalItemCount != 0)
					&& (!this.isLocalMode)) {
				if (!TelephoneUtil.isNetworkAvailable(this.mContext)) {
					mFootView.showFooterViewError(mErrorClickLisenter);
				} else if ((firstVisibleItem + visibleItemCount >= totalItemCount - 1)
						&& (TelephoneUtil.isNetworkAvailable(this.mContext)))
					request();
			}
		}

	}

	public void onListViewStateChanged(int state) {
		switch (state) {
		case P2RListViewStateListener.STATE_PULL:
		case P2RListViewStateListener.STATE_RECOVER:
		case P2RListViewStateListener.STATE_LOADING:
		default:
		}

		if (this.mCurrentStatus != STATUS_LOADING) {
			if (STATUS_PULL_LOADING != this.mCurrentStatus) {
				changeRequestStatus(STATUS_PULL_LOADING);
				doRefreshRequest();
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (this.isLocalMode)
			return;
		if (scrollState != SCROLL_STATE_IDLE) {
			this.mOnScrolleIdle = false;
		} else {
			this.mOnScrolleIdle = true;
			this.mOnScrollChangedHandle.sendEmptyMessage(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	/**
	 * <p>
	 * An array filter constrains the content of the array adapter with a
	 * prefix. Each item that does not start with the supplied prefix is removed
	 * from the list.
	 * </p>
	 */
	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<T>(mObjects);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<T> list;
				synchronized (mLock) {
					list = new ArrayList<T>(mOriginalValues);
				}
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<T> values;
				synchronized (mLock) {
					values = new ArrayList<T>(mOriginalValues);
				}

				final int count = values.size();
				final ArrayList<T> newValues = new ArrayList<T>();

				for (int i = 0; i < count; i++) {
					final T value = values.get(i);
					final String valueText = value.toString().toLowerCase();

					// First match against the whole, non-splitted value
					if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						// Start at index 0, in case valueText starts with
						// space(s)
						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// noinspection unchecked
			mObjects = (List<T>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

}
