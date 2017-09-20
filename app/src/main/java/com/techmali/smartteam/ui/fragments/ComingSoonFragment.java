package com.techmali.smartteam.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BasePermissionFragment;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.ProjectListAdapter;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.ui.activities.ProjectDetailActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComingSoonFragment extends BasePermissionFragment {

    public static final String TAG = ComingSoonFragment.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private PendingDataImpl pendingData;

    private View mRootView;

    private ProjectListAdapter mAdapter;
    private RecyclerView rvActiveProjectList;
    private SwipeRefreshLayout swipe;
    private SwipeLayout swipeLayout;

    private ArrayList<SyncProject> projectArrayList = new ArrayList<>();
    private int mPosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_message, null);

        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();
        pendingData = new PendingDataImpl(getActivity());

//        initView();

        return mRootView;
    }

    private void initView() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).initActionBar(getActivity().getString(R.string.app_name), mRootView);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }
}
