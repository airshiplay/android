package com.airshiplay.mobile.battery.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
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
		TextView view = (TextView) findViewById(R.id.text);
		LayerDrawable l = (LayerDrawable) view.getBackground();
		l.getDrawable(1).setLevel(8000);
		l.getDrawable(2).setLevel(5000);

		startService(new Intent(this, BatteryService.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		Window window = getWindow();
		LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
		windowLayoutParams.width = 0;// (int) (display.getWidth() * 0.7); //
										// 宽度设置为屏幕的0.95
		windowLayoutParams.height = 0;// (int) (display.getHeight() * 0.1); //
										// 高度设置为屏幕的0.6
		windowLayoutParams.alpha = 0.5f;// 设置透明度

		finish();
		Context context;
		try {
			context = createPackageContext("com.airshiplay.mobile.chart.view", CONTEXT_IGNORE_SECURITY);
			SharedPreferences prefs = context.getSharedPreferences(
					"test.prefs",MODE_MULTI_PROCESS+MODE_APPEND+ MODE_WORLD_WRITEABLE+MODE_WORLD_READABLE);
			System.out.println(prefs.getString("key", "key default"));
			System.out.println("other write"+prefs.edit().putString("other", "other-value").commit());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
