package com.techmali.smartteam.touchzoomimageview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.techmali.smartteam.BuildConfig;

/**
 * Base View to manage image zoom/scrool/pinch operations
 *
 * @author Vijay Desai
 */
public abstract class ImageViewTouchBase extends ImageView implements IDisposable {
    /**
     * The constant VERSION.
     */
    public static final String VERSION = BuildConfig.VERSION_NAME;
    /**
     * The constant MIN_SCALE_DIFF.
     */
    public static final float MIN_SCALE_DIFF = 0.1f;

    /**
     * The interface On drawable change listener.
     */
    public interface OnDrawableChangeListener {
        /**
         * Callback invoked when a new drawable has been
         * assigned to the view
         *
         * @param drawable the drawable
         */
        void onDrawableChanged(Drawable drawable);
    }

    /**
     * The interface On layout change listener.
     */
    public interface OnLayoutChangeListener {
        /**
         * Callback invoked when the layout bounds changed
         *
         * @param changed the changed
         * @param left    the left
         * @param top     the top
         * @param right   the right
         * @param bottom  the bottom
         */
        void onLayoutChanged(boolean changed, int left, int top, int right, int bottom);
    }

    /**
     * Use this to change the {@link ImageViewTouchBase#setDisplayType(DisplayType)} of
     * this View
     *
     * @author alessandro
     */
    public enum DisplayType {
        /**
         * Image is not scaled by default
         */
        NONE,
        /**
         * Image will be always presented using this view's bounds
         */
        FIT_TO_SCREEN,
        /**
         * Image will be scaled only if bigger than the bounds of this view
         */
        FIT_IF_BIGGER
    }

    /**
     * The constant TAG.
     */
    public static final String TAG = "ImageViewTouchBase";
    /**
     * The constant DEBUG.
     */
    @SuppressWarnings("checkstyle:staticvariablename")
    protected static boolean DEBUG = false;
    /**
     * The constant ZOOM_INVALID.
     */
    public static final float ZOOM_INVALID = -1f;
    /**
     * The M base matrix.
     */
    protected Matrix mBaseMatrix = new Matrix();
    /**
     * The M supp matrix.
     */
    protected Matrix mSuppMatrix = new Matrix();
    /**
     * The M next matrix.
     */
    protected Matrix mNextMatrix;
    /**
     * The M layout runnable.
     */
    protected Runnable mLayoutRunnable = null;
    /**
     * The M user scaled.
     */
    protected boolean mUserScaled = false;
    /**
     * The M max zoom.
     */
    protected float mMaxZoom = ZOOM_INVALID;
    /**
     * The M min zoom.
     */
    protected float mMinZoom = ZOOM_INVALID;
    /**
     * The M max zoom defined.
     */
// true when min and max zoom are explicitly defined
    protected boolean mMaxZoomDefined;
    /**
     * The M min zoom defined.
     */
    protected boolean mMinZoomDefined;
    /**
     * The M display matrix.
     */
    protected final Matrix mDisplayMatrix = new Matrix();
    /**
     * The M matrix values.
     */
    protected final float[] mMatrixValues = new float[9];
    /**
     * The M scale type.
     */
    protected DisplayType mScaleType = DisplayType.FIT_IF_BIGGER;
    /**
     * The M scale type changed.
     */
    protected boolean mScaleTypeChanged;
    /**
     * The M bitmap changed.
     */
    protected boolean mBitmapChanged;
    /**
     * The M default animation duration.
     */
    protected int mDefaultAnimationDuration;
    /**
     * The M min fling velocity.
     */
    protected int mMinFlingVelocity;
    /**
     * The M max fling velocity.
     */
    protected int mMaxFlingVelocity;
    /**
     * The M center.
     */
    protected PointF mCenter = new PointF();
    /**
     * The M bitmap rect.
     */
    protected RectF mBitmapRect = new RectF();
    /**
     * The M bitmap rect tmp.
     */
    protected RectF mBitmapRectTmp = new RectF();
    /**
     * The M center rect.
     */
    protected RectF mCenterRect = new RectF();
    /**
     * The M scroll point.
     */
    protected PointF mScrollPoint = new PointF();
    /**
     * The M view port.
     */
    protected RectF mViewPort = new RectF();
    /**
     * The M view port old.
     */
    protected RectF mViewPortOld = new RectF();
    private Animator mCurrentAnimation;
    private OnDrawableChangeListener mDrawableChangeListener;
    private OnLayoutChangeListener mOnLayoutChangeListener;

    /**
     * Instantiates a new Image view touch base.
     *
     * @param context the context
     */
    public ImageViewTouchBase(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Image view touch base.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ImageViewTouchBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Image view touch base.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public ImageViewTouchBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    /**
     * Gets bitmap changed.
     *
     * @return the bitmap changed
     */
    public boolean getBitmapChanged() {
        return mBitmapChanged;
    }

    /**
     * Sets on drawable changed listener.
     *
     * @param listener the listener
     */
    public void setOnDrawableChangedListener(OnDrawableChangeListener listener) {
        mDrawableChangeListener = listener;
    }

    /**
     * Sets on layout change listener.
     *
     * @param listener the listener
     */
    public void setOnLayoutChangeListener(OnLayoutChangeListener listener) {
        mOnLayoutChangeListener = listener;
    }

    /**
     * Init.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    protected void init(Context context, AttributeSet attrs, int defStyle) {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        mDefaultAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        setScaleType(ScaleType.MATRIX);
    }

    /**
     * Clear the current drawable
     */
    public void clear() {
        setImageBitmap(null);
    }

    /**
     * Change the display type
     *
     * @param type the type
     * @type
     */
    public void setDisplayType(DisplayType type) {
        if (type != mScaleType) {
            if (DEBUG) {
                Log.i(TAG, "setDisplayType: " + type);
            }
            mUserScaled = false;
            mScaleType = type;
            mScaleTypeChanged = true;
            requestLayout();
        }
    }

    /**
     * Gets display type.
     *
     * @return the display type
     */
    public DisplayType getDisplayType() {
        return mScaleType;
    }

    /**
     * Sets min scale.
     *
     * @param value the value
     */
    protected void setMinScale(float value) {
        if (DEBUG) {
            Log.d(TAG, "setMinZoom: " + value);
        }

        mMinZoom = value;
    }

    /**
     * Sets max scale.
     *
     * @param value the value
     */
    protected void setMaxScale(float value) {
        if (DEBUG) {
            Log.d(TAG, "setMaxZoom: " + value);
        }
        mMaxZoom = value;
    }

    /**
     * On view port changed.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    protected void onViewPortChanged(float left, float top, float right, float bottom) {
        mViewPort.set(left, top, right, bottom);
        mCenter.x = mViewPort.centerX();
        mCenter.y = mViewPort.centerY();
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (DEBUG) {
            Log.e(TAG, "onLayout: " + changed + ", bitmapChanged: " + mBitmapChanged + ", scaleChanged: " + mScaleTypeChanged);
        }

        float deltaX = 0;
        float deltaY = 0;

        if (changed) {
            mViewPortOld.set(mViewPort);
            onViewPortChanged(left, top, right, bottom);

            deltaX = mViewPort.width() - mViewPortOld.width();
            deltaY = mViewPort.height() - mViewPortOld.height();
        }

        super.onLayout(changed, left, top, right, bottom);

        Runnable r = mLayoutRunnable;

        if (r != null) {
            mLayoutRunnable = null;
            r.run();
        }

        final Drawable drawable = getDrawable();

        if (drawable != null) {

            if (changed || mScaleTypeChanged || mBitmapChanged) {

                if (mBitmapChanged) {
                    mUserScaled = false;
                    mBaseMatrix.reset();
                    if (!mMinZoomDefined) {
                        mMinZoom = ZOOM_INVALID;
                    }
                    if (!mMaxZoomDefined) {
                        mMaxZoom = ZOOM_INVALID;
                    }
                }

                float scale = 1;

                // retrieve the old values
                float oldDefaultScale = getDefaultScale(getDisplayType());
                float oldMatrixScale = getScale(mBaseMatrix);
                float oldScale = getScale();
                float oldMinScale = Math.min(1f, 1f / oldMatrixScale);

                getProperBaseMatrix(drawable, mBaseMatrix, mViewPort);

                float newMatrixScale = getScale(mBaseMatrix);

                if (DEBUG) {
                    Log.d(TAG, "old matrix scale: " + oldMatrixScale);
                    Log.d(TAG, "new matrix scale: " + newMatrixScale);
                    Log.d(TAG, "old min scale: " + oldMinScale);
                    Log.d(TAG, "old scale: " + oldScale);
                }

                // 1. bitmap changed or scaletype changed
                if (mBitmapChanged || mScaleTypeChanged) {

                    if (DEBUG) {
                        Log.d(TAG, "display type: " + getDisplayType());
                        Log.d(TAG, "newMatrix: " + mNextMatrix);
                    }

                    if (mNextMatrix != null) {
                        mSuppMatrix.set(mNextMatrix);
                        mNextMatrix = null;
                        scale = getScale();
                    } else {
                        mSuppMatrix.reset();
                        scale = getDefaultScale(getDisplayType());
                    }

                    setImageMatrix(getImageViewMatrix());

                    if (scale != getScale()) {
                        if (DEBUG) {
                            Log.v(TAG, "scale != getScale: " + scale + " != " + getScale());
                        }
                        zoomTo(scale);
                    }

                } else if (changed) {

                    // 2. layout size changed

                    if (!mMinZoomDefined) {
                        mMinZoom = ZOOM_INVALID;
                    }
                    if (!mMaxZoomDefined) {
                        mMaxZoom = ZOOM_INVALID;
                    }

                    setImageMatrix(getImageViewMatrix());
                    postTranslate(-deltaX, -deltaY);

                    if (!mUserScaled) {
                        scale = getDefaultScale(getDisplayType());
                        if (DEBUG) {
                            Log.v(TAG, "!userScaled. scale=" + scale);
                        }
                        zoomTo(scale);
                    } else {
                        if (Math.abs(oldScale - oldMinScale) > MIN_SCALE_DIFF) {
                            scale = (oldMatrixScale / newMatrixScale) * oldScale;
                        }
                        if (DEBUG) {
                            Log.v(TAG, "userScaled. scale=" + scale);
                        }
                        zoomTo(scale);
                    }

                    if (DEBUG) {
                        Log.d(TAG, "old min scale: " + oldDefaultScale);
                        Log.d(TAG, "old scale: " + oldScale);
                        Log.d(TAG, "new scale: " + scale);
                    }
                }

                if (scale > getMaxScale() || scale < getMinScale()) {
                    // if current scale if outside the min/max bounds
                    // then restore the correct scale
                    zoomTo(scale);
                }

                center(true, true);

                if (mBitmapChanged) {
                    onDrawableChanged(drawable);
                }
                if (changed || mBitmapChanged || mScaleTypeChanged) {
                    onLayoutChanged(left, top, right, bottom);
                }

                if (mScaleTypeChanged) {
                    mScaleTypeChanged = false;
                }
                if (mBitmapChanged) {
                    mBitmapChanged = false;
                }

                if (DEBUG) {
                    Log.d(TAG, "scale: " + getScale() + ", minScale: " + getMinScale() + ", maxScale: " + getMaxScale());
                }
            }
        } else {
            // drawable is null
            if (mBitmapChanged) {
                onDrawableChanged(drawable);
            }
            if (changed || mBitmapChanged || mScaleTypeChanged) {
                onLayoutChanged(left, top, right, bottom);
            }

            if (mBitmapChanged) {
                mBitmapChanged = false;
            }
            if (mScaleTypeChanged) {
                mScaleTypeChanged = false;
            }
        }
    }

    @Override
    protected void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (DEBUG) {
            Log.i(
                    TAG,
                    "onConfigurationChanged. scale: " + getScale() + ", minScale: " + getMinScale() + ", mUserScaled: " + mUserScaled
            );
        }

        if (mUserScaled) {
            mUserScaled = Math.abs(getScale() - getMinScale()) > MIN_SCALE_DIFF;
        }

        if (DEBUG) {
            Log.v(TAG, "mUserScaled: " + mUserScaled);
        }
    }

    /**
     * Restore the original display
     */
    public void resetDisplay() {
        mBitmapChanged = true;
        requestLayout();
    }

    /**
     * Reset matrix.
     */
    public void resetMatrix() {
        if (DEBUG) {
            Log.i(TAG, "resetMatrix");
        }
        mSuppMatrix = new Matrix();

        float scale = getDefaultScale(getDisplayType());
        setImageMatrix(getImageViewMatrix());

        if (DEBUG) {
            Log.d(TAG, "default scale: " + scale + ", scale: " + getScale());
        }

        if (scale != getScale()) {
            zoomTo(scale);
        }

        postInvalidate();
    }

    /**
     * Gets default scale.
     *
     * @param type the type
     * @return the default scale
     */
    protected float getDefaultScale(DisplayType type) {
        if (type == DisplayType.FIT_TO_SCREEN) {
            // always fit to screen
            return 1f;
        } else if (type == DisplayType.FIT_IF_BIGGER) {
            // normal scale if smaller, fit to screen otherwise
            return Math.min(1f, 1f / getScale(mBaseMatrix));
        } else {
            // no scale
            return 1f / getScale(mBaseMatrix);
        }
    }

    @Override
    public void setImageResource(int resId) {
        setImageDrawable(getContext().getResources().getDrawable(resId));
    }

    /**
     * {@inheritDoc} Set the new image to display and reset the internal matrix.
     *
     * @param bitmap the {@link Bitmap} to display
     * @see {@link ImageView#setImageBitmap(Bitmap)}
     */
    @Override
    public void setImageBitmap(final Bitmap bitmap) {
        setImageBitmap(bitmap, null, ZOOM_INVALID, ZOOM_INVALID);
    }

    /**
     * Sets image bitmap.
     *
     * @param bitmap  the bitmap
     * @param matrix  the matrix
     * @param minZoom the min zoom
     * @param maxZoom the max zoom
     * @see #setImageDrawable(Drawable, Matrix, float, float) #setImageDrawable(Drawable, Matrix, float, float)#setImageDrawable(Drawable, Matrix, float, float)
     */
    public void setImageBitmap(final Bitmap bitmap, Matrix matrix, float minZoom, float maxZoom) {
        if (bitmap != null) {
            setImageDrawable(new FastBitmapDrawable(bitmap), matrix, minZoom, maxZoom);
        } else {
            setImageDrawable(null, matrix, minZoom, maxZoom);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        setImageDrawable(drawable, null, ZOOM_INVALID, ZOOM_INVALID);
    }

    /**
     * Note: if the scaleType is FitToScreen then min_zoom must be <= 1 and max_zoom must be >= 1
     *
     * @param drawable      the new drawable
     * @param initialMatrix the optional initial display matrix
     * @param minZoom       the optional minimum scale, pass {@link #ZOOM_INVALID} to use the default min_zoom
     * @param maxZoom       the optional maximum scale, pass {@link #ZOOM_INVALID} to use the default max_zoom
     */
    public void setImageDrawable(final Drawable drawable, final Matrix initialMatrix, final float minZoom, final float maxZoom) {
        final int viewWidth = getWidth();

        if (viewWidth <= 0) {
            mLayoutRunnable = new Runnable() {
                @Override
                public void run() {
                    setImageDrawable(drawable, initialMatrix, minZoom, maxZoom);
                }
            };
            return;
        }
        setImageDrawableInternal(drawable, initialMatrix, minZoom, maxZoom);
    }

    /**
     * Sets image drawable internal.
     *
     * @param drawable      the drawable
     * @param initialMatrix the initial matrix
     * @param minZoom       the min zoom
     * @param maxZoom       the max zoom
     */
    protected void setImageDrawableInternal(final Drawable drawable, final Matrix initialMatrix, float minZoom, float maxZoom) {
        mBaseMatrix.reset();
        super.setImageDrawable(drawable);

        if (minZoom != ZOOM_INVALID && maxZoom != ZOOM_INVALID) {
            minZoom = Math.min(minZoom, maxZoom);
            maxZoom = Math.max(minZoom, maxZoom);

            mMinZoom = minZoom;
            mMaxZoom = maxZoom;

            mMinZoomDefined = true;
            mMaxZoomDefined = true;

            if (getDisplayType() == DisplayType.FIT_TO_SCREEN || getDisplayType() == DisplayType.FIT_IF_BIGGER) {

                if (mMinZoom >= 1) {
                    mMinZoomDefined = false;
                    mMinZoom = ZOOM_INVALID;
                }

                if (mMaxZoom <= 1) {
                    mMaxZoomDefined = true;
                    mMaxZoom = ZOOM_INVALID;
                }
            }
        } else {
            mMinZoom = ZOOM_INVALID;
            mMaxZoom = ZOOM_INVALID;

            mMinZoomDefined = false;
            mMaxZoomDefined = false;
        }

        if (initialMatrix != null) {
            mNextMatrix = new Matrix(initialMatrix);
        }
        if (DEBUG) {
            Log.v(TAG, "mMinZoom: " + mMinZoom + ", mMaxZoom: " + mMaxZoom);
        }

        mBitmapChanged = true;
        updateDrawable(drawable);
        requestLayout();
    }

    /**
     * Update drawable.
     *
     * @param newDrawable the new drawable
     */
    protected void updateDrawable(Drawable newDrawable) {
        if (null != newDrawable) {
            mBitmapRect.set(0, 0, newDrawable.getIntrinsicWidth(), newDrawable.getIntrinsicHeight());
        } else {
            mBitmapRect.setEmpty();
        }
    }

    /**
     * Fired as soon as a new Bitmap has been set
     *
     * @param drawable the drawable
     */
    protected void onDrawableChanged(final Drawable drawable) {
        if (DEBUG) {
            Log.i(TAG, "onDrawableChanged");
            Log.v(TAG, "scale: " + getScale() + ", minScale: " + getMinScale());
        }
        fireOnDrawableChangeListener(drawable);
    }

    /**
     * Fire on layout change listener.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    protected void fireOnLayoutChangeListener(int left, int top, int right, int bottom) {
        if (null != mOnLayoutChangeListener) {
            mOnLayoutChangeListener.onLayoutChanged(true, left, top, right, bottom);
        }
    }

    /**
     * Fire on drawable change listener.
     *
     * @param drawable the drawable
     */
    protected void fireOnDrawableChangeListener(Drawable drawable) {
        if (null != mDrawableChangeListener) {
            mDrawableChangeListener.onDrawableChanged(drawable);
        }
    }

    /**
     * Called just after {@link #onLayout(boolean, int, int, int, int)}
     * if the view's bounds has changed or a new Drawable has been set
     * or the {@link DisplayType} has been modified
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    protected void onLayoutChanged(int left, int top, int right, int bottom) {
        if (DEBUG) {
            Log.i(TAG, "onLayoutChanged");
        }
        fireOnLayoutChangeListener(left, top, right, bottom);
    }

    /**
     * Compute max zoom float.
     *
     * @return the float
     */
    protected float computeMaxZoom() {
        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return 1f;
        }
        float fw = mBitmapRect.width() / mViewPort.width();
        float fh = mBitmapRect.height() / mViewPort.height();
        float scale = Math.max(fw, fh) * 4;

        if (DEBUG) {
            Log.i(TAG, "computeMaxZoom: " + scale);
        }
        return scale;
    }

    /**
     * Compute min zoom float.
     *
     * @return the float
     */
    protected float computeMinZoom() {
        if (DEBUG) {
            Log.i(TAG, "computeMinZoom");
        }

        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return 1f;
        }

        float scale = getScale(mBaseMatrix);

        scale = Math.min(1f, 1f / scale);
        if (DEBUG) {
            Log.i(TAG, "computeMinZoom: " + scale);
        }

        return scale;
    }

    /**
     * Returns the current maximum allowed image scale
     *
     * @return max scale
     */
    public float getMaxScale() {
        if (mMaxZoom == ZOOM_INVALID) {
            mMaxZoom = computeMaxZoom();
        }
        return mMaxZoom;
    }

    /**
     * Returns the current minimum allowed image scale
     *
     * @return min scale
     */
    public float getMinScale() {
        if (DEBUG) {
            Log.i(TAG, "getMinScale, mMinZoom: " + mMinZoom);
        }

        if (mMinZoom == ZOOM_INVALID) {
            mMinZoom = computeMinZoom();
        }

        if (DEBUG) {
            Log.v(TAG, "mMinZoom: " + mMinZoom);
        }

        return mMinZoom;
    }

    /**
     * Returns the current view matrix
     *
     * @return image view matrix
     */
    public Matrix getImageViewMatrix() {
        return getImageViewMatrix(mSuppMatrix);
    }

    /**
     * Gets image view matrix.
     *
     * @param supportMatrix the support matrix
     * @return the image view matrix
     */
    public Matrix getImageViewMatrix(Matrix supportMatrix) {
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(supportMatrix);
        return mDisplayMatrix;
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        Matrix current = getImageMatrix();
        boolean needUpdate = false;

        if (matrix == null && !current.isIdentity() || matrix != null && !current.equals(matrix)) {
            needUpdate = true;
        }

        super.setImageMatrix(matrix);
        if (needUpdate) {
            onImageMatrixChanged();
        }
    }

    /**
     * Called just after a new Matrix has been assigned.
     *
     * @see {@link #setImageMatrix(Matrix)}
     */
    protected void onImageMatrixChanged() {
    }

    /**
     * Returns the current image display matrix.<br />
     * This matrix can be used in the next call to the {@link #setImageDrawable(Drawable, Matrix, float, float)} to restore the same
     * view state of the previous {@link Bitmap}.<br />
     * Example:
     * <p/>
     * <pre>
     * Matrix currentMatrix = mImageView.getDisplayMatrix();
     * mImageView.setImageBitmap( newBitmap, currentMatrix, ZOOM_INVALID, ZOOM_INVALID );
     * </pre>
     *
     * @return the current support matrix
     */
    public Matrix getDisplayMatrix() {
        return new Matrix(mSuppMatrix);
    }

    /**
     * Gets proper base matrix.
     *
     * @param drawable the drawable
     * @param matrix   the matrix
     * @param rect     the rect
     */
    protected void getProperBaseMatrix(Drawable drawable, Matrix matrix, RectF rect) {
        float w = mBitmapRect.width();
        float h = mBitmapRect.height();
        float widthScale, heightScale;

        matrix.reset();

        widthScale = rect.width() / w;
        heightScale = rect.height() / h;
        float scale = Math.min(widthScale, heightScale);
        matrix.postScale(scale, scale);
        matrix.postTranslate(rect.left, rect.top);

        float tw = (rect.width() - w * scale) / 2.0f;
        float th = (rect.height() - h * scale) / 2.0f;
        matrix.postTranslate(tw, th);
        printMatrix(matrix);
    }

    /**
     * Gets value.
     *
     * @param matrix     the matrix
     * @param whichValue the which value
     * @return the value
     */
    protected float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    /**
     * Print matrix.
     *
     * @param matrix the matrix
     */
    public void printMatrix(Matrix matrix) {
        float scalex = getValue(matrix, Matrix.MSCALE_X);
        float scaley = getValue(matrix, Matrix.MSCALE_Y);
        float tx = getValue(matrix, Matrix.MTRANS_X);
        float ty = getValue(matrix, Matrix.MTRANS_Y);
        Log.d(TAG, "matrix: { x: " + tx + ", y: " + ty + ", scalex: " + scalex + ", scaley: " + scaley + " }");
    }

    /**
     * Gets bitmap rect.
     *
     * @return the bitmap rect
     */
    public RectF getBitmapRect() {
        return getBitmapRect(mSuppMatrix);
    }

    /**
     * Gets bitmap rect.
     *
     * @param supportMatrix the support matrix
     * @return the bitmap rect
     */
    protected RectF getBitmapRect(Matrix supportMatrix) {
        Matrix m = getImageViewMatrix(supportMatrix);
        m.mapRect(mBitmapRectTmp, mBitmapRect);
        return mBitmapRectTmp;
    }

    /**
     * Gets scale.
     *
     * @param matrix the matrix
     * @return the scale
     */
    protected float getScale(Matrix matrix) {
        return getValue(matrix, Matrix.MSCALE_X);
    }

    @SuppressLint("Override")
    public float getRotation() {
        return 0;
    }

    /**
     * Returns the current image scale
     *
     * @return scale scale
     */
    public float getScale() {
        return getScale(mSuppMatrix);
    }

    /**
     * Gets base scale.
     *
     * @return the base scale
     */
    public float getBaseScale() {
        return getScale(mBaseMatrix);
    }

    /**
     * Center.
     *
     * @param horizontal the horizontal
     * @param vertical   the vertical
     */
    protected void center(boolean horizontal, boolean vertical) {
        final Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        RectF rect = getCenter(mSuppMatrix, horizontal, vertical);

        if (rect.left != 0 || rect.top != 0) {
            postTranslate(rect.left, rect.top);
        }
    }

    /**
     * Gets center.
     *
     * @param supportMatrix the support matrix
     * @param horizontal    the horizontal
     * @param vertical      the vertical
     * @return the center
     */
    protected RectF getCenter(Matrix supportMatrix, boolean horizontal, boolean vertical) {
        final Drawable drawable = getDrawable();

        if (drawable == null) {
            return new RectF(0, 0, 0, 0);
        }

        mCenterRect.set(0, 0, 0, 0);
        RectF rect = getBitmapRect(supportMatrix);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0, deltaY = 0;
        if (vertical) {
            if (height < mViewPort.height()) {
                deltaY = (mViewPort.height() - height) / 2 - (rect.top - mViewPort.top);
            } else if (rect.top > mViewPort.top) {
                deltaY = -(rect.top - mViewPort.top);
            } else if (rect.bottom < mViewPort.bottom) {
                deltaY = mViewPort.bottom - rect.bottom;
            }
        }
        if (horizontal) {
            if (width < mViewPort.width()) {
                deltaX = (mViewPort.width() - width) / 2 - (rect.left - mViewPort.left);
            } else if (rect.left > mViewPort.left) {
                deltaX = -(rect.left - mViewPort.left);
            } else if (rect.right < mViewPort.right) {
                deltaX = mViewPort.right - rect.right;
            }
        }
        mCenterRect.set(deltaX, deltaY, 0, 0);
        return mCenterRect;
    }

    /**
     * Post translate.
     *
     * @param deltaX the delta x
     * @param deltaY the delta y
     */
    protected void postTranslate(float deltaX, float deltaY) {
        if (deltaX != 0 || deltaY != 0) {
            mSuppMatrix.postTranslate(deltaX, deltaY);
            setImageMatrix(getImageViewMatrix());
        }
    }

    /**
     * Post scale.
     *
     * @param scale   the scale
     * @param centerX the center x
     * @param centerY the center y
     */
    protected void postScale(float scale, float centerX, float centerY) {
        mSuppMatrix.postScale(scale, scale, centerX, centerY);
        setImageMatrix(getImageViewMatrix());
    }

    /**
     * Gets center.
     *
     * @return the center
     */
    protected PointF getCenter() {
        return mCenter;
    }

    /**
     * Zoom to.
     *
     * @param scale the scale
     */
    protected void zoomTo(float scale) {
        if (DEBUG) {
            Log.i(TAG, "zoomTo: " + scale);
        }

        if (scale > getMaxScale()) {
            scale = getMaxScale();
        }
        if (scale < getMinScale()) {
            scale = getMinScale();
        }

        if (DEBUG) {
            Log.d(TAG, "sanitized scale: " + scale);
        }

        PointF center = getCenter();
        zoomTo(scale, center.x, center.y);
    }

    /**
     * Scale to the target scale
     *
     * @param scale      the target zoom
     * @param durationMs the animation duration
     */
    public void zoomTo(float scale, long durationMs) {
        PointF center = getCenter();
        zoomTo(scale, center.x, center.y, durationMs);
    }

    /**
     * Zoom to.
     *
     * @param scale   the scale
     * @param centerX the center x
     * @param centerY the center y
     */
    protected void zoomTo(float scale, float centerX, float centerY) {
        if (scale > getMaxScale()) {
            scale = getMaxScale();
        }

        float oldScale = getScale();
        float deltaScale = scale / oldScale;
        postScale(deltaScale, centerX, centerY);
        onZoom(getScale());
        center(true, true);
    }

    /**
     * On zoom.
     *
     * @param scale the scale
     */
    @SuppressWarnings("unused")
    protected void onZoom(float scale) {
    }

    /**
     * On zoom animation completed.
     *
     * @param scale the scale
     */
    @SuppressWarnings("unused")
    protected void onZoomAnimationCompleted(float scale) {
    }

    /**
     * Scrolls the view by the x and y amount
     *
     * @param x the x
     * @param y the y
     */
    public void scrollBy(float x, float y) {
        panBy(x, y);
    }

    /**
     * Pan by.
     *
     * @param dx the dx
     * @param dy the dy
     */
    protected void panBy(double dx, double dy) {
        RectF rect = getBitmapRect();
        mScrollPoint.set((float) dx, (float) dy);
        updateRect(rect, mScrollPoint);

        if (mScrollPoint.x != 0 || mScrollPoint.y != 0) {
            postTranslate(mScrollPoint.x, mScrollPoint.y);
            center(true, true);
        }
    }

    /**
     * Update rect.
     *
     * @param bitmapRect the bitmap rect
     * @param scrollRect the scroll rect
     */
    protected void updateRect(RectF bitmapRect, PointF scrollRect) {
        if (bitmapRect == null) {
            return;
        }
    }

    /**
     * Stop all animations.
     */
    protected void stopAllAnimations() {
        if (null != mCurrentAnimation) {
            mCurrentAnimation.cancel();
            mCurrentAnimation = null;
        }
    }

    /**
     * Scroll by.
     *
     * @param distanceX  the distance x
     * @param distanceY  the distance y
     * @param durationMs the duration ms
     */
    protected void scrollBy(float distanceX, float distanceY, final long durationMs) {
        final ValueAnimator anim1 = ValueAnimator.ofFloat(0, distanceX).setDuration(durationMs);
        final ValueAnimator anim2 = ValueAnimator.ofFloat(0, distanceY).setDuration(durationMs);

        stopAllAnimations();

        mCurrentAnimation = new AnimatorSet();
        ((AnimatorSet) mCurrentAnimation).playTogether(
                anim1, anim2
        );

        mCurrentAnimation.setDuration(durationMs);
        mCurrentAnimation.setInterpolator(new DecelerateInterpolator());
        mCurrentAnimation.start();

        anim2.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    float oldValueX = 0;
                    float oldValueY = 0;

                    @Override
                    public void onAnimationUpdate(final ValueAnimator animation) {
                        float valueX = (Float) anim1.getAnimatedValue();
                        float valueY = (Float) anim2.getAnimatedValue();
                        panBy(valueX - oldValueX, valueY - oldValueY);
                        oldValueX = valueX;
                        oldValueY = valueY;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            postInvalidateOnAnimation();
                        } else {
                            postInvalidate();
                        }
                    }
                }
        );

        mCurrentAnimation.addListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(final Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        RectF centerRect = getCenter(mSuppMatrix, true, true);
                        if (centerRect.left != 0 || centerRect.top != 0) {
                            scrollBy(centerRect.left, centerRect.top);
                        }
                    }

                    @Override
                    public void onAnimationCancel(final Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(final Animator animation) {

                    }
                }
        );
    }

    /**
     * Zoom to.
     *
     * @param scale      the scale
     * @param centerX    the center x
     * @param centerY    the center y
     * @param durationMs the duration ms
     */
    protected void zoomTo(float scale, float centerX, float centerY, final long durationMs) {
        if (scale > getMaxScale()) {
            scale = getMaxScale();
        }

        final float oldScale = getScale();

        Matrix m = new Matrix(mSuppMatrix);
        m.postScale(scale, scale, centerX, centerY);
        RectF rect = getCenter(m, true, true);

        final float finalScale = scale;
        final float destX = centerX + rect.left * scale;
        final float destY = centerY + rect.top * scale;

        stopAllAnimations();

        ValueAnimator animation = ValueAnimator.ofFloat(oldScale, finalScale);
        animation.setDuration(durationMs);
        animation.setInterpolator(new DecelerateInterpolator(1.0f));
        animation.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(final ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        zoomTo(value, destX, destY);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            postInvalidateOnAnimation();
                        } else {
                            postInvalidate();
                        }
                    }
                }
        );
        animation.start();
    }

    @Override
    public void dispose() {
        clear();
    }

    @Override
    protected void onDraw(final Canvas canvas) {

        if (getScaleType() == ScaleType.FIT_XY) {
            final Drawable drawable = getDrawable();
            if (null != drawable) {
                drawable.draw(canvas);
            }
        } else {
            super.onDraw(canvas);
        }
    }
}
