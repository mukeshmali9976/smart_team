package com.techmali.smartteam.database;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mali on 7/9/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SmartTeamDb";

    public static DbHelper instance;


    private DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbParams.CREATE_TBL_USER_INFO);
        db.execSQL(DbParams.CREATE_TBL_PROJECT);
        db.execSQL(DbParams.CREATE_TBL_TASK);
        db.execSQL(DbParams.CREATE_TBL_PROJECT_USER_LINK);
        db.execSQL(DbParams.CREATE_TBL_TASK_USER_LINK);
        db.execSQL(DbParams.CREATE_TBL_ATTENDANCE);
        db.execSQL(DbParams.CREATE_TBL_CHECK_IN);
        db.execSQL(DbParams.CREATE_TBL_EXPENSE);


        db.execSQL(DbParams.CREATE_TBL_APP_SETTING);
        db.execSQL(DbParams.CREATE_TBL_COMPANY);
        db.execSQL(DbParams.CREATE_TBL_SECURITY_MENU_CONTROLLERS_LINK);
        db.execSQL(DbParams.CREATE_TBL_SETTING);
        db.execSQL(DbParams.CREATE_TBL_MESSAGE);
        db.execSQL(DbParams.CREATE_TBL_LEAVE);
        db.execSQL(DbParams.CREATE_TBL_ROLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DbHelper getInstance(Context context) {
        instance = new DbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        return instance;
    }
}
