package com.airshiplay.framework.example.viewflow;

import android.app.Activity;
import android.os.Bundle;

import com.airshiplay.framework.R;
import com.airshiplay.framework.viewflow.TitleFlowIndicator;
import com.airshiplay.framework.viewflow.ViewFlow;

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
