package com.airshiplay.mobile.android.framework.example.viewflow;

import android.app.Activity;
import android.os.Bundle;

import com.airshiplay.mobile.android.framework.R;
import com.airshiplay.mobile.android.framework.viewflow.TitleFlowIndicator;
import com.airshiplay.mobile.android.framework.viewflow.ViewFlow;

public class AsyncDataFlowExample extends Activity {

	private ViewFlow viewFlow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.async_title);
		setContentView(R.layout.title_layout);

		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		AsyncAdapter adapter = new AsyncAdapter(this);
		viewFlow.setAdapter(adapter, adapter.getTodayId());
		
		TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
		indicator.setTitleProvider(adapter);
		
		viewFlow.setFlowIndicator(indicator);
    }
    
}
