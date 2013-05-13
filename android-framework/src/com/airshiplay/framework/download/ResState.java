package com.airshiplay.framework.download;

import android.content.Context;
import android.view.View;

import com.airshiplay.framework.bean.BaseBean;

public class ResState {
	public static void updateState(Context context, View view, int state) {

	}

	public static <T extends BaseBean> void updateProgress(
			final Context context, final ViewState viewState, final T bean) {
		final DownloadTask task = DownloadMgr.findTask(bean.getResId());
		if (task != null) {
			task.addDownloadListener(new DownloadTaskListener(task) {

				@Override
				public void onStateChange(int state) {
					if (viewState == null)
						return;
					ViewState view = viewState.findViewStateWithTag(bean
							.getResId());
					if (null == view)
						return;
					view.updateState(state);
				}

				@Override
				public void onProgressChange(int percent, long loadSize) {
					if (viewState == null)
						return;
					ViewState view = viewState.findViewStateWithTag(bean
							.getResId());
					if (null == view)
						return;
					view.updateProgressChange(percent, loadSize);
				}
			}, bean);
		}
	}
}
