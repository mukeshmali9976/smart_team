package com.techmali.smartteam.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class TypefaceUtils {

    public static final int SEMI_BOLD = 0;
    public static final int BOLD = 1;
    public static final int REGULAR = 2;


    private static TypefaceUtils mInstance = null;
    private Typeface mRegularTypeface;
    private Typeface mSemiBoldTypeface;
    private Typeface mBoldTypeface;

    public static TypefaceUtils getInstance(Context c) {

        if (mInstance == null) {
            mInstance = new TypefaceUtils(c);
        }
        return mInstance;
    }

    private TypefaceUtils(Context c) {
//        mRegularTypeface = Typeface.createFromAsset(c.getAssets(), "fonts/HelveticaNeueLTStd Lt.otf");
//        mSemiBoldTypeface = Typeface.createFromAsset(c.getAssets(), "fonts/HelveticaNeue-Medium.otf");
//        mBoldTypeface = Typeface.createFromAsset(c.getAssets(), "fonts/HelveticaNeue-Medium.otf");
    }

    private TypefaceUtils() {
    }

    /**
     * Method to set typeface on TextView or button
     *
     * @param mTextView
     * @param style
     */
    public void applyTypeface(TextView mTextView, int style) {
        if (mTextView == null) {
            return;
        }
        switch (style) {

            case SEMI_BOLD:
                mTextView.setTypeface(mSemiBoldTypeface);
                break;
            case BOLD:
                mTextView.setTypeface(mBoldTypeface, Typeface.BOLD);
                break;
            case REGULAR:
                mTextView.setTypeface(mRegularTypeface);
                break;
            default:
                mTextView.setTypeface(mSemiBoldTypeface);
                break;
        }
    }
    public Typeface getTypeface(int style) {

        switch (style) {

            case SEMI_BOLD:
                return mSemiBoldTypeface;
            case BOLD:
                return mBoldTypeface;
            case REGULAR:
                return mRegularTypeface;
            default:
                return mRegularTypeface;
        }
    }
}
