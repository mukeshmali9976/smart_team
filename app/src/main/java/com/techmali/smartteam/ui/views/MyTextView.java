package com.techmali.smartteam.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.utils.TypefaceUtils;


public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {

        if (!isInEditMode()) {

            Typeface mTypeface = TypefaceUtils.getInstance(getContext()).getTypeface(TypefaceUtils.REGULAR);

            if (attrs != null) {

                TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.Custom);
                String fontStyle = attr.getString(R.styleable.Custom_font_style);

                if (fontStyle != null) {

                    if (fontStyle.equalsIgnoreCase("bold")) {
                        mTypeface = TypefaceUtils.getInstance(getContext()).getTypeface(TypefaceUtils.BOLD);
                    } else if (fontStyle.equalsIgnoreCase("semibold")) {
                        mTypeface = TypefaceUtils.getInstance(getContext()).getTypeface(TypefaceUtils.SEMI_BOLD);
                    } else {
                        mTypeface = TypefaceUtils.getInstance(getContext()).getTypeface(TypefaceUtils.REGULAR);
                    }
                }
                attr.recycle();
            }
            setTypeface(mTypeface);
        }
    }
}
