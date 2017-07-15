package com.techmali.smartteam.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CryptoManager {

    private static CryptoManager instance = null;
    private SharedPreferences prefs = null;

    private CryptoManager(Context context) {

        prefs = new ObscuredSharedPreferences(context, context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE));
    }

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static CryptoManager getInstance(Context context) {

        if (instance == null) instance = new CryptoManager(context);
        return instance;
    }

    /**
     * Gets prefs.
     *
     * @return the prefs
     */
    public SharedPreferences getPrefs() {

        return prefs;
    }

}
