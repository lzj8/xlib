package com.github.xlh.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

//clock wise of items--> 1, 2, 3, 4...
public class PetalMenuView extends View implements
		GestureDetector.OnGestureListener {
	private static final float MAX_ROTATION = 360.0f;

	private static final int DEFAULT_ROTATE_DURATION = 350;

	private static final int DEFAULT_SCALE_DURATION = 170;// This is one item's
															// duration

	private static final int DEFAULT_SCALE_EACH_DELAY_DURATION = 70;

	private static final float DEFAULT_MENU_OFFSET_TO_CENTER = 0.6f;

	private static final float PRESSED_SCALE_X = 0.9f;

	private static final float PRESSED_SCALE_Y = 0.96f;

	private static final float SELECTION_MAX_DEGREE = 30f;

	private static final float EPS = 1e-8f;

	private static final int STAGE_STABLE = 0;

	private static final int STAGE_SCALE = 1;

	private static final int STAGE_ROTATE = 2;

	private static int REDIUS_INNER = 10;

	private Drawable[] mBgs = null;

	private PetalMenuDrawer mMenuDrawer = null;

	private int mRedius = 0;

	// for animating
	private boolean mIsAnimating = false;

	// for rotate
	private long mRotationStartTime = 0;

	private int mRotateDuration = DEFAULT_ROTATE_DURATION;

	private boolean mIsInShowingOrHidingAnim = true;// or hiding

	Interpolator mRotateToShowInterpolator = new DecelerateInterpolator();

	Interpolator mRotateToHideInterpolator = new AccelerateInterpolator();

	// for scale
	private float[] mScales = null;

	private long mScaleStartTime = 0;

	private int mTotalScaleDuration = DEFAULT_SCALE_DURATION;

	private int mScaleEachDelay = DEFAULT_SCALE_EACH_DELAY_DURATION;

	private int mScaleEachDuration = DEFAULT_SCALE_DURATION;

	Interpolator mScaleToShowInterpolator = new DecelerateInterpolator();

	Interpolator mScaleToHideInterpolator = new AccelerateInterpolator();

	private PetalMenuListener mListener = null;

	// for event
	private boolean mEnableTapDismiss = true;

	private static final int SELECTION_NONE = -1;

	private int mSelectMenuIndex = SELECTION_NONE;

	private GestureDetector mDetector = null;

	private boolean mEnableEdgeDetection = true;// can only detect scroll view

	public static interface PetalMenuListener {
		public void onShowComplete(PetalMenuView view);

		public void onHideComplete(PetalMenuView view);

		public void onClick(PetalMenuView view, int itemIndex);
	};

	public static interface PetalMenuDrawer {
		public void onDrawMenu(PetalMenuView menu, int centerX, int centerY,
				Canvas canvas, int menuIndex);

		public int offsetToCenterOfItem(PetalMenuView menu, int index);
	}

	public static class DrawableMenuDrawer implements PetalMenuDrawer {

		private Drawable[] mMenus = null;

		private int[] mWidth = null;

		private int[] mHeight = null;

		public DrawableMenuDrawer(Context ctx, int[] menus) {
			mMenus = new Drawable[menus.length];
			for (int i = 0; i < mMenus.length; ++i) {
				mMenus[i] = ctx.getResources().getDrawable(menus[i]);
			}
			init(mMenus);
		}

		public DrawableMenuDrawer(Drawable[] drawables) {
			init(drawables);
		}

		private void init(Drawable[] drawables) {
			int len = drawables.length;
			mMenus = drawables;
			mWidth = new int[len];
			mHeight = new int[len];
			for (int i = 0; i < len; ++i) {
				mWidth[i] = drawables[i].getIntrinsicWidth();
				mHeight[i] = drawables[i].getIntrinsicHeight();
			}
		}

		@Override
		public void onDrawMenu(PetalMenuView menu, int centerX, int centerY,
				Canvas canvas, int menuIndex) {
			int startX = centerX - mWidth[menuIndex] / 2;
			int startY = centerY - mHeight[menuIndex] / 2;
			mMenus[menuIndex].setBounds(startX, startY, startX
					+ mWidth[menuIndex], startY + mHeight[menuIndex]);
			mMenus[menuIndex].draw(canvas);
		}

		@Override
		public int offsetToCenterOfItem(PetalMenuView menu, int index) {
			return (int) (mHeight[index] * DEFAULT_MENU_OFFSET_TO_CENTER);
		}
	}

	public void setPetalMenuDrawer(PetalMenuDrawer drawer) {
		mMenuDrawer = drawer;
	}

	public int getRadius() {
		return mRedius;
	}

	public void setEdgeDetectionEnable(boolean enable) {
		mEnableEdgeDetection = enable;
	}

	public void setPetalMenuListener(PetalMenuListener listener) {
		mListener = listener;
	}

	public void setRotateDuration(int rotateDuration) {
		mRotateDuration = rotateDuration;
	}

	public void setRotateInterpolator(Interpolator allInOne) {
		setRotateInterpolator(allInOne, allInOne);
	}

	public void setRotateInterpolator(Interpolator show, Interpolator hide) {
		mRotateToShowInterpolator = show;
		mRotateToHideInterpolator = hide;
	}

	public void setScaleInterpolator(Interpolator allInOne) {
		setScaleInterpolator(allInOne, allInOne);
	}

	public void setScaleInterpolator(Interpolator show, Interpolator hide) {
		mScaleToShowInterpolator = show;
		mScaleToHideInterpolator = hide;
	}

	public void setScaleDuration(int scaleDuration) {
		setScaleDuration(scaleDuration, DEFAULT_SCALE_EACH_DELAY_DURATION);
	}

	public void setScaleDuration(int scaleDuration, int eachDelay) {
		mScaleEachDuration = scaleDuration;
		mScaleEachDelay = eachDelay;
		if (mBgs != null) {
			mTotalScaleDuration += mBgs.length * eachDelay;
			mScales = new float[mBgs.length];
		}
	}

	public PetalMenuView(Context context) {
		super(context);
		init();
	}

	public PetalMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PetalMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void showAtLocation(int centerX, int centerY) {
		showAtLocation(centerX, centerY, true);
	}

	public void showAtLocation(int centerX, int centerY, boolean animated) {

		ViewGroup group = (ViewGroup) getParent();
		if (mEnableEdgeDetection) {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			// int pscrollX = 0;
			int pscrollY = 0;
			// int displayWidth = metrics.widthPixels;
			int displayHeight = metrics.heightPixels;

			Rect outRect = new Rect();
			getWindowVisibleDisplayFrame(outRect);

			int statusBarHeight = outRect.top;

			ViewGroup pp = (ViewGroup) group.getParent();

			if (pp != null && pp instanceof ScrollView) {
				pscrollY = pp.getScrollY();
			}
			if (centerX < mRedius) {
				centerX = mRedius;
			}
			if (centerY - pscrollY < mRedius) {
				centerY = mRedius + pscrollY;
			}
			if (group.getWidth() - centerX < mRedius) {
				centerX = group.getWidth() - mRedius;
			}
			if (displayHeight - statusBarHeight - (centerY - pscrollY) < mRedius) {
				centerY = displayHeight - statusBarHeight + pscrollY - mRedius;
			}
		}

		int height = mRedius << 1;
		int width = mRedius << 1;
		if (group instanceof FrameLayout) {
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width,
					height);
			lp.gravity = Gravity.LEFT | Gravity.TOP;
			lp.leftMargin = centerX - width / 2;
			lp.topMargin = centerY - height / 2;
			setLayoutParams(lp);
		} else if (group instanceof RelativeLayout) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					width, height);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			lp.leftMargin = centerX - width / 2;
			lp.topMargin = centerY - height / 2;
			setLayoutParams(lp);
		} else {
			throw new RuntimeException(
					"PetalMenu Cannot show in ViewGroup "
							+ "that not support margin, please use Relative or FrameLayout as its parent");
		}
		show(animated);
	}

	public void show() {
		show(true);
	}

	public void show(boolean animated) {
		setVisibility(View.VISIBLE);
		mIsInShowingOrHidingAnim = true;
		if (animated) {
			mScaleStartTime = AnimationUtils.currentAnimationTimeMillis();
		} else {
			mScaleStartTime = 0;
		}
		mRotationStartTime = mScaleStartTime + mTotalScaleDuration;
		mIsAnimating = true;
		invalidate();
	}

	public boolean isShow() {
		return getVisibility() == View.VISIBLE;
	}

	public void hide() {
		hide(true);
	}

	public void hide(boolean animated) {
		if (mIsAnimating && !mIsInShowingOrHidingAnim) {
			return;
		}
		mIsInShowingOrHidingAnim = false;
		if (animated) {
			mRotationStartTime = AnimationUtils.currentAnimationTimeMillis();
		}
		mScaleStartTime = mRotationStartTime + mRotateDuration;
		mIsAnimating = true;
		invalidate();
	}

	private void init() {
		setVisibility(View.GONE);
		setScaleDuration(DEFAULT_SCALE_DURATION,
				DEFAULT_SCALE_EACH_DELAY_DURATION);
		mDetector = new GestureDetector(getContext(), this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = (mRedius << 1);
		int height = (mRedius << 1);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		setBounds();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long drawingTime = AnimationUtils.currentAnimationTimeMillis();
		int stage = computeStage(drawingTime);
		if (stage == STAGE_ROTATE) {
			drawRotate(canvas, drawingTime);
			postInvalidate();
		} else if (stage == STAGE_SCALE) {
			drawScale(canvas, drawingTime);
			postInvalidate();
		} else {
			drawStable(canvas, drawingTime);
			if (mIsInShowingOrHidingAnim && mIsAnimating) {
				doShowComplete();
			} else if (mIsAnimating) {
				doHideComplete();
			}
		}
	}

	private void setBounds() {
		for (int i = 0; i < mBgs.length; ++i) {
			int width = mBgs[i].getIntrinsicWidth();
			int height = mBgs[i].getIntrinsicHeight();
			mBgs[i].setBounds(mRedius - width / 2, 0, mRedius + width / 2,
					height);
		}
	}

	private void doShowComplete() {
		if (mListener != null) {
			mListener.onShowComplete(this);
		}
		mIsAnimating = false;
	}

	private void doHideComplete() {
		setVisibility(View.GONE);
		if (mListener != null) {
			mListener.onHideComplete(this);
		}
		mIsAnimating = false;
	}

	private void drawScale(Canvas canvas, long drawingTime) {
		computeScale(drawingTime);
		float initEach = MAX_ROTATION / mBgs.length;
		for (int i = 0; i < mBgs.length; ++i) {
			canvas.save();
			canvas.scale(mScales[i], mScales[i], mRedius, mRedius);
			mBgs[i].draw(canvas);
			if (mMenuDrawer != null) {
				int offset = mMenuDrawer.offsetToCenterOfItem(this, i);
				int centerY = mBgs[i].getIntrinsicHeight() - offset;
				canvas.save();
				canvas.rotate(-initEach * (i + 1), mRedius, centerY);
				mMenuDrawer.onDrawMenu(this, mRedius, centerY, canvas, i);
				canvas.restore();
			}
			canvas.restore();
		}
	}

	private void computeScale(long drawingTime) {
		if (mIsInShowingOrHidingAnim) {
			for (int i = 0; i < mBgs.length; ++i) {
				if (i * mScaleEachDelay + mScaleStartTime > drawingTime) {
					mScales[i] = 0.0f;
				} else {
					long time = drawingTime - i * mScaleEachDelay
							- mScaleStartTime;
					float value = (float) time / mScaleEachDuration;
					if (value > 1.0 + EPS) {
						mScales[i] = 1.0f;
					} else {
						value = mScaleToShowInterpolator
								.getInterpolation(value);
						mScales[i] = value;
					}
				}
			}
		} else {
			int cnt = 0;
			for (int i = mBgs.length - 1; i >= 0; --i) {
				if (cnt * mScaleEachDelay + mScaleStartTime > drawingTime) {
					mScales[i] = 1.0f;
				} else {
					long time = drawingTime - cnt * mScaleEachDelay
							- mScaleStartTime;
					float value = (float) time / mScaleEachDuration;
					if (value > 1.0 + EPS) {
						mScales[i] = 0.0f;
					} else {
						value = mScaleToHideInterpolator
								.getInterpolation(value);
						mScales[i] = 1.0f - value;
					}
				}
				++cnt;
			}
		}
	}

	private void drawStable(Canvas canvas, long drawingTime) {
		if (mIsInShowingOrHidingAnim) {
			drawRotate(canvas, drawingTime);
		} else {
			// do nothing
		}
	}

	private int computeStage(long drawingTime) {
		if (mIsInShowingOrHidingAnim) {
			if (drawingTime >= mScaleStartTime
					&& drawingTime < mRotationStartTime) {
				return STAGE_SCALE;
			} else if (drawingTime >= mRotationStartTime
					&& drawingTime <= mRotationStartTime + mRotateDuration) {
				return STAGE_ROTATE;
			} else {
				return STAGE_STABLE;
			}
		} else {
			if (drawingTime >= mRotationStartTime
					&& drawingTime <= mScaleStartTime) {
				return STAGE_ROTATE;
			} else if (drawingTime >= mScaleStartTime
					&& drawingTime <= mScaleStartTime + mTotalScaleDuration) {
				return STAGE_SCALE;
			} else {
				return STAGE_STABLE;
			}
		}
	}

	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
			| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
			| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
			| Canvas.CLIP_TO_LAYER_SAVE_FLAG;

	private void drawRotate(Canvas canvas, long drawingTime) {
		float maxRotation = computeMaxRotation(drawingTime);
		canvas.save();
		float each = maxRotation / mBgs.length;
		float initEach = MAX_ROTATION / mBgs.length;
		for (int i = 0; i < mBgs.length; ++i) {
			canvas.rotate(each, mRedius, mRedius);
			if (i == mSelectMenuIndex) {
				canvas.saveLayerAlpha(0, 0, (mRedius << 1), (mRedius << 1),
						122, LAYER_FLAGS);
				canvas.scale(PRESSED_SCALE_X, PRESSED_SCALE_Y, mRedius, mRedius);
			}
			mBgs[i].draw(canvas);
			if (mMenuDrawer != null) {
				canvas.save();
				int offset = mMenuDrawer.offsetToCenterOfItem(this, i);
				int centerY = mBgs[i].getIntrinsicHeight() - offset;
				canvas.rotate(-initEach * (i + 1), mRedius, centerY);
				mMenuDrawer.onDrawMenu(this, mRedius, centerY, canvas, i);
				canvas.restore();
			}
			if (i == mSelectMenuIndex) {
				canvas.restore();
			}
		}
		canvas.restore();
	}

	private float computeMaxRotation(long drawingTime) {
		int duration = (int) (drawingTime - mRotationStartTime);
		if (duration > mRotateDuration) {
			if (mIsInShowingOrHidingAnim) {
				return MAX_ROTATION;
			} else {
				return 0.0f;
			}
		}
		float current = (float) duration / mRotateDuration;
		if (mIsInShowingOrHidingAnim) {
			float maped = mRotateToShowInterpolator.getInterpolation(current);
			return MAX_ROTATION * maped;
		} else {
			float maped = mRotateToHideInterpolator.getInterpolation(current);
			return MAX_ROTATION * (1 - maped);
		}
	}

	public void setMenus(int[] bgs, int[] menus) {
		if (menus != null && bgs.length != menus.length) {
			throw new RuntimeException(
					"Backgrounds and Menus should have the same length");
		}
		Context ctx = getContext();
		mBgs = new Drawable[bgs.length];
		if (menus != null) {
			setPetalMenuDrawer(new DrawableMenuDrawer(ctx, menus));
		}
		for (int i = 0; i < mBgs.length; ++i) {
			mBgs[i] = ctx.getResources().getDrawable(bgs[i]);
			int height = mBgs[i].getIntrinsicHeight();
			if (mRedius < height) {
				mRedius = height;
			}
		}
		mRedius += REDIUS_INNER;
		mTotalScaleDuration = mScaleEachDuration + mBgs.length
				* mScaleEachDelay;
		mScales = new float[mBgs.length];

	}

	public void setMenus(int bg, int[] menus) {
		int[] bgs = new int[menus.length];
		for (int i = 0; i < bgs.length; ++i) {
			bgs[i] = bg;
		}
		setMenus(bgs, menus);
	}

	public void setMenus(int[] bgs) {
		setMenus(bgs, null);
	}

	public void replaceMenuBgs(int[] bgs) {
		if (mBgs.length != bgs.length) {
			throw new RuntimeException(
					"replaceMenusBgs should have the same length with initial Bgs");
		}

		for (int i = 0; i < bgs.length; ++i) {
			mBgs[i] = getResources().getDrawable(bgs[i]);
		}
		setBounds();
		invalidate();
	}

	private int findMenuByPos(float x, float y) {
		float dx = x - mRedius;
		float dy = mRedius - y;
		float squareR = dx * dx + dy * dy;
		if (squareR > mRedius * mRedius + EPS) {
			return SELECTION_NONE;
		}

		int pos = SELECTION_NONE;
		float minAnchor = MAX_ROTATION;
		float each = MAX_ROTATION / mBgs.length;
		float radianCosin = (mRedius - y) / (FloatMath.sqrt(squareR));
		float pointDegree = (float) Math.toDegrees(Math.acos(radianCosin));
		if (dx < 0) {
			pointDegree = 360 - pointDegree;
		}
		for (int i = 0; i < mBgs.length; ++i) {
			float current = each * (i + 1);
			float d = Math.abs(current - pointDegree);
			float d1 = Math.abs(current - 360 - pointDegree);
			d = Math.min(d, d1);
			if (d < minAnchor) {
				pos = i;
				minAnchor = d;
			}
		}
		if (pos == SELECTION_NONE || minAnchor > SELECTION_MAX_DEGREE + EPS) {
			return SELECTION_NONE;
		}
		return pos;
	}

	public void setEnableTabDismiss(boolean enable) {
		mEnableTapDismiss = enable;
	}

	// event handling
	public boolean onTouchEvent(MotionEvent event) {
		if (mIsAnimating) {
			return false;
		}
		if (mDetector.onTouchEvent(event)) {
			return true;
		} else {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_CANCEL) {
				mSelectMenuIndex = SELECTION_NONE;
				invalidate();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		mSelectMenuIndex = findMenuByPos(e.getX(), e.getY());
		if (mSelectMenuIndex != SELECTION_NONE || mEnableTapDismiss) {
			invalidate();
			return true;
		}
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		invalidate();
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (mSelectMenuIndex != SELECTION_NONE) {
			if (mListener != null) {
				mListener.onClick(PetalMenuView.this, mSelectMenuIndex);
			}

			mSelectMenuIndex = SELECTION_NONE;
			invalidate();
			return true;
		}
		if (mEnableTapDismiss) {
			hide();
			return true;
		}
		return false;
	};
}
