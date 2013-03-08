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
public interface OnWheelScrollListener {
	public abstract void onScrollingFinished(WheelView wheelView);
	public abstract void onScrollingStarted(WheelView wheelView);
}
