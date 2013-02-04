package com.airshiplay.mobile.battery.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-4
 */
public class BatteryBroadcastReceiver extends BroadcastReceiver {
	private static final String tag = "BatteryBroadcastReceiver";
 
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, BatteryService.class).setAction(intent.getAction()).putExtras(intent).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

}
