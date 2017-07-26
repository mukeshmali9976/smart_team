package com.techmali.smartteam;

import android.app.Application;
import android.graphics.Bitmap;

import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.CountryDetails;
import com.techmali.smartteam.utils.Log;

import me.leolin.shortcutbadger.ShortcutBadger;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mali on 7/1/2017.
 */

public class SmartTeamApplication  extends Application {

    public static final String TAG = SmartTeamApplication.class.getSimpleName();

    CountryDetails mCountrydetails;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    Developer Account KEY & SECRET
    // private static final String TWITTER_KEY_DEV = "YNrHlYTXMxcNSLVLy3stG5QiU";
    // private static final String TWITTER_SECRET_DEV = "9Jl6JgA3SEcVuDY6VZkxD5qySULJLvnCtMhhqaBWQ6LbMbilDU";

    // Client's Account KEY & SECRET
    //  private static final String TWITTER_KEY_LIVE = "jgsTjjGeCh2El9Pw3rHzyiAx1";
    // private static final String TWITTER_SECRET_LIVE = "xeklntDmagUycks1lm1wEbiNCrYO9m24VITohCNJhRaV1MbRdm";
    /**
     * The constant ENABLE_ENCRYPTION.
     */
    public static boolean ENABLE_ENCRYPTION = false;

    @Override
    public void onCreate() {
        super.onCreate();

        /*if (!BuildConfig.DEBUG) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY_LIVE, TWITTER_SECRET_LIVE);
            Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build(), new Crashlytics());
        } else {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY_DEV, TWITTER_SECRET_DEV);
            Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        }*/

        // log will enable or disable in the app
        Log.isLogEnabled = BuildConfig.DEBUG;
        // initialize app constants
        init();

        try {
            ShortcutBadger.removeCount(getApplicationContext()); //for 1.1.4
        } catch (Exception e) {
            ShortcutBadger.applyCount(getApplicationContext(), 0);
        }

        // apply font to app
        defineCalligraphy();

        // initialize image loader with required configuration
        initImageLoader();

        mCountrydetails = new CountryDetails(getApplicationContext());
        Constants.COUNTRY_CODE = mCountrydetails.getCountryCode(mCountrydetails.getUserCountry(getApplicationContext()));
        Constants.COUNTRY_NAME = mCountrydetails.getCountryFullname(mCountrydetails.getUserCountry(getApplicationContext()));
        Log.d(TAG, "Country Name : Code = " + Constants.COUNTRY_NAME + " : " + Constants.COUNTRY_CODE);

    }

    /**
     * Initialize app constants
     */
    private void init() {


    }

    /**
     * Initialize image loader for whole application
     */
    private void initImageLoader() {

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new WeakMemoryCache()).defaultDisplayImageOptions(defaultOptions);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
        L.writeLogs(false);
    }

    private void defineCalligraphy(){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/roboto_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

}