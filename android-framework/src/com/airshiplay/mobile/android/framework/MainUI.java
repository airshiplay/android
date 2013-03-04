package com.airshiplay.mobile.android.framework;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airshiplay.mobile.android.framework.event.SystemEvent;
import com.airshiplay.mobile.android.framework.event.SystemEvent.EventTypeData;
import com.airshiplay.mobile.android.framework.util.Log;
import com.airshiplay.mobile.android.framework.util.LogFactory;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class MainUI extends Activity implements SystemEvent.EventListener {
	private static final Log log =LogFactory.getLog(MainUI.class);
	private TextView text;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.debug("onCreate");
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
