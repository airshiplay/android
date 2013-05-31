package com.airshiplay.framework.example.touch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import com.airshiplay.framework.log.Logger;
import com.airshiplay.framework.log.LoggerFactory;

public class Gesture extends Activity implements
		GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
	private GestureDetectorCompat mDetector;
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new TextView(this));
		mDetector = new GestureDetectorCompat(this, this);
		// Set the gesture detector as the double tap
		// listener.
		mDetector.setOnDoubleTapListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		log.debug("onSingleTapConfirmed:%s", e);
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		log.debug("onDoubleTap:%s", e);
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		log.debug("onDoubleTapEvent:%s", e);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		log.debug("onDown:%s", e);
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		log.debug("onShowPress:%s", e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		log.debug("onSingleTapUp:%s", e);
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		log.debug("onScroll:%s %s", e1, e2);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		log.debug("onLongPress:%s", e);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		log.debug("onFling:%s %s", e1, e2);
		return true;
	}

}
