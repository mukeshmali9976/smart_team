package com.techmali.smartteam.ui.fragments;

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
import com.techmali.smartteam.base.BaseFragment;
import com.techmali.smartteam.base.BasePermissionFragment;
import com.techmali.smartteam.domain.adapters.MyLeavesAdapter;
import com.techmali.smartteam.domain.adapters.NotificationAdapter;
import com.techmali.smartteam.models.NotificationModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends BasePermissionFragment implements RequestListener, View.OnClickListener, NotificationAdapter.OnInnerViewsClickListener {

    public static final String TAG = NotificationFragment.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager;

    private View mRootView;

    private RecyclerView rvNotificationList;
    private NotificationAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_notification_list, null);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();

        initView(mRootView);

        return mRootView;
    }

    private void initView(View mRootView) {
        rvNotificationList = (RecyclerView) mRootView.findViewById(R.id.rvNotificationList);

        ArrayList<NotificationModel> notificationModelList = new ArrayList<>();
        NotificationModel notificationModel;
        for (int i = 0; i <10 ; i++) {
            notificationModel = new NotificationModel();
            notificationModelList.add(notificationModel);
        }

//        mAdapter = new NotificationAdapter(getActivity(), notificationModelList, this);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        rvNotificationList.setLayoutManager(mLayoutManager);
//        rvNotificationList.setItemAnimator(new DefaultItemAnimator());
//        rvNotificationList.setAdapter(mAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initActionBar(getActivity().getString(R.string.title_notification), mRootView);
        setTitle(getString(R.string.title_notification));
        changeToolBarColor();
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

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
