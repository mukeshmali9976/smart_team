package com.techmali.smartteam.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.DateUtils;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class PendingDataImpl {

    private static final String TAG = PendingDataImpl.class.getSimpleName();
    private SQLiteDatabase database;
    private SharedPreferences prefManager = null;
    private Context mContext;

    public PendingDataImpl(Context context) {
        this.mContext = context;
        DbHelper dbHelper = DbHelper.getInstance(mContext);
        database = dbHelper.getWritableDatabase();
        prefManager = CryptoManager.getInstance(context).getPrefs();
    }


    ///////////////////////////////////////////////
    ////////////// DATABASE SYNC PROCESS......
    ///////////////////////////////////////////////

    public long insert(Object object, String table) {
        long id = 0;
//        String local_id = prefManager.getString(PARAMS.KEY_UNIQUE_CODE, "") + "_" + System.currentTimeMillis() + "_" + (new Random().nextInt(9999) + 1001);
        switch (table) {
            case DbParams.TBL_USER_INFO:
                SyncUserInfo userInfo = (SyncUserInfo) object;
                id = database.insert(table, null, this.getUserInfoContentValues(userInfo, userInfo.getServer_user_id(), userInfo.getLocal_user_id()));
                break;
            case DbParams.TBL_PROJECT:
                SyncProject project = (SyncProject) object;
                id = database.insert(table, null, this.getProjectContentValues(project, project.getProject_id(), project.getLocal_project_id()));
                break;
            case DbParams.TBL_PROJECT_USER_LINK:
                SyncProjectUserLink projectUserLink = (SyncProjectUserLink) object;
                id = database.insert(table, null, this.getProjectUserLinkContentValues(projectUserLink, projectUserLink.getProject_user_link_id(), projectUserLink.getLocal_project_user_link_id()));
                break;
            case DbParams.TBL_TASK:
                SyncTask task = (SyncTask) object;
                id = database.insert(table, null, this.getTaskContentValues(task, task.getServer_task_id(), task.getLocal_task_id()));
                break;
            case DbParams.TBL_TASK_TYPE:
                SyncTaskType taskType = (SyncTaskType) object;
                id = database.insert(table, null, this.getTaskTypeContentValues(taskType, taskType.getTask_type_id(), taskType.getTask_type_id()));
                break;
            case DbParams.TBL_TASK_USER_LINK:
                SyncTaskUserLink taskUserLink = (SyncTaskUserLink) object;
                id = database.insert(table, null, this.getTaskUserLinkContentValues(taskUserLink, taskUserLink.getServer_task_user_link_id(), taskUserLink.getLocal_task_user_link_id()));
                break;
            case DbParams.TBL_ATTENDANCE:
                SyncAttendance attendance = (SyncAttendance) object;
                id = database.insert(table, null, this.getAttendanceContentValues(attendance, attendance.getAttendance_id(), attendance.getLocal_attendance_id()));
                break;
            case DbParams.TBL_CHECK_IN:
                SyncCheckIn checkIn = (SyncCheckIn) object;
                id = database.insert(table, null, this.getCheckInContentValues(checkIn, checkIn.getCheckin_id(), checkIn.getLocal_checkin_id()));
                break;
            case DbParams.TBL_EXPENSE:
                SyncExpense expense = (SyncExpense) object;
                id = database.insert(table, null, this.getExpenseContentValues(expense, expense.getExpance_id(), expense.getLocal_expance_id()));
                break;
            case DbParams.TBL_LEAVE:
                SyncLeave leave = (SyncLeave) object;
                id = database.insert(table, null, this.getLeaveContentValues(leave, leave.getLeave_id(), leave.getLocal_leave_id()));
                break;
            case DbParams.TBL_SECURITY_MENU:
                SyncSecurityMenu securityMenu = (SyncSecurityMenu) object;
                id = database.insert(table, null, this.getSecurityMenuContentValues(securityMenu, securityMenu.getSecurity_menu_id(), securityMenu.getSecurity_menu_id()));
                break;
            case DbParams.TBL_SECURITY_MENU_CONTROLLERS_ACTION:
                SyncSecurityMenuControllerAction securityMenuControllerAction = (SyncSecurityMenuControllerAction) object;
                id = database.insert(table, null, this.getSecurityMenuControllerActionContentValues(securityMenuControllerAction, securityMenuControllerAction.getSecurity_menu_controllers_action_id(), securityMenuControllerAction.getSecurity_menu_controllers_action_id()));
                break;
            case DbParams.TBL_SECURITY_MENU_CONTROLLERS_LINK:
                SyncSecurityMenuControllerLink securityMenuControllerLink = (SyncSecurityMenuControllerLink) object;
                id = database.insert(table, null, this.getSecurityMenuControllerLinkContentValues(securityMenuControllerLink, securityMenuControllerLink.getMenu_controllers_link_id(), securityMenuControllerLink.getMenu_controllers_link_id()));
                break;
            case DbParams.TBL_SECURITY_CONTROLLERS:
                SyncSecurityController securityController = (SyncSecurityController) object;
                id = database.insert(table, null, this.getSecurityControllerContentValues(securityController, securityController.getSecurity_controllers_id(), securityController.getSecurity_controllers_id()));
                break;
            case DbParams.TBL_ROLE:
                SyncRole role = (SyncRole) object;
                id = database.insert(table, null, this.getRoleContentValues(role, role.getRole_id(), role.getRole_id()));
                break;
            case DbParams.TBL_COMPANY:
                SyncCompany syncCompany = (SyncCompany) object;
                id = database.insert(table, null, this.getCompanyContentValues(syncCompany, syncCompany.getCompany_id(), syncCompany.getCompany_id()));
                break;
        }
        return id;
    }

    public boolean checkRecordExist(String TableName, String local_id_key, String local_id_value) {
        if (!Utils.isEmptyString(local_id_value)) {
            String query = "SELECT " + local_id_key + " FROM " + TableName + " WHERE " + local_id_key + " = '" + local_id_value + "'";
            Cursor cursor = database.rawQuery(query, null);
            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            return exists;
        } else {
            return false;
        }
    }

    public long update(Object object, String table, String server_id) {
        long id = 0;
        switch (table) {
            case DbParams.TBL_USER_INFO:
                SyncUserInfo userInfo = (SyncUserInfo) object;
                id = database.update(table, this.getUserInfoContentValues(userInfo, server_id, userInfo.getLocal_user_id()),
                        DbParams.CLM_USER_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_PROJECT:
                SyncProject project = (SyncProject) object;
                id = database.update(table, this.getProjectContentValues(project, server_id, project.getLocal_project_id()),
                        DbParams.CLM_SERVER_PROJECT_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_PROJECT_USER_LINK:
                SyncProjectUserLink projectUserLink = (SyncProjectUserLink) object;
                id = database.update(table, this.getProjectUserLinkContentValues(projectUserLink, server_id, projectUserLink.getLocal_project_user_link_id()),
                        DbParams.CLM_SERVER_PROJECT_USER_LINK_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_TASK:
                SyncTask task = (SyncTask) object;
                id = database.update(table, this.getTaskContentValues(task, server_id, task.getLocal_task_id()),
                        DbParams.CLM_SERVER_TASK_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_TASK_TYPE:
                SyncTaskType taskType = (SyncTaskType) object;
                id = database.update(table, this.getTaskTypeContentValues(taskType, server_id, taskType.getTask_type_id()),
                        DbParams.CLM_TASK_TYPE_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_TASK_USER_LINK:
                SyncTaskUserLink taskUserLink = (SyncTaskUserLink) object;
                id = database.update(table, this.getTaskUserLinkContentValues(taskUserLink, server_id, taskUserLink.getLocal_task_user_link_id()),
                        DbParams.CLM_SERVER_TASK_USER_LINK_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_ATTENDANCE:
                SyncAttendance attendance = (SyncAttendance) object;
                id = database.update(table, this.getAttendanceContentValues(attendance, server_id, attendance.getLocal_attendance_id()),
                        DbParams.CLM_ATTENDANCE_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_CHECK_IN:
                SyncCheckIn checkIn = (SyncCheckIn) object;
                id = database.update(table, this.getCheckInContentValues(checkIn, server_id, checkIn.getLocal_checkin_id()),
                        DbParams.CLM_CHECK_IN_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_EXPENSE:
                SyncExpense expense = (SyncExpense) object;
                id = database.update(table, this.getExpenseContentValues(expense, server_id, expense.getLocal_expance_id()),
                        DbParams.CLM_EXPENSE_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_LEAVE:
                SyncLeave leave = (SyncLeave) object;
                id = database.update(table, this.getLeaveContentValues(leave, server_id, leave.getLocal_leave_id()),
                        DbParams.CLM_LEAVE_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_SECURITY_MENU:
                SyncSecurityMenu securityMenu = (SyncSecurityMenu) object;
                id = database.update(table, this.getSecurityMenuContentValues(securityMenu, server_id, securityMenu.getSecurity_menu_id()),
                        DbParams.CLM_SECURITY_MENU_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_SECURITY_MENU_CONTROLLERS_ACTION:
                SyncSecurityMenuControllerAction securityMenuControllerAction = (SyncSecurityMenuControllerAction) object;
                id = database.update(table, this.getSecurityMenuControllerActionContentValues(securityMenuControllerAction, server_id, securityMenuControllerAction.getSecurity_menu_controllers_action_id()),
                        DbParams.CLM_SECURITY_MENU_CONTROLLERS_ACTION_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_SECURITY_MENU_CONTROLLERS_LINK:
                SyncSecurityMenuControllerLink securityMenuControllerLink = (SyncSecurityMenuControllerLink) object;
                id = database.update(table, this.getSecurityMenuControllerLinkContentValues(securityMenuControllerLink, server_id, securityMenuControllerLink.getMenu_controllers_link_id()),
                        DbParams.CLM_MENU_CONTROLLERS_LINK_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_SECURITY_CONTROLLERS:
                SyncSecurityController securityController = (SyncSecurityController) object;
                id = database.update(table, this.getSecurityControllerContentValues(securityController, server_id, securityController.getSecurity_controllers_id()),
                        DbParams.CLM_SECURITY_CONTROLLER_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_ROLE:
                SyncRole role = (SyncRole) object;
                id = database.update(table, this.getRoleContentValues(role, server_id, role.getRole_id()),
                        DbParams.CLM_ROLE_ID + "=?", new String[]{server_id});
                break;
            case DbParams.TBL_COMPANY:
                SyncCompany mDbModel = (SyncCompany) object;
                id = database.update(table, this.getCompanyContentValues(mDbModel, String.valueOf(server_id), mDbModel.getCompany_id()),
                        DbParams.CLM_COMPANY_ID + "=?", new String[]{server_id});
                break;
        }
        return id;
    }

    public String getSyncData() {

        JSONObject reqObject = new JSONObject();
        try {
            String last_sync_date = prefManager.getString(PARAMS.KEY_LAST_SYNC_DATE, "");
            reqObject.put(PARAMS.KEY_LAST_SYNC_DATE, last_sync_date);

            // USERINFO TBL.......
            String whereCondUserinfo = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondUserinfo = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";
//                whereCondUserinfo = " WHERE " + DbParams.CLM_IS_UPDATED + " = 0";

            String userinfo = "SELECT * FROM " + DbParams.TBL_USER_INFO + whereCondUserinfo;
            Cursor userinfoCursor = database.rawQuery(userinfo, null);
            JSONArray userinfoArray = new JSONArray();
            if (userinfoCursor.getCount() > 0) {
                userinfoCursor.moveToFirst();
                do {
                    JSONObject userinfoObject = new JSONObject();
                    userinfoObject.put(DbParams.CLM_SERVER_USER_ID, Utils.createBlank(userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_SERVER_USER_ID))));
                    userinfoObject.put(DbParams.CLM_LOCAL_USER_ID, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));
                    userinfoObject.put(DbParams.CLM_USERNAME, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_USERNAME)));
                    userinfoObject.put(DbParams.CLM_FIRST_NAME, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_FIRST_NAME)));
                    userinfoObject.put(DbParams.CLM_LAST_NAME, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_LAST_NAME)));
                    userinfoObject.put(DbParams.CLM_PHONE_NO, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_PHONE_NO)));
                    userinfoObject.put(DbParams.CLM_EMAIL, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_EMAIL)));
                    userinfoObject.put(DbParams.CLM_GENDER, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_GENDER)));
                    userinfoObject.put(DbParams.CLM_COMPANY_ID, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_COMPANY_ID)));
                    userinfoObject.put(DbParams.CLM_ROLE_ID, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_ROLE_ID)));
                    userinfoObject.put(DbParams.CLM_HOME_ADDRESS, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_HOME_ADDRESS)));
                    userinfoObject.put(DbParams.CLM_WORK_ADDRESS, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_WORK_ADDRESS)));
                    userinfoObject.put(DbParams.CLM_IS_SMARTPHONE, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_IS_SMARTPHONE)));
                    userinfoObject.put(DbParams.CLM_STATUS_ID, userinfoCursor.getInt(userinfoCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));

                    userinfoArray.put(userinfoObject);
                } while (userinfoCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_USER_INFO, userinfoArray);

            // PROJECT TBL........
            String whereCondProject = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondProject = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";

            String project = "SELECT * FROM " + DbParams.TBL_PROJECT + whereCondProject;
            Cursor projectCursor = database.rawQuery(project, null);
            JSONArray projectArray = new JSONArray();
            if (projectCursor.getCount() > 0) {
                projectCursor.moveToFirst();
                do {

                    JSONObject projectObject = new JSONObject();
                    projectObject.put(DbParams.CLM_SERVER_PROJECT_ID, Utils.createBlank(projectCursor.getString(projectCursor.getColumnIndex(DbParams.CLM_SERVER_PROJECT_ID))));
                    projectObject.put(DbParams.CLM_LOCAL_PROJECT_ID, projectCursor.getString(projectCursor.getColumnIndex(DbParams.CLM_LOCAL_PROJECT_ID)));
                    projectObject.put(DbParams.CLM_COMPANY_ID, projectCursor.getString(projectCursor.getColumnIndex(DbParams.CLM_COMPANY_ID)));
                    projectObject.put(DbParams.CLM_TITLE, projectCursor.getString(projectCursor.getColumnIndex(DbParams.CLM_TITLE)));
                    projectObject.put(DbParams.CLM_DESCRIPTION, projectCursor.getString(projectCursor.getColumnIndex(DbParams.CLM_DESCRIPTION)));
                    projectObject.put(DbParams.CLM_START_DATE, projectCursor.getString(projectCursor.getColumnIndex(DbParams.CLM_START_DATE)));
                    projectObject.put(DbParams.CLM_END_DATE, projectCursor.getString(projectCursor.getColumnIndex(DbParams.CLM_END_DATE)));
                    projectObject.put(DbParams.CLM_STATUS_ID, projectCursor.getInt(projectCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));
                    projectObject.put(DbParams.CLM_IS_DELETE, projectCursor.getInt(projectCursor.getColumnIndex(DbParams.CLM_IS_DELETE)));

                    projectArray.put(projectObject);
                } while (projectCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_PROJECT, projectArray);

            // TASK TBL........
            String whereCondTask = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondTask = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";

            String task = "SELECT * FROM " + DbParams.TBL_TASK + whereCondTask;
            Cursor taskCursor = database.rawQuery(task, null);
            JSONArray taskArray = new JSONArray();
            if (taskCursor.getCount() > 0) {
                taskCursor.moveToFirst();
                do {

                    JSONObject taskObject = new JSONObject();
                    taskObject.put(DbParams.CLM_SERVER_TASK_ID, Utils.createBlank(taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_SERVER_TASK_ID))));
                    taskObject.put(DbParams.CLM_LOCAL_TASK_ID, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_LOCAL_TASK_ID)));
                    taskObject.put(DbParams.CLM_SERVER_PROJECT_ID, Utils.createBlank(taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_SERVER_PROJECT_ID))));
                    taskObject.put(DbParams.CLM_LOCAL_PROJECT_ID, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_LOCAL_PROJECT_ID)));
                    taskObject.put(DbParams.CLM_COMPANY_ID, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_COMPANY_ID)));
                    taskObject.put(DbParams.CLM_TITLE, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_TITLE)));
                    taskObject.put(DbParams.CLM_DESCRIPTION, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_DESCRIPTION)));
                    taskObject.put(DbParams.CLM_START_DATE, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_START_DATE)));
                    taskObject.put(DbParams.CLM_END_DATE, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_END_DATE)));
                    taskObject.put(DbParams.CLM_STATUS_ID, taskCursor.getInt(taskCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));

                    taskArray.put(taskObject);
                } while (taskCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_TASK, taskArray);

            // PROJECT_USER_LINK TBL........
            String whereCondProjectUserLink = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondProjectUserLink = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";

            String projectUserLink = "SELECT * FROM " + DbParams.TBL_PROJECT_USER_LINK + whereCondProjectUserLink;
            Cursor projectUserLinkCursor = database.rawQuery(projectUserLink, null);
            JSONArray projectUserLinkArray = new JSONArray();
            if (projectUserLinkCursor.getCount() > 0) {
                projectUserLinkCursor.moveToFirst();
                do {

                    JSONObject projectUserLinkObject = new JSONObject();
                    projectUserLinkObject.put(DbParams.CLM_SERVER_PROJECT_USER_LINK_ID, Utils.createBlank(projectUserLinkCursor.getString(projectUserLinkCursor.getColumnIndex(DbParams.CLM_SERVER_PROJECT_USER_LINK_ID))));
                    projectUserLinkObject.put(DbParams.CLM_LOCAL_PROJECT_USER_LINK_ID, projectUserLinkCursor.getString(projectUserLinkCursor.getColumnIndex(DbParams.CLM_LOCAL_PROJECT_USER_LINK_ID)));
                    projectUserLinkObject.put(DbParams.CLM_SERVER_PROJECT_ID, Utils.createBlank(projectUserLinkCursor.getString(projectUserLinkCursor.getColumnIndex(DbParams.CLM_SERVER_PROJECT_ID))));
                    projectUserLinkObject.put(DbParams.CLM_LOCAL_PROJECT_ID, projectUserLinkCursor.getString(projectUserLinkCursor.getColumnIndex(DbParams.CLM_LOCAL_PROJECT_ID)));
                    projectUserLinkObject.put(DbParams.CLM_SERVER_USER_ID, Utils.createBlank(projectUserLinkCursor.getString(projectUserLinkCursor.getColumnIndex(DbParams.CLM_USER_ID))));
                    projectUserLinkObject.put(DbParams.CLM_LOCAL_USER_ID, projectUserLinkCursor.getString(projectUserLinkCursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));

                    projectUserLinkArray.put(projectUserLinkObject);
                } while (projectUserLinkCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_PROJECT_USER_LINK, projectUserLinkArray);

            // TASK_USER_LINK TBL........
            String whereCondTaskUserLink = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondTaskUserLink = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";

            String taskUserLink = "SELECT * FROM " + DbParams.TBL_TASK_USER_LINK + whereCondTaskUserLink;
            Cursor taskUserLinkCursor = database.rawQuery(taskUserLink, null);
            JSONArray taskUserLinkArray = new JSONArray();
            if (taskUserLinkCursor.getCount() > 0) {
                taskUserLinkCursor.moveToFirst();
                do {

                    JSONObject taskUserLinkObject = new JSONObject();
                    taskUserLinkObject.put(DbParams.CLM_SERVER_TASK_USER_LINK_ID, Utils.createBlank(taskUserLinkCursor.getString(taskUserLinkCursor.getColumnIndex(DbParams.CLM_SERVER_TASK_USER_LINK_ID))));
                    taskUserLinkObject.put(DbParams.CLM_LOCAL_TASK_USER_LINK_ID, taskUserLinkCursor.getString(taskUserLinkCursor.getColumnIndex(DbParams.CLM_LOCAL_TASK_USER_LINK_ID)));
                    taskUserLinkObject.put(DbParams.CLM_SERVER_TASK_ID, Utils.createBlank(taskUserLinkCursor.getString(taskUserLinkCursor.getColumnIndex(DbParams.CLM_SERVER_TASK_ID))));
                    taskUserLinkObject.put(DbParams.CLM_LOCAL_TASK_ID, taskUserLinkCursor.getString(taskUserLinkCursor.getColumnIndex(DbParams.CLM_LOCAL_TASK_ID)));
                    taskUserLinkObject.put(DbParams.CLM_SERVER_USER_ID, Utils.createBlank(taskUserLinkCursor.getString(taskUserLinkCursor.getColumnIndex(DbParams.CLM_USER_ID))));
                    taskUserLinkObject.put(DbParams.CLM_LOCAL_USER_ID, taskUserLinkCursor.getString(taskUserLinkCursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));

                    taskUserLinkArray.put(taskUserLinkObject);
                } while (taskUserLinkCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_TASK_USER_LINK, taskUserLinkArray);

            // TASK TBL........
            String whereCondAttendance = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondAttendance = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";

            String attendance = "SELECT * FROM " + DbParams.TBL_ATTENDANCE + whereCondAttendance;
            Cursor attendanceCursor = database.rawQuery(attendance, null);
            JSONArray attendanceArray = new JSONArray();
            if (attendanceCursor.getCount() > 0) {
                attendanceCursor.moveToFirst();
                do {

                    JSONObject attendanceObject = new JSONObject();
                    attendanceObject.put(DbParams.CLM_ATTENDANCE_ID, attendanceCursor.getString(attendanceCursor.getColumnIndex(DbParams.CLM_ATTENDANCE_ID)));
                    attendanceObject.put(DbParams.CLM_LOCAL_ATTENDANCE_ID, attendanceCursor.getString(attendanceCursor.getColumnIndex(DbParams.CLM_LOCAL_ATTENDANCE_ID)));
                    attendanceObject.put(DbParams.CLM_USER_ID, attendanceCursor.getString(attendanceCursor.getColumnIndex(DbParams.CLM_USER_ID)));
                    attendanceObject.put(DbParams.CLM_LOCAL_USER_ID, attendanceCursor.getString(attendanceCursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));
                    attendanceObject.put(DbParams.CLM_START_DATE, attendanceCursor.getString(attendanceCursor.getColumnIndex(DbParams.CLM_START_DATE)));
                    attendanceObject.put(DbParams.CLM_END_DATE, attendanceCursor.getString(attendanceCursor.getColumnIndex(DbParams.CLM_END_DATE)));
                    attendanceObject.put(DbParams.CLM_COMPANY_ID, attendanceCursor.getString(attendanceCursor.getColumnIndex(DbParams.CLM_COMPANY_ID)));
                    attendanceObject.put(DbParams.CLM_ATTENDANCE_TYPE, attendanceCursor.getInt(attendanceCursor.getColumnIndex(DbParams.CLM_ATTENDANCE_TYPE)));
                    attendanceObject.put(DbParams.CLM_STATUS_ID, attendanceCursor.getInt(attendanceCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));

                    attendanceArray.put(attendanceObject);
                } while (attendanceCursor.moveToNext());
            }
//            reqObject.put(DbParams.TBL_ATTENDANCE, attendanceArray);

            // CHECK_IN TBL........
            String whereCondCheckIn = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondCheckIn = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";

            String checkIn = "SELECT * FROM " + DbParams.TBL_CHECK_IN + whereCondCheckIn;
            Cursor checkInCursor = database.rawQuery(checkIn, null);
            JSONArray checkInArray = new JSONArray();
            if (checkInCursor.getCount() > 0) {
                checkInCursor.moveToFirst();
                do {

                    JSONObject checkInObject = new JSONObject();
                    checkInObject.put(DbParams.CLM_CHECK_IN_ID, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_CHECK_IN_ID)));
                    checkInObject.put(DbParams.CLM_LOCAL_CHECK_IN_ID, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_LOCAL_CHECK_IN_ID)));
                    checkInObject.put(DbParams.CLM_ATTENDANCE_ID, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_ATTENDANCE_ID)));
                    checkInObject.put(DbParams.CLM_LOCAL_ATTENDANCE_ID, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_LOCAL_ATTENDANCE_ID)));
                    checkInObject.put(DbParams.CLM_USER_ID, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_USER_ID)));
                    checkInObject.put(DbParams.CLM_LOCAL_USER_ID, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));
                    checkInObject.put(DbParams.CLM_COMPANY_ID, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_COMPANY_ID)));
                    checkInObject.put(DbParams.CLM_CHECK_IN_TIME, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_CHECK_IN_TIME)));
                    checkInObject.put(DbParams.CLM_LAT, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_LAT)));
                    checkInObject.put(DbParams.CLM_LONG, checkInCursor.getString(checkInCursor.getColumnIndex(DbParams.CLM_LONG)));
                    checkInObject.put(DbParams.CLM_STATUS_ID, checkInCursor.getInt(checkInCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));

                    checkInArray.put(checkInObject);
                } while (checkInCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_CHECK_IN, checkInArray);

            // EXPENSE TBL........
            String whereCondExpense = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondExpense = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";

            String expense = "SELECT * FROM " + DbParams.TBL_EXPENSE + whereCondExpense;
            Cursor expenseCursor = database.rawQuery(expense, null);
            JSONArray expenseArray = new JSONArray();
            if (expenseCursor.getCount() > 0) {
                expenseCursor.moveToFirst();
                do {

                    JSONObject expenseObject = new JSONObject();
                    expenseObject.put(DbParams.CLM_EXPENSE_ID, Utils.createBlank(expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_EXPENSE_ID))));
                    expenseObject.put(DbParams.CLM_LOCAL_EXPENSE_ID, expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_LOCAL_EXPENSE_ID)));
                    expenseObject.put(DbParams.CLM_COMPANY_ID, expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_COMPANY_ID)));
                    expenseObject.put(DbParams.CLM_USER_ID, Utils.createBlank(expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_USER_ID))));
                    expenseObject.put(DbParams.CLM_LOCAL_USER_ID, expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));
                    expenseObject.put(DbParams.CLM_SERVER_TASK_ID, Utils.createBlank(expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_SERVER_TASK_ID))));
                    expenseObject.put(DbParams.CLM_LOCAL_TASK_ID, expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_LOCAL_TASK_ID)));
                    expenseObject.put(DbParams.CLM_PROJECT_ID, Utils.createBlank(expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_PROJECT_ID))));
                    expenseObject.put(DbParams.CLM_LOCAL_PROJECT_ID, expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_LOCAL_PROJECT_ID)));
                    expenseObject.put(DbParams.CLM_TITLE, expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_TITLE)));
                    expenseObject.put(DbParams.CLM_DESCRIPTION, expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_DESCRIPTION)));
                    expenseObject.put(DbParams.CLM_AMOUNT, expenseCursor.getString(expenseCursor.getColumnIndex(DbParams.CLM_AMOUNT)));
                    expenseObject.put(DbParams.CLM_PAYOUT_STATUS, expenseCursor.getInt(expenseCursor.getColumnIndex(DbParams.CLM_PAYOUT_STATUS)));
                    expenseObject.put(DbParams.CLM_STATUS_ID, expenseCursor.getInt(expenseCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));

                    expenseArray.put(expenseObject);
                } while (expenseCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_EXPENSE, expenseArray);

            // LEAVE TBL........
            String whereCondLeave = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCondLeave = " WHERE " + DbParams.CLM_UPDATED_ON + " >= DATETIME('" + last_sync_date + "')";

            String leave = "SELECT * FROM " + DbParams.TBL_LEAVE + whereCondLeave;
            Cursor leaveCursor = database.rawQuery(leave, null);
            JSONArray leaveArray = new JSONArray();
            if (leaveCursor.getCount() > 0) {
                leaveCursor.moveToFirst();
                do {

                    JSONObject leaveObject = new JSONObject();
                    leaveObject.put(DbParams.CLM_LEAVE_ID, Utils.createBlank(leaveCursor.getString(leaveCursor.getColumnIndex(DbParams.CLM_LEAVE_ID))));
                    leaveObject.put(DbParams.CLM_LOCAL_LEAVE_ID, leaveCursor.getString(leaveCursor.getColumnIndex(DbParams.CLM_LOCAL_LEAVE_ID)));
                    leaveObject.put(DbParams.CLM_COMPANY_ID, leaveCursor.getString(leaveCursor.getColumnIndex(DbParams.CLM_COMPANY_ID)));
                    leaveObject.put(DbParams.CLM_USER_ID, Utils.createBlank(leaveCursor.getString(leaveCursor.getColumnIndex(DbParams.CLM_USER_ID))));
                    leaveObject.put(DbParams.CLM_LOCAL_USER_ID, leaveCursor.getString(leaveCursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));
                    leaveObject.put(DbParams.CLM_START_DATE, leaveCursor.getString(leaveCursor.getColumnIndex(DbParams.CLM_START_DATE)));
                    leaveObject.put(DbParams.CLM_END_DATE, leaveCursor.getString(leaveCursor.getColumnIndex(DbParams.CLM_END_DATE)));
                    leaveObject.put(DbParams.CLM_NOTE, leaveCursor.getString(leaveCursor.getColumnIndex(DbParams.CLM_NOTE)));
                    leaveObject.put(DbParams.CLM_IS_DELETE, leaveCursor.getInt(leaveCursor.getColumnIndex(DbParams.CLM_IS_DELETE)));
                    leaveObject.put(DbParams.CLM_STATUS_ID, leaveCursor.getInt(leaveCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));

                    leaveArray.put(leaveObject);
                } while (leaveCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_LEAVE, leaveArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reqObject.toString();
    }

    private ContentValues getCompanyContentValues(SyncCompany model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_COMPANY_ID, id);
        values.put(DbParams.CLM_COMPANY_NAME, model.getCompany_name());
        values.put(DbParams.CLM_REG_NO, model.getReg_no());
        values.put(DbParams.CLM_THUMB, model.getThumb());
        values.put(DbParams.CLM_CONTACT_PERSON, model.getContact_person());
        values.put(DbParams.CLM_PHONE_NO, model.getPhone_no());
        values.put(DbParams.CLM_EMAIL, model.getEmail());
        values.put(DbParams.CLM_ADMIN_USER_ID, model.getAdmin_user_id());
        values.put(DbParams.CLM_OBJECT_MESSAGE, model.getObject_message());
        values.put(DbParams.CLM_STATUS_ID, model.getStatus_id());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        return values;
    }

    private ContentValues getUserInfoContentValues(SyncUserInfo model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_SERVER_USER_ID, id);
        values.put(DbParams.CLM_LOCAL_USER_ID, local_id);
        values.put(DbParams.CLM_USERNAME, model.getUsername());
        values.put(DbParams.CLM_EMAIL, model.getEmail());
        values.put(DbParams.CLM_FIRST_NAME, model.getFirst_name());
        values.put(DbParams.CLM_LAST_NAME, model.getLast_name());
        values.put(DbParams.CLM_PHONE_NO, model.getPhone_no());
        values.put(DbParams.CLM_GENDER, model.getGender());
        values.put(DbParams.CLM_THUMB, model.getThumb());
        values.put(DbParams.CLM_COMPANY_ID, model.getCompany_id());
        values.put(DbParams.CLM_ROLE_ID, model.getRole_id());
        values.put(DbParams.CLM_HOME_ADDRESS, model.getHome_address());
        values.put(DbParams.CLM_WORK_ADDRESS, model.getWork_address());
        values.put(DbParams.CLM_STATUS_ID, model.getStatus_id());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getProjectContentValues(SyncProject model, String id, String local_id) {
        ContentValues values = new ContentValues();
        values.put(DbParams.CLM_LOCAL_PROJECT_ID, local_id);
        values.put(DbParams.CLM_SERVER_PROJECT_ID, id);
        values.put(DbParams.CLM_COMPANY_ID, model.getCompany_id());
        values.put(DbParams.CLM_TITLE, model.getTitle());
        values.put(DbParams.CLM_DESCRIPTION, model.getDescription());
        values.put(DbParams.CLM_THUMB, model.getThumb());
        values.put(DbParams.CLM_START_DATE, model.getStart_date());
        values.put(DbParams.CLM_END_DATE, model.getEnd_date());
        values.put(DbParams.CLM_STATUS_ID, model.getStatus_id());
        values.put(DbParams.CLM_IS_DELETE, model.getIs_delete());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getProjectUserLinkContentValues(SyncProjectUserLink model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_LOCAL_PROJECT_USER_LINK_ID, local_id);
        values.put(DbParams.CLM_SERVER_PROJECT_USER_LINK_ID, id);
        values.put(DbParams.CLM_SERVER_PROJECT_ID, model.getServer_Project_id());
        values.put(DbParams.CLM_LOCAL_PROJECT_ID, model.getLocal_project_id());
        values.put(DbParams.CLM_LOCAL_USER_ID, model.getLocal_user_id());
        values.put(DbParams.CLM_USER_ID, model.getUser_id());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getTaskContentValues(SyncTask model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_LOCAL_TASK_ID, local_id);
        values.put(DbParams.CLM_SERVER_TASK_ID, id);
        values.put(DbParams.CLM_COMPANY_ID, model.getCompany_id());
        values.put(DbParams.CLM_SERVER_PROJECT_ID, model.getProject_id());
        values.put(DbParams.CLM_TITLE, model.getTitle());
        values.put(DbParams.CLM_DESCRIPTION, model.getDescription());
        values.put(DbParams.CLM_THUMB, model.getThumb());
        values.put(DbParams.CLM_START_DATE, model.getStart_date());
        values.put(DbParams.CLM_END_DATE, model.getEnd_date());
        values.put(DbParams.CLM_TYPE, model.getType());
        values.put(DbParams.CLM_STATUS_ID, model.getStatus_id());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getTaskTypeContentValues(SyncTaskType model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_TASK_TYPE_ID, id);
        values.put(DbParams.CLM_TASK_TYPE_NAME, model.getType_name());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getTaskUserLinkContentValues(SyncTaskUserLink model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_LOCAL_TASK_USER_LINK_ID, local_id);
        values.put(DbParams.CLM_SERVER_TASK_USER_LINK_ID, id);
        values.put(DbParams.CLM_SERVER_TASK_ID, model.getServer_task_id());
        values.put(DbParams.CLM_LOCAL_TASK_ID, model.getLocal_task_id());
        values.put(DbParams.CLM_USER_ID, model.getUser_id());
        values.put(DbParams.CLM_LOCAL_USER_ID, model.getLocal_user_id());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getAttendanceContentValues(SyncAttendance model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_LOCAL_ATTENDANCE_ID, local_id);
        values.put(DbParams.CLM_ATTENDANCE_ID, id);
        values.put(DbParams.CLM_COMPANY_ID, model.getCompany_id());
        values.put(DbParams.CLM_USER_ID, model.getUser_id());
        values.put(DbParams.CLM_LOCAL_USER_ID, model.getLocal_user_id());
        values.put(DbParams.CLM_START_DATE, model.getStart_date());
        values.put(DbParams.CLM_END_DATE, model.getEnd_date());
        values.put(DbParams.CLM_ATTENDANCE_TYPE, model.getAttandance_type());
        values.put(DbParams.CLM_STATUS_ID, model.getStatus_id());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getCheckInContentValues(SyncCheckIn model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_CHECK_IN_ID, id);
        values.put(DbParams.CLM_LOCAL_CHECK_IN_ID, local_id);
        values.put(DbParams.CLM_COMPANY_ID, model.getCompany_id());
        values.put(DbParams.CLM_ATTENDANCE_ID, model.getAttendance_id());
        values.put(DbParams.CLM_LOCAL_ATTENDANCE_ID, model.getLocal_attendance_id());
        values.put(DbParams.CLM_USER_ID, model.getUser_id());
        values.put(DbParams.CLM_LOCAL_USER_ID, model.getLocal_user_id());
        values.put(DbParams.CLM_CHECK_IN_TIME, model.getCheckin_time());
        values.put(DbParams.CLM_LAT, model.getLat());
        values.put(DbParams.CLM_LONG, model.getLng());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getExpenseContentValues(SyncExpense model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_LOCAL_EXPENSE_ID, local_id);
        values.put(DbParams.CLM_EXPENSE_ID, id);
        values.put(DbParams.CLM_COMPANY_ID, model.getCompany_id());
        values.put(DbParams.CLM_LOCAL_USER_ID, model.getLocal_user_id());
        values.put(DbParams.CLM_USER_ID, model.getUser_id());
        values.put(DbParams.CLM_SERVER_TASK_ID, model.getTask_id());
        values.put(DbParams.CLM_LOCAL_TASK_ID, model.getLocal_task_id());
        values.put(DbParams.CLM_PROJECT_ID, model.getProject_id());
        values.put(DbParams.CLM_LOCAL_PROJECT_ID, model.getLocal_project_id());
        values.put(DbParams.CLM_TITLE, model.getTitle());
        values.put(DbParams.CLM_DESCRIPTION, model.getDescription());
        values.put(DbParams.CLM_AMOUNT, model.getAmount());
        values.put(DbParams.CLM_PAYOUT_STATUS, model.getPayout_status());
        values.put(DbParams.CLM_STATUS_ID, model.getStatus_id());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getLeaveContentValues(SyncLeave model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_LOCAL_LEAVE_ID, local_id);
        values.put(DbParams.CLM_LEAVE_ID, id);
        values.put(DbParams.CLM_COMPANY_ID, model.getCompany_id());
        values.put(DbParams.CLM_USER_ID, model.getUser_id());
        values.put(DbParams.CLM_LOCAL_USER_ID, model.getLocal_user_id());
        values.put(DbParams.CLM_START_DATE, model.getStart_date());
        values.put(DbParams.CLM_END_DATE, model.getEnd_date());
        values.put(DbParams.CLM_NOTE, model.getNote());
        values.put(DbParams.CLM_STATUS_ID, model.getStatus_id());
        values.put(DbParams.CLM_IS_DELETE, model.getIs_delete());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getSecurityMenuContentValues(SyncSecurityMenu model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_SECURITY_MENU_ID, id);
        values.put(DbParams.CLM_MENU_NAME, model.getMenu_name());
        values.put(DbParams.CLM_MENU_DISPLAY_NAME, model.getMenu_display_name());
        values.put(DbParams.CLM_MENU_LABEL, model.getMenu_label());
        values.put(DbParams.CLM_URL, model.getUrl());
        values.put(DbParams.CLM_URL_PARAMETER, model.getUrl_parameter());
        values.put(DbParams.CLM_LINK_OPTIONS, model.getLink_options());
        values.put(DbParams.CLM_ICON, model.getIcon());
        values.put(DbParams.CLM_FOR, "for");
        values.put(DbParams.CLM_SORT_ORDER, model.getSort_order());
        values.put(DbParams.CLM_IS_ALWAYS_TRUE, model.getIs_always_true());
        values.put(DbParams.CLM_STATUS, model.getStatus());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getSecurityMenuControllerActionContentValues(SyncSecurityMenuControllerAction model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_SECURITY_MENU_CONTROLLERS_ACTION_ID, local_id);
        values.put(DbParams.CLM_MENU_ID, model.getMenu_id());
        values.put(DbParams.CLM_CONTROLLER_ID, model.getControllers_id());
        values.put(DbParams.CLM_MENU_CONTROLLERS_LINK_ID, model.getMenu_controllers_link_id());
        values.put(DbParams.CLM_ACTION_NAME, model.getAction_name());
        values.put(DbParams.CLM_ACTION_DISPLAY_NAME, model.getAction_display_name());
        values.put(DbParams.CLM_SORT_ORDER, model.getSort_order());
        values.put(DbParams.CLM_STATUS, model.getStatus());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getSecurityMenuControllerLinkContentValues(SyncSecurityMenuControllerLink model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_MENU_CONTROLLERS_LINK_ID, local_id);
        values.put(DbParams.CLM_MENU_ID, model.getMenu_id());
        values.put(DbParams.CLM_CONTROLLER_ID, model.getController_id());
        values.put(DbParams.CLM_STATUS, model.getStatus());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getSecurityControllerContentValues(SyncSecurityController model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_SECURITY_CONTROLLER_ID, local_id);
        values.put(DbParams.CLM_NAME, model.getName());
        values.put(DbParams.CLM_DISPLAY_NAME, model.getDisplay_name());
        values.put(DbParams.CLM_CONTROLLER_NAME, model.getController_name());
        values.put(DbParams.CLM_SORT_ORDER, model.getSort_order());
        values.put(DbParams.CLM_STATUS, model.getStatus());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }

    private ContentValues getRoleContentValues(SyncRole model, String id, String local_id) {
        ContentValues values = new ContentValues();

        values.put(DbParams.CLM_ROLE_ID, model.getRole_id());
        values.put(DbParams.CLM_NAME, model.getName());
        values.put(DbParams.CLM_SORT_ORDER, model.getSort_order());
        values.put(DbParams.CLM_ROLE_GROUP, model.getRole_group());
        values.put(DbParams.CLM_IS_CUSTOM, model.getIs_custom());
        values.put(DbParams.CLM_STATUS, model.getStatus());
        values.put(DbParams.CLM_CREATED_ON, model.getCreated_on());
        values.put(DbParams.CLM_UPDATED_ON, model.getUpdated_on());
        values.put(DbParams.CLM_CREATED_BY, model.getCreated_by());
        values.put(DbParams.CLM_UPDATED_BY, model.getUpdated_by());
        values.put(DbParams.CLM_IS_UPDATED, 1);
        return values;
    }


    ////////////////////////////////////////////////////////
    //////////////////// DATABASE OPERATIONS..........
    ////////////////////////////////////////////////////////

    public String getProjectList() {

        JSONObject response = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            String query = "SELECT * FROM " + DbParams.TBL_PROJECT + " WHERE " + DbParams.CLM_STATUS_ID + "=1";
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    JSONObject project = new JSONObject();
                    project.put(DbParams.CLM_SERVER_PROJECT_ID, cursor.getString(cursor.getColumnIndex(DbParams.CLM_SERVER_PROJECT_ID)));
                    project.put(DbParams.CLM_LOCAL_PROJECT_ID, cursor.getString(cursor.getColumnIndex(DbParams.CLM_LOCAL_PROJECT_ID)));
                    project.put(DbParams.CLM_TITLE, cursor.getString(cursor.getColumnIndex(DbParams.CLM_TITLE)));
                    project.put(DbParams.CLM_START_DATE, cursor.getString(cursor.getColumnIndex(DbParams.CLM_START_DATE)));
                    project.put(DbParams.CLM_END_DATE, cursor.getString(cursor.getColumnIndex(DbParams.CLM_END_DATE)));
                    project.put(DbParams.CLM_THUMB, cursor.getString(cursor.getColumnIndex(DbParams.CLM_THUMB)));
                    project.put(DbParams.CLM_DESCRIPTION, cursor.getString(cursor.getColumnIndex(DbParams.CLM_DESCRIPTION)));

                    array.put(project);
                } while (cursor.moveToNext());
            }
            cursor.close();
            response.put(PARAMS.TAG_STATUS, array.length() > 0 ? PARAMS.TAG_STATUS_200 : PARAMS.TAG_STATUS_4004);
            response.put(PARAMS.TAG_RESULT, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public String getUserList(String project_id) {

        JSONObject response = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            String query = "";

            if (Utils.isEmptyString(project_id))
                query = "SELECT * FROM " + DbParams.TBL_USER_INFO + " WHERE " + DbParams.CLM_STATUS_ID + "=1";
            else
                query = "SELECT user.*, count(p_link." + DbParams.CLM_LOCAL_PROJECT_ID + ") As is_selected from " + DbParams.TBL_USER_INFO + " As user " +
                        "LEFT JOIN " + DbParams.TBL_PROJECT_USER_LINK + " As p_link ON p_link." + DbParams.CLM_LOCAL_USER_ID + "=user." + DbParams.CLM_LOCAL_USER_ID +
                        " and p_link." + DbParams.CLM_LOCAL_PROJECT_ID + "='" + project_id + "' WHERE user." + DbParams.CLM_STATUS_ID +
                        "=1 Group by user." + DbParams.CLM_LOCAL_USER_ID + " having is_selected > 0";

            Log.e(TAG, query);
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    JSONObject project = new JSONObject();
                    project.put(DbParams.CLM_LOCAL_USER_ID, cursor.getString(cursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));
                    project.put(DbParams.CLM_SERVER_USER_ID, cursor.getString(cursor.getColumnIndex(DbParams.CLM_SERVER_USER_ID)));
                    project.put(DbParams.CLM_FIRST_NAME, cursor.getString(cursor.getColumnIndex(DbParams.CLM_FIRST_NAME)));
                    project.put(DbParams.CLM_LAST_NAME, cursor.getString(cursor.getColumnIndex(DbParams.CLM_LAST_NAME)));
                    project.put(DbParams.CLM_THUMB, cursor.getString(cursor.getColumnIndex(DbParams.CLM_THUMB)));
                    project.put(DbParams.CLM_PHONE_NO, cursor.getString(cursor.getColumnIndex(DbParams.CLM_PHONE_NO)) + "");

                    array.put(project);
                } while (cursor.moveToNext());
            }
            cursor.close();
            response.put(PARAMS.TAG_STATUS, array.length() > 0 ? PARAMS.TAG_STATUS_200 : PARAMS.TAG_STATUS_4004);
            response.put(PARAMS.TAG_RESULT, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public boolean createProject(String p_name, String start_date, String end_date, String description, ArrayList<SyncUserInfo> member_id, ArrayList<SyncUserInfo> selected_id,
                                 boolean isExists, String local_project_id, String project_id) {
        boolean isCreated = false;
        try {
            String id = prefManager.getString(PARAMS.KEY_UNIQUE_CODE, "") + "_" + System.currentTimeMillis();
            String query = "";
            if (!isExists) {
                query = "INSERT INTO " + DbParams.TBL_PROJECT + " (" + DbParams.CLM_LOCAL_PROJECT_ID + "," + DbParams.CLM_SERVER_PROJECT_ID + "," + DbParams.CLM_COMPANY_ID + "," +
                        DbParams.CLM_TITLE + "," + DbParams.CLM_START_DATE + "," + DbParams.CLM_END_DATE + "," + DbParams.CLM_DESCRIPTION + "," + DbParams.CLM_CREATED_BY + "," +
                        DbParams.CLM_UPDATED_BY + "," + DbParams.CLM_UPDATED_ON + ") VALUES ('" + id + "','','" + prefManager.getString(PARAMS.KEY_COMPANY_ID, "") +
                        "','" + p_name + "','" + start_date + "','" + end_date + "'," + DatabaseUtils.sqlEscapeString(description) + ",'" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") +
                        "','" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") + "','" + DateUtils.currentUTCDateTime() + "')";
                local_project_id = id;
            } else {
                query = "UPDATE " + DbParams.TBL_PROJECT + " SET " + DbParams.CLM_TITLE + "='" + p_name + "', " + DbParams.CLM_START_DATE + "='" + start_date + "', " + DbParams.CLM_END_DATE +
                        "='" + end_date + "', " + DbParams.CLM_DESCRIPTION + "=" + DatabaseUtils.sqlEscapeString(description) + ", " + DbParams.CLM_UPDATED_ON + "='" + DateUtils.currentUTCDateTime() +
                        "' WHERE " + DbParams.CLM_LOCAL_PROJECT_ID + "='" + local_project_id + "'";
            }
            Log.e(TAG, query);
            database.execSQL(query);

            for (int i = 0; i < member_id.size(); i++) {

                String query1 = "UPDATE " + DbParams.TBL_PROJECT_USER_LINK + " SET " + DbParams.CLM_STATUS_ID + "=3, " + DbParams.CLM_UPDATED_ON + "='" + DateUtils.currentUTCDateTime() +
                        "',  " + DbParams.CLM_UPDATED_BY + "='" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") + "' WHERE " + DbParams.CLM_LOCAL_PROJECT_ID + "='" +
                        local_project_id + "' AND " + DbParams.CLM_LOCAL_USER_ID + " LIKE '" + member_id.get(i).getLocal_user_id() + "'";
                Log.e(TAG, query1);
                database.execSQL(query1);

                String query2 = "UPDATE " + DbParams.TBL_TASK_USER_LINK + " SET " + DbParams.CLM_STATUS_ID + "=3, " + DbParams.CLM_UPDATED_ON + "='" + DateUtils.currentUTCDateTime() +
                        "',  " + DbParams.CLM_UPDATED_BY + "='" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") + "' WHERE " + DbParams.CLM_LOCAL_TASK_ID +
                        " IN (SELECT " + DbParams.CLM_LOCAL_TASK_ID + " FROM " + DbParams.TBL_TASK + " WHERE " + DbParams.CLM_LOCAL_PROJECT_ID + "='" + local_project_id +
                        "') AND " + DbParams.CLM_LOCAL_USER_ID + " LIKE '" + member_id.get(i).getLocal_user_id() + "' AND " + DbParams.CLM_STATUS_ID + "!=3";
                Log.e(TAG, query2);
                database.execSQL(query2);

                String query3 = "SELECT GROUP_CONCAT(" + DbParams.CLM_LOCAL_USER_ID + ") AS v_user_id_added FROM " + DbParams.TBL_PROJECT_USER_LINK +
                        " WHERE " + DbParams.CLM_LOCAL_PROJECT_ID + "='" + local_project_id + "' AND " + DbParams.CLM_STATUS_ID + "=1";
                Log.e(TAG, query3);
                database.rawQuery(query3, null);
            }

            for (int i = 0; i < selected_id.size(); i++) {
                String local_project_user_link_id = prefManager.getString(PARAMS.KEY_UNIQUE_CODE, "") + "_" + System.currentTimeMillis() + "_" + (new Random().nextInt(9999) + 1001);
                String query4 = "INSERT INTO " + DbParams.TBL_PROJECT_USER_LINK + " (" + DbParams.CLM_LOCAL_PROJECT_USER_LINK_ID + "," + DbParams.CLM_SERVER_PROJECT_ID + "," +
                        DbParams.CLM_LOCAL_PROJECT_ID + "," + DbParams.CLM_USER_ID + "," + DbParams.CLM_LOCAL_USER_ID + "," + DbParams.CLM_CREATED_BY + "," + DbParams.CLM_UPDATED_BY
                        + "," + DbParams.CLM_UPDATED_ON + ") VALUES ('" + local_project_user_link_id + "','" + project_id + "','" + local_project_id + "','"
                        + selected_id.get(i).getServer_user_id() + "','" + selected_id.get(i).getLocal_user_id() + "','" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") +
                        "','" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") + "','" + DateUtils.currentUTCDateTime() + "')";
                Log.e(TAG, query4);
                database.execSQL(query4);
            }

            isCreated = true;
        } catch (SQLException e) {
            e.printStackTrace();
            isCreated = false;
        }
        return isCreated;
    }

    public boolean createTask(String task_name, String start_date, String end_date, String description, ArrayList<SyncUserInfo> member_id, ArrayList<SyncUserInfo> selected_id,
                             boolean isExists, String local_project_id, String project_id, String local_task_id, String server_task_id) {
        boolean isCreated = false;
        try {
            String id = prefManager.getString(PARAMS.KEY_UNIQUE_CODE, "") + "_" + System.currentTimeMillis();
            String query = "";
            if (!isExists) {
                query = "INSERT INTO " + DbParams.TBL_TASK + " (" + DbParams.CLM_LOCAL_PROJECT_ID + "," + DbParams.CLM_SERVER_PROJECT_ID + "," + DbParams.CLM_COMPANY_ID + "," +
                        DbParams.CLM_LOCAL_TASK_ID + "," + DbParams.CLM_SERVER_TASK_ID + "," + DbParams.CLM_TITLE + "," + DbParams.CLM_START_DATE + "," + DbParams.CLM_END_DATE + ","
                        + DbParams.CLM_DESCRIPTION + "," + DbParams.CLM_CREATED_BY + "," + DbParams.CLM_UPDATED_BY + "," + DbParams.CLM_UPDATED_ON +
                        ") VALUES ('" + local_project_id + "','" + project_id + "','" + prefManager.getString(PARAMS.KEY_COMPANY_ID, "") +
                        "','" + id + "','" + server_task_id +"','" +  task_name + "','" + start_date + "','" + end_date + "'," +
                        DatabaseUtils.sqlEscapeString(description) + ",'" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") +
                        "','" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") + "','" + DateUtils.currentUTCDateTime() + "')";
                local_task_id = id;
            } else {
                query = "UPDATE " + DbParams.TBL_TASK + " SET " + DbParams.CLM_TITLE + "='" + task_name + "', " + DbParams.CLM_START_DATE + "='" + start_date + "', " + DbParams.CLM_END_DATE +
                        "='" + end_date + "', " + DbParams.CLM_DESCRIPTION + "=" + DatabaseUtils.sqlEscapeString(description) + ", " + DbParams.CLM_UPDATED_ON + "='" + DateUtils.currentUTCDateTime() +
                        "' WHERE " + DbParams.CLM_LOCAL_TASK_ID + "='" + local_task_id + "'";
            }
            Log.e(TAG, query);

            database.execSQL(query);

            for (int i = 0; i < member_id.size(); i++) {

                String query1 = "UPDATE " + DbParams.TBL_TASK_USER_LINK + " SET " + DbParams.CLM_STATUS_ID + "=3, " + DbParams.CLM_UPDATED_ON + "='" + DateUtils.currentUTCDateTime() +
                        "',  " + DbParams.CLM_UPDATED_BY + "='" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") + "' WHERE " + DbParams.CLM_LOCAL_TASK_ID + "='" +
                        local_task_id + "' AND " + DbParams.CLM_LOCAL_USER_ID + " LIKE '" + member_id.get(i).getLocal_user_id() + "'";
                Log.e(TAG, query1);
                database.execSQL(query1);

                String query2 = "SELECT GROUP_CONCAT(" + DbParams.CLM_LOCAL_USER_ID + ") AS v_user_id_added FROM " + DbParams.TBL_TASK_USER_LINK +
                        " WHERE " + DbParams.CLM_LOCAL_TASK_ID + "='" + local_task_id + "' AND " + DbParams.CLM_STATUS_ID + "=1";
                Log.e(TAG, query2);
                database.rawQuery(query2, null);
            }

            for (int i = 0; i < selected_id.size(); i++) {
                String local_task_user_link_id = prefManager.getString(PARAMS.KEY_UNIQUE_CODE, "") + "_" + System.currentTimeMillis() + "_" + (new Random().nextInt(9999) + 1001);
                String query4 = "INSERT INTO " + DbParams.TBL_TASK_USER_LINK + " (" +
                        DbParams.CLM_LOCAL_TASK_USER_LINK_ID + "," + DbParams.CLM_SERVER_TASK_USER_LINK_ID + "," + DbParams.CLM_LOCAL_TASK_ID + "," + DbParams.CLM_SERVER_TASK_ID
                        + "," + DbParams.CLM_USER_ID + "," + DbParams.CLM_LOCAL_USER_ID + "," + DbParams.CLM_CREATED_BY + ","
                        + DbParams.CLM_UPDATED_BY  + "," + DbParams.CLM_UPDATED_ON + ") VALUES ('" +
                        local_task_user_link_id + "','','" + local_task_id + "','" + server_task_id + "','" +
                        selected_id.get(i).getServer_user_id() + "','" + selected_id.get(i).getLocal_user_id() + "','" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") +
                        "','" + prefManager.getString(PARAMS.KEY_LOGGED_IN_USER_ID, "") + "','" + DateUtils.currentUTCDateTime() + "')";
                Log.e(TAG, query4);
                database.execSQL(query4);
            }

            isCreated = true;
        } catch (SQLException e) {
            e.printStackTrace();
            isCreated = false;
        }
        return isCreated;
    }

    public String getProjectDetail(String p_id) {
        JSONObject response = new JSONObject();
        JSONObject resultObject = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            String query = "SELECT * FROM " + DbParams.TBL_PROJECT + " WHERE " + DbParams.CLM_LOCAL_PROJECT_ID + "='" + p_id + "' AND " + DbParams.CLM_STATUS_ID + "=1";
            Log.e(TAG, query);
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    JSONObject projectDetail = new JSONObject();
                    projectDetail.put(DbParams.CLM_SERVER_PROJECT_ID, cursor.getString(cursor.getColumnIndex(DbParams.CLM_SERVER_PROJECT_ID)));
                    projectDetail.put(DbParams.CLM_LOCAL_PROJECT_ID, cursor.getString(cursor.getColumnIndex(DbParams.CLM_LOCAL_PROJECT_ID)));
                    projectDetail.put(DbParams.CLM_TITLE, cursor.getString(cursor.getColumnIndex(DbParams.CLM_TITLE)));
                    projectDetail.put(DbParams.CLM_START_DATE, cursor.getString(cursor.getColumnIndex(DbParams.CLM_START_DATE)));
                    projectDetail.put(DbParams.CLM_END_DATE, cursor.getString(cursor.getColumnIndex(DbParams.CLM_END_DATE)));
                    projectDetail.put(DbParams.CLM_THUMB, cursor.getString(cursor.getColumnIndex(DbParams.CLM_THUMB)));
                    projectDetail.put(DbParams.CLM_DESCRIPTION, cursor.getString(cursor.getColumnIndex(DbParams.CLM_DESCRIPTION)));

                    array.put(projectDetail);
                } while (cursor.moveToNext());
            }
            cursor.close();
            resultObject.put(PARAMS.TAG_PROJECT_DETAIL, array);

            JSONArray userArray = new JSONArray();
            query = "SELECT user.*, count(p_link." + DbParams.CLM_LOCAL_PROJECT_ID + ") As is_selected from " + DbParams.TBL_USER_INFO + " As user " +
                    "LEFT JOIN " + DbParams.TBL_PROJECT_USER_LINK + " As p_link ON p_link." + DbParams.CLM_LOCAL_USER_ID + "=user." + DbParams.CLM_LOCAL_USER_ID +
                    " and p_link." + DbParams.CLM_LOCAL_PROJECT_ID + "='" + p_id + "' WHERE user." + DbParams.CLM_STATUS_ID +
                    "=1 Group by user." + DbParams.CLM_LOCAL_USER_ID + " having is_selected > 0";
            Log.e(TAG, query);
            Cursor userCursor = database.rawQuery(query, null);
            if (userCursor.getCount() > 0) {
                userCursor.moveToFirst();
                do {
                    JSONObject userObject = new JSONObject();
                    userObject.put(DbParams.CLM_LOCAL_USER_ID, userCursor.getString(userCursor.getColumnIndex(DbParams.CLM_LOCAL_USER_ID)));
                    userObject.put(DbParams.CLM_SERVER_USER_ID, userCursor.getString(userCursor.getColumnIndex(DbParams.CLM_SERVER_USER_ID)));
                    userObject.put(DbParams.CLM_FIRST_NAME, userCursor.getString(userCursor.getColumnIndex(DbParams.CLM_FIRST_NAME)));
                    userObject.put(DbParams.CLM_LAST_NAME, userCursor.getString(userCursor.getColumnIndex(DbParams.CLM_LAST_NAME)));
                    userObject.put(DbParams.CLM_THUMB, userCursor.getString(userCursor.getColumnIndex(DbParams.CLM_THUMB)));
                    userObject.put(DbParams.CLM_PHONE_NO, userCursor.getString(userCursor.getColumnIndex(DbParams.CLM_PHONE_NO)) + "");

                    userArray.put(userObject);
                } while (userCursor.moveToNext());
            }
            userCursor.close();
            resultObject.put(PARAMS.TAG_USER_LIST, userArray);

            JSONArray taskArray = new JSONArray();
            query = "SELECT task.*,project." + DbParams.CLM_LOCAL_PROJECT_ID +" FROM " + DbParams.TBL_TASK + " As task INNER JOIN " + DbParams.TBL_PROJECT +
                    " As project ON task." + DbParams.CLM_LOCAL_PROJECT_ID + "=project." + DbParams.CLM_LOCAL_PROJECT_ID +
                    " WHERE task." + DbParams.CLM_LOCAL_PROJECT_ID + "='" + p_id + "' AND task." + DbParams.CLM_STATUS_ID + "=1";
            Log.e(TAG, query);
            Cursor taskCursor = database.rawQuery(query, null);
            if (taskCursor.getCount() > 0) {
                taskCursor.moveToFirst();
                do {
                    JSONObject taskObject = new JSONObject();
                    taskObject.put(DbParams.CLM_LOCAL_TASK_ID, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_LOCAL_TASK_ID)));
                    taskObject.put(DbParams.CLM_SERVER_TASK_ID, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_SERVER_TASK_ID)));
                    taskObject.put(DbParams.CLM_LOCAL_PROJECT_ID, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_LOCAL_PROJECT_ID)));
                    taskObject.put(DbParams.CLM_SERVER_PROJECT_ID, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_SERVER_PROJECT_ID)));
                    taskObject.put(DbParams.CLM_STATUS_ID, taskCursor.getInt(taskCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));
                    taskObject.put(DbParams.CLM_TITLE, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_TITLE)));
                    taskObject.put(DbParams.CLM_THUMB, taskCursor.getString(taskCursor.getColumnIndex(DbParams.CLM_THUMB)));

                    taskArray.put(taskObject);
                } while (taskCursor.moveToNext());
            }
            taskCursor.close();
            resultObject.put(PARAMS.TAG_TASK_LIST, taskArray);

            response.put(PARAMS.TAG_STATUS, PARAMS.TAG_STATUS_200);
            response.put(PARAMS.TAG_RESULT, resultObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public boolean deleteProject(String project_id) {
        boolean isCreated = false;
        try {
            String query = "UPDATE " + DbParams.TBL_PROJECT + " SET " + DbParams.CLM_STATUS_ID + "=3," + DbParams.CLM_UPDATED_ON + "='" + DateUtils.currentUTCDateTime()
                    + "'," + DbParams.CLM_IS_DELETE + "=1 WHERE " + DbParams.CLM_LOCAL_PROJECT_ID + " = '" + project_id + "'";
            Log.e(TAG, query);
            database.execSQL(query);
            isCreated = true;
        } catch (SQLException e) {
            e.printStackTrace();
            isCreated = false;
        }
        return isCreated;
    }

}