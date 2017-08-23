package com.techmali.smartteam.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.techmali.smartteam.R;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.services.GPSTracker;
import com.techmali.smartteam.domain.services.LocationUpdateService;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.slidingmenu.SlidingActivity;
import com.techmali.smartteam.slidingmenu.SlidingMenu;
import com.techmali.smartteam.ui.fragments.HomeFragment;
import com.techmali.smartteam.ui.fragments.MenuFragment;
import com.techmali.smartteam.ui.views.MyProgressDialog;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import java.text.DateFormat;

public class MainActivity extends SlidingActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static int REQUEST_CHECK_SETTINGS = 101;

    protected MenuFragment menuFragment;
    public Activity mActivity;

    private PendingDataImpl dbHelper;
    private SharedPreferences prefManager = null;
    private MyProgressDialog dialog;

    private GPSTracker gps;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    private MenuItem menuItem;
    private boolean isCheckedIn = false;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        updateLocation();
    }

    private void updateLocation() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            Log.e(TAG, mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        mActivity = this;
        prefManager = CryptoManager.getInstance(mActivity).getPrefs();
        dbHelper = new PendingDataImpl(mActivity);

        dialog = new MyProgressDialog(mActivity);
        dialog.dismiss();

//        if (isGooglePlayServicesAvailable()) {
//            createLocationRequest();
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addApi(LocationServices.API)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .build();
//        }

        // set the Behind View
        setBehindContentView(R.layout.layout_menu_drawer_fragment_container);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            menuFragment = new MenuFragment();
            t.replace(R.id.menuFragmentContainer, menuFragment);
            t.commit();
            replaceFragment(new HomeFragment(), "Home");
        } else {
            menuFragment = (MenuFragment) this.getSupportFragmentManager().findFragmentById(R.id.menuFragmentContainer);
        }
//        Collections.synchronizedCollection(mPostList);
//        initActionBar(getString(R.string.app_name));
        SlidingMenu sm = getSlidingMenu();
        sm.setMode(SlidingMenu.LEFT);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setFadeEnabled(true);
        sm.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                menuFragment = new MenuFragment();
                t.replace(R.id.menuFragmentContainer, menuFragment);
                t.commit();
            }
        });
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menuItem = menu.findItem(R.id.action_menu_sync);
        if (prefManager.getBoolean(PARAMS.KEY_IS_CHECKED_IN, false)) {
            isCheckedIn = true;
            menuItem.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            isCheckedIn = false;
            menuItem.getIcon().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                break;
            case R.id.action_menu_sync:
                Log.e(TAG, "Getting location....");
                if (!Utils.isLocationEnabled(this))
                    Utils.showSettingsAlert(this);
                else
                    startService(new Intent(MainActivity.this, LocationUpdateService.class));
                break;
            case R.id.action_menu_checkin_checkout:
                Utils.hideKeyboard(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLocation() {

        gps = new GPSTracker(MainActivity.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.e(TAG, latitude + "," + longitude);
            if (latitude == 0.0 && longitude == 0.0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getLocation();
            } else {
                isCheckedIn = !isCheckedIn;
                prefManager.edit().putBoolean(PARAMS.KEY_IS_CHECKED_IN, isCheckedIn).apply();
                if (isCheckedIn)
                    menuItem.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                else
                    menuItem.getIcon().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

                dialog.dismiss();
            }
        } else {
            dialog.dismiss();
            gps.showSettingsAlert();
        }
    }

    public void reset(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                reset((ViewGroup) view);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "Call MainActivity onActivityResult");
        Log.e(TAG, "requestCode" + requestCode);
        Log.e(TAG, "resultCode" + resultCode);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

//                if (Utils.isInternetAvailable(this)) {
//                    Intent intent = new Intent(MainActivity.this, MyLocationService.class);
//                    startService(intent);
//                } else {
//                    getLocationWithoutInternet();
//                }
                mGoogleApiClient.connect();
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void replaceFragment(Fragment frm, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, frm, tag).addToBackStack(null).commit();
    }

    public void addFragment(Fragment f) {
        this.addFragment(f, true);
    }

    public void addFragment(Fragment f, boolean clearStack) {
        if (clearStack) {
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, f).addToBackStack(null).commit();
    }

    public Fragment getActiveFragment() {

        return getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    public void clearBackStack() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        if (getSlidingMenu().isMenuShowing()) {
            getSlidingMenu().toggle();
        }

        if (getActiveFragment() instanceof HomeFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(R.string.alert);
                    builder.setCancelable(false);
                    builder.setMessage("finish");
                    builder.setPositiveButton(R.string.lbl_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // create alert dialog
                    AlertDialog alertDialog = builder.create();
//                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    // show it
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            getSupportFragmentManager().popBackStack();
            replaceFragment(new HomeFragment(), "Home");
        }
    }

    @Override
    protected void onDestroy() {
        try {
            mActivity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private void getLocationWithoutInternet() {

        dialog.show();
        Log.e(TAG, "Location without internet...");
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e(TAG, "Provider changed...");
            }

            public void onProviderEnabled(String provider) {
                Log.e(TAG, provider + " Enabled...");
            }

            public void onProviderDisabled(String provider) {
                Log.e(TAG, provider + " Disabled....");
            }

            @Override
            public void onLocationChanged(Location location) {
                Log.e(TAG, "Latitude : " + location.getLatitude() + "nLongitude : " + location.getLongitude());
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        };

//        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
//        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    private boolean isLocationEnable() {

        final boolean[] isAvailable = {true};
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates LS_state = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        isAvailable[0] = true;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        isAvailable[0] = false;
                        break;
                }
            }
        });
        return isAvailable[0];
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }
}
