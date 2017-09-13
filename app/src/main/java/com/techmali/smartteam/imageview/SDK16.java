package com.techmali.smartteam.imageview;

import android.annotation.TargetApi;
import android.view.View;

/**
 * The Sdk 16.
 *
 * @author Vijay Desai
 */
@TargetApi(16)
public class SDK16 {

    /**
     * Post on animation.
     *
     * @param view the view
     * @param r    the r
     */
    public static void postOnAnimation(View view, Runnable r) {
        view.postOnAnimation(r);
    }

}
