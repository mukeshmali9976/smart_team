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
import android.view.View;
import android.view.ViewGroup;


import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BasePermissionFragment;
import com.techmali.smartteam.domain.adapters.ProjectListAdapter;
import com.techmali.smartteam.models.ProjectListModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.activities.ProjectDetailActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class ActiveProjectFragment extends BasePermissionFragment implements RequestListener, View.OnClickListener, ProjectListAdapter.OnInnerViewsClickListener {

    public static final String TAG = ActiveProjectFragment.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager;

    private View mRootView;
    private ProjectListAdapter mAdapter;
    private RecyclerView rvActiveProjectList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_active_project, null);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();

        initView();

        return mRootView;
    }

    private void initView() {

        List<ProjectListModel> listModels = new ArrayList<>();
        ProjectListModel projectListModel;

        for (int i = 0; i < 10; i++) {
            projectListModel = new ProjectListModel();
            listModels.add(projectListModel);
        }
        rvActiveProjectList = (RecyclerView) mRootView.findViewById(R.id.rvActiveProjectList);

        mAdapter = new ProjectListAdapter(getActivity(), listModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvActiveProjectList.setLayoutManager(mLayoutManager);
        rvActiveProjectList.setItemAnimator(new DefaultItemAnimator());
        rvActiveProjectList.setAdapter(mAdapter);
        rvActiveProjectList.setAdapter(mAdapter);

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

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.llRowProjectList:
                startActivity(new Intent(getActivity(), ProjectDetailActivity.class));
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
