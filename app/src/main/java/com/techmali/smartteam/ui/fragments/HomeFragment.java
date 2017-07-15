package com.techmali.smartteam.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseFragment;
import com.techmali.smartteam.models.LoginDetailResponse;
import com.techmali.smartteam.models.LoginResponse;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.network.RequestMethod;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.request.RequestBuilder;
import com.techmali.smartteam.ui.activities.AttendanceActivity;
import com.techmali.smartteam.ui.activities.CreateUserActivity;
import com.techmali.smartteam.ui.activities.LoginActivity;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.ui.activities.MyAttendanceActivity;
import com.techmali.smartteam.ui.activities.MyExpenseActivity;
import com.techmali.smartteam.ui.activities.MyTimeSheetActivity;
import com.techmali.smartteam.ui.activities.ProjectListActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

public class HomeFragment extends BaseFragment implements View.OnClickListener, RequestListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private View mRootView;

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;
    private int reqIdLoginDetail = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, null);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();

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
        initActionBar(getActivity().getString(R.string.app_name), mRootView);
        setTitle("");
        getLoginDetail();
        changeToolBarColor();
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
            if (!Utils.isEmptyString(response)) {
                if (id == reqIdLoginDetail) {
                    LoginDetailResponse loginDetailResponse = new Gson().fromJson(response,LoginDetailResponse.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int id, String message) {

    }
}
