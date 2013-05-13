package com.airshiplay.framework.download;

public interface ViewState {
	ViewState findViewStateWithTag(Object tag);

	/**
	 * {@link DownloadTask#STATE_CONNETING}
	 * 
	 * @param taskState
	 */
	void updateState(int taskState);

	/**
	 * @param percent
	 * @param loadSize
	 */
	void updateProgressChange(int percent, long loadSize);
}
