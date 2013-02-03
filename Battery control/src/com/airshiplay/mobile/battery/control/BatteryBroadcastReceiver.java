package com.airshiplay.mobile.battery.control;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BatteryBroadcastReceiver extends BroadcastReceiver {

	private static final String tag = "BatteryBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(tag, "receive");
		if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
			context.startActivity(new Intent(context, MainUI.class).putExtras(intent).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		} else if (Intent.ACTION_BATTERY_OKAY.equals(intent.getAction())) {
			context.startActivity(new Intent(context, MainUI.class).putExtras(intent).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		} else if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
			context.startActivity(new Intent(context, MainUI.class).putExtras(intent).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
		Toast.makeText(context, "Battery!", Toast.LENGTH_LONG).show();  
	}

}
