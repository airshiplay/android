package com.airshiplay.framework.download;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.airshiplay.framework.bean.BaseBean;

import android.content.Context;
import android.os.Handler;

/**
 * 下载管理
 * 
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class DownloadMgr {
	static Map<String, DownloadTask> taskMap = new ConcurrentHashMap<String, DownloadTask>();
	static Context mCtx;
	public static Handler no_wifi = new Handler();

	public static DownloadTask addTask(DownloadTask task) {
		taskMap.put(task.bean.getResId(), task);
		return task;
	}

	public static<T extends BaseBean> DownloadTask addTask(T bean) {
		return null;
	}

	static void apkInstall(DownloadTask task) {

	}

	public static DownloadTask changeDownLoadBean(DownloadTask task) {
		return task;
	}

	public static void deleteTask(DownloadTask task) {

	}

	public static DownloadTask findTask(String resId) {
		return null;
	}

	public static int getCount() {
		return 0;
	}

	public static List<DownloadTask> getDownloadList() {
		return null;
	}

	public static void initScheduleTask() {

	}

	static void onFinish(DownloadTask task) {

	}

	static void pauseAllTask() {

	}

	static void resumeAllTask() {

	}

	boolean scheduleTask() {
		return false;
	}

	private void setWifiPolicy() {

	}

	static void scheduleTask(DownloadTask task) {
		task.download();
	}

	public static void stopAll() {

	}

}
