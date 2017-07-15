package com.techmali.smartteam.database;

/**
 * Created by Mali on 7/9/2017.
 */

public class DbParams {

    //////////////////////
    ////// TABLE NAMES.........
    /////////////////////
    public static final String TBL_APP_SETTING = "app_setting";
    public static final String TBL_COMPANY = "company";
    public static final String TBL_EXPENSE = "expance";
    public static final String TBL_TASK = "task";
    public static final String TBL_SETTING = "setting";
    public static final String TBL_SECURITY_MENU_CONTROLLERS_LINK = "security_menu_controllers_link";
    public static final String TBL_LEAVE = "leave";
    public static final String TBL_ROLE = "role";
    public static final String TBL_SECURITY_ACTION_USER_PERMISSION = "security_action_user_permission";

    /////////////////////
    ///// COLUMNS NAME..........
    /////////////////////

    ////// App Setting Table...
    public static final String CLM_APP_SETTING_ID = "app_setting_id";
    public static final String CLM_SETTING_KEY = "setting_key";
    public static final String CLM_SETTING_VALUE = "setting_value";
    public static final String CLM_SETTING_VALUE_TYPE = "setting_value_type";
    public static final String CLM_APP_TYPE = "app_type";
    public static final String CLM_STATUS_ID = "status_id";

    ///// Company Table.......
    public static final String CLM_LOCAL_COMPANY_ID = "local_company_id";
    public static final String CLM_COMPANY_NAME = "company_name";
    public static final String CLM_REG_NO = "reg_no";
    public static final String CLM_THUMB = "thumb";
    public static final String CLM_CONTACT_PERSON = "contact_person";
    public static final String CLM_PHONE_NO = "phone_no";
    public static final String CLM_EMAIL = "email";
    public static final String CLM_ADMIN_USER_ID = "admin_user_id";
    public static final String CLM_OBJECT_MESSAGE = "object_message";
    public static final String CLM_CREATED_BY = "created_by";
    public static final String CLM_CREATED_ON = "created_on";
    public static final String CLM_UPDATED_BY = "updated_by";
    public static final String CLM_UPDATED_ON = "updated_on";

    ///// Expense Table.........
    static final String CLM_LOCAL_EXPENSE_ID = "local_expance_id";
    static final String CLM_SERVER_EXPENSE_ID = "server_expance_id";
    static final String CLM_COMPANY_ID = "company_id";
    static final String CLM_USER_ID = "user_id";
    static final String CLM_TASK_ID = "task_id";
    static final String CLM_PROJECT_ID = "project_id";
    static final String CLM_TITLE = "title";
    static final String CLM_DESCRIPTION = "description";
    static final String CLM_PAYOUT_STATUS = "payout_status";

    // Task
    static final String CLM_LOCAL_TASK_ID = "local_task_id";
    static final String CLM_SERVER_TASK_ID = "server_task_id";
    static final String CLM_TYPE = "type";
    static final String CLM_START_DATE = "start_date";
    static final String CLM_END_DATE = "end_date";

    //Setting
    static final String CLM_SETTING_ID = "setting_id";
    static final String CLM_ADMIN_EMAIL = "admin_email";
    static final String CLM_PORT = "port";
    static final String CLM_SERVER = "server";
    static final String CLM_USER_NAME = "username";
    static final String CLM_ENCRYPTION = "encryption";
    static final String CLM_APP_NAME = "app_name";
    static final String CLM_TERM_AND_CONDITINS_USERS = "terms_and_conditions_user";
    static final String CLM_PRIVACY_POLICY_USER = "privacy_policy_user";
    static final String CLM_FAQ_USER = "faq_user";
    static final String CLM_PRIVATE_KEY = "private_key";
    static final String CLM_HOST = "host";


    // Role table
    static final String CLM__ROLE_ID = "role_id";
    static final String CLM_SORT_ORDER = "sort_order";
    static final String CLM_ROLE_GROUP = "role_group";
    static final String CLM_IS_CUSTOM = "is_custom";
    static final String CLM_STATUS = "status";

    //Ref Table
    static final String CLM_REF_TABLE_ID = "ref_table_id";
    static final String CLM_NAME = "name";
    static final String CLM_IS_TABLE = "is_table";

    // Project
    static final String CLM_LOCAL_PROJECT_ID = "local_project_id";
    static final String CLM_SERVER_PROJECT_ID = "server_project_id";

    //Leave Apply
    static final String CLM_LOCAL_LEAVE_ID = "local_leave_id";
    static final String CLM_SERVER_LEAVE_ID = "server_leave_id";
    static final String CLM_IS_DELETE = "is_delete";

    //TABLE SECURITY MENU CONTROLLERS LINK
    static final String CLM_MENU_CONTROLLERS_LINK_ID = "menu_controllers_link_id";
    static final String CLM_MENU_ID = "menu_id";
    static final String CLM_CONTROLLER_ID = "controller_id";
    static final String CLM_CREATED = "created";
    static final String CLM_UPDATED = "updated";


    static final String CLM_USER_PERMISSION_ID = "user_permission_id";
    static final String CLM__SECURIY_MENU_CONROLLERS_ACION_ID = "security_menu_controllers_action_id";


    ////////////////////
    ////// CREATE TABLE QUERY....
    ///////////////////
    /*
        setting_value_type : 1=> test, 2 => json text
        app_type : 0=> Both, 1=>user, 2=> driver
    */
    static final String CREATE_TBL_APP_SETTING = "CREATE TABLE " + TBL_APP_SETTING + "(" +
            CLM_APP_SETTING_ID + "  INTEGER PRIMARY KEY," +
            CLM_SETTING_KEY + " VARCHAR," +
            CLM_SETTING_VALUE + " VARCHAR," +
            CLM_SETTING_VALUE_TYPE + " INTEGER," +
            CLM_APP_TYPE + " INTEGER," +
            CLM_STATUS_ID + " INTEGER)";
    /*
        thumb: Company Logo image
        admin_user_id: Ref to Admin user account which is associated with user
        status_id: 1-Active,2-Inactive,3-Deleted,4-Pending Activation
     */
    static final String CREATE_TBL_COMPANY = "CREATE TABLE " + TBL_COMPANY + "(" +
            CLM_LOCAL_COMPANY_ID + " VARCHAR," +
            CLM_COMPANY_NAME + " VARCHAR," +
            CLM_REG_NO + " VARCHAR," +
            CLM_THUMB + " VARCHAR," +
            CLM_CONTACT_PERSON + " VARCHAR," +
            CLM_PHONE_NO + " VARCHAR," +
            CLM_EMAIL + " VARCHAR," +
            CLM_ADMIN_USER_ID +
            CLM_OBJECT_MESSAGE + " VARCHAR," +
            CLM_STATUS_ID + " INTEGER," +
            CLM_CREATED_BY + " INTEGER," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " INTEGER," +
            CLM_UPDATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP))";

    static final String CREATE_TBL_EXPENSE = "CREATE TABLE " + TBL_EXPENSE + " (" +
            CLM_LOCAL_EXPENSE_ID + " VARCHAR," +
            CLM_SERVER_EXPENSE_ID + " INTEGER, " +
            CLM_COMPANY_ID + " INTEGER, " +
            CLM_USER_ID + " INTEGER, " +
            CLM_TASK_ID + " INTEGER, " +
            CLM_PROJECT_ID + " INTEGER, " +
            CLM_TITLE + " VARCHAR," +
            CLM_DESCRIPTION + " VARCHAR," +
            CLM_PAYOUT_STATUS + " INTEGER, " +
            CLM_STATUS_ID + " INTEGER, " +
            CLM_CREATED_BY + " INTEGER," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " INTEGER," +
            CLM_UPDATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP))";

    static final String CREATE_TBL_TASK = "CREATE TABLE " + TBL_TASK + "(" +
            CLM_LOCAL_TASK_ID + " VARCHAR," +
            CLM_SERVER_TASK_ID + " INTEGER," +
            CLM_COMPANY_ID + " INTEGER," +
            CLM_PROJECT_ID + " INTEGER," +
            CLM_DESCRIPTION + " VARCHAR," +
            CLM_THUMB + " VARCHAR," +
            CLM_START_DATE + " VARCHAR," +
            CLM_END_DATE + " VARCHAR," +
            CLM_TYPE + " INTEGER," +
            CLM_CREATED_BY + " INTEGER," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " INTEGER," +
            CLM_UPDATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP))";

    static final String CREATE_TBL_SETTING = "CREATE TABLE " + TBL_SETTING + "(" +
            CLM_SETTING_ID + " INTEGER PRIMARY KEY AUTO INCREMENT," +
            CLM_ADMIN_EMAIL + " VARCHAR," +
            CLM_HOST + " VARCHAR," +
            CLM_PORT + " VARCHAR," +
            CLM_SERVER + " VARCHAR," +
            CLM_USER_NAME + " VARCHAR," +
            CLM_ENCRYPTION + " VARCHAR," +
            CLM_APP_NAME + " VARCHAR," +
            CLM_THUMB + " VARCHAR," +
            CLM_TERM_AND_CONDITINS_USERS + " VARCHAR," +
            CLM_PRIVACY_POLICY_USER + " VARCHAR," +
            CLM_FAQ_USER + " VARCHAR," +
            CLM_PRIVATE_KEY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR)";

    static final String CREATE_TBL_SECURITY_MENU_CONTROLLERS_LINK = "CREATE TABLE " + TBL_SECURITY_MENU_CONTROLLERS_LINK + " (" +
            CLM_MENU_CONTROLLERS_LINK_ID + " INTEGER PRIMARY KEY," +
            CLM_MENU_ID + " INTEGER," +
            CLM_CONTROLLER_ID + " INTEGER," +
            CLM_STATUS + " INTEGER," +
            CLM_CREATED_BY + " INTEGER," +
            CLM_CREATED + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED + " VARCHAR)";

    static final String CREATE_TBL_LEAVE = "CREATE TABLE " + TBL_LEAVE + "(" +
            CLM_LOCAL_LEAVE_ID + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_SERVER_LEAVE_ID + " INTEGER," +
            CLM_COMPANY_ID + " INTEGER," +
            CLM_USER_ID + " INTEGER," +
            CLM_START_DATE + "  VARCHAR," +
            CLM_END_DATE + "  VARCHAR," +
            CLM_STATUS_ID + " INTEGER," +
            CLM_IS_DELETE + " INTEGER," +
            CLM_CREATED_BY + " INTEGER," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " INTEGER," +
            CLM_UPDATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP))";

    static final String CREATE_TBL_ROLE = "CREATE TABLE " + TBL_ROLE + "(" +
            CLM__ROLE_ID + " INTEGER PRIMARY KEY," +
            CLM_NAME + "  VARCHAR," +
            CLM_SORT_ORDER + " INTEGER," +
            CLM_ROLE_GROUP + " INTEGER," +
            CLM_IS_CUSTOM + " INTEGER," +
            CLM_STATUS + " INTEGER," +
            CLM_CREATED_BY + " INTEGER," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " INTEGER," +
            CLM_UPDATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP))";
}
