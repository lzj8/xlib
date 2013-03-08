package com.github.xlh.webimage;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.util.Log;

@SuppressLint({ "ParserError", "ParserError" })
public class ImageUtil {
	private static final String TAG = "ImageUtil";

	private static final boolean DEBUG = false;

	/**
	 * get image size
	 * 
	 * @param is
	 *            - InputStream
	 * @param options
	 *            as output (outWidht and outHeight)
	 * @return True if succeeded, False else.
	 */
	public static boolean getImageSize(InputStream is,
			BitmapFactory.Options options) {
		if (DEBUG) {
			Log.d(TAG, "getImageSize");
		}

		boolean succeeded = false;
		if (is != null && options != null) {
			// get size of decoded image, then calculate sample size
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, options);
			if (options.outWidth > 0 && options.outHeight > 0) {
				if (DEBUG)
					Log.d(TAG, "outWidth = [" + options.outWidth
							+ "], outHeight = [" + options.outHeight + "]");
				succeeded = true;
			}
		}
		if (DEBUG) {
			Log.v(TAG, "getImageSize/out - [" + succeeded + "]");
		}
		return succeeded;
	}

}
