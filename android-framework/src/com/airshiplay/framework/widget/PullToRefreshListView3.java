/**
 * 
 */
package com.airshiplay.framework.widget;

/**https://github.com/fruitranger/PulltorefreshListView.git
 * @author airshiplay
 * @Create Date 2013-2-27
 * @version 1.0
 * @since 1.0
 */

/**
 * 
 * @author Yao Changwei(yaochangwei@gmail.com)
 * 
 *        Pull To  Refresh List View Demo, Including the arrow and text change.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airshiplay.framework.R;

public class PullToRefreshListView3 extends RefreshableListView {

	public PullToRefreshListView3(final Context context, AttributeSet attrs) {
		super(context, attrs);

		setContentView(R.layout.pull_to_refresh);
		mListHeaderView.setBackgroundColor(Color.parseColor("#e0e0e0"));
		setOnHeaderViewChangedListener(new OnHeaderViewChangedListener() {

			@Override
			public void onViewChanged(View v, boolean canUpdate) {
				Log.d("View", "onViewChanged" + canUpdate);
				TextView tv = (TextView) v.findViewById(R.id.refresh_text);
				ImageView img = (ImageView) v.findViewById(R.id.refresh_icon);
				Animation anim;
				if (canUpdate) {
					anim = AnimationUtils.loadAnimation(context,
							R.anim.rotate_up);
					tv.setText(R.string.refresh_release);
				} else {
					tv.setText(R.string.refresh_pull_down);
					anim = AnimationUtils.loadAnimation(context,
							R.anim.rotate_down);
				}
				img.startAnimation(anim);
			}

			@Override
			public void onViewUpdating(View v) {
				Log.d("View", "onViewUpdating");
				TextView tv = (TextView) v.findViewById(R.id.refresh_text);
				ImageView img = (ImageView) v.findViewById(R.id.refresh_icon);
				ProgressBar pb = (ProgressBar) v
						.findViewById(R.id.refresh_loading);
				pb.setVisibility(View.VISIBLE);
				tv.setText(R.string.loading);
				img.clearAnimation();
				img.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onViewUpdateFinish(View v) {
				Log.d("View", "onViewUpdateFinish");
				TextView tv = (TextView) v.findViewById(R.id.refresh_text);
				ImageView img = (ImageView) v.findViewById(R.id.refresh_icon);
				ProgressBar pb = (ProgressBar) v
						.findViewById(R.id.refresh_loading);

				tv.setText(R.string.refresh_pull_down);
				pb.setVisibility(View.INVISIBLE);
				tv.setVisibility(View.VISIBLE);
				img.setVisibility(View.VISIBLE);
			}

		});
	}

}