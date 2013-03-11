package com.github.xlh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

public class StackLayerLayout extends ViewGroup {

	private int mRowHeight = 0;

	private int mRows = 0;

	private int mMarginBetweenRows = 0;

	private int mGravity = Gravity.CENTER;

	public StackLayerLayout(Context context) {
		super(context);
	}

	public StackLayerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StackLayerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setRowHeight(int rowHeight) {
		mRowHeight = rowHeight;
	}

	public void setMarginBetweenRows(int margin) {
		mMarginBetweenRows = margin;
	}

	public void setGravity(int gravity) {
		mGravity = gravity;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			throw new RuntimeException(
					"width of stack layer layout should not be unspecified");
		}
		int padding = getPaddingLeft() + getPaddingRight();
		measureChildren(width - padding);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		if (mRowHeight == 0) {
			caculateRowHeightByChild(width);
		}
		if (heightMode == MeasureSpec.UNSPECIFIED) {
			height = mRowHeight * mRows + mMarginBetweenRows * (mRows - 1)
					+ getPaddingTop() + getPaddingBottom();
		}
		setMeasuredDimension(width, height);
	}

	private void caculateRowHeightByChild(int maxWidth) {
		int childcnt = getChildCount();
		if (childcnt == 0) {
			return;
		}

		View first = getChildAt(0);
		first.measure(
				MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		mRowHeight = first.getMeasuredHeight();
	}

	private void measureChildren(int width) {
		int cnt = getChildCount();
		if (cnt == 0) {
			return;
		}
		int rowcnt = 1;
		int currentWidth = 0;
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mRowHeight,
				MeasureSpec.EXACTLY);
		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.AT_MOST);
		for (int i = 0; i < cnt; ++i) {
			View v = getChildAt(i);
			v.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			int measuredWidth = v.getMeasuredWidth();
			if (currentWidth + measuredWidth > width) {
				++rowcnt;
				currentWidth = measuredWidth;
			} else {
				currentWidth += measuredWidth;
			}
		}
		mRows = rowcnt;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutChildren(r - l);
	}

	private void layoutChildren(int width) {
		if (mGravity == Gravity.LEFT) {
			layoutLeft(width);
		} else if (mGravity == Gravity.CENTER
				|| mGravity == Gravity.CENTER_HORIZONTAL) {
			layoutCenter(width);
		} else {
			throw new RuntimeException(
					"StackLayerLayout does not support Gravity out of[LEFT, CENTER, CENTER_HORIZONTAL]");
		}
	}

	private void layoutCenter(int width) {
		int cnt = getChildCount();
		int top = getPaddingTop();
		int left = 0;
		int paddingLeft = getPaddingLeft();
		int start = 0;
		width = width - paddingLeft - getPaddingRight();

		for (int i = 0; i < cnt; ++i) {
			View child = getChildAt(i);
			int w = child.getMeasuredWidth();
			if (w + left > width) {
				int leftmargin = (width - left) / 2;
				left = leftmargin + paddingLeft;
				for (int j = start; j < i; ++j) {
					View v = getChildAt(j);
					int t = v.getMeasuredWidth();
					v.layout(left, top, left + t, top + mRowHeight);
					left += t;
				}
				left = 0;
				start = i;
				top += mRowHeight + mMarginBetweenRows;
			}
			left += w;
		}

		int leftmargin = (width - left) / 2;
		left = paddingLeft + leftmargin;
		for (int j = start; j < cnt; ++j) {
			View v = getChildAt(j);
			int t = v.getMeasuredWidth();
			v.layout(left, top, left + t, top + mRowHeight);
			left += t;
		}
	}

	private void layoutLeft(int width) {
		int cnt = getChildCount();
		int top = getPaddingTop();
		int paddingLeft = getPaddingLeft();
		width = width - paddingLeft - getPaddingRight();
		int left = 0;
		for (int i = 0; i < cnt; ++i) {
			View child = getChildAt(i);
			int w = child.getMeasuredWidth();
			if (w + left > width) {
				left = 0;
				top += mRowHeight + mMarginBetweenRows;
			}
			child.layout(left + paddingLeft, top, left + paddingLeft + w, top
					+ mRowHeight);
			left += w;
		}
	}
}
