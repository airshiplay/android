package com.airshiplay.framework.download;

import java.io.File;

import android.content.Context;

import com.airshiplay.framework.bean.BaseBean;
import com.airshiplay.mobile.util.TelephoneUtil;

public abstract class AbstractFlowImp<Bean extends BaseBean> implements
		IDownFlow {
	private static final long FREE_SPACE = 20971520L;// 20M
	public static final int RESULT_LOAD_CONSUME = -1;
	/** * 已添加到下载队列 */
	public static final int RESULT_ADD_QUEUE = 1;

	/** * 要下载的文件已经存在 */
	public static final int RESULT_EXIST = 2;
	/** * 下载中 */
	public static final int RESULT_DONWLOADING = 3;
	/** * 请启动WIFI后，再进行访问 */
	public static final int RESULT_WIFI_NOT_START = 4;
	/** * SD卡不存在,请插入SD卡后再重试 */
	public static final int RESULT_SDCARD_NOT_FOUND = 5;
	/** * 已在下载队列中 */
	public static final int RESULT_IN_THE_QUEUE = 6;
	/** * 空间不足，请清理空间会再重试 */
	public static final int RESULT_NOT_ENOUGH_SPACE = 7;
	Bean mBean = null;
	protected Context mCtx;
	File mFile = null;

	public AbstractFlowImp(Context context, Bean bean) {
		mCtx = context;
		mBean = bean;
	}

	@Override
	public DownloadTask excute(boolean showTip) {
		if (hasInstalled())
			return null;
		if (isExistFile())
			return null;
		if (isDownLoading())
			return null;
		if (!isWifiEnable())
			return null;
		if (!isEnoughSpace())
			return null;
		return download(showTip);
	}

	protected DownloadTask download(boolean showTip) {
		DownloadTask task = DownloadMgr.addTask(mBean);
		if ((task != null) && (showTip))
			showTip(RESULT_ADD_QUEUE);
		return task;
	}

	protected boolean isEnoughSpace() {
		long l = this.mBean.getSizeValue();
		if (TelephoneUtil.isSdcardExist()) {
			if (TelephoneUtil.getAvailableExternalMemorySize() > l + FREE_SPACE)
				return true;
			showTip(7);
			return false;
		}
		if (TelephoneUtil.getAvailableInternalMemorySize() > l + FREE_SPACE)
			return true;
		showTip(7);
		return false;
	}

	protected abstract void showTip(int i);

	private boolean isWifiEnable() {
		// if (!TelephoneUtil.isWifiEnable(this.mCtx)) {
		// boolean bool = PreferenceUtil.getBoolean(this.mCtx,
		// "NOTIFY_LARGE_WITHOUT_WIFI",
		// PreferenceUtil.DEFAULT_NOTIFY_LARGE_FILE_WITHOUT_WIFI);
		// if (1024.0F * (1024.0F * PreferenceUtil.getFloat(this.mCtx,
		// "GPRS_DOWNLOAD_MAXSIZE",
		// PreferenceUtil.DEFAULT_GPRS_DOWNLOAD_MAXSIZE.floatValue())) <=
		// (float) this.mBean
		// .getSizeValue())
		// ;
		// for (int i = 1; (i != 0) && (bool); i = 0) {
		// String str1 = this.mCtx.getResources().getString(2131297527);
		// Object[] arrayOfObject = new Object[2];
		// arrayOfObject[0] = this.mBean.name;
		// arrayOfObject[1] = this.mBean.size;
		// String str2 = String.format(str1, arrayOfObject);
		// // showLargeFileInGPRSDialog(this.mCtx, str2);
		// return false;
		// }
		// }
		return true;
	}

	protected boolean isDownLoading() {
		if (null != DownloadMgr.findTask(mBean.getResId())) {
			showTip(RESULT_IN_THE_QUEUE);
			return true;
		}
		return false;
	}

	protected boolean isExistFile() {
		return false;
	}

	public boolean isSdcardExist() {
		if (!TelephoneUtil.isSdcardExist()) {
			showTip(RESULT_SDCARD_NOT_FOUND);
			return false;
		}
		return true;
	}

	public boolean hasInstalled() {
		return false;
	}

}
