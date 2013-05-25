package com.airshiplay.framework;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airshiplay.framework.R;
import com.airshiplay.framework.event.SystemEvent;
import com.airshiplay.framework.event.SystemEvent.EventTypeData;
import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class MainUI extends Activity implements SystemEvent.EventListener {
	private static final Logger log =LoggerFactory.getLogger(MainUI.class);
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
