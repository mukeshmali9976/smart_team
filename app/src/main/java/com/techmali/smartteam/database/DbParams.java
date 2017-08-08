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
    public static final String TBL_TASK_TYPE = "task_type";
    public static final String TBL_PROJECT_USER_LINK = "project_user_link";
    public static final String TBL_TASK_USER_LINK = "task_user_link";
    public static final String TBL_ATTENDANCE = "attendance";
    public static final String TBL_CHECK_IN = "checkin";
    public static final String TBL_EXPENSE = "expance";
    public static final String TBL_LEAVE = "leave";
    public static final String TBL_SECURITY_MENU = "security_menu";
    public static final String TBL_SECURITY_MENU_CONTROLLERS_ACTION = "security_menu_controllers_action";
    public static final String TBL_SECURITY_MENU_CONTROLLERS_LINK = "security_menu_controllers_link";
    public static final String TBL_SECURITY_CONTROLLERS = "security_controllers";


    public static final String TBL_ROLE = "role";
    public static final String TBL_MESSAGE = "message";
    public static final String TBL_REF_TABLE = "ref_table";
    public static final String TBL_APP_SETTING = "app_setting";
    public static final String TBL_COMPANY = "company";
    public static final String TBL_SETTING = "setting";
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
    public static final String CLM_USER_ID = "user_id";
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
    public static final String CLM_TASK_ID = "task_id";
    public static final String CLM_LOCAL_TASK_ID = "local_task_id";
    public static final String CLM_TYPE = "type";

    ////// Task Type.......
    public static final String CLM_TASK_TYPE_ID = "task_type_id";
    public static final String CLM_TASK_TYPE_NAME = "type_name";

    ////// Project User Link.......
    public static final String CLM_PROJECT_ID = "project_id";
    public static final String CLM_PROJECT_USER_LINK_ID = "project_user_link_id";
    public static final String CLM_LOCAL_PROJECT_USER_LINK_ID = "local_project_user_link_id";

    ////// Task User Link.......
    public static final String CLM_TASK_USER_LINK_ID = "task_user_link_id";
    public static final String CLM_LOCAL_TASK_USER_LINK_ID = "local_task_user_link_id";

    ////// ATTENDANCE........
    public static final String CLM_ATTENDANCE_ID = "attendance_id";
    public static final String CLM_LOCAL_ATTENDANCE_ID = "local_attendance_id";
    public static final String CLM_ATTENDANCE_TYPE = "attandance_type";

    ////// Check in..........
    public static final String CLM_CHECK_IN_ID = "checkin_id";
    public static final String CLM_LOCAL_CHECK_IN_ID = "local_checkin_id";
    public static final String CLM_CHECK_IN_TIME = "checkin_time";

    ///// Expense.........
    public static final String CLM_LOCAL_EXPENSE_ID = "local_expance_id";
    public static final String CLM_EXPENSE_ID = "expance_id";
    public static final String CLM_AMOUNT = "amount";
    public static final String CLM_PAYOUT_STATUS = "payout_status";

    ////// Leave..........
    public static final String CLM_LOCAL_LEAVE_ID = "local_leave_id";
    public static final String CLM_LEAVE_ID = "leave_id";
    public static final String CLM_IS_DELETE = "is_delete";
    public static final String CLM_NOTE = "note";

    ////// Security Menu........
    public static final String CLM_SECURITY_MENU_ID = "security_menu_id";
    public static final String CLM_MENU_NAME = "menu_name";
    public static final String CLM_MENU_DISPLAY_NAME = "menu_display_name";
    public static final String CLM_MENU_LABEL = "menu_label";
    public static final String CLM_URL = "url";
    public static final String CLM_URL_PARAMETER = "url_parameter";
    public static final String CLM_LINK_OPTIONS = "link_options";
    public static final String CLM_ICON = "icon";
    public static final String CLM_FOR = "for";
    public static final String CLM_SORT_ORDER = "sort_order";
    public static final String CLM_IS_ALWAYS_TRUE = "is_always_true";
    public static final String CLM_STATUS = "status";

    ////// Security Menu Controller Action.......
    public static final String CLM_SECURITY_MENU_CONTROLLERS_ACTION_ID = "security_menu_controllers_action_id";
    public static final String CLM_MENU_ID = "menu_id";
    public static final String CLM_CONTROLLER_ID = "controllers_id";
    public static final String CLM_MENU_CONTROLLERS_LINK_ID = "menu_controllers_link_id";
    public static final String CLM_ACTION_NAME = "action_name";
    public static final String CLM_ACTION_DISPLAY_NAME = "action_display_name";

    ////// Security Controller....
    public static final String CLM_SECURITY_CONTROLLER_ID = "security_controllers_id";
    public static final String CLM_NAME = "name";
    public static final String CLM_DISPLAY_NAME = "display_name";
    public static final String CLM_CONTROLLER_NAME = "controller_name";


    ////// Role table.......
    public static final String CLM_ROLE_ID = "role_id";
    public static final String CLM_ROLE_GROUP = "role_group";
    public static final String CLM_IS_CUSTOM = "is_custom";

    ////// Message............
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

    ///// Company.......
    public static final String CLM_COMPANY_NAME = "company_name";
    public static final String CLM_REG_NO = "reg_no";
    public static final String CLM_THUMB = "thumb";
    public static final String CLM_CONTACT_PERSON = "contact_person";
    public static final String CLM_PHONE_NO = "phone_no";
    public static final String CLM_EMAIL = "email";
    public static final String CLM_ADMIN_USER_ID = "admin_user_id";
    public static final String CLM_OBJECT_MESSAGE = "object_message";

    ////// Ref Table.......
    public static final String CLM_REF_TABLE_ID = "ref_table_id";
    public static final String CLM_IS_TABLE = "is_table";

    ////// App Setting........
    public static final String CLM_APP_SETTING_ID = "app_setting_id";
    public static final String CLM_SETTING_KEY = "setting_key";
    public static final String CLM_SETTING_VALUE = "setting_value";
    public static final String CLM_SETTING_VALUE_TYPE = "setting_value_type";
    public static final String CLM_APP_TYPE = "app_type";

    ////// Setting........
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

    ////// Security_Action_User_Permission........
    public static final String CLM_USER_PERMISSION_ID = "user_permission_id";


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
            CLM_USER_ID + " VARCHAR," +
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
            CLM_TASK_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR," +
            CLM_PROJECT_ID + " VARCHAR," +
            CLM_LOCAL_PROJECT_ID + " VARCHAR," +
            CLM_DESCRIPTION + " VARCHAR," +
            CLM_TITLE + " VARCHAR," +
            CLM_THUMB + " VARCHAR," +
            CLM_START_DATE + " VARCHAR," +
            CLM_END_DATE + " VARCHAR," +
            CLM_TYPE + " VARCHAR," +
            CLM_STATUS_ID + " INTEGER, " +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
            TASK_TYPE TABLE.........
     */
    static final String CREATE_TBL_TASK_TYPE = "CREATE TABLE " + TBL_TASK_TYPE + "(" +
            CLM_TASK_TYPE_ID + " VARCHAR PRIMARY KEY," +
            CLM_TASK_TYPE_NAME + " VARCHAR," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
           PROJECT_USER_LINK TABLE.......
           status_id : 1=> active, 2=> inactive, 3 => delete            DEFAULT : 1
     */
    static final String CREATE_TBL_PROJECT_USER_LINK = "CREATE TABLE " + TBL_PROJECT_USER_LINK + "(" +
            CLM_LOCAL_PROJECT_USER_LINK_ID + " VARCHAR PRIMARY KEY," +
            CLM_PROJECT_USER_LINK_ID + " VARCHAR," +
            CLM_LOCAL_PROJECT_ID + " VARCHAR," +
            CLM_PROJECT_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_USER_ID + " VARCHAR," +
            CLM_STATUS_ID + " INTEGER DEFAULT 1," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
           TASK_USER_LINK TABLE.......
           status_id : 1=> active, 2=> inactive, 3 => delete            DEFAULT : 1
    */
    static final String CREATE_TBL_TASK_USER_LINK = "CREATE TABLE " + TBL_TASK_USER_LINK + "(" +
            CLM_LOCAL_TASK_USER_LINK_ID + " VARCHAR PRIMARY KEY," +
            CLM_TASK_USER_LINK_ID + " VARCHAR," +
            CLM_LOCAL_TASK_ID + " VARCHAR," +
            CLM_TASK_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_USER_ID + " VARCHAR," +
            CLM_STATUS_ID + " INTEGER DEFAULT 1," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
           ATTENDANCE TABLE.........
           attandance_type : 1=> auto, 2=> manual                           DEFAULT 1
    */
    static final String CREATE_TBL_ATTENDANCE = "CREATE TABLE " + TBL_ATTENDANCE + " (" +
            CLM_LOCAL_ATTENDANCE_ID + " VARCHAR PRIMARY KEY," +
            CLM_ATTENDANCE_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_USER_ID + " VARCHAR," +
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
            CLM_CHECK_IN_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR DEFAULT '0'," +
            CLM_LOCAL_ATTENDANCE_ID + " VARCHAR DEFAULT '0'," +
            CLM_ATTENDANCE_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_USER_ID + " VARCHAR," +
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
            CLM_EXPENSE_ID + " VARCHAR," +
            CLM_COMPANY_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_USER_ID + " VARCHAR," +
            CLM_LOCAL_TASK_ID + " VARCHAR," +
            CLM_TASK_ID + " VARCHAR," +
            CLM_LOCAL_PROJECT_ID + " VARCHAR," +
            CLM_PROJECT_ID + " VARCHAR," +
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
          LEAVE TABLE.........
          status_id : '1=>pending, 2=>approved, 3=>reject',          DEFAULT '1'
          is_delete : '0=> not delete, 1=> deleted',                 DEFAULT '0'
    */
    static final String CREATE_TBL_LEAVE = "CREATE TABLE " + TBL_LEAVE + "(" +
            CLM_LOCAL_LEAVE_ID + " VARCHAR PRIMARY KEY," +
            CLM_LEAVE_ID + " VARCHAR," +
            CLM_COMPANY_ID + " INTEGER," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_USER_ID + " VARCHAR," +
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
            SECURITY_MENU TABLE.........
            menu_label : json or txt of label
            url : url text
            url_parameter : json text of url array
            link_options : json text for link option
            is_always_true : this menu will always appear in list
            status: 1=>Active, 2=>inactive, 3=>Deleted              DEFAULT => 1
     */
    static final String CREATE_TBL_SECURITY_MENU = "CREATE TABLE " + TBL_SECURITY_MENU + " (" +
            CLM_SECURITY_MENU_ID + " VARCHAR PRIMARY KEY," +
            CLM_MENU_NAME + " VARCHAR," +
            CLM_MENU_DISPLAY_NAME + " VARCHAR," +
            CLM_MENU_LABEL + " VARCHAR," +
            CLM_URL + " VARCHAR," +
            CLM_URL_PARAMETER + " VARCHAR," +
            CLM_LINK_OPTIONS + " VARCHAR," +
            CLM_FOR + " VARCHAR," +
            CLM_ICON + " VARCHAR," +
            CLM_SORT_ORDER + " INTEGER," +
            CLM_IS_ALWAYS_TRUE + " VARCHAR," +
            CLM_STATUS + " INTEGER DEFAULT 1," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
            SECURITY_MENU_CONTROLLER_LINK_ACTION TABLE.....
            menu_id : ref. to security_module table
            controllers_id : ref. to security_module_permission table
            action_name : comma separated action name
            action_display_name : ref. to role table
     */
    static final String CREATE_TBL_SECURITY_MENU_CONTROLLER_ACTION = "CREATE TABLE " + TBL_SECURITY_MENU_CONTROLLERS_ACTION + " (" +
            CLM_SECURITY_MENU_CONTROLLERS_ACTION_ID + " VARCHAR PRIMARY KEY," +
            CLM_MENU_ID + " VARCHAR," +
            CLM_CONTROLLER_ID + " VARCHAR," +
            CLM_MENU_CONTROLLERS_LINK_ID + " VARCHAR," +
            CLM_ACTION_NAME + " VARCHAR," +
            CLM_ACTION_DISPLAY_NAME + " VARCHAR," +
            CLM_SORT_ORDER + " INTEGER DEFAULT 0," +
            CLM_STATUS + " INTEGER," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
            SECURITY_MENU_CONTROLLER_LINK TABLE...........
     */
    static final String CREATE_TBL_SECURITY_MENU_CONTROLLERS_LINK = "CREATE TABLE " + TBL_SECURITY_MENU_CONTROLLERS_LINK + " (" +
            CLM_MENU_CONTROLLERS_LINK_ID + " VARCHAR PRIMARY KEY," +
            CLM_MENU_ID + " VARCHAR," +
            CLM_CONTROLLER_ID + " VARCHAR," +
            CLM_STATUS + " INTEGER," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
            SECURITY_CONTROLLER TABLE..........
            name : Ref. to security_module table
            status : 1=> Active, 2=> Inactive, 3=> Deleted              DEFAULT 1
     */
    static final String CREATE_TBL_SECURITY_CONTROLLER = "CREATE TABLE " + TBL_SECURITY_CONTROLLERS + " (" +
            CLM_SECURITY_CONTROLLER_ID + " VARCHAR PRIMARY KEY," +
            CLM_NAME + " VARCHAR," +
            CLM_DISPLAY_NAME + " VARCHAR," +
            CLM_CONTROLLER_NAME + " VARCHAR," +
            CLM_SORT_ORDER + " INTEGER DEFAULT 0," +
            CLM_STATUS + " INTEGER DEFAULT 1," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
            COMPANY TABLE.....
            thumb: Company Logo image
            admin_user_id: Ref to Admin user account which is associated with user
            status_id: 1=> Active, 2=> Inactive, 3=> Deleted, 4=> Pending Activation
     */
    static final String CREATE_TBL_COMPANY = "CREATE TABLE " + TBL_COMPANY + "(" +
            CLM_COMPANY_ID + " VARCHAR primary key," +
            CLM_COMPANY_NAME + " VARCHAR," +
            CLM_REG_NO + " VARCHAR," +
            CLM_THUMB + " VARCHAR," +
            CLM_CONTACT_PERSON + " VARCHAR," +
            CLM_PHONE_NO + " VARCHAR," +
            CLM_EMAIL + " VARCHAR," +
            CLM_ADMIN_USER_ID + " VARCHAR," +
            CLM_OBJECT_MESSAGE + " VARCHAR," +
            CLM_STATUS_ID + " INTEGER," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";


    /*
         MESSAGE TABLE.........
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
            CLM_MESSAGE_ID + " VARCHAR PRIMARY KEY," +
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
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
            REF_TABLE TABLE........
     */
    static final String CREATE_TBL_REF_TABLE = "CREATE TABLE " + TBL_REF_TABLE + " (" +
            CLM_REF_TABLE_ID + " VARCHAR PRIMARY KEY," +
            CLM_NAME + " VARCHAR," +
            CLM_IS_TABLE + " INTEGER DEFAULT 1)";

    /*
            SECURITY_ACTION_USER_PERMISSION TABLE.......
     */
    static final String CREATE_TBL_SECURITY_ACTION_USER_PERMISSION = "CREATE TABLE " + TBL_SECURITY_ACTION_USER_PERMISSION + " (" +
            CLM_USER_PERMISSION_ID + " VARCHAR PRIMARY KEY," +
            CLM_SECURITY_MENU_CONTROLLERS_ACTION_ID + " VARCHAR," +
            CLM_LOCAL_USER_ID + " VARCHAR," +
            CLM_STATUS + " INTEGER DEFAULT 1," +
            CLM_CREATED_BY + " VARCHAR," +
            CLM_UPDATED_BY + " VARCHAR," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_ON + " DATETIME," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";

    /*
        SETTING TABLE.........
        admin_email : 1=> Admin, 2=> App
        app_name : Setting tag
 */
    static final String CREATE_TBL_SETTING = "CREATE TABLE " + TBL_SETTING + "(" +
            CLM_SETTING_ID + " VARCHAR PRIMARY KEY," +
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

    /*
            APP_SETTING TABLE.........
            setting_value_type : 1=> test, 2 => json VARCHAR
            app_type : 0=> Both, 1=>user, 2=> driver
    */
    static final String CREATE_TBL_APP_SETTING = "CREATE TABLE " + TBL_APP_SETTING + "(" +
            CLM_APP_SETTING_ID + "  VARCHAR PRIMARY KEY," +
            CLM_SETTING_KEY + " VARCHAR," +
            CLM_SETTING_VALUE + " VARCHAR," +
            CLM_SETTING_VALUE_TYPE + " INTEGER," +
            CLM_APP_TYPE + " INTEGER," +
            CLM_STATUS_ID + " INTEGER)";

    /*
            Role.....
            role_group : enum('1','2','3','4','5','6','7','8','9') 1-Super Admin[Platform Staff],2- Company Admin[Bus Staff users]
            is_custom : enum('1','2') 1-System Role,2-Custom Role                   DEFAULT 2
            status : enum('1','2','3')  1-Active,2-InActive,3-Deleted                 DEFAULT 1
     */
    static final String CREATE_TBL_ROLE = "CREATE TABLE " + TBL_ROLE + "(" +
            CLM_ROLE_ID + " VARCHAR PRIMARY KEY," +
            CLM_NAME + "  VARCHAR," +
            CLM_SORT_ORDER + " INTEGER DEFAULT 0," +
            CLM_ROLE_GROUP + " VARCHAR," +
            CLM_IS_CUSTOM + " VARCHAR," +
            CLM_STATUS + " INTEGER," +
            CLM_CREATED_BY + " INTEGER," +
            CLM_CREATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_UPDATED_BY + " INTEGER," +
            CLM_UPDATED_ON + " DATETIME DEFAULT (CURRENT_TIMESTAMP)," +
            CLM_IS_UPDATED + " INTEGER DEFAULT 0)";
}
