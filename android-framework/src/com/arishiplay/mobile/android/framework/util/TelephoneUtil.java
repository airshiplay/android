/**
 * 
 */
package com.arishiplay.mobile.android.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author airshiplay
 * @Create Date 2013-2-24
 * @version 1.0
 * @since 1.0
 */
public class TelephoneUtil {

	/**
	 * @param mContext
	 * @return
	 */
	public static boolean isNetworkAvailable(Context mContext) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService("connectivity");
			if (connectivityManager != null) {
				NetworkInfo networkInfo = connectivityManager
						.getActiveNetworkInfo();
				if (networkInfo != null) {
					if (networkInfo.isAvailable() && networkInfo.isConnected()) {
						return true;
					}
				}
			}
		} finally {
		}
		return false;
	}
}
