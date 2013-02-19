/**
 * 
 */
package com.arishiplay.mobile.android.framework;

import android.app.Activity;
import android.os.Bundle;

import com.arishiplay.mobile.android.framework.widget.PullToRefreshListView1;

/**
 * @author airshiplay
 * @Create Date 2013-2-19
 * @version 1.0
 * @since 1.0
 */
public class PullToRefreshUI extends Activity {

	private PullToRefreshListView1 pullToRefreshListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_pull_to_refresh);
		pullToRefreshListView=new PullToRefreshListView1(this);
		pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView1.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				
			}
		});
	}
}
