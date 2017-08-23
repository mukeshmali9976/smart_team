package com.techmali.smartteam.domain.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.CryptoManager;

import java.text.DateFormat;
import java.util.Date;

public class LocationUpdateService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    protected static final String TAG = LocationUpdateService.class.getName();

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 2500;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    protected Location mCurrentLocation;
    protected GoogleApiClient mGoogleApiClient;
    protected String mLastUpdateTime;
    protected LocationRequest mLocationRequest;

    private SharedPreferences prefManager = null;

    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LOC", "Service init...");
        this.mLastUpdateTime = "";
        buildGoogleApiClient();
        Constants.IS_LOCATION_SERVICE_RUNNING = true;
        return START_STICKY;
    }

    public void onConnected(Bundle bundle) {
        Log.e(TAG, "Connected...");
        startLocationUpdates();
    }

    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection suspended==");
        mGoogleApiClient.connect();
    }

    public void onLocationChanged(Location location) {
        Toast.makeText(this, "location changed... " + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, "location changed... " + location.getLatitude() + "," + location.getLongitude());
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        Log.e(TAG, "Building GoogleApiClient===");
        prefManager = CryptoManager.getInstance(this).getPrefs();
        mGoogleApiClient = new Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void updateUI() {
        Log.e(TAG, "Latitude:==" + mCurrentLocation.getLatitude() + "\n Longitude:==" + mCurrentLocation.getLongitude());
        prefManager.edit().putString("latitude", mCurrentLocation.getLatitude() + "").apply();
        prefManager.edit().putString("longitude", mCurrentLocation.getLongitude() + "").apply();
    }

    protected void startLocationUpdates() {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(102);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.e(TAG, " startLocationUpdates===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates() {
        Log.e(TAG, "stopLocationUpdates();==");
        Constants.IS_LOCATION_SERVICE_RUNNING = false;
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
}
