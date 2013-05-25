/**
 * 
 */
package com.airshiplay.framework.receiver;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.airshiplay.framework.event.SystemEvent;
import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

/**
 * @author airshiplay
 * @Create Date 2013-2-14
 * @version 1.0
 * @since 1.0
 */
public class PackageChangeReceiver extends BroadcastReceiver {
	private static final Logger log = LoggerFactory
			.getLogger(PackageChangeReceiver.class);
	/** key:packageName,value:Handler */
	private static Map<String, Handler> listeners = new HashMap<String, Handler>();

	@Override
	public void onReceive(Context context, Intent intent) {
		log.debug("" + intent);
		String pkgName = intent.getDataString().substring(
				intent.getData().getScheme().length());
		if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
			intent.getIntExtra(Intent.EXTRA_UID, 0);
			intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
			log.debug("ACTION_PACKAGE_ADDED:" + intent.getExtras());
			SystemEvent.EventTypeInstall data = new SystemEvent.EventTypeInstall();
			SystemEvent.fireEvent(SystemEvent.EVENT_TYPE_INSTALL, data);
			notifyChange(pkgName, Intent.ACTION_PACKAGE_ADDED);
		} else if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
			log.debug("ACTION_PACKAGE_REPLACED:" + intent.getExtras());
			SystemEvent.fireEvent(SystemEvent.EVENT_TYPE_APP_CHANGE);
		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
			intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
			log.debug("ACTION_PACKAGE_REMOVED:" + intent.getExtras());
		}
	}

	public static void clear() {
		listeners.clear();
	}

	public static void notifyChange(String pkgName, String value) {
		Handler handler = listeners.get(pkgName);
		if (handler != null) {
			Message message = new Message();
			message.obj = value;
			handler.sendMessage(message);
		}
	}

	public static void unregisterListener(String pkgName) {
		if (listeners.containsKey(pkgName))
			listeners.remove(pkgName);
	}

	public static void registerListener(String pkgName, Handler handler) {
		listeners.put(pkgName, handler);
	}
}
