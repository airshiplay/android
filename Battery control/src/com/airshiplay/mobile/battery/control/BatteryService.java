package com.airshiplay.mobile.battery.control;

import java.text.NumberFormat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.BatteryManager;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-4
 */
public class BatteryService extends Service {

	private static final String tag = "BatteryService";
	private int statusBarHeight;// 状态栏高度
	private View view;// 透明窗体
	private boolean viewAdded = false;// 透明窗体是否已经显示
	private WindowManager windowManager;
	private WindowManager.LayoutParams layoutParams;
	private BatteryChangedReceiver mBatteryChangedReceiver;
	private int level;
	private int scale;
	private int status;
	private TextView content;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(tag, "onCreate");
		view = LayoutInflater.from(this).inflate(R.layout.floating, null);
		content = (TextView) view.findViewById(R.id.air_content);
		windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
		/*
		 * LayoutParams.TYPE_SYSTEM_ERROR：保证该悬浮窗所有View的最上层
		 * LayoutParams.FLAG_NOT_FOCUSABLE:该浮动窗不会获得焦点，但可以获得拖动
		 * PixelFormat.TRANSPARENT：悬浮窗透明
		 */
		layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ERROR, LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSPARENT);
		layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

		view.setOnTouchListener(new OnTouchListener() {
			float[] temp = new float[] { 0f, 0f };

			public boolean onTouch(View v, MotionEvent event) {
				layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
				int eventaction = event.getAction();
				switch (eventaction) {
				case MotionEvent.ACTION_DOWN: // 按下事件，记录按下时手指在悬浮窗的XY坐标值
					temp[0] = event.getX();
					temp[1] = event.getY();
					break;

				case MotionEvent.ACTION_MOVE:
					refreshView((int) (event.getRawX() - temp[0]), (int) (event.getRawY() - temp[1]));
					break;
				}
				return true;
			}
		});
		mBatteryChangedReceiver = new BatteryChangedReceiver();
		registerReceiver(mBatteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(tag, "onStart");
		refresh();
		handleIntent(intent);
	}

	void handleIntent(Intent intent) {
		if (intent == null)
			return;
		if (TextUtils.isEmpty(intent.getAction()))
			return;
		if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
			level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
			status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
			double value = (level * 1.0 / scale);
			switch (status) {
			case BatteryManager.BATTERY_STATUS_CHARGING:
				content.setText(R.string.battery_status_changing);
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				content.setText(R.string.battery_status_dischanging);
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				content.setText(R.string.battery_status_full);
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				content.setText(R.string.battery_status_not_changing);
				break;
			default:
				content.setText(R.string.battery_status_unknown);
				break;
			}
			content.setText(content.getText().toString() + NumberFormat.getPercentInstance().format(value));
		} else if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
			Toast.makeText(this, R.string.battery_low, Toast.LENGTH_LONG).show();
		} else if (Intent.ACTION_BATTERY_OKAY.equals(intent.getAction())) {
			Toast.makeText(this, R.string.battery_ok, Toast.LENGTH_LONG).show();
		} else if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
			Toast.makeText(this, R.string.power_connected, Toast.LENGTH_LONG).show();
		} else if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
			Toast.makeText(this, R.string.power_disconnected, Toast.LENGTH_LONG).show();
		}else if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){
			
		}else if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){
			
		}else if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
			
		}
		Log.d(tag, intent.getAction());
	}

	/**
	 * 刷新悬浮窗
	 * 
	 * @param x
	 *            拖动后的X轴坐标
	 * @param y
	 *            拖动后的Y轴坐标
	 */
	public void refreshView(int x, int y) {
		// 状态栏高度不能立即取，不然得到的值是0
		if (statusBarHeight == 0) {
			View rootView = view.getRootView();
			Rect r = new Rect();
			rootView.getWindowVisibleDisplayFrame(r);
			statusBarHeight = r.top;
		}
		layoutParams.x = x;
		// y轴减去状态栏的高度，因为状态栏不是用户可以绘制的区域，不然拖动的时候会有跳动
		layoutParams.y = y - statusBarHeight;// STATUS_HEIGHT;
		refresh();
	}

	/**
	 * 添加悬浮窗或者更新悬浮窗 如果悬浮窗还没添加则添加 如果已经添加则更新其位置
	 */
	private void refresh() {
		if (viewAdded) {
			windowManager.updateViewLayout(view, layoutParams);
		} else {
			windowManager.addView(view, layoutParams);
			viewAdded = true;
		}
	}

	/**
	 * 关闭悬浮窗
	 */
	public void removeView() {
		if (viewAdded) {
			windowManager.removeView(view);
			viewAdded = false;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBatteryChangedReceiver);
		removeView();
		Log.d(tag, "onDestroy");
	}

	private class BatteryChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			handleIntent(intent);
		}

	}
}
