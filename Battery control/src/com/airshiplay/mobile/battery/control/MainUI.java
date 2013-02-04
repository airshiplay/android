package com.airshiplay.mobile.battery.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

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
		//startService(new Intent(this,BatteryService.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
