package com.airshiplay.mobile.pulltorefresh.internal;

public interface PullToRefreshResource {
	public static interface layout {
		static int mobile_pull_to_refresh_header_horizontal = 0x7f030000;
		static int mobile_pull_to_refresh_header_vertical = 0x7f030001;
	}

	public static interface id {
		static int fl_inner = 0x7f080000;
		static int pull_to_refresh_text = 0x7f080001;
		static int pull_to_refresh_progress = 0x7f080002;
		static int pull_to_refresh_sub_text = 0x7f080003;
		static int pull_to_refresh_image = 0x7f080004;
	}

	public static interface string {
		int pull_to_refresh_from_bottom_pull_label = 0x7f090000;
		int pull_to_refresh_from_bottom_refreshing_label = 0x7f090001;
	}
}
