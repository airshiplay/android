package com.airshiplay.framework.bean;

/**
 * @author lig
 * @version 1.0
 * @since 1.0 2013-2-6
 */
public class AppBean extends BaseBean {
	public static final int ACTION_TYPE_DOWNLOAD = 1;
	public static final int ACTION_TYPE_PAY = 2;

	public static final int BUTTON_DOWNLOADING_STATE = 0;
	public static final int BUTTON_DOWNLOAD_STATE = 1;
	public static final int BUTTON_PAY_STATE = 2;
	public static final int BUTTON_INSTALL_STATE = 3;
	public static final int BUTTON_UPDATE_STATE = 4;
	public static final int BUTTON_INSTALLED_STATE = 5;
	public static final int BUTTON_CANCEL_STATE = 6;
	public static final int BUTTON_REFRESH_STATE = 7;
	public static final int BUTTON_INSTALLING = 8;
	public static final int BUTTON_PAUSE = 9;
	public static final int BUTTON_ERROR = 10;
	public static final int BUTTON_INSTALL_ING = 11;

	public static final String CB_ADVERT = "3";
	public static final String CB_BEST = "2";
	public static final String CB_COMMON = "1";
	public static final String CB_UNKOWN = "0";

	public static final int STATE_LOADING = 1;
	public static final int STATE_LOADED = 2;
	public static final int STATE_NOLOADED = 3;
	public int actionType;
	public String author;
	public String cb;
	public String detailUrl;
	public String explain;
	public int icon_state;
	public String logo;
	public String number;
	public int star;
	public int state = -1;
	public boolean warn;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AppBean) {
			return this.resId.equals(((AppBean) obj).resId);
		}
		return false;
	}
}
