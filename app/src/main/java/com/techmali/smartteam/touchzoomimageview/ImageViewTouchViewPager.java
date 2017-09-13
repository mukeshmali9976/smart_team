package com.techmali.smartteam.touchzoomimageview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.techmali.smartteam.utils.Log;

/**
 * Created by Vijay Desai on 29-04-2016.
 */
public class ImageViewTouchViewPager extends ViewPager {

    private static final String TAG = "ImageViewTouchViewPager";
    public static final String VIEW_PAGER_OBJECT_TAG = "image#";

    private int previousPosition;

    private OnPageSelectedListener onPageSelectedListener;

    public ImageViewTouchViewPager(Context context) {
        super(context);
        init();
    }

    public ImageViewTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnPageSelectedListener(OnPageSelectedListener listener) {
        onPageSelectedListener = listener;
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ImageViewTouch) {
            if (((ImageViewTouch)v).getScale() == ((ImageViewTouch)v).getMinScale()) {
                return super.canScroll(v, checkV, dx, x, y);
            }
            return ((ImageViewTouch) v).canScroll(dx);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }

    public interface OnPageSelectedListener {

        public void onPageSelected(int position);

    }

    private void init() {
        previousPosition = getCurrentItem();

        setOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (onPageSelectedListener != null) {
                    onPageSelectedListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_SETTLING && previousPosition != getCurrentItem()) {
                    try {
                        ImageViewTouch imageViewTouch = (ImageViewTouch)
                                findViewWithTag(VIEW_PAGER_OBJECT_TAG + getCurrentItem());
                        if (imageViewTouch != null) {
                            imageViewTouch.zoomTo(1f, 300);
                        }

                        previousPosition = getCurrentItem();
                    } catch (ClassCastException ex) {
                        Log.e(TAG, "This view pager should have only ImageViewTouch as a children.", ex);
                    }
                }
            }
        });
    }
}