/**
 * 
 */
package com.airshiplay.mobile.android.framework.wheel;

/**
 * @author airshiplay
 * @Create Date 2013-3-8
 * @version 1.0
 * @since 1.0
 */
public interface OnWheelChangedListener {
	public abstract void onChanged(WheelView wheelView, int old, int current);
}
