package com.airshiplay.mobile.battery.control;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
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
		TextView view=(TextView)findViewById(R.id.text);
		LayerDrawable l=(LayerDrawable) view.getBackground();
		l.getDrawable(1).setLevel(8000);
		l.getDrawable(2).setLevel(5000);

		startService(new Intent(this,BatteryService.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	
		
		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		Window window = getWindow();
		LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
		windowLayoutParams.width =0;// (int) (display.getWidth() * 0.7); // 宽度设置为屏幕的0.95
		windowLayoutParams.height = 0;//(int) (display.getHeight() * 0.1); // 高度设置为屏幕的0.6
		windowLayoutParams.alpha = 0.5f;// 设置透明度
		
		finish();
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
