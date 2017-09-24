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
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BasePermissionFragment;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.MyTimeSheetAdapter;
import com.techmali.smartteam.domain.adapters.ProjectListAdapter;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.TimeSheetModel;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.ui.activities.MyTimeSheetActivity;
import com.techmali.smartteam.ui.activities.ProjectDetailActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyFrameFragment extends BasePermissionFragment {

    public static final String TAG = MyFrameFragment.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private PendingDataImpl model;

    private View mRootView;

    private MyTimeSheetAdapter mAdapter;
    private RecyclerView rvTimesheet;
    private TextView tvTimeSheet;

    private List<TimeSheetModel> listModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my_frame, null);

        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();
        model = new PendingDataImpl(getActivity());

        initView();
        return mRootView;
    }

    private void initView() {
        tvTimeSheet = (TextView) mRootView.findViewById(R.id.tvTimeSheet);
        rvTimesheet = (RecyclerView) mRootView.findViewById(R.id.rvTimesheet);
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetTimesheetList().execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).initActionBar(getActivity().getString(R.string.app_name), mRootView);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }

    private class GetTimesheetList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return model.getTimesheet();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!Utils.isEmptyString(result)) {
                android.util.Log.e(TAG, result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        listModels = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<TimeSheetModel>>() {
                        }.getType());

                        if (listModels != null && !listModels.isEmpty()) {

                            mAdapter = new MyTimeSheetAdapter(getActivity(), listModels, true);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                            rvTimesheet.setLayoutManager(mLayoutManager);
                            rvTimesheet.setItemAnimator(new DefaultItemAnimator());
                            rvTimesheet.setAdapter(mAdapter);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
