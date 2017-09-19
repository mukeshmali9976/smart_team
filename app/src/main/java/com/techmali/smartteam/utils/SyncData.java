package com.techmali.smartteam.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
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
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.network.RequestMethod;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.request.RequestBuilder;
import com.techmali.smartteam.ui.views.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gaurav on 9/10/2017.
 */

public class SyncData extends Service implements RequestListener {

    private static final String TAG = SyncData.class.getSimpleName();

    private int reqIdSyncData = -1;

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager;
    private PendingDataImpl pendingData;
    private MyProgressDialog dialog;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(this).getPrefs();
        pendingData = new PendingDataImpl(this);
        networkManager.setListener(this);

        new GetSyncData().execute();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class GetSyncData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new MyProgressDialog(getApplicationContext());
            Log.e(TAG, "GetSyncData");
        }

        @Override
        protected String doInBackground(Void... voids) {
            return pendingData.getSyncData();
        }

        @Override
        protected void onPostExecute(String reqParams) {
            super.onPostExecute(reqParams);
            if (!Utils.isEmptyString(reqParams)) {
                Log.e(TAG, reqParams);
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put(PARAMS.TAG_PARAMS, reqParams);
                reqIdSyncData = networkManager.addRequest(parameters, getApplicationContext(), RequestMethod.POST, RequestBuilder.METHOD_SYNC_DATA);
            } else {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onSuccess(int id, String response) {
        try {
            if (!TextUtils.isEmpty(response)) {
                if (id == reqIdSyncData) {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        new SaveData().execute(object.getString(PARAMS.TAG_RESULT), object.getString(PARAMS.TAG_LAST_SYNC_DATE));
                    }
                }
            } else {
                dialog.dismiss();
                DialogUtils.showDialog(getApplicationContext(), "", getString(R.string.no_network_connection), getString(R.string.ok));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int id, String message) {
        if (id == reqIdSyncData) {
            dialog.dismiss();
            DialogUtils.showDialog(getApplicationContext(), "", message, getString(R.string.ok));
        }
    }

    private class SaveData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
//                    insertAttendance(object.getString(DbParams.TBL_ATTENDANCE), DbParams.TBL_ATTENDANCE);
//                    insertCheckIn(object.getString(DbParams.TBL_CHECK_IN), DbParams.TBL_CHECK_IN);
                    insertExpense(object.getString(DbParams.TBL_EXPENSE), DbParams.TBL_EXPENSE);
//                    insertLeave(object.getString(DbParams.TBL_LEAVE), DbParams.TBL_LEAVE);
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
            dialog.dismiss();
            stopSelf();
        }
    }

    private void insertUserInfo(String syncModel, String table) {
        if (!Utils.isEmptyString(syncModel)) {
            ArrayList<SyncUserInfo> syncList = new Gson().fromJson(syncModel, new TypeToken<List<SyncUserInfo>>() {
            }.getType());
            if (syncList != null && !syncList.isEmpty()) {
                for (int i = 0; i < syncList.size(); i++) {
                    boolean isExists = pendingData.checkRecordExist(table, DbParams.CLM_LOCAL_USER_ID, syncList.get(i).getLocal_user_id());
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getProject_id());
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getServer_task_id());
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getServer_task_user_link_id());
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getServer_checkin_id());
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
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
                    android.util.Log.e(TAG, "isAvailable: " + isExists);
                    if (isExists)
                        pendingData.update(syncList.get(i), table, syncList.get(i).getCompany_id());
                    else
                        pendingData.insert(syncList.get(i), table);
                }
            }
        }
    }
}

