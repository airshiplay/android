/**
 * 
 */
package com.airshiplay.framework.desktop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;

import com.airshiplay.mobile.application.MobileApplication;

/**
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public class SurfDeskTopService extends Service {
	private OverLayoutView c = null;
	private WindowManager wm;
	private WindowManager.LayoutParams lp;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	void init() {
		this.wm = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
		this.lp = ((MobileApplication) getApplication()).getWinLayoutParams();
		this.lp.type = 2002;
		this.lp.format = 1;
		lp.flags = (0x8 | lp.flags);
		this.lp.gravity = 51;
		this.lp.x = 0;
		this.lp.y = 0;
		this.lp.width = -2;
		this.lp.height = -2;
		if (this.c == null) {
			this.c = new OverLayoutView(this);
			this.wm.addView(this.c, this.lp);
			return;
		}
		this.wm.updateViewLayout(this.c, this.lp);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
