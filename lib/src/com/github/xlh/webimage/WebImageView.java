package com.github.xlh.webimage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class WebImageView extends ImageView {
	private WebImageHelper mHelper;

	public static final int VIEW_SIZE_TYPE_LARGE = WebImageHelper.VIEW_SIZE_TYPE_LARGE;

	public static final int VIEW_SIZE_TYPE_MEDIUM = WebImageHelper.VIEW_SIZE_TYPE_MEDIUM;

	public static final int VIEW_SIZE_TYPE_SMALL = WebImageHelper.VIEW_SIZE_TYPE_SMALL;

	public static void setSmallImageSize(int size) {
		WebImageHelper.setSmallImageSize(size);
	}

	public static void setMediumImageSize(int size) {
		WebImageHelper.setMediumImageSize(size);
	}

	public static void setLargeImageSize(int size) {
		WebImageHelper.setLargeImageSize(size);
	}

	public WebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WebImageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mHelper = new WebImageHelper(this);
	}

	public void setImageAutoDownload(boolean auto) {
		mHelper.setImageAutoDownload(auto);
	}

	public boolean isImageAutoDownload() {
		return mHelper.isImageAutoDownload();
	}

	public void setDefaultImageResource(int resource) {
		mHelper.setDefaultImageResource(resource);
	}

	public void setWebImageUrl(String url) {
		mHelper.setWebImageUrl(url);
	}

	public void setViewSizeType(int type) {
		mHelper.setViewSizeType(type);
	}

	public void startFetchImage() {
		mHelper.startFetchImage();
	}
}
