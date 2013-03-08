package com.github.xlh.utils;

import android.util.Log;

import com.github.xlh.lib.BuildConfig;

public class XLog {
	private static final String TAG = "xlog";

	public static void e(String msg) {
		e(TAG, msg);
	}

	public static void d(String msg) {
		d(TAG, msg);
	}

	public static void e(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}
}
