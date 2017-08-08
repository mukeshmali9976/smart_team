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
import com.techmali.smartteam.models.SyncAttendance;
import com.techmali.smartteam.models.SyncCheckIn;
import com.techmali.smartteam.models.SyncCompany;
import com.techmali.smartteam.models.SyncExpense;
import com.techmali.smartteam.models.SyncLeave;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncProjectUserLink;
import com.techmali.smartteam.models.SyncRole;
import com.techmali.smartteam.models.SyncSecurityController;
import com.techmali.smartteam.models.SyncSecurityMenu;
import com.techmali.smartteam.models.SyncSecurityMenuControllerAction;
import com.techmali.smartteam.models.SyncSecurityMenuControllerLink;
import com.techmali.smartteam.models.SyncTask;
import com.techmali.smartteam.models.SyncTaskType;
import com.techmali.smartteam.models.SyncTaskUserLink;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.models.UserData;
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
        setTitle(getResources().getString(R.string.app_name));
        getLoginDetail();

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
                    JSONObject object = new JSONObject(response);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {

                        String roleList = object.getJSONObject(PARAMS.TAG_RESULT).getString(PARAMS.TAG_ROLE_LIST);
                        prefManager.edit().putString(PARAMS.KEY_ROLE_LIST, roleList).apply();

                        String userObject = object.getJSONObject(PARAMS.TAG_RESULT).getJSONArray(PARAMS.TAG_USER_DATA).getString(0);
                        UserData data = new Gson().fromJson(userObject, UserData.class);
                        saveLoginDataInPref(data);
                    }
                    new GetSyncData().execute();
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

    private void saveLoginDataInPref(UserData data) {

        prefManager.edit().putString(PARAMS.KEY_COMPANY_ID, data.getCompany_id()).apply();
        prefManager.edit().putString(PARAMS.KEY_HEADER_TOKEN, data.getHeader_token()).apply();
        prefManager.edit().putString(PARAMS.KEY_COMPANY_NAME, data.getCompany_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_UNIQUE_CODE, data.getUnique_code()).apply();
        prefManager.edit().putString(PARAMS.KEY_FIRST_NAME, data.getFirst_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_LAST_NAME, data.getLast_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_GENDER, data.getGender()).apply();
        prefManager.edit().putString(PARAMS.KEY_EMAIL, data.getEmail()).apply();
        prefManager.edit().putString(PARAMS.KEY_ROLE_NAME, data.getRole_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_STATUS_ID, data.getStatus_id()).apply();

        prefManager.edit().putBoolean(PARAMS.KEY_IS_LOGGED_IN, true).apply();
    }


    private class SaveData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (!Utils.isEmptyString(strings[0])) {
                    JSONObject object = new JSONObject(strings[0]);
                    insertUserInfo(object.getString(DbParams.TBL_USER_INFO), DbParams.TBL_USER_INFO);
                    insertProject(object.getString(DbParams.TBL_PROJECT), DbParams.TBL_PROJECT);
                    insetProjectUserLink(object.getString(DbParams.TBL_PROJECT_USER_LINK), DbParams.TBL_PROJECT_USER_LINK);
                    insertTask(object.getString(DbParams.TBL_TASK), DbParams.TBL_TASK);
                    insertTaskUserLink(object.getString(DbParams.TBL_TASK_USER_LINK), DbParams.TBL_TASK_USER_LINK);
                    insertAttendance(object.getString(DbParams.TBL_ATTENDANCE), DbParams.TBL_ATTENDANCE);
                    insertCheckIn(object.getString(DbParams.TBL_CHECK_IN), DbParams.TBL_CHECK_IN);
                    insertExpense(object.getString(DbParams.TBL_EXPENSE), DbParams.TBL_EXPENSE);
                    insertLeave(object.getString(DbParams.TBL_LEAVE), DbParams.TBL_LEAVE);
                    insertSecurityMenu(object.getString(DbParams.TBL_SECURITY_MENU), DbParams.TBL_SECURITY_MENU);
                    insertSecurityMenuControllerAction(object.getString(DbParams.TBL_SECURITY_MENU_CONTROLLERS_ACTION), DbParams.TBL_SECURITY_MENU_CONTROLLERS_ACTION);
                    insertSecurityMenuControllerLink(object.getString(DbParams.TBL_SECURITY_MENU_CONTROLLERS_LINK), DbParams.TBL_SECURITY_MENU_CONTROLLERS_LINK);
                    insertSecurityController(object.getString(DbParams.TBL_SECURITY_CONTROLLERS), DbParams.TBL_SECURITY_CONTROLLERS);
                    insertRole(object.getString(DbParams.TBL_ROLE), DbParams.TBL_ROLE);
                    insertCompany(object.getString(DbParams.TBL_COMPANY), DbParams.TBL_COMPANY);
//                    insertTaskType(object.getString(DbParams.TBL_TASK_TYPE), DbParams.TBL_TASK_TYPE);
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
            if (!Utils.isEmptyString(result)) {
                prefManager.edit().putString(PARAMS.KEY_LAST_SYNC_DATE, result).apply();
            }
            networkManager.isProgressBarVisible(false);
        }
    }

    private void insertUserInfo(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncUserInfo> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncUserInfo>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_USER_ID, syncList.get(i).getLocal_user_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getServer_user_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertProject(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncProject> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncProject>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_PROJECT_ID, syncList.get(i).getLocal_project_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getServer_project_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insetProjectUserLink(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncProjectUserLink> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncProjectUserLink>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_PROJECT_USER_LINK_ID, syncList.get(i).getLocal_project_user_link_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getProject_user_link_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertTask(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncTask> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncTask>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_TASK_ID, syncList.get(i).getLocal_task_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getTask_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertTaskType(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncTaskType> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncTaskType>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_TASK_TYPE_ID, syncList.get(i).getTask_type_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getTask_type_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertTaskUserLink(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncTaskUserLink> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncTaskUserLink>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_TASK_USER_LINK_ID, syncList.get(i).getLocal_task_user_link_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getTask_user_link_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertAttendance(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncAttendance> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncAttendance>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_ATTENDANCE_ID, syncList.get(i).getLocal_attendance_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getAttendance_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertCheckIn(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncCheckIn> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncCheckIn>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_CHECK_IN_ID, syncList.get(i).getLocal_checkin_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getCheckin_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertExpense(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncExpense> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncExpense>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_EXPENSE_ID, syncList.get(i).getLocal_expance_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getExpance_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertLeave(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncLeave> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncLeave>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_LEAVE_ID, syncList.get(i).getLocal_leave_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getLeave_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertSecurityMenu(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncSecurityMenu> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncSecurityMenu>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_SECURITY_MENU_ID, syncList.get(i).getSecurity_menu_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getSecurity_menu_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertSecurityMenuControllerAction(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncSecurityMenuControllerAction> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncSecurityMenuControllerAction>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_SECURITY_MENU_CONTROLLERS_ACTION_ID, syncList.get(i).getSecurity_menu_controllers_action_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getSecurity_menu_controllers_action_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertSecurityMenuControllerLink(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncSecurityMenuControllerLink> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncSecurityMenuControllerLink>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_MENU_CONTROLLERS_LINK_ID, syncList.get(i).getMenu_controllers_link_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getMenu_controllers_link_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertSecurityController(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncSecurityController> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncSecurityController>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_SECURITY_CONTROLLER_ID, syncList.get(i).getSecurity_controllers_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getSecurity_controllers_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertRole(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncRole> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncRole>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_ROLE_ID, syncList.get(i).getRole_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getRole_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }

    private void insertCompany(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncCompany> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncCompany>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_COMPANY_ID, syncList.get(i).getCompany_id());
                    Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getCompany_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }
}
