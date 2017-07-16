package com.techmali.smartteam.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.techmali.smartteam.models.LoginDetailResponse;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.network.RequestMethod;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.request.RequestBuilder;
import com.techmali.smartteam.ui.activities.AttendanceActivity;
import com.techmali.smartteam.ui.activities.CreateUserActivity;
import com.techmali.smartteam.ui.activities.MyAttendanceActivity;
import com.techmali.smartteam.ui.activities.MyExpenseActivity;
import com.techmali.smartteam.ui.activities.MyTimeSheetActivity;
import com.techmali.smartteam.ui.activities.ProjectListActivity;
import com.techmali.smartteam.utils.CryptoManager;
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

        new GetSyncData().execute();
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
        setTitle("Smart Team");
        getLoginDetail();
        changeToolBarColor();
    }

    private class GetSyncData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            networkManager.isProgressBarVisible(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String reqParams = pendingData.getSyncData();
            return reqParams;
        }

        @Override
        protected void onPostExecute(String reqParams) {
            super.onPostExecute(reqParams);
            if (!Utils.isEmptyString(reqParams)) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put(PARAMS.TAG_PARAMS, reqParams);
                reqIdSyncData = networkManager.addRequest(parameters, getActivity(), RequestMethod.POST, RequestBuilder.METHOD_SYNC_DATA);
            }
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
                    LoginDetailResponse loginDetailResponse = new Gson().fromJson(response, LoginDetailResponse.class);
                } else if (id == reqIdSyncData) {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        new SaveData().execute(object.getString(PARAMS.TAG_RESULT), object.getString(PARAMS.TAG_LAST_SYNC_DATE));
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

    class SaveData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (!Utils.isEmptyString(strings[0])) {
                    JSONObject object = new JSONObject(strings[0]);
                    insetUserinfo(object.getString(DbParams.TBL_USER_INFO));
                    return strings[1];
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!Utils.isEmptyString(result)){
                prefManager.edit().putString(PARAMS.KEY_LAST_SYNC_DATE, result).apply();
            }
            networkManager.isProgressBarVisible(false);
        }
    }

    private void insetUserinfo(String userinfo) {
        if(!Utils.isEmptyString(userinfo)){
            ArrayList<SyncUserInfo> userInfoArrayList = new Gson().fromJson(userinfo, new TypeToken<List<SyncUserInfo>>(){}.getType());
            if(userInfoArrayList.size() > 0){
                for (int i=0; i<userInfoArrayList.size(); i++){
                    boolean isExists = pendingData.checkRecordExist(DbParams.TBL_USER_INFO, DbParams.CLM_LOCAL_USER_ID, userInfoArrayList.get(i).getLocal_user_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if(isExists)
                        pendingData.update(userInfoArrayList.get(i), DbParams.TBL_USER_INFO, userInfoArrayList.get(i).getServer_user_id());
                    else
                        pendingData.insert(userInfoArrayList.get(i), DbParams.TBL_USER_INFO);
                }
            }
        }
    }
}
