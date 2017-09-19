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

public class ComingSoonFragment extends BasePermissionFragment implements ProjectListAdapter.OnInnerViewsClickListener {

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

        swipe = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe);
        rvActiveProjectList = (RecyclerView) mRootView.findViewById(R.id.rvActiveProjectList);

        swipe.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        new GetProjectList().execute();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetProjectList().execute();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).initActionBar(getActivity().getString(R.string.app_name), mRootView);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
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
    public void onDelete(View view, int position) {
        swipeLayout = (SwipeLayout) view;
        mPosition = position;
        new DeleteProject().execute(projectArrayList.get(mPosition).getLocal_project_id());
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
                        projectArrayList.clear();
                        projectArrayList = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<SyncProject>>() {
                        }.getType());

                        mAdapter = new ProjectListAdapter(getActivity(), projectArrayList, ComingSoonFragment.this);
                        rvActiveProjectList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvActiveProjectList.setItemAnimator(new DefaultItemAnimator());
                        mAdapter.setMode(Attributes.Mode.Single);
                        rvActiveProjectList.setAdapter(mAdapter);
                    }
                }
                if (swipe.isRefreshing())
                    swipe.setRefreshing(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DeleteProject extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return pendingData.deleteProject(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            try {
                if (result) {
                    displayError("Project Deleted Successfully....");
                    mAdapter.mItemManger.removeShownLayouts(swipeLayout);
                    projectArrayList.remove(mPosition);
                    mAdapter.notifyItemRemoved(mPosition);
                    mAdapter.notifyItemRangeChanged(mPosition, projectArrayList.size());
                    mAdapter.mItemManger.closeAllItems();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
