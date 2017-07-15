package com.techmali.smartteam.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by nrana on 25-Mar-16.
 * DatabaseHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase sqliteDb;

    public static DatabaseHelper instance;

    private static final int DATABASE_VERSION = 1;

    public static final String PENDING_TABLE = "PendingData";

    public static final String ADMIN_ASSESSMENTS_TABLE = "admin_assessments";


    // columns
    public static final String local_id = "local_id";
    public static final String update_status = "update_status";
    public static final String id = "id";
    public static final String company_id = "company_id";
    public static final String assessment_name = "assessment_name";
    public static final String tags = "tags";
    public static final String thumb_image = "thumb_image";
    public static final String isactive = "isactive";
    public static final String application_type = "application_type";
    public static final String created_on = "created_on";
    public static final String updated_on = "updated_on";
    public static final String category_id = "category_id";
    public static final String report_pdf = "report_pdf";
    public static final String desc = "desc";
    public static final String student_rank = "student_rank";
    public static final String student_position = "student_position";


    public static final String ASSESSMENT_BEHAVIOURS_TABLE = "assessment_behaviors";

    // columns
    public static final String compentency_id = "compentency_id";
    public static final String assessment_id = "assessment_id";
    public static final String behavior_id = "behavior_id";
    public static final String behavior_name = "behavior_name";
    public static final String behavior_score = "behavior_score";
    public static final String is_selected = "is_selected";


    public static final String ASSESSMENT_COMPENTENCIES_TABLE = "assessment_compentencies";
    // columns
    public static final String compentency_name = "compentency_name";
    public static final String abbreviation = "abbreviation";

    public static final String COMPANY_ASSESSMENTS_TABLE = "company_assessments";
    // columns
    public static final String assessor_id = "assessor_id";
    public static final String org_assessment_id = "org_assessment_id";
    public static final String is_deleted = "is_deleted";
    public static final String category_name = "category_name";
    public static final String sequence_order = "sequence_order";


    public static final String COMPANY_ASSESSMENTS_BEHAVIOURS_TABLE = "company_assessments_behaviors";

    public static final String COMPANY_ASSESSMENTS_COMPENTENCIES_TABLE = "company_assessments_compentencies";
    // columns
    public static final String compentency_order = "compentency_order";

    public static final String COMPANY_BEHAVIOURS_TABLE = "company_behaviors";
    // columns
    public static final String company_compentency_id = "company_compentency_id";
    public static final String org_behavior_id = "org_behavior_id";
    public static final String is_capture = "is_capture";
    public static final String is_compid_updated = "is_compid_updated";


    public static final String COMPANY_COMPENTENCIES_TABLE = "company_compentencies";
    // columns
    public static final String org_compentency_id = "org_compentency_id";


    public static final String MEASURE_ASSESSMENTS_TABLE = "measure_assessments";
    // columns
    public static final String createdby_assessor_id = "createdby_assessor_id";
    public static final String scale_id = "scale_id";
    public static final String assesse_id = "assesse_id";
    public static final String assessment_type = "assessment_type";
    public static final String team_id = "team_id";
    public static final String due_date = "due_date";
    public static final String instruction = "instruction";
    public static final String type = "type";
    public static final String status = "status";
    public static final String completed_on = "completed_on";
    public static final String isshared = "isshared";


    public static final String MEASURE_ASSESSMENTS_BEHAVIOURS_TABLE = "measure_assessments_behaviors";
    // columns
    public static final String measure_assessments_id = "measure_assessments_id";
    public static final String compentencies_id = "compentencies_id";
    public static final String behaviors_id = "behaviors_id";


    public static final String MEASURE_ASSESSMENTS_COMPENTENCIES_TABLE = "measure_assessments_compentencies";

    public static final String MEASURE_ASSESSMENTS_USERS_TABLE = "measure_assessments_users";
    // columns
    public static final String users_id = "users_id";
    public static final String user_roles_id = "user_roles_id";
    public static final String is_completed = "is_completed";


    public static final String SCALE_DESCRIPTION_TABLE = "scale_description";
    // columns
    public static final String description = "description";

    public static final String SCALES_TABLE = "scales";
    // columns
    public static final String scale_name = "scale_name";
    public static final String scale_display_name = "scale_display_name";
    public static final String scale_type = "scale_type";

    public static final String TEAM_TABLE = "team";
    // columns
    public static final String name = "name";
    public static final String members_id = "members_id";

    public static final String USER_ROLES_TABLE = "user_roles";
    // columns
    public static final String role_name = "role_name";
    public static final String sort_order = "sort_order";

    public static final String USERS_TABLE = "users";
    // columns
    public static final String full_name = "full_name";
    public static final String email = "email";
    public static final String password = "password";
    public static final String assosicated_teams = "assosicated_teams";
    public static final String contact_name = "contact_name";
    public static final String position = "position";
    public static final String address = "address";
    public static final String phone = "phone";
    public static final String url = "url";
    public static final String user_type = "user_type";
    public static final String coaching_auth = "coaching_auth";
    public static final String device_type = "device_type";
    public static final String device_token = "device_token";
    public static final String last_login_on = "last_login_on";
    public static final String db_name = "db_name";

    public static final String PERFORM_ASSESSMENTS_TABLE = "perform_assessments";
    //columns
    public static final String assessment_score = "assessment_score";
    public static final String score = "score";


    public static final String PERFORM_ASSESSMENTS_COMPENTENCIES_TABLE = "perform_assessments_compentencies";
    //columns
    public static final String pa_id = "pa_id";

    public static final String PERFORM_ASSESSMENTS_SCALES_TABLE = "perform_assessments_scales";
    //------------------------------

    // the default database path is :
    // /data/data/pkgNameOfYourApplication/databases/
    private static String DB_PATH_PREFIX = "/data/data/";

    private static String DB_PATH_SUFFIX = "/databases/";

    private static final String TAG = "SQLiteDatabaseHelper";

    private Context context;

    //    public static final String id = "id";
//    public static final String company_id = "company_id";
//    public static final String assessor_id = "assessor_id";
    public static final String measure_assessment_id = "measure_assessment_id";
    //    public static final String compentency_id = "compentency_id";
    public static final String behaviour_id = "behaviour_id";
    public static final String scale_no = "scale_no";
    public static final String behaviour_name = "behaviour_name";
    public static final String comment = "comment";
    public static final String overall_comment = "overall_comment";
    public static final String isAssessed = "isAssessed";
    public static final String user_id = "user_id";


    public static final String PEER_TABLE = "peer";
    // columns
    public static final String peer_group_id = "peer_group_id";
    public static final String rank_order = "rank_order";
    public static final String total_rank = "total_rank";
    public static final String created_by = "created_by";
    public static final String peer_category_id = "peer_category_id";
    public static final String peer_comment_category_id = "peer_comment_category_id";
    public static final String peer_evaluation_category_id = "peer_evaluation_category_id";
    public static final String evaluation_type = "evaluation_type";


    public static final String PEER_CATEGORY_TABLE = "peer_categories";
    public static final String updated_by = "updated_by";
    public static final String category_type = "category_type";

    public static final String PEER_POWER_RANK = "peer_power_rank";


    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + PENDING_TABLE + "("
            + company_id + " INTEGER," + assessor_id + " INTEGER," +
            measure_assessment_id + " INTEGER," +
            compentency_id + " INTEGER," +
            behaviour_id + " TEXT," +
            scale_no + " FLOAT," +
            behaviour_name + " TEXT," +
            comment + " TEXT," +
            isAssessed + " INTEGER," +
            user_id + " INTEGER," +
            peer_comment_category_id + " VARCHAR, " +
            peer_evaluation_category_id + " VARCHAR," +
            is_selected + " VARCHAR NOT NULL  DEFAULT 0," +
            overall_comment + " VARCHAR," +
            behavior_score + " VARCHAR NOT NULL  DEFAULT 0 " + ")";


    private static final String CREATE_PEER_TABLE = "CREATE TABLE IF NOT EXISTS "
            + PEER_TABLE + "(" + local_id + " TEXT," +
            company_id + " INTEGER," +
            update_status + " TEXT," +
            id + " TEXT PRIMARY KEY," +
            peer_group_id + " TEXT," +
            user_id + " TEXT," +
            rank_order + " INTEGER," +
            total_rank + " INTEGER," +
            comment + " TEXT," +
            created_by + " TEXT, " +
            created_on + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            peer_category_id + " VARCHAR, " +
            peer_comment_category_id + " VARCHAR, " +
            peer_evaluation_category_id + " VARCHAR, " +
            evaluation_type + " VARCHAR, " +
            is_deleted + " VARCHAR, " +
            updated_by + " VARCHAR, " +
            updated_on + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";


    private static final String CREATE_PEER_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS "
            + PEER_CATEGORY_TABLE + "(" + local_id + " TEXT," +
            update_status + " TEXT," +
            id + " VARCHAR PRIMARY KEY," +
            company_id + " VARCHAR," +
            name + " VARCHAR," +
            evaluation_type + " VARCHAR, " +
            is_deleted + " VARCHAR," +
            created_by + " VARCHAR," +
            updated_by + " VARCHAR," +
            description + " VARCHAR," +
            created_on + "  TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            updated_on + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            category_type + " VARCHAR)";


    private static final String CREATE_PEER_POWER_RANK = "CREATE TABLE peer_power_rank"
            + "(" + local_id + " VARCHAR" +
            ", " + update_status + " VARCHAR NOT NULL  DEFAULT 0" +
            "," + id + " VARCHAR NOT NULL  DEFAULT 0" +
            "," + company_id + " VARCHAR DEFAULT (NULL)" +
            "," + rank_order + "  VARCHAR DEFAULT (NULL)" +
            "," + score + " VARCHAR DEFAULT (NULL)" +
            "," + is_deleted + " VARCHAR DEFAULT 0" +
            "," + created_by + " VARCHAR DEFAULT (NULL)" +
            "," + updated_by + " VARCHAR DEFAULT (NULL)" +
            "," + created_on + " DATETIME NOT NULL  DEFAULT (CURRENT_TIMESTAMP)" +
            "," + updated_on + " DATETIME NOT NULL  DEFAULT ('0000-00-00 00:00:00'))";

    /***
     * Contructor
     *
     * @param context : app context
     * @param name    : database name
     * @param factory : cursor Factory
     * @param version : DB version
     */
    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        Log.i(TAG, "Create or Open database : " + name);
    }

    /***
     * Initialize method
     *
     * @param context      : application context
     * @param databaseName : database name
     */
    private static void initialize(Context context, String databaseName) {
        if (instance == null) {
            /**
             * Try to check if there is an Original copy of DB in asset
             * Directory
             */
            if (!checkDatabase(context, databaseName)) {
                // if not exists, I try to copy from asset dir
                try {
                    copyDataBase(context, databaseName);
                } catch (IOException e) {
                    Log.e(TAG, "Database " + databaseName
                            + " does not exists and there is no Original Version in Asset dir");
                }
            }

            Log.i(TAG, "Try to create instance of database (" + databaseName + ")");
            instance = new DatabaseHelper(context, databaseName, null, DATABASE_VERSION);
            sqliteDb = instance.getWritableDatabase();
            Log.i(TAG, "instance of database (" + databaseName + ") created !");
        }
    }

    /***
     * Static method for getting singleton instance
     *
     * @param context      : application context
     * @param databaseName : database name
     * @return : singleton instance
     */
    public static DatabaseHelper getInstance(Context context, String databaseName) {
        initialize(context, databaseName);
        return instance;
    }

    /***
     * Method to get database instance
     *
     * @return database instance
     */
    public SQLiteDatabase getDatabase() {
        return sqliteDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate : nothing to do");
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_PEER_TABLE);
        db.execSQL(CREATE_PEER_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade : Add new table");
        // If you need to add a new column

        try {
            if (oldVersion < 4) {
                db.execSQL(CREATE_PEER_CATEGORY_TABLE);
                db.execSQL("ALTER TABLE " + PEER_TABLE + " ADD COLUMN " + peer_category_id + " VARCHAR");
                db.execSQL("ALTER TABLE " + PEER_TABLE + " ADD COLUMN " + peer_comment_category_id + " VARCHAR");
                db.execSQL("ALTER TABLE " + PEER_TABLE + " ADD COLUMN " + is_deleted + " VARCHAR");
                db.execSQL("ALTER TABLE " + PEER_TABLE + " ADD COLUMN " + updated_by + " VARCHAR");
                db.execSQL("ALTER TABLE " + PEER_TABLE + " ADD COLUMN " + updated_on + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                db.execSQL("ALTER TABLE " + PEER_TABLE + " ADD COLUMN " + peer_evaluation_category_id + " VARCHAR");
                db.execSQL("ALTER TABLE " + TEAM_TABLE + " ADD COLUMN " + type + " VARCHAR");
            }
            if (oldVersion < 5) {
                db.execSQL("ALTER TABLE " + PEER_CATEGORY_TABLE + " ADD COLUMN " + description + " VARCHAR");
                db.execSQL("ALTER TABLE " + PERFORM_ASSESSMENTS_TABLE + " ADD COLUMN " + peer_comment_category_id + " VARCHAR");
                db.execSQL("ALTER TABLE " + PERFORM_ASSESSMENTS_TABLE + " ADD COLUMN " + peer_evaluation_category_id + " VARCHAR");
            }
            if (oldVersion < 6) {
                db.execSQL("ALTER TABLE " + PENDING_TABLE + " ADD COLUMN " + peer_comment_category_id + " VARCHAR");
                db.execSQL("ALTER TABLE " + PENDING_TABLE + " ADD COLUMN " + peer_evaluation_category_id + " VARCHAR");
            }
            if (oldVersion < 7) {
                db.execSQL("ALTER TABLE " + COMPANY_ASSESSMENTS_BEHAVIOURS_TABLE + " ADD COLUMN " + created_on + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                db.execSQL("ALTER TABLE " + COMPANY_ASSESSMENTS_BEHAVIOURS_TABLE + " ADD COLUMN " + is_deleted + " VARCHAR DEFAULT 0");
                db.execSQL("ALTER TABLE " + COMPANY_ASSESSMENTS_BEHAVIOURS_TABLE + " ADD COLUMN " + updated_on + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                db.execSQL("ALTER TABLE " + COMPANY_ASSESSMENTS_COMPENTENCIES_TABLE + " ADD COLUMN " + created_on + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                db.execSQL("ALTER TABLE " + COMPANY_ASSESSMENTS_COMPENTENCIES_TABLE + " ADD COLUMN " + is_deleted + " VARCHAR DEFAULT 0");
                db.execSQL("ALTER TABLE " + COMPANY_ASSESSMENTS_TABLE + " ADD COLUMN " + application_type + " VARCHAR");
            }
            if (oldVersion < 8) {
                db.execSQL("ALTER TABLE " + PERFORM_ASSESSMENTS_TABLE + " ADD COLUMN " + report_pdf + " VARCHAR NOT NULL  DEFAULT ''");
                db.execSQL("ALTER TABLE " + SCALE_DESCRIPTION_TABLE + " ADD COLUMN " + is_deleted + " VARCHAR NOT NULL  DEFAULT 0");
                db.execSQL("ALTER TABLE " + SCALES_TABLE + " ADD COLUMN " + is_deleted + " VARCHAR NOT NULL  DEFAULT 0");
                db.execSQL("ALTER TABLE " + TEAM_TABLE + " ADD COLUMN " + is_deleted + " VARCHAR NOT NULL  DEFAULT 0");
                db.execSQL("ALTER TABLE " + USER_ROLES_TABLE + " ADD COLUMN " + is_deleted + " VARCHAR NOT NULL  DEFAULT 0");
            }
            if (oldVersion < 9) {
                db.execSQL(CREATE_PEER_POWER_RANK);
            }
            if (oldVersion < 10) {
                db.execSQL("ALTER TABLE " + PEER_TABLE + " ADD COLUMN " + evaluation_type + " VARCHAR NOT NULL  DEFAULT 1");
                db.execSQL("ALTER TABLE " + PEER_CATEGORY_TABLE + " ADD COLUMN " + evaluation_type + " VARCHAR NOT NULL  DEFAULT 1");
            }
            if (oldVersion < 11) {
                db.execSQL("ALTER TABLE " + MEASURE_ASSESSMENTS_TABLE + " ADD COLUMN " + application_type + " VARCHAR DEFAULT 1");
                db.execSQL("ALTER TABLE " + MEASURE_ASSESSMENTS_TABLE + " ADD COLUMN " + student_rank + " VARCHAR");
                db.execSQL("ALTER TABLE " + MEASURE_ASSESSMENTS_TABLE + " ADD COLUMN " + student_position + " VARCHAR");
                db.execSQL("ALTER TABLE " + COMPANY_ASSESSMENTS_BEHAVIOURS_TABLE + " ADD COLUMN " + behavior_score + " VARCHAR NOT NULL  DEFAULT 0");
                db.execSQL("ALTER TABLE " + PENDING_TABLE + " ADD COLUMN " + behavior_score + " VARCHAR NOT NULL  DEFAULT 0");
                db.execSQL("ALTER TABLE " + MEASURE_ASSESSMENTS_BEHAVIOURS_TABLE + " ADD COLUMN " + behavior_score + " VARCHAR NOT NULL  DEFAULT 0");
                db.execSQL("ALTER TABLE " + PENDING_TABLE + " ADD COLUMN " + is_selected + " VARCHAR NOT NULL  DEFAULT 0");
                db.execSQL("ALTER TABLE " + PERFORM_ASSESSMENTS_SCALES_TABLE + " ADD COLUMN " + is_selected + " VARCHAR NOT NULL  DEFAULT 0''");
                db.execSQL("ALTER TABLE " + PENDING_TABLE + " ADD COLUMN " + overall_comment + " VARCHAR''");
                db.execSQL("ALTER TABLE " + PERFORM_ASSESSMENTS_TABLE + " ADD COLUMN " + comment + " VARCHAR''");
                db.execSQL("ALTER TABLE " + COMPANY_ASSESSMENTS_COMPENTENCIES_TABLE + " ADD COLUMN " + updated_on + " TIMESTAMP");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Method for Copy the database from asset directory to application's data
     * directory
     *
     * @param databaseName : database name
     * @throws IOException : exception if file does not exists
     */
    @SuppressWarnings("unused")
    private void copyDataBase(String databaseName) throws IOException {
        copyDataBase(context, databaseName);
    }

    /***
     * Static method for copy the database from asset directory to application's
     * data directory
     *
     * @param aContext     : application context
     * @param databaseName : database name
     * @throws IOException : exception if file does not exists
     */
    private static void copyDataBase(Context aContext, String databaseName) throws IOException {

        // Open your local db as the input stream
        InputStream myInput = aContext.getAssets().open(databaseName);

        // Path to the just created empty db
        String outFileName = getDatabasePath(aContext, databaseName);

        Log.i(TAG, "Check if create dir : " + DB_PATH_PREFIX + aContext.getPackageName()
                + DB_PATH_SUFFIX);

        // if the path doesn't exist first, create it
        File f = new File(DB_PATH_PREFIX + aContext.getPackageName() + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        Log.i(TAG, "Trying to copy local DB to : " + outFileName);

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

        Log.i(TAG, "DB (" + databaseName + ") copied!");
    }

    /***
     * Method to check if database exists in application's data directory
     *
     * @param databaseName : database name
     * @return : boolean (true if exists)
     */
    public boolean checkDatabase(String databaseName) {
        return checkDatabase(context, databaseName);
    }

    /***
     * Static Method to check if database exists in application's data directory
     *
     * @param aContext     : application context
     * @param databaseName : database name
     * @return : boolean (true if exists)
     */
    public static boolean checkDatabase(Context aContext, String databaseName) {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = getDatabasePath(aContext, databaseName);

            Log.i(TAG, "Trying to conntect to : " + myPath);
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            Log.i(TAG, "Database " + databaseName + " found!");
            checkDB.close();
        } catch (SQLiteException e) {
            Log.i(TAG, "Database " + databaseName + " does not exists!");
            e.printStackTrace();
        }

        return checkDB != null;
    }

    /***
     * Method that returns database path in the application's data directory
     *
     * @param databaseName : database name
     * @return : complete path
     */
    @SuppressWarnings("unused")
    private String getDatabasePath(String databaseName) {
        return getDatabasePath(context, databaseName);
    }

    /***
     * Static Method that returns database path in the application's data
     * directory
     *
     * @param aContext     : application context
     * @param databaseName : database name
     * @return : complete path
     */
    private static String getDatabasePath(Context aContext, String databaseName) {
        return DB_PATH_PREFIX + aContext.getPackageName() + DB_PATH_SUFFIX + databaseName;
    }

}
