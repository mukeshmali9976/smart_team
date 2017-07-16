package com.techmali.smartteam.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.techmali.smartteam.models.SyncCompanyModel;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public long insert(Object object, String table) {
        long id = 0;
        switch (table) {
            case DbParams.TBL_COMPANY:
                SyncCompanyModel DbModel = (SyncCompanyModel) object;
                id = database.insert(DbParams.TBL_COMPANY, null, this.getCompanyContentValues(DbModel, DbModel.getLocal_company_id()));
                break;
            case DbParams.TBL_USER_INFO:
                SyncUserInfo userInfo = (SyncUserInfo) object;
                id = database.insert(DbParams.TBL_USER_INFO, null, this.getUserInfoContentValues(userInfo, userInfo.getServer_user_id()));
                break;
        }
        return id;
    }

    public boolean checkRecordExist(String TableName, String local_id_key, String local_id_value) {
        String query = "SELECT " + local_id_key + " FROM " + TableName + " WHERE " + local_id_key + " = '" + local_id_value + "'";
        Cursor cursor = database.rawQuery(query, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public long update(Object object, String table, String server_id) {
        long id = 0;
        switch (table) {
            case DbParams.TBL_COMPANY:
                SyncCompanyModel mDbModel = (SyncCompanyModel) object;
                id = database.update(DbParams.TBL_COMPANY, this.getCompanyContentValues(mDbModel, String.valueOf(mDbModel.getLocal_company_id())),
                        "local_id = ?", new String[]{server_id});
                break;
            case DbParams.TBL_USER_INFO:
                SyncUserInfo userInfo = (SyncUserInfo) object;
                id = database.update(DbParams.TBL_USER_INFO, this.getUserInfoContentValues(userInfo, server_id),
                        DbParams.CLM_SERVER_USER_ID + "=?", new String[]{server_id});
        }
        return id;
    }

    public String getSyncData() {

        JSONObject reqObject = new JSONObject();
        try {
            String last_sync_date = prefManager.getString(PARAMS.KEY_LAST_SYNC_DATE, "");
            reqObject.put(PARAMS.KEY_LAST_SYNC_DATE, last_sync_date);
            String whereCond = "";
            if (!Utils.isEmptyString(last_sync_date))
                whereCond = " WHERE " + DbParams.CLM_UPDATED_ON + " >= '" + last_sync_date + "'";

            String userinfo = "SELECT * FROM " + DbParams.TBL_USER_INFO + whereCond;
            Cursor userinfoCursor = database.rawQuery(userinfo, null);
            JSONArray userinfoArray = new JSONArray();
            if (userinfoCursor.getCount() > 0) {
                userinfoCursor.moveToFirst();
                do {
                    JSONObject userinfoObject = new JSONObject();
                    userinfoObject.put(DbParams.CLM_SERVER_USER_ID, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_SERVER_USER_ID)));
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
                    userinfoObject.put(DbParams.CLM_STATUS_ID, userinfoCursor.getString(userinfoCursor.getColumnIndex(DbParams.CLM_STATUS_ID)));

                    userinfoArray.put(userinfoObject);
                } while (userinfoCursor.moveToNext());
            }
            reqObject.put(DbParams.TBL_USER_INFO, userinfoArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reqObject.toString();
    }

    private ContentValues getCompanyContentValues(SyncCompanyModel model, String id) {
        ContentValues values = new ContentValues();
        values.put(DbParams.CLM_LOCAL_COMPANY_ID, id);
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

    private ContentValues getUserInfoContentValues(SyncUserInfo model, String id) {
        ContentValues values = new ContentValues();
        values.put(DbParams.CLM_SERVER_USER_ID, id);
        values.put(DbParams.CLM_LOCAL_USER_ID, model.getLocal_user_id());
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

}