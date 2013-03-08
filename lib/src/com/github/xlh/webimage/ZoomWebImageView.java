package com.github.xlh.webimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.github.xlh.imagezoom.ImageViewTouch;

public class ZoomWebImageView extends ImageViewTouch {
	private WebImageHelper mWebImageHelper;

	public ZoomWebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

	public ZoomWebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZoomWebImageView(Context context) {
		super(context, null);
	}

	protected void init() {
		super.init();
		mWebImageHelper = new WebImageHelper(this);
	}

	public void setViewSizeType(int type) {
		mWebImageHelper.setViewSizeType(type);
	}

	public void setImageAutoDownload(boolean auto) {
		mWebImageHelper.setImageAutoDownload(auto);
	}

	public boolean isImageAutoDownload() {
		return mWebImageHelper.isImageAutoDownload();
	}

	public void setDefaultImageDrawable(Drawable defaultDrawable) {
		mWebImageHelper.setDefaultImageDrawable(defaultDrawable);
	}

	public void setDefaultImageResource(int resourece) {
		mWebImageHelper.setDefaultImageResource(resourece);
	}

	public void setWebImageUrl(String url) {
		mWebImageHelper.setWebImageUrl(url);
	}

	public void startFetchImage() {
		mWebImageHelper.startFetchImage();
	}

}
