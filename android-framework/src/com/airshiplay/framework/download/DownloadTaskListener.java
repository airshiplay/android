package com.airshiplay.framework.download;

import android.os.Handler;
import android.os.Message;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public abstract class DownloadTaskListener extends Handler {
	/** 下载进度改变 */
	public static final int PROGRESS_CHANGE = 1;
	/** 下载状态改变 */
	public static final int STATE_CHANGE = 2;
	DownloadTask task = null;

	public DownloadTaskListener(DownloadTask task) {
		this.task = task;
	}

	@Override
	public void handleMessage(Message msg) {
		if (task != null) {
			switch (msg.what) {
			case PROGRESS_CHANGE:
				onProgressChange(this.task.percent, this.task.loadSize);
				break;
			case STATE_CHANGE:
				onStateChange(this.task.state);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 下载任务进度改变
	 * 
	 * @param percent
	 *            已下载文件百分比,取值 0-100
	 * @param loadSize
	 *            已下载文件的大小
	 */
	public abstract void onProgressChange(int percent, long loadSize);

	/**
	 * 下载任务状态改变
	 * 
	 * @param state
	 */
	public abstract void onStateChange(int state);
}
