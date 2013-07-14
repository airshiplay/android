package com.airshiplay.framework.example.viewflow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.airshiplay.framework.R;
import com.airshiplay.framework.viewflow.ViewFlowScrollView;
import com.airshiplay.mobile.viewflow.ViewFlow;
import com.airshiplay.mobile.viewflow.ViewFlowAdapter;

public class ScrollViewFlowExample extends Activity {

	private ViewFlowScrollView viewFlowScroll;
	private ViewFlow viewFlow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll_view_flow_example);
		initView();
		setViewContent();
	}

	void initView() {
		viewFlowScroll = (ViewFlowScrollView) findViewById(R.id.scrollview);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
	}

	void setViewContent() {
		viewFlowScroll.setViewFlow(viewFlow);
		AndroidVersionAdapter adapter = new AndroidVersionAdapter(this);
		viewFlow.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scroll_view_flow_example, menu);
		return true;
	}

}
