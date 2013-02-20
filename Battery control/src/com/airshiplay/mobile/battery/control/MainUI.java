package com.airshiplay.mobile.battery.control;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-4
 */
public class MainUI extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView view=(TextView)findViewById(R.id.text);
		LayerDrawable l=(LayerDrawable) view.getBackground();
		l.getDrawable(1).setLevel(8000);
		l.getDrawable(2).setLevel(5000);

		startService(new Intent(this,BatteryService.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

}
