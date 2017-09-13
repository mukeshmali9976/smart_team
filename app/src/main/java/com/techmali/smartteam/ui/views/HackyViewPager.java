package com.techmali.smartteam.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * The Customized view pager to display full screen images.
 *
 * @author Vijay Desai
 */
public class HackyViewPager extends ViewPager {

	/**
	 * Instantiates a new Hacky view pager.
	 *
	 * @param context the context
	 */
	public HackyViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
	}

}
