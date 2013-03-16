/**
 * 
 */
package com.airshiplay.framework;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airshiplay.framework.R;
import com.airshiplay.framework.data.FetchDataHelper;
import com.airshiplay.framework.net.BaseFetchHandle;
import com.airshiplay.framework.widget.AbsListViewAdapter;
import com.airshiplay.framework.widget.PullToRefreshListView;

/**
 * @author airshiplay
 * @Create Date 2013-2-19
 * @version 1.0
 * @since 1.0
 */
public class PullToRefreshUI extends Activity {

	private PullToRefreshListView pullToRefreshListView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> objects;
	private LinearLayout line;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.test_pull_to_refresh);
		// pullToRefreshListView = new PullToRefreshListView1(this);
		// pullToRefreshListView
		// .setOnRefreshListener(new PullToRefreshListView1.OnRefreshListener()
		// {
		//
		// @Override
		// public void onRefresh() {
		// objects.clear();
		// for (int i = 0; i < 50; i++) {
		// objects.add("ojbects update:" + i);
		// }
		// mAdapter.notifyDataSetChanged();
		// }
		// });
		// objects = new ArrayList<String>();
		// for (int i = 0; i < 50; i++) {
		// objects.add("ojbects:" + i);
		// }
		// mAdapter = new ArrayAdapter<String>(this, R.layout.textview,0,
		// objects);
		// pullToRefreshListView.setAdapter(mAdapter);
		// setContentView(pullToRefreshListView);
		new ArrayAdapter<String>(this, 0);
		pullToRefreshListView = new PullToRefreshListView(this);
		line=new LinearLayout(this);
		line.addView(pullToRefreshListView);
		setContentView(line);

		AbsListViewAdapter<String> adapter = new AbsListViewAdapter<String>(
				this, pullToRefreshListView,"http://baidu.com") {

			@Override
			protected void doRequest(String url) {
				FetchDataHelper.request(url, new BaseFetchHandle<List<String>>() {

					@Override
					protected List<String> onReceivedData(String value)
							throws Exception {
						List<String> object=new ArrayList<String>();
						for(int i=0;i<30;i++){
							object.add("init data:"+i);
						}
						return object;
					}

					@Override
					protected void onFailure(Throwable tr) {
						notifyRequestError();
					}

					@Override
					protected void onSuccess(List<String> t) {
						appendData(t, false,10);
					}
				});
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view;
				TextView text;
				if (convertView == null) {
					view = mInflater.inflate(R.layout.textview, null);
				} else {
					view = convertView;
				}
				text = (TextView) view;
				text.setText(getItem(position));
				return view;
			}
		};
		adapter.request();
	}
}
