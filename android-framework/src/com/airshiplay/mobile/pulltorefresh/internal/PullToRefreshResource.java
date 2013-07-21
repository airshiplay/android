package com.airshiplay.mobile.pulltorefresh.internal;

/**
 * 资源1 pull to refresh
 * 
 * @author airshiplay
 * @Create 2013-7-20
 * @version 1.0
 * @since 1.0
 */
public interface PullToRefreshResource {
	public static interface layout {
		final static int pull_to_refresh_header_horizontal = 0x7f030000;
		final static int pull_to_refresh_header_vertical = 0x7f030001;
	}

	public static interface id {
		static int pull_to_refresh_fl_inner = 0x7f080000;
		static int pull_to_refresh_text = 0x7f080001;
		static int pull_to_refresh_progress = 0x7f080002;
		static int pull_to_refresh_sub_text = 0x7f080003;
		static int pull_to_refresh_image = 0x7f080004;
		int gridview = 0x7f080005;
		int webview = 0x7f080006;
		int scrollview = 0x7f080007;
		int viewpager = 0x7f080008;
	}

	public static interface string {
		int pull_to_refresh_pull_label = 0x7f090000;
		int pull_to_refresh_release_label = 0x7f090001;
		int pull_to_refresh_refreshing_label = 0x7f090002;
		int pull_to_refresh_from_bottom_pull_label = 0x7f090003;
		int pull_to_refresh_from_bottom_release_label = 0x7f090004;
		int pull_to_refresh_from_bottom_refreshing_label = 0x7f090005;
	}

	public static interface dimen {
		int pull_to_refresh_indicator_right_padding = 0x7f070000;
		int pull_to_refresh_indicator_corner_radius = 0x7f070001;
		int pull_to_refresh_indicator_internal_padding = 0x7f070002;
	}

	public static interface anim {
		int slide_in_from_top = 0x7f040000;
		int slide_in_from_bottom = 0x7f040001;
		int slide_out_to_top = 0x7f040002;
		int slide_out_to_bottom = 0x7f040003;
	}

	public static interface drawable {
		int pull_to_refresh_default_ptr_flip = 0x7f020000;
		int pull_to_refresh_default_ptr_rotate = 0x7f020001;
		int pull_to_refresh_indicator_bg_top = 0x7f020002;
		int pull_to_refresh_indicator_bg_bottom = 0x7f020003;
		int pull_to_refresh_indicator_arrow = 0x7f020004;
	}

	public static interface attr {
		int ptrRefreshableViewBackground = 0x7f010000;
		int ptrHeaderBackground = 0x7f010001;
		int ptrHeaderTextColor = 0x7f010002;
		int ptrHeaderSubTextColor = 0x7f010003;
		int ptrMode = 0x7f010004;
		int ptrShowIndicator = 0x7f010005;
		int ptrDrawable = 0x7f010006;
		int ptrDrawableStart = 0x7f010007;
		int ptrDrawableEnd = 0x7f010008;
		int ptrOverScroll = 0x7f010009;
		int ptrHeaderTextAppearance = 0x7f01000a;
		int ptrSubHeaderTextAppearance = 0x7f01000b;
		int ptrAnimationStyle = 0x7f01000c;
		int ptrScrollingWhileRefreshingEnabled = 0x7f01000d;
		int ptrListViewExtrasEnabled = 0x7f01000e;
		int ptrRotateDrawableWhilePulling = 0x7f01000f;
		int ptrAdapterViewBackground = 0x7f010010;
		int ptrDrawableTop = 0x7f010011;
		int ptrDrawableBottom = 0x7f010012;
	}

	public static interface styleable {
		public static final int[] PullToRefresh = { 0x7f010000, 0x7f010001,
				0x7f010002, 0x7f010003, 0x7f010004, 0x7f010005, 0x7f010006,
				0x7f010007, 0x7f010008, 0x7f010009, 0x7f01000a, 0x7f01000b,
				0x7f01000c, 0x7f01000d, 0x7f01000e, 0x7f01000f, 0x7f010010,
				0x7f010011, 0x7f010012 };
		public static final int PullToRefresh_ptrRefreshableViewBackground = 0x00;
		public static final int PullToRefresh_ptrHeaderBackground = 0x01;
		public static final int PullToRefresh_ptrHeaderTextColor = 0x02;
		public static final int PullToRefresh_ptrHeaderSubTextColor = 0x03;
		public static final int PullToRefresh_ptrMode = 0x04;
		public static final int PullToRefresh_ptrShowIndicator = 0x05;
		public static final int PullToRefresh_ptrDrawable = 0x06;
		public static final int PullToRefresh_ptrDrawableStart = 0x07;
		public static final int PullToRefresh_ptrDrawableEnd = 0x08;
		public static final int PullToRefresh_ptrOverScroll = 0x09;
		public static final int PullToRefresh_ptrHeaderTextAppearance = 0x0a;
		public static final int PullToRefresh_ptrSubHeaderTextAppearance = 0x0b;
		public static final int PullToRefresh_ptrAnimationStyle = 0x0c;
		public static final int PullToRefresh_ptrScrollingWhileRefreshingEnabled = 0x0d;
		public static final int PullToRefresh_ptrListViewExtrasEnabled = 0x0e;
		public static final int PullToRefresh_ptrRotateDrawableWhilePulling = 0x0f;
		public static final int PullToRefresh_ptrAdapterViewBackground = 0x10;
		public static final int PullToRefresh_ptrDrawableTop = 0x11;
		public static final int PullToRefresh_ptrDrawableBottom = 0x12;
	}
}
