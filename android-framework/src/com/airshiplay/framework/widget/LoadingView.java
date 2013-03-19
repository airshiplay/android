/**
 * 
 */
package com.airshiplay.framework.widget;

import java.util.Random;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.airshiplay.framework.R;

/**
 * @author airshiplay
 * @Create Date 2013-2-11
 * @version 1.0
 * @since 1.0
 */
public class LoadingView {
	private static final int S_LOADDING = 0;
	private static final int S_LOAD_ERROR = 1;
	private static final int S_LOAD_NOTICE = 2;
	private Context context;
	private ImageView mDyImage;
	private LayoutInflater mInflater;
	private View.OnClickListener mListener;
	private ImageView mLoadImage;
	private TextView mLoadText;
	private LinearLayout mLoadingPage;
	private TextView mNoticeText;
	private boolean mSmall;
	private int mStatus;
	private String mTip = "";

	public LoadingView(LayoutInflater paramLayoutInflater) {
		this.mInflater = paramLayoutInflater;
		Context localContext = paramLayoutInflater.getContext()
				.getApplicationContext();
		this.context = localContext;
		init();
	}

	private void init() {
		this.mLoadingPage = new LoadingContainer(context);
		this.mLoadingPage.setGravity(Gravity.CENTER);
		this.mLoadingPage.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		this.mLoadingPage.addView(this.mInflater.inflate(R.layout.loading_page,
				null));
	}

	private void initContentView() {
		int i = 1097859072;
		int j = 1094713344;
		this.mLoadingPage.removeAllViews();
		View localView = this.mInflater.inflate(R.layout.loading_page, null);
		this.mLoadingPage.addView(localView);
		this.mLoadImage = (ImageView) localView
				.findViewById(R.id.loading_image);
		this.mDyImage = (ImageView) localView.findViewById(R.id.gf_loading);
		this.mLoadText = (TextView) localView.findViewById(R.id.loading_text);
		this.mNoticeText = (TextView) localView.findViewById(R.id.notic_text);
		if (this.mSmall) {
			this.mLoadText.setTextSize(j);
			this.mNoticeText.setTextSize(j);
		} else {
			this.mLoadText.setTextSize(i);
			this.mNoticeText.setTextSize(i);
		}
		show();
	}

	private void show() {
		switch (this.mStatus) {
		case S_LOADDING:
			this.mLoadImage.setVisibility(View.GONE);
			this.mDyImage.setVisibility(View.VISIBLE);
			this.mLoadText.setVisibility(View.VISIBLE);
			this.mNoticeText.setVisibility(View.GONE);
			Random localRandom = new Random();
			String[] arrayOfString = this.context.getResources()
					.getStringArray(R.array.load_tips);
			int k = arrayOfString.length;
			int m = localRandom.nextInt(k);
			String str1 = arrayOfString[m];
			this.mLoadText.setText(str1);
			this.mLoadingPage.setOnClickListener(null);
			this.mDyImage.post(new Runnable() {
				@Override
				public void run() {
					AnimationDrawable animationDrawable = (AnimationDrawable) LoadingView.this.mDyImage
							.getDrawable();
					if (animationDrawable != null) {
						animationDrawable.stop();
						animationDrawable.start();
					}
				}
			});
			break;
		case S_LOAD_NOTICE:
			this.mLoadImage.setVisibility(View.VISIBLE);
			this.mLoadText.setVisibility(View.VISIBLE);
			this.mNoticeText.setVisibility(View.GONE);
			this.mDyImage.setVisibility(View.GONE);
			this.mLoadImage.setImageResource(R.drawable.list_load_notice);
			this.mLoadText.setText(this.mTip);
			this.mLoadingPage.setOnClickListener(this.mListener);
			break;
		case S_LOAD_ERROR:
			this.mLoadImage.setVisibility(View.VISIBLE);
			this.mLoadText.setVisibility(View.VISIBLE);
			this.mNoticeText.setVisibility(View.GONE);
			this.mDyImage.setVisibility(View.GONE);
			this.mLoadText.setText(R.string.loading_failed);
			this.mLoadingPage.setOnClickListener(this.mListener);
			break;
		}
	}

	private void updateEmptyStatus(ListView listView) {
		ListAdapter adapter = listView.getAdapter();
		View emptyView = listView.getEmptyView();
		if ((adapter == null) || (adapter.isEmpty())) {
			if (emptyView != null) {
				emptyView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		} else {
			if (emptyView != null)
				emptyView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
	}

	public void changeNormalMode() {
		int i = 1097859072;
		this.mSmall = false;
		if (this.mLoadText != null) {
			this.mLoadText.setTextSize(i);
			this.mNoticeText.setTextSize(i);
		}
	}

	public void changeSmallMode() {
		int i = 1094713344;
		this.mSmall = true;
		if (this.mLoadText != null) {
			this.mLoadText.setTextSize(i);
			this.mNoticeText.setTextSize(i);
		}
	}

	public View getView() {
		return this.mLoadingPage;
	}

	public void setEmptyView(final ListView listView) {
		this.mLoadingPage.setVisibility(View.GONE);
		ViewGroup viewGroup = (ViewGroup) listView.getParent();
		viewGroup.addView(this.mLoadingPage);
		listView.setEmptyView(this.mLoadingPage);
		viewGroup.requestChildFocus(listView,  this.mLoadingPage);
		ListAdapter adapter = listView.getAdapter();
		adapter.registerDataSetObserver(new DataSetObserver() {

			@Override
			public void onChanged() {
				LoadingView.this.updateEmptyStatus(listView);
				super.onChanged();
			}

			@Override
			public void onInvalidated() {
				LoadingView.this.updateEmptyStatus(listView);
				super.onInvalidated();
			}

		});
	}

	public void showLoadFailed(View.OnClickListener paramOnClickListener) {
		this.mStatus = S_LOAD_ERROR;
		this.mListener = paramOnClickListener;
		if (this.mLoadImage != null)
			show();
	}

	public void showNotice(String paramString,
			View.OnClickListener paramOnClickListener) {
		this.mStatus = S_LOAD_NOTICE;
		this.mListener = paramOnClickListener;
		this.mTip = paramString;
		if (this.mLoadImage != null)
			show();
	}

	public void showViewLoading() {
		this.mStatus = S_LOADDING;
		if (this.mLoadImage != null)
			show();
	}

	class LoadingContainer extends LinearLayout {
		public LoadingContainer(Context context) {
			super(context);
		}

		protected void onAttachedToWindow() {
			if (LoadingView.this.mLoadingPage != null)
				LoadingView.this.initContentView();
			super.onAttachedToWindow();
		}

		protected void onDetachedFromWindow() {
			if (LoadingView.this.mLoadingPage != null) {
				LoadingView.this.mLoadingPage.removeAllViews();
				LoadingView.this.mLoadImage = null;
				LoadingView.this.mDyImage = null;
				LoadingView.this.mLoadText = null;
				LoadingView.this.mNoticeText = null;
			}
			super.onDetachedFromWindow();
		}
	}
}
