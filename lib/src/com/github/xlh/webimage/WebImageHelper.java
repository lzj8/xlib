package com.github.xlh.webimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.github.xlh.webimage.ImageCache.ImageCacheParams;

public class WebImageHelper {
	private ImageView mImageView;

	private String mImageUrl = "";

	private static final String IMAGE_CACHE_DIR = "img_cache";

	private Drawable mLoadingDrawable;

	private boolean mAutoDownLoadImage = true;

	public static final int INITIAL = 0;

	public static final int DOWNLOADING = 1;

	public static final int FAILED = 2;

	public static final int SUCCEEDED = 3;

	public static final int VIEW_SIZE_TYPE_SMALL = -1;

	public static final int VIEW_SIZE_TYPE_MEDIUM = -2;

	public static final int VIEW_SIZE_TYPE_LARGE = -3;

	public static final int VIEW_SIZE_TYPE_DEFAULT = VIEW_SIZE_TYPE_SMALL;

	private int mViewSizeType = VIEW_SIZE_TYPE_DEFAULT;

	private static ImageFetcher smallImageFetcher = null;

	private static ImageFetcher mediumImageFetcher = null;

	private static ImageFetcher largeImageFetcher = null;

	private static ImageCache imageCache = null;

	private static int smallImageSize = 0;

	private static int mediumImageSize = 0;

	private static int largeImageSize = 0;

	public static void setSmallImageSize(int size) {
		smallImageSize = size;
	}

	public static void setMediumImageSize(int size) {
		mediumImageSize = size;
	}

	public static void setLargeImageSize(int size) {
		largeImageSize = size;
	}

	public WebImageHelper(ImageView view) {
		mImageView = view;
	}

	public void setImageAutoDownload(boolean auto) {
		mAutoDownLoadImage = auto;
	}

	public boolean isImageAutoDownload() {
		return mAutoDownLoadImage;
	}

	public void setDefaultImageResource(int resource) {
		Drawable drawable = mImageView.getResources().getDrawable(resource);
		setDefaultImageDrawable(drawable);
	}

	public void setDefaultImageDrawable(Drawable drawable) {
		mLoadingDrawable = drawable;
	}

	public void setWebImageUrl(String url) {
		makesureCache();
		makesureImageFetcher();
		mImageUrl = url;
		if (mImageUrl == null) {
			mImageUrl = "";
		}
		mImageView.setImageDrawable(null);
		mImageView.setBackgroundDrawable(mLoadingDrawable);
		if (mAutoDownLoadImage && !mImageUrl.equals("")) {
			startFetchImage();
		}
	}

	public void setViewSizeType(int type) {
		mViewSizeType = type;
	}

	private void makesureImageFetcher() {
		switch (mViewSizeType) {
		case VIEW_SIZE_TYPE_SMALL:
			if (smallImageFetcher == null) {
				smallImageFetcher = initialImageFecher(smallImageSize);
			}
			break;
		case VIEW_SIZE_TYPE_MEDIUM:
			if (mediumImageFetcher == null) {
				mediumImageFetcher = initialImageFecher(mediumImageSize);

			}
			break;
		case VIEW_SIZE_TYPE_LARGE:
			if (largeImageFetcher == null) {
				largeImageFetcher = initialImageFecher(largeImageSize);
			}
		default:
			break;
		}
	}

	private void makesureCache() {
		if (imageCache == null) {
			ImageCacheParams params = new ImageCacheParams(
					mImageView.getContext(), IMAGE_CACHE_DIR);
			params.initDiskCacheOnCreate = true;
			imageCache = new ImageCache(params);
		}
	}

	private ImageFetcher initialImageFecher(int size) {
		if (size == 0) {
			throw new RuntimeException("WebImageView should setImageSize first");
		}
		ImageFetcher fecher = new ImageFetcher(mImageView.getContext(), size);
		fecher.setImageCache(imageCache, true);
		return fecher;
	}

	public void startFetchImage() {
		if (mImageUrl == null || mImageUrl.equals("")) {
			throw new RuntimeException("Imageurl is Empty");
		}
		switch (mViewSizeType) {
		case VIEW_SIZE_TYPE_SMALL:
			smallImageFetcher.setLoadingDrawable(mLoadingDrawable);
			smallImageFetcher.loadImage(mImageUrl, mImageView);
			break;
		case VIEW_SIZE_TYPE_MEDIUM:
			mediumImageFetcher.setLoadingDrawable(mLoadingDrawable);
			mediumImageFetcher.loadImage(mImageUrl, mImageView);
			break;
		case VIEW_SIZE_TYPE_LARGE:
			largeImageFetcher.setLoadingDrawable(mLoadingDrawable);
			largeImageFetcher.loadImage(mImageUrl, mImageView);
		default:
			break;
		}
	}

	public Uri saveImageToFile(Context context, String key) {
		if (key == null || key.equals("")) {
			throw new RuntimeException("Imageurl is Empty");
		}
		makesureImageFetcher();
		switch (mViewSizeType) {
		case VIEW_SIZE_TYPE_SMALL:
			return smallImageFetcher.saveImage(context, key);
		case VIEW_SIZE_TYPE_MEDIUM:
			return mediumImageFetcher.saveImage(context, key);
		case VIEW_SIZE_TYPE_LARGE:
			return largeImageFetcher.saveImage(context, key);
		default:
			return null;
		}
	}

	public static boolean clearCache() {
		if (imageCache == null) {
			return true;
		}
		return imageCache.clearCache();
	}

	public static long cacheSize() {
		if (imageCache == null) {
			return 0;
		}
		return imageCache.getCacheSize();
	}

	public static Bitmap getBitmapFromCache(String url) {
		if (imageCache == null) {
			return null;
		}
		Bitmap bitmap = imageCache.getBitmapFromMemCache(url);
		if (bitmap != null) {
			return bitmap;
		}
		return imageCache.getBitmapFromDiskCache(url);
	}
}
