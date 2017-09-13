package com.techmali.smartteam.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;

/**
 * The Video display activity.
 * Videos are displayed in this screen, There is a webview in which video will be played.
 *
 * @author Vijay Desai
 */
public class VideoDisplayActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private static final String TAG = VideoDisplayActivity.class.getSimpleName();

    /**
     * The constant EXTRA_VIDEODISPLAY_URL.
     */
    public static String EXTRA_VIDEODISPLAY_URL = "extra_videodisplay_url";
    /**
     * The constant EXTRA_ISDOCUMENT.
     */
    public static String EXTRA_ISDOCUMENT = "extra_isdocument";
    private ImageView ivCloseVideo;
    private WebView webViewVideo;

    /**
     * The M video url.
     */
    String mVideoURL = "";
    /**
     * The Is document.
     */
    boolean isDocument;
    private float fontSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodisplay);

        if (getIntent() != null) {
            mVideoURL = getIntent().getStringExtra(EXTRA_VIDEODISPLAY_URL);
            if (getIntent().hasExtra(EXTRA_ISDOCUMENT)) {
                isDocument = true;
            }
        }

        initViews();
    }

    /**
     * Initialize all views of current screen.
     */
    private void initViews() {
        ivCloseVideo = (ImageView) findViewById(R.id.ivCloseVideo);
        webViewVideo = (WebView) findViewById(R.id.webViewVideo);
        fontSize=getResources().getDimension(R.dimen.font_16);
        webViewVideo.setWebViewClient(new MyWebViewClient());
        webViewVideo.getSettings().setJavaScriptEnabled(true);
        webViewVideo.getSettings().setUseWideViewPort(true);
        webViewVideo.getSettings().setAllowFileAccess(true);
        webViewVideo.getSettings().setDisplayZoomControls(true);
        webViewVideo.getSettings().setBuiltInZoomControls(true);

//        webViewVideo.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
        webViewVideo.getSettings().setPluginState(WebSettings.PluginState.ON);

        try {
            webViewVideo.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
            webViewVideo.setInitialScale(1);
            webViewVideo.getSettings().setLoadWithOverviewMode(true);
            webViewVideo.getSettings().setUseWideViewPort(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isDocument) {
            mVideoURL = "http://docs.google.com/gview?embedded=true&url=" + mVideoURL;
        }
        if (mVideoURL.startsWith("http"))
            webViewVideo.loadUrl(mVideoURL);
        else {
            webViewVideo.getSettings().setDefaultFontSize((int) fontSize);
            webViewVideo.setPadding(12,12,12,12);
            webViewVideo.loadData("<html><body>" + mVideoURL + "</html></body>", "text/html; charset=utf-8", "UTF-8");
        }
//            webViewVideo.loadDataWithBaseURL(", mVideoURL, "text/html", "utf-8", "");

        ivCloseVideo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCloseVideo:
                finish();
                break;
        }
    }

    /**
     * The type My web view client.
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        if (hasFocus) {
            try {
                Class.forName("android.webkit.WebView")
                        .getMethod("onResume", (Class[]) null)
                        .invoke(webViewVideo, (Object[]) null);
            } catch (Exception e) {

            }
            webViewVideo.resumeTimers();
            if (mVideoURL.startsWith("http"))
                webViewVideo.loadUrl(mVideoURL);
            else {
                webViewVideo.getSettings().setDefaultFontSize((int) fontSize);
                webViewVideo.setPadding(12,12,12,12);
                webViewVideo.loadData("<html><body>" + mVideoURL + "</html></body>", "text/html; charset=utf-8", "UTF-8");
            }
        } else {
            try {
                Class.forName("android.webkit.WebView")
                        .getMethod("onPause", (Class[]) null)
                        .invoke(webViewVideo, (Object[]) null);
            } catch (Exception e) {

            }
            webViewVideo.pauseTimers();
        }
        super.onWindowFocusChanged(hasFocus);
    }

}
