package com.techmali.smartteam.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseFragment;
import com.techmali.smartteam.database.DbParams;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.HomePagerAdapter;
import com.techmali.smartteam.models.SyncAttendance;
import com.techmali.smartteam.models.SyncCheckIn;
import com.techmali.smartteam.models.SyncCompany;
import com.techmali.smartteam.models.SyncExpense;
import com.techmali.smartteam.models.SyncLeave;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncProjectUserLink;
import com.techmali.smartteam.models.SyncRole;
import com.techmali.smartteam.models.SyncSecurityController;
import com.techmali.smartteam.models.SyncSecurityMenu;
import com.techmali.smartteam.models.SyncSecurityMenuControllerAction;
import com.techmali.smartteam.models.SyncSecurityMenuControllerLink;
import com.techmali.smartteam.models.SyncTask;
import com.techmali.smartteam.models.SyncTaskType;
import com.techmali.smartteam.models.SyncTaskUserLink;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.models.UserData;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.network.RequestMethod;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.request.RequestBuilder;
import com.techmali.smartteam.ui.activities.AttendanceActivity;
import com.techmali.smartteam.ui.activities.CreateUserActivity;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.ui.activities.MyAttendanceActivity;
import com.techmali.smartteam.ui.activities.MyExpenseActivity;
import com.techmali.smartteam.ui.activities.MyTimeSheetActivity;
import com.techmali.smartteam.ui.activities.ProjectListActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.SyncData;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener, RequestListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private View mRootView;

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;
    private PendingDataImpl pendingData;
    private Activity mActivity;

    private int reqIdLoginDetail = -1, reqIdSyncData = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, null);

        mActivity = getActivity();
        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();
        pendingData = new PendingDataImpl(mActivity);

        initView();
        return mRootView;
    }


    private void initView() {

        mRootView.findViewById(R.id.tvMyTimeSheet).setOnClickListener(this);
        mRootView.findViewById(R.id.tvMyExpense).setOnClickListener(this);
        mRootView.findViewById(R.id.tvMyAttendance).setOnClickListener(this);
        mRootView.findViewById(R.id.tvCamera).setOnClickListener(this);

        mRootView.findViewById(R.id.tvProject).setOnClickListener(this);
        mRootView.findViewById(R.id.tvTimeSheet).setOnClickListener(this);
        mRootView.findViewById(R.id.tvExpense).setOnClickListener(this);
        mRootView.findViewById(R.id.tvAttendance).setOnClickListener(this);
        mRootView.findViewById(R.id.tvLeaveApplication).setOnClickListener(this);
        mRootView.findViewById(R.id.tvTraking).setOnClickListener(this);
        mRootView.findViewById(R.id.tvCreateUser).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        networkManager.setListener(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        networkManager.removeListener(this);
        super.onStop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).initActionBar(getActivity().getString(R.string.app_name), mRootView);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));

        if (Utils.isInternetAvailable(mActivity)) {
//            getLoginDetail();
//            SyncData data = new SyncData(mActivity);
//            data.startSync();
        }
    }


    private void getLoginDetail() {
        networkManager.isProgressBarVisible(true);
        reqIdLoginDetail = networkManager.addRequest(RequestBuilder.blankRequest(), getActivity(), RequestMethod.POST, RequestBuilder.METHOD_LOGIN_DETAIL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMyTimeSheet:
                startActivity(new Intent(getActivity(), MyTimeSheetActivity.class));
                break;

            case R.id.tvMyExpense:
                startActivity(new Intent(getActivity(), MyExpenseActivity.class));
                break;

            case R.id.tvMyAttendance:
                startActivity(new Intent(getActivity(), MyAttendanceActivity.class));
                break;
            case R.id.tvCamera:
                break;

            case R.id.tvProject:
                startActivity(new Intent(getActivity(), ProjectListActivity.class));
                break;

            case R.id.tvTimeSheet:
                break;

            case R.id.tvExpense:
                break;

            case R.id.tvAttendance:
                startActivity(new Intent(getActivity(), AttendanceActivity.class));
                break;

            case R.id.tvLeaveApplication:

                break;

            case R.id.tvTraking:

                break;

            case R.id.tvCreateUser:
                startActivity(new Intent(getActivity(), CreateUserActivity.class));
                break;
        }

    }

    @Override
    public void onSuccess(int id, String response) {
        try {
            if (!TextUtils.isEmpty(response)) {
                if (id == reqIdLoginDetail) {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {

                        String roleList = object.getJSONObject(PARAMS.TAG_RESULT).getString(PARAMS.TAG_ROLE_LIST);
                        prefManager.edit().putString(PARAMS.KEY_ROLE_LIST, roleList).apply();

                        String userObject = object.getJSONObject(PARAMS.TAG_RESULT).getJSONArray(PARAMS.TAG_USER_DATA).getString(0);
                        UserData data = new Gson().fromJson(userObject, UserData.class);
                        saveLoginDataInPref(data);
                    }
                }
            } else {
                displayError(getString(R.string.no_network_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int id, String message) {
        displayError(message);
    }


    private void saveLoginDataInPref(UserData data) {

        prefManager.edit().putString(PARAMS.KEY_COMPANY_ID, data.getCompany_id()).apply();
        prefManager.edit().putString(PARAMS.KEY_HEADER_TOKEN, data.getHeader_token()).apply();
        prefManager.edit().putString(PARAMS.KEY_COMPANY_NAME, data.getCompany_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_UNIQUE_CODE, data.getUnique_code()).apply();
        prefManager.edit().putString(PARAMS.KEY_FIRST_NAME, data.getFirst_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_LAST_NAME, data.getLast_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_GENDER, data.getGender()).apply();
        prefManager.edit().putString(PARAMS.KEY_EMAIL, data.getEmail()).apply();
        prefManager.edit().putString(PARAMS.KEY_ROLE_NAME, data.getRole_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_STATUS_ID, data.getStatus_id()).apply();

        prefManager.edit().putBoolean(PARAMS.KEY_IS_LOGGED_IN, true).apply();
    }

}
