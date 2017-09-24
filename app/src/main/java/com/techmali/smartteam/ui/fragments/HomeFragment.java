package com.techmali.smartteam.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseFragment;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.models.UserData;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.network.RequestMethod;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.request.RequestBuilder;
import com.techmali.smartteam.ui.activities.AttendanceActivity;
import com.techmali.smartteam.ui.activities.CreateUserActivity;
import com.techmali.smartteam.ui.activities.ImageSelectionActivity;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.ui.activities.MyAttendanceActivity;
import com.techmali.smartteam.ui.activities.MyExpenseActivity;
import com.techmali.smartteam.ui.activities.MyTimeSheetActivity;
import com.techmali.smartteam.ui.activities.ProjectListActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).initActionBar(getActivity().getString(R.string.app_name), mRootView);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
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
                startActivity(new Intent(getActivity(), HomeMenuFragment.class));
                break;

            case R.id.tvProject:
                startActivity(new Intent(getActivity(), ProjectListActivity.class));
                break;

            case R.id.tvTimeSheet:
                startActivity(new Intent(getActivity(), ImageSelectionActivity.class));
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

}
