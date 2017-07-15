package com.techmali.smartteam.ui.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techmali.smartteam.R;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.activities.CompanyProfileActivity;
import com.techmali.smartteam.ui.activities.LoginActivity;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.CryptoManager;


public class MenuFragment extends Fragment implements View.OnClickListener, RequestListener {

    public static final String TAG = MenuFragment.class.getSimpleName();

    private NetworkManager networkManager;
    private SharedPreferences prefManager = null;
    private View mRootView;
    private View selectedView;

    private int reqIdLogout = -1;

    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private ImageView ivProfile, ivEdit;
    private TextView tvName, tvVersion, tvHome, tvNotification, tvCompanyProfile, tvHolidays, tvHelp, tvLogout;
    private DisplayImageOptions options;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_drawer_menu, null);
        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_notification)
                .showImageForEmptyUri(R.drawable.ic_notification)
                .showImageOnFail(R.drawable.ic_notification)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        initView();
        return mRootView;
    }

    private void initView() {


        tvName = (TextView) mRootView.findViewById(R.id.tvName);
        ivProfile = (ImageView) mRootView.findViewById(R.id.ivProfile);
        tvVersion = (TextView) mRootView.findViewById(R.id.tvVersion);


        tvHome = (TextView) mRootView.findViewById(R.id.tvHome);
        tvNotification = (TextView) mRootView.findViewById(R.id.tvNotification);
        tvCompanyProfile = (TextView) mRootView.findViewById(R.id.tvCompanyProfile);
        tvHolidays = (TextView) mRootView.findViewById(R.id.tvHolidays);
        tvHelp = (TextView) mRootView.findViewById(R.id.tvHelp);
        tvLogout = (TextView) mRootView.findViewById(R.id.tvLogout);


        mRootView.findViewById(R.id.rlMenu).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.app_background_color));
        tvHome.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        tvNotification.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        tvCompanyProfile.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        tvHolidays.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        tvHelp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        tvLogout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));


//            setTextViewDrawableColor(tvHome, R.color.colorTextHint);
//            setTextViewDrawableColor(tvMyperformance, R.color.white_color);
//            setTextViewDrawableColor(tvNotification, R.color.white_color);
//            setTextViewDrawableColor(tvHelp, R.color.white_color);
//            setTextViewDrawableColor(tvLogout, R.color.white_color);
//            tvHome.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
//            tvMyperformance.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
//            tvNotification.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
//            tvHelp.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
//            tvLogout.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
//            tvName.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
//            tvVersion.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));


        TextView tvBadge = (TextView) mRootView.findViewById(R.id.tvBadge);

        tvVersion.setText("App Version : 1.0.1");
        if (!TextUtils.isEmpty(prefManager.getString(Constants.PREF_PROFILE_IMAGE, ""))) {
            mImageLoader.displayImage(prefManager.getString(Constants.PREF_PROFILE_IMAGE, ""), ivProfile, options);
        }
        if (!TextUtils.isEmpty(prefManager.getString(Constants.PREF_FULL_NAME, ""))) {
            tvName.setText(prefManager.getString(Constants.PREF_FULL_NAME, ""));
        }

        if (prefManager.getInt(Constants.PREF_NOTIFICATION_COUNT, 0) == 0) {
            mRootView.findViewById(R.id.rltSquare).setVisibility(View.GONE);
        } else {
            mRootView.findViewById(R.id.rltSquare).setVisibility(View.VISIBLE);
        }

        tvBadge.setText(String.valueOf(prefManager.getInt(Constants.PREF_NOTIFICATION_COUNT, 0)));

        mRootView.findViewById(R.id.ivEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).getSlidingMenu().toggle();
                ((MainActivity) getActivity()).clearBackStack();
                ((MainActivity) getActivity()).replaceFragment(new ProfileFragment(), "Profile");
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).getSlidingMenu().toggle();
                ((MainActivity) getActivity()).clearBackStack();
                ((MainActivity) getActivity()).replaceFragment(new ProfileFragment(), "Profile");
            }
        });

        tvHome.setOnClickListener(this);
        tvNotification.setOnClickListener(this);
        tvCompanyProfile.setOnClickListener(this);
        tvHolidays.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).getSlidingMenu().toggle();
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        ((MainActivity) getActivity()).clearBackStack();
        switch (v.getId()) {
            case R.id.tvHome:
                ((MainActivity) getActivity()).replaceFragment(new HomeFragment(), "Home");
                break;
            case R.id.tvNotification:
                ((MainActivity) getActivity()).replaceFragment(new HomeFragment(), "Notifications");
                break;
            case R.id.tvCompanyProfile:
                startActivity(new Intent(getActivity(), CompanyProfileActivity.class));
                break;
            case R.id.tvHolidays:

                break;
            case R.id.tvHelp:

                break;
            case R.id.tvLogout:
                showDialog();
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        networkManager.setListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        networkManager.removeListener(this);
    }


    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getActivity(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private void logoutRequest() {


    }

    @Override
    public void onSuccess(int id, String response) {


    }

    @Override
    public void onError(int id, String message) {
        if (id == reqIdLogout) {

        }
    }

    private void showDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setMessage("finish");
            builder.setPositiveButton(R.string.lbl_yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            logoutRequest();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    });
            builder.setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
