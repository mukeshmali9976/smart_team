package com.techmali.smartteam.request;

public class PARAMS {

    // web service parameter keys
    public static final String TAG_PARAMS = "params";
    public static final String TAG_STATUS = "status";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_HEADER_TOKEN = "header_token";
    public static final String TAG_RESULT = "result";
    public static final String TAG_LAST_SYNC_DATE = "last_syncdate";
    public static final String TAG_APP_VERSION = "app_version";

    // Login
    public static final String TAG_PHONE = "phone";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_DEVICE_TYPE = "devicetype";

    // Login Detail
    public static final String TAG_USER_DATA = "user_data";
    public static final String TAG_ROLE_LIST = "role_list";

    // Project Detail
    public static final String TAG_PROJECT_DETAIL = "project_detail";
    public static final String TAG_USER_LIST = "user_list";
    public static final String TAG_TASK_LIST = "task_list";


    public static final String TAG_DEVICE_TOKEN = "device_token";
    public static final String TAG_APPLICATION_TYPE = "application_type";




    //Add Media
    public static final String TAG_FILE_THUMB = "thumb";


    // Status codes.
    public static final int TAG_STATUS_200 = 200;                       // Success tag for DB operations
    public static final int TAG_STATUS_503 = 503;
    public static final int TAG_STATUS_507 = 507;
    public static final int TAG_STATUS_1001 = 1001;
    public static final int TAG_STATUS_4004 = 4004;                     // Blank result for DB operations

    public static final int TAG_STATUS_112 = 112;
    public static final int TAG_STATUS_121 = 121;
    public static final int TAG_STATUS_120 = 120;


    // Shared Preference keys
    public static final String KEY_AES_PASS_KEY = "aesPassKey";
    public static final String KEY_HEADER_TOKEN = "header_token";
    public static final String KEY_LAST_SYNC_DATE = "last_syncdate";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    public static final String KEY_LOGGED_IN_USER_ID = "user_id";
    public static final String KEY_UNIQUE_CODE = "unique_code";
    public static final String KEY_COMPANY_ID = "company_id";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_ROLE_NAME = "role_name";
    public static final String KEY_STATUS_ID = "status_id";
    public static final String KEY_ROLE_LIST = "role_list";
}
