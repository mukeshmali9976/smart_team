package com.techmali.smartteam.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BasePermissionFragment;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.ProjectListAdapter;
import com.techmali.smartteam.models.ProjectListModel;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.ui.activities.ProjectDetailActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActiveProjectFragment extends BasePermissionFragment implements View.OnClickListener, ProjectListAdapter.OnInnerViewsClickListener {

    public static final String TAG = ActiveProjectFragment.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private PendingDataImpl pendingData;

    private View mRootView;

    private ProjectListAdapter mAdapter;
    private RecyclerView rvActiveProjectList;

    private ArrayList<SyncProject> projectArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_active_project, null);

        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();
        pendingData = new PendingDataImpl(getActivity());

        initView();

        return mRootView;
    }

    private void initView() {

        rvActiveProjectList = (RecyclerView) mRootView.findViewById(R.id.rvActiveProjectList);
        new GetProjectList().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
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

    private class GetProjectList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return pendingData.getProjectList();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (!Utils.isEmptyString(result)) {
                    Log.e(TAG, result);
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        projectArrayList = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<SyncProject>>() {
                        }.getType());
                        if (projectArrayList != null && !projectArrayList.isEmpty()) {
                            mAdapter = new ProjectListAdapter(getActivity(), projectArrayList, ActiveProjectFragment.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                            rvActiveProjectList.setLayoutManager(mLayoutManager);
                            rvActiveProjectList.setItemAnimator(new DefaultItemAnimator());
                            rvActiveProjectList.setAdapter(mAdapter);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
