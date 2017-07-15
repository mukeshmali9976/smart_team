package com.techmali.smartteam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.techmali.smartteam.models.SyncCompanyModel;

public class PendingDataImpl {

    private static final String TAG = PendingDataImpl.class.getSimpleName();
    private SQLiteDatabase database;
    private Context mContext;

    public PendingDataImpl(Context context) {
        this.mContext = context;
        DbHelper dbHelper = DbHelper.getInstance(mContext);
        database = dbHelper.getWritableDatabase();
    }

    public long insert(Object object, String table) {
        long id = 0;
        switch (table) {
            case DbParams.TBL_COMPANY:
                SyncCompanyModel DbModel = (SyncCompanyModel) object;
                id = database.insert(DbParams.TBL_COMPANY, null, this.getCompanyContentValues(DbModel, DbModel.getLocal_company_id()));
                break;
        }
        return id;
    }

    public boolean checkRecordExist(String local_id_key, String local_id_value, String TableName) {
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
        }
        return id;
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

//    public void getData() {
//        String query = "select * from " + DbParams.TBL_COMPANY;
//        Cursor cursor = database.rawQuery(query, null);
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            do {
//                Log.e(TAG, cursor.getString(cursor.getColumnIndex(DbParams.CLM_COMPANY_NAME)));
//            } while (cursor.moveToNext());
//        }
//    }
}