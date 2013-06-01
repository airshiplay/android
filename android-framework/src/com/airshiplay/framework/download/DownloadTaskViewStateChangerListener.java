package com.airshiplay.framework.download;

public class DownloadTaskViewStateChangerListener extends DownloadTaskListener {
	protected ViewState view;

	public DownloadTaskViewStateChangerListener(DownloadTask task,
			ViewState viewState) {
		super(task);
		this.view = viewState;
	}

	@Override
	public void onProgressChange(int percent, long loadSize) {
		ViewState viewState = view.findViewStateWithTag(task.bean.getResId());
		if (null != viewState)
			viewState.updateProgressChange(percent, loadSize);
	}

	@Override
	public void onStateChange(int state) {
		ViewState viewState = view.findViewStateWithTag(task.bean.getResId());
		if (null != viewState)
			viewState.updateState(state);

	}

}
