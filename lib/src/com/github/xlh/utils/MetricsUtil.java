package com.github.xlh.utils;

import android.content.Context;
import android.util.TypedValue;

public class MetricsUtil {

	public static int getScreenWidth(final Context ctx) {
		return ctx.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(final Context ctx) {
		return ctx.getResources().getDisplayMetrics().heightPixels;
	}

	public static int dp2px(final Context ctx, final float dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				ctx.getResources().getDisplayMetrics());
	}

	public static float px2dp(final Context ctx, final float px) {
		final float scale = ctx.getResources().getDisplayMetrics().density;
		return px / scale;
	}
}
