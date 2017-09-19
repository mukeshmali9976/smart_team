package com.techmali.smartteam.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.models.LoginResponse;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.network.RequestMethod;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.request.RequestBuilder;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

import me.leolin.shortcutbadger.ShortcutBadger;


public class SplashActivity extends BaseAppCompatActivity implements RequestListener {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;
    private int reqIdLogin = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(SplashActivity.this).getPrefs();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(prefManager.getBoolean(PARAMS.KEY_IS_LOGGED_IN, false)) {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }else {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }
            }
        }, 3000);

    }

    @Override
    public void onStart() {
        super.onStart();
        networkManager.setListener(this);
    }

    @Override
    public void onStop() {
        networkManager.removeListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSuccess(int id, String response) {
        try {
            if (!Utils.isEmptyString(response)) {
                if (reqIdLogin == id) {
                    LoginResponse mLoginResponse = new Gson().fromJson(response, LoginResponse.class);
                    if (mLoginResponse.getStatus() == PARAMS.TAG_STATUS_200) {
                        prefManager.edit().putString(PARAMS.KEY_HEADER_TOKEN, "").apply();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void performLogin() {
        networkManager.isProgressBarVisible(true);
        reqIdLogin = networkManager.addRequest(
                RequestBuilder.performLogin("", ""),
                this, RequestMethod.POST, RequestBuilder.METHOD_LOGIN);

    }

    DialogInterface.OnClickListener okClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String token = prefManager.getString(Constants.REGISTRATION_TOKEN, "");
            CryptoManager.getInstance(SplashActivity.this).getPrefs().edit().clear().apply();
            prefManager.edit().putString(Constants.REGISTRATION_TOKEN, token).apply();

            ShortcutBadger.removeCount(getApplicationContext()); //for 1.1.4

            Intent mLoginIntent = new Intent(SplashActivity.this, LoginActivity.class);
            mLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mLoginIntent);
        }
    };

    @Override
    public void onError(int id, String message) {
        if (id == reqIdLogin) {
            // showRequestErrorDialog(message);
        }
    }

//    private boolean checkPlayServices() {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.i(TAG, "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
}
