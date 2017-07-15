package com.techmali.smartteam.domain.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.CryptoManager;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private SharedPreferences prefManager = null;

    @Override
    public void onTokenRefresh() {
        prefManager = CryptoManager.getInstance(this).getPrefs();

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token:-> " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);

        // You should store a boolean that indicates whether the generated token has been
        // sent to your server. If the boolean is false, send the token to your server,
        // otherwise your server should have already received the token.
        prefManager.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, true).apply();

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        //Intent registrationComplete = new Intent(ConstantData.REGISTRATION_COMPLETE);
        // LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        prefManager.edit().putString(Constants.REGISTRATION_TOKEN, token).apply();
    }
}