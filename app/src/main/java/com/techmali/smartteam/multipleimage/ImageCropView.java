package com.techmali.smartteam.multipleimage;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ViewConfiguration;

/**
 * The Customised Image crop view.
 *
 * @author Vijay Desai
 */
public class ImageCropView extends ImageCropViewBase {

    /**
     * The M scale detector.
     */
    protected ScaleGestureDetector mScaleDetector;
    /**
     * The M gesture detector.
     */
    protected GestureDetector mGestureDetector;
    /**
     * The M touch slop.
     */
    protected int mTouchSlop;
    /**
     * The M scale factor.
     */
    protected float mScaleFactor;
    /**
     * The M double tap direction.
     */
    protected int mDoubleTapDirection;
    /**
     * The M gesture listener.
     */
    protected OnGestureListener mGestureListener;
    /**
     * The M scale listener.
     */
    protected OnScaleGestureListener mScaleListener;
    /**
     * The M double tap enabled.
     */
    protected boolean mDoubleTapEnabled = false;
    /**
     * The M scale enabled.
     */
    protected boolean mScaleEnabled = true;
    /**
     * The M scroll enabled.
     */
    protected boolean mScrollEnabled = true;
    private OnImageViewTouchDoubleTapListener mDoubleTapListener;
    private OnImageViewTouchSingleTapListener mSingleTapListener;

    private boolean isChangingScale = false;

    /**
     * Instantiates a new Image crop view.
     *
     * @param context the context
     */
    public ImageCropView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Image crop view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ImageCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Image crop view.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public ImageCropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyle) {
        super.init(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mGestureListener = getGestureListener();
        mScaleListener = getScaleListener();

        mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
        mGestureDetector = new GestureDetector(getContext(), mGestureListener, null, true);

        mDoubleTapDirection = 1;
    }

    /**
     * Sets double tap listener.
     *
     * @param listener the listener
     */
    public void setDoubleTapListener(OnImageViewTouchDoubleTapListener listener) {
        mDoubleTapListener = listener;
    }

    /**
     * Sets single tap listener.
     *
     * @param listener the listener
     */
    public void setSingleTapListener(OnImageViewTouchSingleTapListener listener) {
        mSingleTapListener = listener;
    }

    /**
     * Sets double tap enabled.
     *
     * @param value the value
     */
    public void setDoubleTapEnabled(boolean value) {
        mDoubleTapEnabled = value;
    }

    /**
     * Sets scale enabled.
     *
     * @param value the value
     */
    public void setScaleEnabled(boolean value) {
        mScaleEnabled = value;
    }

    /**
     * Sets scroll enabled.
     *
     * @param value the value
     */
    public void setScrollEnabled(boolean value) {
        mScrollEnabled = value;
    }

    /**
     * Gets double tap enabled.
     *
     * @return the double tap enabled
     */
    public boolean getDoubleTapEnabled() {
        return mDoubleTapEnabled;
    }

    /**
     * Gets gesture listener.
     *
     * @return the gesture listener
     */
    protected OnGestureListener getGestureListener() {
        return new GestureListener();
    }

    /**
     * Gets scale listener.
     *
     * @return the scale listener
     */
    protected OnScaleGestureListener getScaleListener() {
        return new ScaleListener();
    }

    @Override
    protected void _setImageDrawable(final Drawable drawable, final Matrix initial_matrix, float min_zoom, float max_zoom) {
        super._setImageDrawable(drawable, initial_matrix, min_zoom, max_zoom);
        mScaleFactor = getMaxScale() / 3;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getBitmapChanged()) return false;
        mScaleDetector.onTouchEvent(event);

        if (!mScaleDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }

        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                return onUp(event);
        }
        return true;
    }


    @Override
    protected void onZoomAnimationCompleted(float scale) {

        if (LOG_ENABLED) {
            Log.d(LOG_TAG, "onZoomAnimationCompleted. scale: " + scale + ", minZoom: " + getMinScale());
        }

        if (scale < getMinScale()) {
            zoomTo(getMinScale(), 50);
        }
    }

    /**
     * On double tap post float.
     *
     * @param scale   the scale
     * @param maxZoom the max zoom
     * @return the float
     */
    protected float onDoubleTapPost(float scale, float maxZoom) {
        if (mDoubleTapDirection == 1) {
            if ((scale + (mScaleFactor * 2)) <= maxZoom) {
                return scale + mScaleFactor;
            } else {
                mDoubleTapDirection = -1;
                return maxZoom;
            }
        } else {
            mDoubleTapDirection = 1;
            return 1f;
        }
    }

    /**
     * On single tap confirmed boolean.
     *
     * @param e the e
     * @return the boolean
     */
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    /**
     * On scroll boolean.
     *
     * @param e1        the e 1
     * @param e2        the e 2
     * @param distanceX the distance x
     * @param distanceY the distance y
     * @return the boolean
     */
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mUserScaled = true;
        scrollBy(-distanceX, -distanceY);
        invalidate();
        return true;
    }

    /**
     * On fling boolean.
     *
     * @param e1        the e 1
     * @param e2        the e 2
     * @param velocityX the velocity x
     * @param velocityY the velocity y
     * @return the boolean
     */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(velocityX) > 800 || Math.abs(velocityY) > 800) {
            mUserScaled = true;
            scrollBy(diffX / 2, diffY / 2, 300);
            invalidate();
            return true;
        }
        return false;
    }

    /**
     * On down boolean.
     *
     * @param e the e
     * @return the boolean
     */
    public boolean onDown(MotionEvent e) {
        return !getBitmapChanged();
    }

    /**
     * On up boolean.
     *
     * @param e the e
     * @return the boolean
     */
    public boolean onUp(MotionEvent e) {
        if (getBitmapChanged()) return false;
        if (getScale() < getMinScale()) {
            zoomTo(getMinScale(), 50);
        }
        return true;
    }

    /**
     * On single tap up boolean.
     *
     * @param e the e
     * @return the boolean
     */
    public boolean onSingleTapUp(MotionEvent e) {
        return !getBitmapChanged();
    }

    /**
     * The type Gesture listener.
     */
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (null != mSingleTapListener) {
                mSingleTapListener.onSingleTapConfirmed();
            }

            return ImageCropView.this.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (LOG_ENABLED) {
                Log.i(LOG_TAG, "onDoubleTap. double tap enabled? " + mDoubleTapEnabled);
            }
            if (mDoubleTapEnabled) {
                mUserScaled = true;
                float scale = getScale();
                float targetScale = scale;
                Log.d(LOG_TAG, "targetScale : " + targetScale);
                targetScale = onDoubleTapPost(scale, getMaxScale());
                Log.d(LOG_TAG, "targetScale : " + targetScale);
                targetScale = Math.min(getMaxScale(), Math.max(targetScale, getMinScale()));
                Log.d(LOG_TAG, "targetScale : " + targetScale);
                zoomTo(targetScale, e.getX(), e.getY(), DEFAULT_ANIMATION_DURATION);
                Log.d(LOG_TAG, "targetScale : " + targetScale);
                invalidate();
            }

            if (null != mDoubleTapListener) {
                mDoubleTapListener.onDoubleTap();
            }

            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isLongClickable()) {
                if (!mScaleDetector.isInProgress()) {
                    setPressed(true);
                    performLongClick();
                }
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!mScrollEnabled) return false;
            if (e1 == null || e2 == null) return false;
            if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
            if (mScaleDetector.isInProgress()) return false;
            return ImageCropView.this.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!mScrollEnabled) return false;

            if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
            if (mScaleDetector.isInProgress()) return false;
//			if (getScale() == 1f) return false;

            return ImageCropView.this.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return ImageCropView.this.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return ImageCropView.this.onDown(e);
        }
    }


    /**
     * The type Scale listener.
     */
    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        /**
         * The M scaled.
         */
        protected boolean mScaled = false;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isChangingScale = true;
            return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float span = detector.getCurrentSpan() - detector.getPreviousSpan();
            float targetScale = getScale() * detector.getScaleFactor();

            if (mScaleEnabled) {
                if (mScaled && span != 0) {
                    mUserScaled = true;
                    targetScale = Math.min(getMaxScale(), Math.max(targetScale, getMinScale() - 0.1f));
                    zoomTo(targetScale, detector.getFocusX(), detector.getFocusY());
                    mDoubleTapDirection = 1;
                    invalidate();
                    return true;
                }

                // This is to prevent a glitch the first time
                // image is scaled.
                if (!mScaled) mScaled = true;
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isChangingScale = false;
            super.onScaleEnd(detector);
        }
    }

    /**
     * The interface On image view touch double tap listener.
     */
    public interface OnImageViewTouchDoubleTapListener {

        /**
         * On double tap.
         */
        void onDoubleTap();
    }

    /**
     * The interface On image view touch single tap listener.
     */
    public interface OnImageViewTouchSingleTapListener {

        /**
         * On single tap confirmed.
         */
        void onSingleTapConfirmed();
    }

    /**
     * Is changing scale boolean.
     *
     * @return the boolean
     */
    public boolean isChangingScale() {
        return isChangingScale;
    }
}
