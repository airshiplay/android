package com.airshiplay.mobile.android.framework.event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

import com.airshiplay.mobile.android.framework.util.Log;
import com.airshiplay.mobile.android.framework.util.LogFactory;

/**
 * 自定义事件,在各个Activity中实现接收处理
 * 
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class SystemEvent {

	public static final int EVENT_TYPE_INSTALL = 1;
	public static final int EVENT_TYPE_UNINSTALL = 2;
	public static final int EVENT_TYPE_DELETE_TASK = 3;
	public static final int EVENT_TYPE_APP_CHANGE = 4;
	public static final int EVENT_TYPE_UPGRADE = 5;
	public static final int EVENT_TYPE_COLLECT = 6;
	public static final int EVENT_TYPE_LOGIN_CHANGE = 7;
	public static final int EVENT_TYPE_ACTIVITY_LOADED = 8;
	public static final int EVENT_TYPE_COMMENT = 9;
	public static final int EVENT_TYPE_SNS_ATTENTION = 10;
	public static final int EVENT_TYPE_SNS_RECOMMEND = 11;
	public static final int EVENT_TYPE_SNS_FRIEND = 12;
	public static final int EVENT_TYPE_PAYFOR_SUCCESS = 13;
	public static final int EVENT_TYPE_SNS_SCORE_REFLESH = 14;
	public static final int EVENT_TYPE_INSTALLED_LIST_REFLESH = 15;
	public static final int EVENT_TYPE_THEME_CHANGE = 16;
	public static final int EVENT_TYPE_RING_CHANGE = 17;
	public static final int EVENT_TYPE_PICTURE_CHANGE = 18;
	public static final int EVENT_TYPE_RING_PLAYSTATE = 22;
	public static final int EVENT_TYPE_RING_STOP = 21;

	private static final Log log = LogFactory.getLog(SystemEvent.class);

	private static SparseArray<List<WeakReference<EventListener>>> mEventMap = new SparseArray<List<WeakReference<EventListener>>>();

	private static SparseArray<List<WeakReference<PreEventListener>>> mPreEventMap = new SparseArray<List<WeakReference<PreEventListener>>>();

	public static void addListener(int eventType, EventListener eventListener) {
		List<WeakReference<EventListener>> list = mEventMap.get(eventType);
		if (list == null)
			list = new ArrayList<WeakReference<EventListener>>();
		list.add(new WeakReference<EventListener>(eventListener));
		mEventMap.put(eventType, list);
		log.debug("addListener-eventType="+eventType);
	}

	public static void addPreListener(int eventType,
			PreEventListener preEventListener) {
		List<WeakReference<PreEventListener>> list = mPreEventMap
				.get(eventType);
		if (list == null)
			list = new ArrayList<WeakReference<PreEventListener>>();
		list.add(new WeakReference<PreEventListener>(preEventListener));
		mPreEventMap.put(eventType, list);
	}

	public static void fireEvent(int eventType) {
		fireEvent(eventType, null);
	}

	public static void fireEvent(int eventType, EventTypeData eventTypeData) {
		if (eventTypeData != null) {
			int type = eventTypeData.getEventType();
			if (type != eventType)
				throw new RuntimeException("Event type does not match!");
		}
		List<WeakReference<PreEventListener>> preEventList = mPreEventMap
				.get(eventType);
		if (preEventList != null) {
			for (int i = 0; i < preEventList.size(); i++) {
				PreEventListener preEvent = preEventList.get(i).get();
				if (preEvent != null)
					preEvent.onPreEvent(eventType, eventTypeData);
			}
		}
		List<WeakReference<EventListener>> eventList = mEventMap.get(eventType);
		if (eventList != null) {
			for (int i = 0; i < eventList.size(); i++) {
				EventListener eventListener = eventList.get(i).get();
				if (eventListener != null)
					eventListener.onEvent(eventType, eventTypeData);
			}
		}

	}

	public static void removeListener(int eventType, EventListener eventListener) {
		List<WeakReference<EventListener>> list = mEventMap.get(eventType);
		if (list == null)
			return;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get() == eventListener) {
				list.remove(i);
				break;
			}
		}
	}

	public static void removePreListener(int eventType,
			PreEventListener preEventListener) {
		List<WeakReference<PreEventListener>> list = mPreEventMap
				.get(eventType);
		if (list == null)
			return;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get() == preEventListener) {
				list.remove(i);
				break;
			}
		}
	}

	public abstract interface EventListener {
		public abstract void onEvent(int eventType,
				SystemEvent.EventTypeData eventTypeData);
	}

	public abstract interface PreEventListener {
		public abstract void onPreEvent(int eventType,
				SystemEvent.EventTypeData eventTypeData);
	}

	public abstract interface EventTypeData {
		public abstract int getEventType();
	}

	public static class EventTypeInstall implements SystemEvent.EventTypeData {
		public String packageName;

		/**
 * 
 */
		public EventTypeInstall() {
		}

		public int getEventType() {
			return EVENT_TYPE_INSTALL;
		}
	}

	public class EventTypeUninstall implements SystemEvent.EventTypeData {
		public String packageName;

		public int getEventType() {
			return EVENT_TYPE_UNINSTALL;
		}
	}

	public class EventTypeAppChange implements SystemEvent.EventTypeData {
		public boolean multiple;
		public String packageName;

		public int getEventType() {
			return EVENT_TYPE_APP_CHANGE;
		}
	}

	public class EventTypeSnsFriend implements SystemEvent.EventTypeData {
		public int state;
		public String uin;

		public int getEventType() {
			return EVENT_TYPE_SNS_FRIEND;
		}
	}

	public class EventTypeSnsScoreReflesh implements SystemEvent.EventTypeData {
		public boolean force;

		public int getEventType() {
			return EVENT_TYPE_SNS_SCORE_REFLESH;
		}
	}

	public class EventTypeThemeChange implements SystemEvent.EventTypeData {
		public boolean multiple;
		public String path;

		public int getEventType() {
			return EVENT_TYPE_THEME_CHANGE;
		}
	}

	public class EventTypeRingChange implements SystemEvent.EventTypeData {
		public boolean multiple;
		public String path;

		public int getEventType() {
			return EVENT_TYPE_RING_CHANGE;
		}
	}

	public class EventTypePictureChange implements SystemEvent.EventTypeData {
		public boolean multiple;
		public String path;

		public int getEventType() {
			return EVENT_TYPE_PICTURE_CHANGE;
		}
	}

}
