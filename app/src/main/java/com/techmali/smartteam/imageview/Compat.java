package com.techmali.smartteam.imageview;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

/**
 * The Compat for photo view attache.
 *
 * @author Vijay Desai
 */
public class Compat {
	
	private static final int SIXTY_FPS_INTERVAL = 1000 / 60;

	/**
	 * Post on animation.
	 *
	 * @param view     the view
	 * @param runnable the runnable
	 */
	public static void postOnAnimation(View view, Runnable runnable) {
		if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			SDK16.postOnAnimation(view, runnable);
		} else {
			view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
		}
	}

}
