
package com.techmali.smartteam.imageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * The Scroller proxy helper utility for photoview
 *
 * @author Vijay Desai
 */
public abstract class ScrollerProxy {

    /**
     * Gets scroller.
     *
     * @param context the context
     * @return the scroller
     */
    public static ScrollerProxy getScroller(Context context) {
        if (VERSION.SDK_INT < VERSION_CODES.GINGERBREAD) {
            return new PreGingerScroller(context);
        } else {
            return new GingerScroller(context);
        }
    }

    /**
     * Compute scroll offset boolean.
     *
     * @return the boolean
     */
    public abstract boolean computeScrollOffset();

    /**
     * Fling.
     *
     * @param startX    the start x
     * @param startY    the start y
     * @param velocityX the velocity x
     * @param velocityY the velocity y
     * @param minX      the min x
     * @param maxX      the max x
     * @param minY      the min y
     * @param maxY      the max y
     * @param overX     the over x
     * @param overY     the over y
     */
    public abstract void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY,
                               int maxY, int overX, int overY);

    /**
     * Force finished.
     *
     * @param finished the finished
     */
    public abstract void forceFinished(boolean finished);

    /**
     * Gets curr x.
     *
     * @return the curr x
     */
    public abstract int getCurrX();

    /**
     * Gets curr y.
     *
     * @return the curr y
     */
    public abstract int getCurrY();

    @TargetApi(9)
    private static class GingerScroller extends ScrollerProxy {

        private OverScroller mScroller;

        /**
         * Instantiates a new Ginger scroller.
         *
         * @param context the context
         */
        public GingerScroller(Context context) {
            mScroller = new OverScroller(context);
        }

        @Override
        public boolean computeScrollOffset() {
            return mScroller.computeScrollOffset();
        }

        @Override
        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY,
                          int overX, int overY) {
            mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
        }

        @Override
        public void forceFinished(boolean finished) {
            mScroller.forceFinished(finished);
        }

        @Override
        public int getCurrX() {
            return mScroller.getCurrX();
        }

        @Override
        public int getCurrY() {
            return mScroller.getCurrY();
        }
    }

    private static class PreGingerScroller extends ScrollerProxy {

        private Scroller mScroller;

        /**
         * Instantiates a new Pre ginger scroller.
         *
         * @param context the context
         */
        public PreGingerScroller(Context context) {
            mScroller = new Scroller(context);
        }

        @Override
        public boolean computeScrollOffset() {
            return mScroller.computeScrollOffset();
        }

        @Override
        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY,
                          int overX, int overY) {
            mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
        }

        @Override
        public void forceFinished(boolean finished) {
            mScroller.forceFinished(finished);
        }

        @Override
        public int getCurrX() {
            return mScroller.getCurrX();
        }

        @Override
        public int getCurrY() {
            return mScroller.getCurrY();
        }
    }
}
