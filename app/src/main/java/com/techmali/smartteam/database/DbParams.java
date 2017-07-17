package com.techmali.smartteam.database;

/**
 * Created by Mali on 7/9/2017.
 */

public class DbParams {

    //////////////////////
    ////// TABLE NAMES.........
    /////////////////////

    public static final String TBL_USER_INFO = "userinfo";
    public static final String TBL_PROJECT = "project";
    public static final String TBL_TASK = "task";
    public static final String TBL_PROJECT_USER_LINK = "project_user_link";
    public static final String TBL_TASK_USER_LINK = "task_user_link";
    public static final String TBL_ATTENDANCE = "attendance";
    public static final String TBL_CHECK_IN = "checkin";
    public static final String TBL_EXPENSE = "expance";
    public static final String TBL_LEAVE = "leave";


    public static final String TBL_APP_SETTING = "app_setting";
    public static final String TBL_COMPANY = "company";
    public static final String TBL_SETTING = "setting";
    public static final String TBL_SECURITY_MENU_CONTROLLERS_LINK = "security_menu_controllers_link";
    public static final String TBL_ROLE = "role";
    public static final String TBL_MESSAGE = "message";
    public static final String TBL_SECURITY_ACTION_USER_PERMISSION = "security_action_user_permission";


    /////////////////////
    ///// COLUMNS NAME..........
    /////////////////////

    public static final String CLM_IS_UPDATED = "is_updated";
    public static final String CLM_CREATED_BY = "created_by";
    public static final String CLM_CREATED_ON = "created_on";
    public static final String CLM_UPDATED_BY = "updated_by";
    public static final String CLM_UPDATED_ON = "updated_on";

    ////// User Info....
    public static final String CLM_SERVER_USER_ID = "server_user_id";
    public static final String CLM_LOCAL_USER_ID = "local_user_id";
    public static final String CLM_USERNAME = "username";
    public static final String CLM_GENDER = "gender";
    public static final String CLM_BADGE_COUNT = "badge_count";
    public static final String CLM_LAT = "lat";
    public static final String CLM_LONG = "lng";
    public static final String CLM_HOME_ADDRESS = "home_address";
    public static final String CLM_WORK_ADDRESS = "work_address";
    public static final String CLM_IS_SMARTPHONE = "is_smart_phone";
    public static final String CLM_VERIFICATION_TOKEN = "verification_token";
    public static final String CLM_VERIFICATION_TOKEN_TIME = "verification_token_time";
    public static final String CLM_LAST_LOGIN_ON = "last_login_on";
    public static final String CLM_STATUS_ID = "status_id";

    ////// Project.....
    public static final String CLM_LOCAL_PROJECT_ID = "local_project_id";
    public static final String CLM_SERVER_PROJECT_ID = "server_project_id";
    public static final String CLM_COMPANY_ID = "company_id";
    public static final String CLM_TITLE = "title";
    public static final String CLM_DESCRIPTION = "description";
    public static final String CLM_START_DATE = "start_date";
    public static final String CLM_END_DATE = "end_date";

    ////// Task......
    public static final String CLM_SERVER_TASK_ID = "server_task_id";
    public static final String CLM_LOCAL_TASK_ID = "local_task_id";
    public static final String CLM_PROJECT_ID = "project_id";
    public static final String CLM_TYPE = "type";

    ////// Project User Link.......
    public static final String CLM_SERVER_PROJECT_USER_LINK_ID = "server_project_user_link_id";
    public static final String CLM_LOCAL_PROJECT_USER_LINK_ID = "local_project_user_link_id";

    ////// Task User Link.......
    public static final String CLM_SERVER_TASK_USER_LINK_ID = "server_task_user_link_id";
    public static final String CLM_LOCAL_TASK_USER_LINK_ID = "local_task_user_link_id";

    ////// ATTENDANCE........
    public static final String CLM_SERVER_ATTENDANCE_ID = "server_attendance_id";
    public static final String CLM_LOCAL_ATTENDANCE_ID = "local_attendance_id";
    public static final String CLM_ATTENDANCE_TYPE = "attandance_type";

    ////// Check in..........
    public static final String CLM_SERVER_CHECK_IN_ID = "server_checkin_id";
    public static final String CLM_LOCAL_CHECK_IN_ID = "local_checkin_id";
    public static final String CLM_CHECK_IN_TIME = "checkin_time";

    ///// Expense.........
    public static final String CLM_LOCAL_EXPENSE_ID = "local_expance_id";
    public static final String CLM_SERVER_EXPENSE_ID = "server_expance_id";
    public static final String CLM_AMOUNT = "amount";
    public static final String CLM_PAYOUT_STATUS = "payout_status";

    //Leave Apply
    public static final String CLM_LOCAL_LEAVE_ID = "local_leave_id";
    public static final String CLM_SERVER_LEAVE_ID = "server_leave_id";
    public static final String CLM_IS_DELETE = "is_delete";
    public static final String CLM_NOTE = "note";



    ////// App Setting Table...
    public static final String CLM_APP_SETTING_ID = "app_setting_id";
    public static final String CLM_SETTING_KEY = "setting_key";
    public static final String CLM_SETTING_VALUE = "setting_value";
    public static final String CLM_SETTING_VALUE_TYPE = "setting_value_type";
    public static final String CLM_APP_TYPE = "app_type";

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


    // Message
    public static final String CLM_MESSAGE_ID = "message_id";
    public static final String CLM_TICKET_NUMBER = "ticket_number";
    public static final String CLM_AUTHOR_ID = "author_id";
    public static final String CLM_RECEIPT_ID = "recipient_id";
    public static final String CLM_SUBJECT = "subject";
    public static final String CLM_FIRST_NAME = "first_name";
    public static final String CLM_LAST_NAME = "last_name";
    public static final String CLM_THREAD_ID = "thread_id";
    public static final String CLM_CUSTOM_FIELD = "custom_field";
    public static final String CLM_ACTIVITY_TYPE_ID = "activity_type_id";
    public static final String CLM_REF_TABLE = "ref_table";
    public static final String CLM_REF_ID = "ref_id";
    public static final String CLM_REF_CODE = "ref_code";
    public static final String CLM_IS_READ = "is_read";
    public static final String CLM_SYSTEM_GENERATED = "system_generated";
    public static final String CLM_ACCEPTED_SEND_TIME = "accepted_send_time";
    public static final String CLM_IS_SEND = "is_send";
    public static final String CLM_IN_QUEUE = "in_queue";
    public static final String CLM_DEVICE_TYPE = "device_type";
    public static final String CLM_DEVICE_TOKEN = "device_token";

    //Setting
    public static final String CLM_SETTING_ID = "setting_id";
    public static final String CLM_ADMIN_EMAIL = "admin_email";
    public static final String CLM_PORT = "port";
    public static final String CLM_SERVER = "server";
    public static final String CLM_ENCRYPTION = "encryption";
    public static final String CLM_APP_NAME = "app_name";
    public static final String CLM_TERM_AND_CONDITINS_USERS = "terms_and_conditions_user";
    public static final String CLM_PRIVACY_POLICY_USER = "privacy_policy_user";
    public static final String CLM_FAQ_USER = "faq_user";
    public static final String CLM_PRIVATE_KEY = "private_key";
    public static final String CLM_HOST = "host";


    // Role table
    public static final String CLM_ROLE_ID = "role_id";
    public static final String CLM_SORT_ORDER = "sort_order";
    public static final String CLM_ROLE_GROUP = "role_group";
    public static final String CLM_IS_CUSTOM = "is_custom";
    public static final String CLM_STATUS = "status";

    //Ref Table
    public static final String CLM_REF_TABLE_ID = "ref_table_id";
    public static final String CLM_NAME = "name";
    public static final String CLM_IS_TABLE = "is_table";

    //TABLE SECURITY MENU CONTROLLERS LINK
    public static final String CLM_MENU_CONTROLLERS_LINK_ID = "menu_controllers_link_id";
    public static final String CLM_MENU_ID = "menu_id";
    public static final String CLM_CONTROLLER_ID = "controller_id";
    public static final String CLM_CREATED = "created_on";
    public static final String CLM_UPDATED = "updated_on";


    public static final String CLM_USER_PERMISSION_ID = "user_permission_id";
    public static final String CLM__SECURIY_MENU_CONROLLERS_ACION_ID = "security_menu_controllers_action_id";


    ////////////////////
    ////// CREATE TABLE QUERY....
    ///////////////////

    /*
           USERINFO TABLE......
           user_name : DEFAULT login name
           gender : 1=> Male, 2=> Female
           company_id : Can be Null in case of Platform Admin users
           role_id : 1=> Customer, 2=> Driver, 3=> Web
           device_type : 1=> iOS, 2=> android, 3=> web
           varification_token : use for varification, for activation or forgot password
           status_id : 1=> active user, 2=> inactive, 3=> deleted, 4=> pending
     */
    static final String CREATE_TBL_USER_INFO = "CREATE TABLE " + TBL_USER_INFO + " (" +
            CLM_LOCAL_USER_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_USER_ID + " VARCHAR," +
            CLM_USERNAME + " VARCHAR," +
            CLM_EMAIL + " VARCHAR," +
            CLM_FIRST_NAME + " VARCHAR," +
            CLM_LAST_NAME + " VARCHAR," +
            CLM_PHONE_NO + " VARCHAR," +
            CLM_THUMB + " VARCHAR," +
            CLM_GENDER + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR DEFAULT '0'," +
            CLM_ROLE_ID + " VARCHAR," +
            CLM_BADGE_COUNT + " VARCHAR," +
            CLM_DEVICE_TYPE + " INTEGER DEFAULT 2," +
            CLM_DEVICE_TOKEN + " VARCHAR," +
            CLM_LAT + " VARCHAR," +
            CLM_LONG + " VARCHAR," +
            CLM_STATUS_ID + " INTEGER," +
            CLM_HOME_ADDRESS + " VARCHAR," +
            CLM_WORK_ADDRESS + " VARCHAR," +
            CLM_VERIFICATION_TOKEN + " VARCHAR," +
            CLM_VERIFICATION_TOKEN_TIME + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_LAST_LOGIN_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_OBJECT_MESSAGE + " VARCHAR," +
            CLM_IS_SMARTPHONE + " VARCHAR," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";


    /*
          PROJECT TABLE.........
          status_id : 1=> Active, 2=> inactive, 3=> delete                   DEFAULT '1'
     */
    static final String CREATE_TBL_PROJECT = "CREATE TABLE " + TBL_PROJECT + " (" +
            CLM_LOCAL_PROJECT_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_PROJECT_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR," +
            CLM_TITLE + " VARCHAR," +
            CLM_DESCRIPTION + " VARCHAR," +
            CLM_THUMB + " VARCHAR," +
            CLM_START_DATE + " VARCHAR," +
            CLM_END_DATE + " VARCHAR," +
            CLM_STATUS_ID + " INTEGER DEFAULT 1," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
           TASK TABLE........
           status_id : 1=> active, 2=> inactive, 3 => delete
     */
    static final String CREATE_TBL_TASK = "CREATE TABLE " + TBL_TASK + "(" +
            CLM_LOCAL_TASK_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_TASK_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR," +
            CLM_PROJECT_ID + " VARCHAR," +
            CLM_DESCRIPTION + " VARCHAR," +
            CLM_THUMB + " VARCHAR," +
            CLM_START_DATE + " VARCHAR," +
            CLM_END_DATE + " VARCHAR," +
            CLM_TYPE + " VARCHAR," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
           PROJECT_USER_LINK TABLE.......
     */
    static final String CREATE_TBL_PROJECT_USER_LINK = "CREATE TABLE " + TBL_PROJECT_USER_LINK + "(" +
            CLM_LOCAL_PROJECT_USER_LINK_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_PROJECT_USER_LINK_ID + " VARCHAR," +
            CLM_LOCAL_PROJECT_ID + " VARCHAR," +
            CLM_SERVER_PROJECT_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_SERVER_USER_ID + " VARCHAR," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
           TASK_USER_LINK TABLE.......
    */
    static final String CREATE_TBL_TASK_USER_LINK = "CREATE TABLE " + TBL_TASK_USER_LINK + "(" +
            CLM_LOCAL_TASK_USER_LINK_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_TASK_USER_LINK_ID + " VARCHAR," +
            CLM_LOCAL_TASK_ID + " VARCHAR," +
            CLM_SERVER_TASK_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_SERVER_USER_ID + " VARCHAR," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
           ATTENDANCE TABLE.........
           attandance_type : 1=> auto, 2=> menual                           DEFAULT 1
    */
    static final String CREATE_TBL_ATTENDANCE = "CREATE TABLE " + TBL_ATTENDANCE + " (" +
            CLM_LOCAL_ATTENDANCE_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_ATTENDANCE_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_SERVER_USER_ID + " VARCHAR," +
            CLM_START_DATE + " VARCHAR," +
            CLM_END_DATE + " VARCHAR," +
            CLM_ATTENDANCE_TYPE + " INTEGER DEFAULT 1," +
            CLM_STATUS_ID + " INTEGER DEFAULT 1," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
           CHECK_IN TABLE.........
    */
    static final String CREATE_TBL_CHECK_IN = "CREATE TABLE " + TBL_CHECK_IN + " (" +
            CLM_LOCAL_CHECK_IN_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_CHECK_IN_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR DEFAULT '0'," +
            CLM_LOCAL_ATTENDANCE_ID + " VARCHAR DEFAULT '0'," +
            CLM_SERVER_ATTENDANCE_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_SERVER_USER_ID + " VARCHAR," +
            CLM_CHECK_IN_TIME + " VARCHAR," +
            CLM_LAT + " VARCHAR DEFAULT '0.00000000'," +
            CLM_LONG + " VARCHAR DEFAULT '0.00000000'," +
            CLM_STATUS_ID + " INTEGER DEFAULT 1," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
            EXPENSE TABLE.........
            payout_status : '1=> Unpaid, 2=> paid',            DEFAULT '1'
            status_id : '1=> pending, 2=> approved, 3=> reject, 4=> delete',
    */
    static final String CREATE_TBL_EXPENSE = "CREATE TABLE " + TBL_EXPENSE + " (" +
            CLM_LOCAL_EXPENSE_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_EXPENSE_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_SERVER_USER_ID + " VARCHAR," +
            CLM_LOCAL_TASK_ID + " VARCHAR," +
            CLM_SERVER_TASK_ID + " VARCHAR," +
            CLM_LOCAL_PROJECT_ID + " VARCHAR," +
            CLM_SERVER_PROJECT_ID + " VARCHAR," +
            CLM_TITLE + " VARCHAR," +
            CLM_DESCRIPTION + " VARCHAR," +
            CLM_AMOUNT + " VARCHAR DEFAULT '0.00'," +
            CLM_PAYOUT_STATUS + " INTEGER DEFAULT 1," +
            CLM_STATUS_ID + " INTEGER," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
          status_id : '1=>pending, 2=> approved, 3=> reject',         DEFAULT '1'
          is_delete : '0=> not delete, 1=> deleted',                 DEFAULT '0'
    */
    static final String CREATE_TBL_LEAVE = "CREATE TABLE " + TBL_LEAVE + "(" +
            CLM_LOCAL_LEAVE_ID + " VARCHAR PRIMARY KEY," +
            CLM_SERVER_LEAVE_ID + " VARCHAR," +
            CLM_COMPANY_ID + " INTEGER," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_SERVER_USER_ID + " VARCHAR," +
            CLM_START_DATE + "  VARCHAR," +
            CLM_END_DATE + "  VARCHAR," +
            CLM_NOTE + "  VARCHAR," +
            CLM_STATUS_ID + " INTEGER DEFAULT 1," +
            CLM_IS_DELETE + " INTEGER DEFAULT 0," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";





    /*
       setting_value_type : 1=> test, 2 => json VARCHAR
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
        status_id: 1-Active, 2-Inactive, 3-Deleted, 4-Pending Activation
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


    /*
           author_id : 'Ref to user table, who raised this ticket',
           recipient_id : 'Ref to user table, who has received the message',
           thread_id : 'If ticket is refering to reply of previous ticket, can be null, content parant message id ',
           activity_type_id :'Ref to ActivityTypeID - TODO-- Ref to message_type tbable 1-Normal Message,2-Push,3-Email,4-Ticket,5-Booking Request,6-Staff Assignment,7-Booking Status Notification  8 - Accepr Reject Notification',
           ref_id : 'refer to original table from where this message is generated'
           status_id : 1=> active, 3=> Delete (Other status will not require)             DEFAULT '1'
           is_read : DEFAULT '0',
           system_generated : 1=> Yes, 0=> No                                  DEFAULT '0'
           accepted_send_time` : timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'future notification',
           is_send : DEFAULT '0',
           in_queue : DEFAULT '0',
           device_type : 1-iOS, 2-Android, 3-Web Admin
     */
    static final String CREATE_TBL_MESSAGE = "CREATE TABLE " + TBL_MESSAGE + " (" +
            CLM_MESSAGE_ID + " INTEGER PRIMARY KEY," +
            CLM_TICKET_NUMBER + " VARCHAR," +
            CLM_AUTHOR_ID + " VARCHAR," +
            CLM_RECEIPT_ID + " VARCHAR," +
            CLM_SUBJECT + " VARCHAR," +
            CLM_DESCRIPTION + " VARCHAR," +
            CLM_FIRST_NAME + " VARCHAR," +
            CLM_LAST_NAME + " VARCHAR," +
            CLM_THREAD_ID + " VARCHAR," +
            CLM_CUSTOM_FIELD + " VARCHAR," +
            CLM_ACTIVITY_TYPE_ID + " VARCHAR," +
            CLM_REF_TABLE + " VARCHAR," +
            CLM_REF_ID + " VARCHAR," +
            CLM_REF_CODE + " VARCHAR," +
            CLM_STATUS_ID + " VARCHAR," +
            CLM_IS_READ + " INTEGER DEFAULT 0," +
            CLM_SYSTEM_GENERATED + " INTEGER DEFAULT 0," +
            CLM_ACCEPTED_SEND_TIME + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_IS_SEND + " INTEGER DEFAULT 0," +
            CLM_IN_QUEUE + " INTEGER DEFAULT 0," +
            CLM_DEVICE_TYPE + " INTEGER DEFAULT 2," +
            CLM_DEVICE_TOKEN + " VARCHAR," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED + " DATETIME DEFAULT (CURRENT_TIMESTAMP))";


    static final String CREATE_TBL_SETTING = "CREATE TABLE " + TBL_SETTING + "(" +
            CLM_SETTING_ID + " INTEGER PRIMARY KEY," +
            CLM_ADMIN_EMAIL + " VARCHAR," +
            CLM_HOST + " VARCHAR," +
            CLM_PORT + " VARCHAR," +
            CLM_SERVER + " VARCHAR," +
            CLM_USERNAME + " VARCHAR," +
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

    static final String CREATE_TBL_ROLE = "CREATE TABLE " + TBL_ROLE + "(" +
            CLM_ROLE_ID + " INTEGER PRIMARY KEY," +
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
