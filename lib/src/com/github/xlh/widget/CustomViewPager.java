package com.github.xlh.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class CustomViewPager extends ViewPager {

	public interface OnTouchEventListener {
		public void onLongPressed(View view, int x, int y);

		public void onSingleTapConfirmed(View view, int x, int y);
	}

	private GestureDetector mGestureDetector = null;
	private OnTouchEventListener mLongPressedListener;
	private boolean mInterceptTouchEvent = false;

	public CustomViewPager(Context context) {
		super(context);
		init();
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setOnLongPressedListener(OnTouchEventListener listener) {
		mLongPressedListener = listener;
	}

	public void setInterceptTouchEvent(boolean mInterceptTouchEvent) {
		this.mInterceptTouchEvent = mInterceptTouchEvent;
	}

	private void init() {
		mGestureDetector = new GestureDetector(getContext(),
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public void onLongPress(MotionEvent e) {
						int x = (int) e.getX();
						int y = (int) e.getY();
						if (mLongPressedListener != null) {
							mLongPressedListener.onLongPressed(
									CustomViewPager.this, x, y);
						}
					}

					@Override
					public boolean onSingleTapConfirmed(MotionEvent e) {
						int x = (int) e.getX();
						int y = (int) e.getY();
						if (mLongPressedListener != null) {
							mLongPressedListener.onSingleTapConfirmed(
									CustomViewPager.this, x, y);
						}
						return true;
					}

				});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		if (getAdapter() == null || getAdapter().getCount() == 0) {
			return false;
		} else {
			if (mInterceptTouchEvent) {
				return false;
			} else {
				try {
					return super.onInterceptTouchEvent(event);
				} catch (Exception e) {
					return false;
				}
			}
		}
	}

}
