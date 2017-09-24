package com.techmali.smartteam.database;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Mali on 7/9/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "SmartTeamDb";

    public static DbHelper instance;
    private Context context;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
        db.execSQL(DbParams.CREATE_TBL_LEAVE);
        db.execSQL(DbParams.CREATE_TBL_SECURITY_MENU);
        db.execSQL(DbParams.CREATE_TBL_SECURITY_MENU_CONTROLLER_ACTION);
        db.execSQL(DbParams.CREATE_TBL_SECURITY_MENU_CONTROLLERS_LINK);
        db.execSQL(DbParams.CREATE_TBL_SECURITY_CONTROLLER);
        db.execSQL(DbParams.CREATE_TBL_COMPANY);

        db.execSQL(DbParams.CREATE_TBL_ROLE);
        db.execSQL(DbParams.CREATE_TBL_MESSAGE);
        db.execSQL(DbParams.CREATE_TBL_REF_TABLE);
        db.execSQL(DbParams.CREATE_TBL_SECURITY_ACTION_USER_PERMISSION);
        db.execSQL(DbParams.CREATE_TBL_SETTING);
        db.execSQL(DbParams.CREATE_TBL_APP_SETTING);
        db.execSQL(DbParams.CREATE_TBL_TRACKING);
        db.execSQL(DbParams.CREATE_TBL_TIMESHEET);

        String dbpath = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        Log.e("Path", dbpath);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbParams.TBL_TIMESHEET);
        db.execSQL(DbParams.CREATE_TBL_TIMESHEET);
    }

    public static DbHelper getInstance(Context context) {
        instance = new DbHelper(context);
        return instance;
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }

}
