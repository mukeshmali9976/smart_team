package com.techmali.smartteam.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BasePermissionFragment;
import com.techmali.smartteam.domain.adapters.MyAttendanceAdapter;
import com.techmali.smartteam.models.AttendanceModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.activities.AddMyAttendanceActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyAttendanceFragment extends BasePermissionFragment implements RequestListener, View.OnClickListener {

    public static final String TAG = MyAttendanceFragment.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager;

    private View mRootView;
    private MyAttendanceAdapter mAdapter;
    private RecyclerView rvMyAttendanceList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my_attendance, null);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();

        initView();

        return mRootView;
    }

    private void initView() {


        List<AttendanceModel> listModels = new ArrayList<>();
        AttendanceModel attendanceModel;

        for (int i = 0; i < 10; i++) {
            attendanceModel = new AttendanceModel();
            listModels.add(attendanceModel);
        }
        rvMyAttendanceList = (RecyclerView) mRootView.findViewById(R.id.rvMyAttendanceList);

        mAdapter = new MyAttendanceAdapter(getActivity(), listModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvMyAttendanceList.setLayoutManager(mLayoutManager);
        rvMyAttendanceList.setItemAnimator(new DefaultItemAnimator());
        rvMyAttendanceList.setAdapter(mAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {



        }
    }

    @Override
    public void onSuccess(int id, String response) {
        try {
            Log.e(TAG, response);
            if (!TextUtils.isEmpty(response)) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int id, String message) {
    }
}
