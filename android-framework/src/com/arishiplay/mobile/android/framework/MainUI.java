package com.arishiplay.mobile.android.framework;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.arishiplay.mobile.android.framework.event.SystemEvent;
import com.arishiplay.mobile.android.framework.event.SystemEvent.EventTypeData;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class MainUI extends Activity implements SystemEvent.EventListener {
	private TextView text;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		text =(TextView)findViewById(R.id.text);
		text.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println(v);
			}
		});
	}

	@Override
	public void onEvent(int eventType, EventTypeData eventTypeData) {
		
	}
}
