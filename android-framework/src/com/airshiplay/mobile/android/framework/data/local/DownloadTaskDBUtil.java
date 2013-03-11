/**
 * 
 */
package com.airshiplay.mobile.android.framework.data.local;

import android.content.Context;

/**
 * @author airshiplay
 * @Create Date 2013-3-10
 * @version 1.0
 * @since 1.0
 */
public class DownloadTaskDBUtil extends BaseDBUtil {

	private static DownloadTaskDBUtil downloadTaskDBUtil;

	/**
	 * @param context
	 */
	public DownloadTaskDBUtil(Context context) {
		super(context);
	}

	public static DownloadTaskDBUtil getInstance(Context context) {
		if (downloadTaskDBUtil == null)
			downloadTaskDBUtil = new DownloadTaskDBUtil(context);
		return downloadTaskDBUtil;
	}
}
