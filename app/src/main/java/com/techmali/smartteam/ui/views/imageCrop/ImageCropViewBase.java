package com.techmali.smartteam.ui.views.imageCrop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;


import com.techmali.smartteam.R;

import it.sephiroth.android.library.easing.Cubic;
import it.sephiroth.android.library.easing.Easing;


/**
 * The type Image crop view base.
 */
public abstract class ImageCropViewBase extends ImageView {

    /**
     * The interface On drawable change listener.
     */
    public interface OnDrawableChangeListener {
        /**
         * On drawable changed.
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
         * On layout changed.
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
     * The constant LOG_TAG.
     */
    public static final String LOG_TAG = "ImageViewTouchBase";
    /**
     * The constant LOG_ENABLED.
     */
    protected static final boolean LOG_ENABLED = false;

    /**
     * The constant ZOOM_INVALID.
     */
    public static final float ZOOM_INVALID = -1f;

    /**
     * The constant DEFAULT_ASPECT_RATIO_WIDTH.
     */
    public static final int DEFAULT_ASPECT_RATIO_WIDTH = 1;
    /**
     * The constant DEFAULT_ASPECT_RATIO_HEIGHT.
     */
    public static final int DEFAULT_ASPECT_RATIO_HEIGHT = 1;

    /**
     * The constant GRID_OFF.
     */
    public static final int GRID_OFF = 0;
    /**
     * The constant GRID_ON.
     */
    public static final int GRID_ON = 1;

    /**
     * The M easing.
     */
    protected Easing mEasing = new Cubic();
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
     * The M handler.
     */
    protected Handler mHandler = new Handler();
    /**
     * The M layout runnable.
     */
    protected Runnable mLayoutRunnable = null;
    /**
     * The M user scaled.
     */
    protected boolean mUserScaled = false;

    private float mMaxZoom = ZOOM_INVALID;
    private float mMinZoom = ZOOM_INVALID;

    // true when min and max zoom are explicitly defined
    private boolean mMaxZoomDefined;
    private boolean mMinZoomDefined;

    /**
     * The M display matrix.
     */
    protected final Matrix mDisplayMatrix = new Matrix();
    /**
     * The M matrix values.
     */
    protected final float[] mMatrixValues = new float[9];

    private int mThisWidth = -1;
    private int mThisHeight = -1;
    private PointF mCenter = new PointF();

    private boolean mScaleTypeChanged;
    private boolean mBitmapChanged;

    /**
     * The Default animation duration.
     */
    final protected int DEFAULT_ANIMATION_DURATION = 200;
    private static final String DEFAULT_BACKGROUND_COLOR_ID = "#99000000";

    /**
     * The M bitmap rect.
     */
    protected RectF mBitmapRect = new RectF();
    /**
     * The M center rect.
     */
    protected RectF mCenterRect = new RectF();
    /**
     * The M scroll rect.
     */
    protected RectF mScrollRect = new RectF();
    /**
     * The M crop rect.
     */
    protected RectF mCropRect = new RectF();

    private OnDrawableChangeListener mDrawableChangeListener;
    private OnLayoutChangeListener mOnLayoutChangeListener;

    private Paint mTransparentLayerPaint;

    private int mAspectRatioWidth = ImageCropViewBase.DEFAULT_ASPECT_RATIO_WIDTH;
    private int mAspectRatioHeight = ImageCropViewBase.DEFAULT_ASPECT_RATIO_HEIGHT;

    private float mTargetAspectRatio = mAspectRatioHeight / mAspectRatioWidth;

    private float[] mPts;
    private final int GRID_ROW_COUNT = 3;
    private final int GRID_COLUMN_COUNT = 3;
    private Paint mGridInnerLinePaint;
    private Paint mGridOuterLinePaint;
    private int gridInnerMode;
    private int gridOuterMode;

    private String imageFilePath;

    /**
     * Instantiates a new Image crop view base.
     *
     * @param context the context
     */
    public ImageCropViewBase(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Image crop view base.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ImageCropViewBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Image crop view base.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public ImageCropViewBase(Context context, AttributeSet attrs, int defStyle) {
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

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageCropView);

        mTransparentLayerPaint = new Paint();
        mTransparentLayerPaint.setColor(Color.parseColor(DEFAULT_BACKGROUND_COLOR_ID));

        setScaleType(ScaleType.MATRIX);

        mGridInnerLinePaint = new Paint();
        float gridInnerStrokeWidth = a.getDimension(R.styleable.ImageCropView_gridInnerStroke, 1);
        mGridInnerLinePaint.setStrokeWidth(gridInnerStrokeWidth);
        int gridInnerColor = a.getColor(R.styleable.ImageCropView_gridInnerColor, Color.WHITE);
        mGridInnerLinePaint.setColor(gridInnerColor);

        mGridOuterLinePaint = new Paint();
        float gridOuterStrokeWidth = a.getDimension(R.styleable.ImageCropView_gridOuterStroke, 1);
        mGridOuterLinePaint.setStrokeWidth(gridOuterStrokeWidth);
        int gridOuterColor = a.getColor(R.styleable.ImageCropView_gridOuterColor, Color.WHITE);
        mGridOuterLinePaint.setColor(gridOuterColor);
        mGridOuterLinePaint.setStyle(Paint.Style.STROKE);

        gridInnerMode = a.getInt(R.styleable.ImageCropView_setInnerGridMode, GRID_OFF);
        gridOuterMode = a.getInt(R.styleable.ImageCropView_setOuterGridMode, GRID_OFF);

        int rowLineCount = (GRID_ROW_COUNT - 1) * 4;
        int columnLineCount = (GRID_COLUMN_COUNT - 1) * 4;
        mPts = new float[rowLineCount + columnLineCount];
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.MATRIX) {
            super.setScaleType(scaleType);
        } else {
            Log.w(LOG_TAG, "Unsupported scaletype. Only MATRIX can be used");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTransparentLayer(canvas);
        drawGrid(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (LOG_ENABLED) {
            Log.e(LOG_TAG, "onLayout: " + changed + ", bitmapChanged: " + mBitmapChanged + ", scaleChanged: " + mScaleTypeChanged);
        }

        super.onLayout(changed, left, top, right, bottom);

        int deltaX = 0;
        int deltaY = 0;

        if (changed) {
            int oldw = mThisWidth;
            int oldh = mThisHeight;

            mThisWidth = right - left;
            mThisHeight = bottom - top;

            deltaX = mThisWidth - oldw;
            deltaY = mThisHeight - oldh;

            // update center point
            mCenter.x = mThisWidth / 2f;
            mCenter.y = mThisHeight / 2f;
        }

        int height = (int) (mThisWidth * mTargetAspectRatio);
        if (height > mThisHeight) {
            int width = (int) (mThisHeight / mTargetAspectRatio);
            int halfDiff = (mThisWidth - width) / 2;
            mCropRect.set(left + halfDiff, top, right - halfDiff, bottom);
        } else {
            int halfDiff = (mThisHeight - height) / 2;
            mCropRect.set(left, halfDiff - top, right, height + halfDiff);
        }

        Runnable r = mLayoutRunnable;

        if (r != null) {
            mLayoutRunnable = null;
            r.run();
        }

        final Drawable drawable = getDrawable();

        if (drawable != null) {

            if (changed || mScaleTypeChanged || mBitmapChanged) {

                if (mBitmapChanged) {
                    mBaseMatrix.reset();
                    if (!mMinZoomDefined) mMinZoom = ZOOM_INVALID;
                    if (!mMaxZoomDefined) mMaxZoom = ZOOM_INVALID;
                }

                float scale = 1;

                // retrieve the old values
                float old_matrix_scale = getScale(mBaseMatrix);
                float old_scale = getScale();
                float old_min_scale = Math.min(1f, 1f / old_matrix_scale);

                getProperBaseMatrix(drawable, mBaseMatrix);

                float new_matrix_scale = getScale(mBaseMatrix);

                if (LOG_ENABLED) {
                    Log.d(LOG_TAG, "old matrix scale: " + old_matrix_scale);
                    Log.d(LOG_TAG, "new matrix scale: " + new_matrix_scale);
                    Log.d(LOG_TAG, "old min scale: " + old_min_scale);
                    Log.d(LOG_TAG, "old scale: " + old_scale);
                }

                // 1. bitmap changed or scaletype changed
                if (mBitmapChanged || mScaleTypeChanged) {

                    if (LOG_ENABLED) {
                        Log.d(LOG_TAG, "newMatrix: " + mNextMatrix);
                    }

                    setImageMatrix(getImageViewMatrix());

                } else if (changed) {

                    // 2. layout size changed

                    if (!mMinZoomDefined) mMinZoom = ZOOM_INVALID;
                    if (!mMaxZoomDefined) mMaxZoom = ZOOM_INVALID;

                    setImageMatrix(getImageViewMatrix());
                    postTranslate(-deltaX, -deltaY);


                    if (!mUserScaled) {
                        zoomTo(scale);
                    } else {
                        if (Math.abs(old_scale - old_min_scale) > 0.001) {
                            scale = (old_matrix_scale / new_matrix_scale) * old_scale;
                        }
                        if (LOG_ENABLED) {
                            Log.v(LOG_TAG, "userScaled. scale=" + scale);
                        }
                        zoomTo(scale);
                    }

                    if (LOG_ENABLED) {
                        Log.d(LOG_TAG, "old scale: " + old_scale);
                        Log.d(LOG_TAG, "new scale: " + scale);
                    }


                }

                mUserScaled = false;

                if (scale > getMaxScale() || scale < getMinScale()) {
                    // if current scale if outside the min/max bounds
                    // then restore the correct scale
                    zoomTo(scale);
                }

                center(true, true);

                if (mBitmapChanged) onDrawableChanged(drawable);
                if (changed || mBitmapChanged || mScaleTypeChanged)
                    onLayoutChanged(left, top, right, bottom);

                if (mScaleTypeChanged) mScaleTypeChanged = false;
                if (mBitmapChanged) mBitmapChanged = false;

                if (LOG_ENABLED) {
                    Log.d(LOG_TAG, "new scale: " + getScale());
                }
            }
        } else {
            // drawable is null
            if (mBitmapChanged) onDrawableChanged(drawable);
            if (changed || mBitmapChanged || mScaleTypeChanged)
                onLayoutChanged(left, top, right, bottom);

            if (mBitmapChanged) mBitmapChanged = false;
            if (mScaleTypeChanged) mScaleTypeChanged = false;

        }
    }

    /**
     * Reset display.
     */
    public void resetDisplay() {
        mBitmapChanged = true;
        resetMatrix();
        requestLayout();
    }

    /**
     * Reset matrix.
     */
    public void resetMatrix() {
        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "resetMatrix");
        }
        mSuppMatrix = new Matrix();

        setImageMatrix(getImageViewMatrix());

        zoomTo(1f);

        postInvalidate();
    }

    private void drawTransparentLayer(Canvas canvas) {
        /*-
          -------------------------------------
          |                top                |
          -------------------------------------
          |      |                    |       |
          |      |                    |       |
          | left |      mCropRect     | right |
          |      |                    |       |
          |      |                    |       |
          -------------------------------------
          |              bottom               |
          -------------------------------------
         */

        Rect r = new Rect();
        getLocalVisibleRect(r);

        canvas.drawRect(r.left, r.top, r.right, mCropRect.top, mTransparentLayerPaint);                       // top
        canvas.drawRect(r.left, mCropRect.bottom, mCropRect.right, r.bottom, mTransparentLayerPaint);         // bottom
        canvas.drawRect(r.left, mCropRect.top, mCropRect.left, mCropRect.bottom, mTransparentLayerPaint);     // left
        canvas.drawRect(mCropRect.right, mCropRect.top, r.right, mCropRect.bottom, mTransparentLayerPaint);   // right
    }

    private void drawGrid(Canvas canvas) {
        int index = 0;
        for (int i = 0; i < GRID_ROW_COUNT - 1; i++) {
            mPts[index++] = mCropRect.left;                                                                                //start Xi
            mPts[index++] = (mCropRect.height() * (((float) i + 1.0f) / (float) GRID_ROW_COUNT)) + mCropRect.top;            //start Yi
            mPts[index++] = mCropRect.right;                                                                            //stop  Xi
            mPts[index++] = (mCropRect.height() * (((float) i + 1.0f) / (float) GRID_ROW_COUNT)) + mCropRect.top;            //stop  Yi
        }

        for (int i = 0; i < GRID_COLUMN_COUNT - 1; i++) {
            mPts[index++] = (mCropRect.width() * (((float) i + 1.0f) / (float) GRID_COLUMN_COUNT)) + mCropRect.left;        //start Xi
            mPts[index++] = mCropRect.top;                                                                            //start Yi
            mPts[index++] = (mCropRect.width() * (((float) i + 1.0f) / (float) GRID_COLUMN_COUNT)) + mCropRect.left;       //stop  Xi
            mPts[index++] = mCropRect.bottom;                                                                        //stop  Yi
        }

        if (gridInnerMode == GRID_ON) {
            canvas.drawLines(mPts, mGridInnerLinePaint);
        }

        if (gridOuterMode == GRID_ON) {
            float halfLineWidth = mGridOuterLinePaint.getStrokeWidth() * 0.5f;
            canvas.drawRect(mCropRect.left + halfLineWidth, mCropRect.top + halfLineWidth, mCropRect.right - halfLineWidth, mCropRect.bottom - halfLineWidth, mGridOuterLinePaint);
        }
    }

    @Override
    public void setImageResource(int resId) {
        setImageDrawable(getContext().getResources().getDrawable(resId));
    }

    /**
     * Sets aspect ratio.
     *
     * @param aspectRatioWidth  the aspect ratio width
     * @param aspectRatioHeight the aspect ratio height
     */
    public void setAspectRatio(int aspectRatioWidth, int aspectRatioHeight) {
        if (aspectRatioWidth <= 0 || aspectRatioHeight <= 0) {
            throw new IllegalArgumentException("Cannot set aspect ratio mDisplayAddressvalue to a number less than or equal to 0.");
        } else {
            mAspectRatioWidth = aspectRatioWidth;
            mAspectRatioHeight = aspectRatioHeight;
            mTargetAspectRatio = (float) mAspectRatioHeight / (float) mAspectRatioWidth;
        }

        resetDisplay();
    }

    /**
     * Sets image file path.
     *
     * @param imageFilePath the image file path
     */
    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
        int reqSize = 1000;
        Bitmap bitmap = BitmapLoadUtils.decode(imageFilePath, reqSize, reqSize, true);
        setImageBitmap(bitmap);
    }

    @Override
    public void setImageBitmap(final Bitmap bitmap) {
        float minScale = 1f;
        float maxScale = 8f;
        Matrix m = new Matrix();
        m.postScale(minScale, minScale);
        setImageBitmap(bitmap, m, minScale, maxScale);
    }

    /**
     * Sets image bitmap.
     *
     * @param bitmap   the bitmap
     * @param matrix   the matrix
     * @param min_zoom the min zoom
     * @param max_zoom the max zoom
     */
    public void setImageBitmap(final Bitmap bitmap, final Matrix matrix, final float min_zoom, final float max_zoom) {
        final int viewWidth = getWidth();
        if (viewWidth <= 0) {
            mLayoutRunnable = new Runnable() {

                @Override
                public void run() {
                    setImageBitmap(bitmap, matrix, min_zoom, max_zoom);
                }
            };
            return;
        }

        if (bitmap != null) {
            setImageDrawable(new FastBitmapDrawable(bitmap), matrix, min_zoom, max_zoom);
        } else {
            setImageDrawable(null, matrix, min_zoom, max_zoom);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        float minScale = 1f;
        float maxScale = 8f;
        Matrix m = new Matrix();
        m.postScale(minScale, minScale);
        setImageDrawable(drawable, m, minScale, maxScale);
    }

    /**
     * Sets image drawable.
     *
     * @param drawable       the drawable
     * @param initial_matrix the initial matrix
     * @param min_zoom       the min zoom
     * @param max_zoom       the max zoom
     */
    public void setImageDrawable(final Drawable drawable, final Matrix initial_matrix, final float min_zoom, final float max_zoom) {
        final int viewWidth = getWidth();

        if (viewWidth <= 0) {
            mLayoutRunnable = new Runnable() {

                @Override
                public void run() {
                    setImageDrawable(drawable, initial_matrix, min_zoom, max_zoom);
                }
            };
            return;
        }
        _setImageDrawable(drawable, initial_matrix, min_zoom, max_zoom);
    }

    /**
     * Set image drawable.
     *
     * @param drawable       the drawable
     * @param initial_matrix the initial matrix
     * @param min_zoom       the min zoom
     * @param max_zoom       the max zoom
     */
    protected void _setImageDrawable(final Drawable drawable, final Matrix initial_matrix, float min_zoom, float max_zoom) {

        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "_setImageDrawable");
        }

        mBaseMatrix.reset();

        if (drawable != null) {
            if (LOG_ENABLED) {
                Log.d(LOG_TAG, "size: " + drawable.getIntrinsicWidth() + "x" + drawable.getIntrinsicHeight());
            }
            super.setImageDrawable(drawable);
        } else {
            super.setImageDrawable(null);
        }

        if (min_zoom != ZOOM_INVALID && max_zoom != ZOOM_INVALID) {
            min_zoom = Math.min(min_zoom, max_zoom);
            max_zoom = Math.max(min_zoom, max_zoom);

            mMinZoom = min_zoom;
            mMaxZoom = max_zoom;

            mMinZoomDefined = true;
            mMaxZoomDefined = true;
        } else {
            mMinZoom = ZOOM_INVALID;
            mMaxZoom = ZOOM_INVALID;

            mMinZoomDefined = false;
            mMaxZoomDefined = false;
        }

        if (initial_matrix != null) {
            mNextMatrix = new Matrix(initial_matrix);
        }

        if (LOG_ENABLED) {
            Log.v(LOG_TAG, "mMinZoom: " + mMinZoom + ", mMaxZoom: " + mMaxZoom);
        }

        mBitmapChanged = true;
        requestLayout();
    }

    /**
     * On drawable changed.
     *
     * @param drawable the drawable
     */
    protected void onDrawableChanged(final Drawable drawable) {
        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "onDrawableChanged");
            Log.v(LOG_TAG, "scale: " + getScale() + ", minScale: " + getMinScale());
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
     * On layout changed.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    protected void onLayoutChanged(int left, int top, int right, int bottom) {
        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "onLayoutChanged");
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
            return 1F;
        }

        float fw = (float) drawable.getIntrinsicWidth() / (float) mThisWidth;
        float fh = (float) drawable.getIntrinsicHeight() / (float) mThisHeight;
        float scale = Math.max(fw, fh) * 8;

        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "computeMaxZoom: " + scale);
        }
        return scale;
    }

    /**
     * Compute min zoom float.
     *
     * @return the float
     */
    protected float computeMinZoom() {
        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "computeMinZoom");
        }

        final Drawable drawable = getDrawable();

        if (drawable == null) {
            return 1F;
        }

        float scale = getScale(mBaseMatrix);
        scale = Math.min(1f, 1f / scale);

        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "computeMinZoom: " + scale);
        }

        return scale;
    }

    /**
     * Gets max scale.
     *
     * @return the max scale
     */
    public float getMaxScale() {
        if (mMaxZoom == ZOOM_INVALID) {
            mMaxZoom = computeMaxZoom();
        }
        return mMaxZoom;
    }

    /**
     * Gets min scale.
     *
     * @return the min scale
     */
    public float getMinScale() {
        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "getMinScale, mMinZoom: " + mMinZoom);
        }

        if (mMinZoom == ZOOM_INVALID) {
            mMinZoom = computeMinZoom();
        }

        if (LOG_ENABLED) {
            Log.v(LOG_TAG, "mMinZoom: " + mMinZoom);
        }

        return mMinZoom;
    }

    /**
     * Gets image view matrix.
     *
     * @return the image view matrix
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

        if (needUpdate) onImageMatrixChanged();
    }

    /**
     * On image matrix changed.
     */
    protected void onImageMatrixChanged() {
    }

    private float baseScale = 1f;

    /**
     * Gets proper base matrix.
     *
     * @param drawable the drawable
     * @param matrix   the matrix
     */
    protected void getProperBaseMatrix(Drawable drawable, Matrix matrix) {
        float viewWidth = mCropRect.width();
        float viewHeight = mCropRect.height();

        if (LOG_ENABLED) {
            Log.d(LOG_TAG, "getProperBaseMatrix. view: " + viewWidth + "x" + viewHeight);
        }

        float w = drawable.getIntrinsicWidth();
        float h = drawable.getIntrinsicHeight();
        float widthScale, heightScale;
        matrix.reset();

        if (w > viewWidth || h > viewHeight) {
            widthScale = viewWidth / w;
            heightScale = viewHeight / h;
            baseScale = Math.max(widthScale, heightScale);
            matrix.postScale(baseScale, baseScale);

            float tw = (viewWidth - w * baseScale) / 2.0f;
            float th = (viewHeight - h * baseScale) / 2.0f;
            matrix.postTranslate(tw, th);

        } else {
            widthScale = viewWidth / w;
            heightScale = viewHeight / h;
            baseScale = Math.max(widthScale, heightScale);
            matrix.postScale(baseScale, baseScale);

            float tw = (viewWidth - w * baseScale) / 2.0f;
            float th = (viewHeight - h * baseScale) / 2.0f;
            matrix.postTranslate(tw, th);
        }

        if (LOG_ENABLED) {
            printMatrix(matrix);
        }
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
        Log.d(LOG_TAG, "matrix: { x: " + tx + ", y: " + ty + ", scalex: " + scalex + ", scaley: " + scaley + " }");
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
        final Drawable drawable = getDrawable();

        if (drawable == null) return null;
        Matrix m = getImageViewMatrix(supportMatrix);
        mBitmapRect.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        m.mapRect(mBitmapRect);
        return mBitmapRect;
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
     * Gets scale.
     *
     * @return the scale
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
        if (drawable == null) return;

        RectF rect = getCenter(mSuppMatrix, horizontal, vertical);

        if (rect.left != 0 || rect.top != 0) {

            if (LOG_ENABLED) {
                Log.i(LOG_TAG, "center");
            }
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

        if (drawable == null) return new RectF(0, 0, 0, 0);

        mCenterRect.set(0, 0, 0, 0);
        RectF rect = getBitmapRect(supportMatrix);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0, deltaY = 0;
        if (vertical) {
            int viewHeight = mThisHeight;
            if (height < viewHeight) {
                deltaY = (viewHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < viewHeight) {
                deltaY = mThisHeight - rect.bottom;
            }
        }
        if (horizontal) {
            int viewWidth = mThisWidth;
            if (width < viewWidth) {
                deltaX = (viewWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right;
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
            if (LOG_ENABLED) {
                Log.i(LOG_TAG, "postTranslate: " + deltaX + "x" + deltaY);
            }
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
        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "postScale: " + scale + ", center: " + centerX + "x" + centerY);
        }
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
        if (LOG_ENABLED) {
            Log.i(LOG_TAG, "zoomTo: " + scale);
        }

        if (scale > getMaxScale()) scale = getMaxScale();
        if (scale < getMinScale()) scale = getMinScale();

        if (LOG_ENABLED) {
            Log.d(LOG_TAG, "sanitized scale: " + scale);
        }


        PointF center = getCenter();
        zoomTo(scale, center.x, center.y);
    }

    /**
     * Zoom to.
     *
     * @param scale      the scale
     * @param durationMs the duration ms
     */
    public void zoomTo(float scale, float durationMs) {
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
        if (scale > getMaxScale()) scale = getMaxScale();

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
    protected void onZoom(float scale) {
    }

    /**
     * On zoom animation completed.
     *
     * @param scale the scale
     */
    protected void onZoomAnimationCompleted(float scale) {
    }

    /**
     * Scroll by.
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
        mScrollRect.set((float) dx, (float) dy, 0, 0);
        postTranslate(mScrollRect.left, mScrollRect.top);
        adjustCropAreaImage();
    }

    private void adjustCropAreaImage() {
        final Drawable drawable = getDrawable();
        if (drawable == null) return;

        RectF rect = getAdjust(mSuppMatrix);

        if (rect.left != 0 || rect.top != 0) {

            if (LOG_ENABLED) {
                Log.i(LOG_TAG, "center");
            }
            postTranslate(rect.left, rect.top);
        }
    }

    private RectF getAdjust(Matrix supportMatrix) {
        final Drawable drawable = getDrawable();

        if (drawable == null) return new RectF(0, 0, 0, 0);

        mCenterRect.set(0, 0, 0, 0);
        RectF rect = getBitmapRect(supportMatrix);
        float deltaX = 0, deltaY = 0;

        //Y
        if (rect.top > mCropRect.top) {
            deltaY = mCropRect.top - rect.top;
        } else if (rect.bottom < mCropRect.bottom) {
            deltaY = mCropRect.bottom - rect.bottom;

        }

        //X
        if (rect.left > mCropRect.left) {
            deltaX = mCropRect.left - rect.left;
        } else if (rect.right < mCropRect.right) {
            deltaX = mCropRect.right - rect.right;
        }

        mCenterRect.set(deltaX, deltaY, 0, 0);
        return mCenterRect;

    }

    /**
     * Scroll by.
     *
     * @param distanceX  the distance x
     * @param distanceY  the distance y
     * @param durationMs the duration ms
     */
    protected void scrollBy(float distanceX, float distanceY, final double durationMs) {
        final double dx = distanceX;
        final double dy = distanceY;
        final long startTime = System.currentTimeMillis();
        mHandler.post(
                new Runnable() {

                    double old_x = 0;
                    double old_y = 0;

                    @Override
                    public void run() {
                        long now = System.currentTimeMillis();
                        double currentMs = Math.min(durationMs, now - startTime);
                        double x = mEasing.easeOut(currentMs, 0, dx, durationMs);
                        double y = mEasing.easeOut(currentMs, 0, dy, durationMs);
                        panBy((x - old_x), (y - old_y));
                        old_x = x;
                        old_y = y;
                        if (currentMs < durationMs) {
                            mHandler.post(this);
                        }
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
    protected void zoomTo(float scale, float centerX, float centerY, final float durationMs) {
        if (scale > getMaxScale()) scale = getMaxScale();

        final long startTime = System.currentTimeMillis();
        final float oldScale = getScale();

        final float deltaScale = scale - oldScale;

        Matrix m = new Matrix(mSuppMatrix);
        m.postScale(scale, scale, centerX, centerY);
        RectF rect = getCenter(m, true, true);

        final float destX = centerX + rect.left * scale;
        final float destY = centerY + rect.top * scale;

        mHandler.post(
                new Runnable() {

                    @Override
                    public void run() {
                        long now = System.currentTimeMillis();
                        float currentMs = Math.min(durationMs, now - startTime);
                        float newScale = (float) mEasing.easeInOut(currentMs, 0, deltaScale, durationMs);
                        zoomTo(oldScale + newScale, destX, destY);
                        if (currentMs < durationMs) {
                            mHandler.post(this);
                        } else {
                            onZoomAnimationCompleted(getScale());
                            center(true, true);
                        }
                    }
                }
        );
    }

    /**
     * Gets cropped image.
     *
     * @return the cropped image
     */
    public Bitmap getCroppedImage() {

        Bitmap viewBitmap = getViewBitmap();
        Bitmap sourceBitmap = viewBitmap;

        float scale = baseScale * getScale();

        if (imageFilePath != null) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int imageWidth = (int) ((float) metrics.widthPixels / 2);
            int imageHeight = (int) ((float) metrics.heightPixels / 2);

//			sourceBitmap = BitmapLoadUtils.decode(imageFilePath, imageWidth, imageHeight);
            sourceBitmap = decodeSampledBitmapFromPath(imageFilePath, imageWidth, imageHeight);

            scale = scale * ((float) viewBitmap.getWidth() / (float) sourceBitmap.getWidth());
        }

        RectF viewImageRect = getBitmapRect();

        float x = Math.abs(viewImageRect.left - mCropRect.left) / scale;
        float y = Math.abs(viewImageRect.top - mCropRect.top) / scale;
        float actualCropWidth = mCropRect.width() / scale;
        float actualCropHeight = mCropRect.height() / scale;

        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        if (y + actualCropHeight > sourceBitmap.getHeight()) {
            actualCropHeight = sourceBitmap.getHeight() - y;
        }

        if (x + actualCropWidth > sourceBitmap.getWidth()) {
            actualCropWidth = sourceBitmap.getWidth() - x;
        }

        return Bitmap.createBitmap(sourceBitmap, (int) x, (int) y, (int) actualCropWidth, (int) actualCropHeight);
    }

    /**
     * Gets view bitmap.
     *
     * @return the view bitmap
     */
    public Bitmap getViewBitmap() {
        return ((FastBitmapDrawable) getDrawable()).getBitmap();
    }

    /**
     * Sets grid inner mode.
     *
     * @param gridInnerMode the grid inner mode
     */
    public void setGridInnerMode(int gridInnerMode) {
        this.gridInnerMode = gridInnerMode;
        invalidate();
    }

    /**
     * Sets grid outer mode.
     *
     * @param gridOuterMode the grid outer mode
     */
    public void setGridOuterMode(int gridOuterMode) {
        this.gridOuterMode = gridOuterMode;
        invalidate();
    }

    /**
     * Decode sampled bitmap from path bitmap.
     *
     * @param path      the path
     * @param reqWidth  the req width
     * @param reqHeight the req height
     * @return the bitmap
     */
    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * Calculate in sample size int.
     *
     * @param options   the options
     * @param reqWidth  the req width
     * @param reqHeight the req height
     * @return the int
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize mDisplayAddressvalue that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
