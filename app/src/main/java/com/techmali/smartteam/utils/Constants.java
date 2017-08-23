package com.techmali.smartteam.utils;


public class Constants {

    // Local web service url
    public static final String SERVER_NAME = "local";
    public static final String SERVER_URL = "http://13.126.183.36/test/api/web/";


    public static final String TEMP_DIRECTORY_NAME = "SmartTeam";
    public static final String PREF_PROFILE_IMAGE = "profile_image";
    public static final String PREF_FULL_NAME = "full_name";
    public static final String PREF_NOTIFICATION_COUNT = "notification_count";

    // shared preference name for this app
    public static String SHARED_PREF_NAME = "smart_team_shared_prefs";

    public static String CURRENCY_SYMBOL = "S$";

    // Device Type
    public static String DEVICE_TYPE = "2";
    public static String APPLICATION_TYPE = "2";

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1005;

    //Action
    public static final int INTENT_CAMERA = 1000;
    public static final int INTENT_GALLERY = 1001;
    public static final int INTENT_CROP = 1002;
    public static final int INTENT_MULTIPLE_SELECT = 1003;

    // notification
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String REGISTRATION_TOKEN = "registrationToken";
    public static boolean IS_FROM_PUSH = false;
    public static final String EXTRA_FROM_FROM_NOTIFICATION_LIST = "fromNotificationList";

    public static final String DEFAULT_COUNTRY_CODE = "IN";
    public static String COUNTRY_CODE = "+91";
    public static String COUNTRY_NAME = "";

    public static final String TAB_PROJECT = "Project";
    public static final String TAB_TASK = "Task";
    public static final String TAB_REPORT = "Report";
    public static final String TAB_ATTENDANCE = "Ateendance";
    public static final String TAB_MESSAGE = "Message";


    public static final String TAB_HOME = "Home";
    public static final String TAB_MY_ASSIGNMENTS = "My Assignments";
    public static final String TAB_MY_TRAININGS = "My Trainings";
    public static final String TAB_MY_PROFILE = "My Profile";
    public static final String TAB_SETTINGS = "Settings";
    public static final String TAB_MENU = "Menu";


    public static final String USER_LIST = "5001";


    public static final String CURRENT_LATITUDE = "currant_latitude";
    public static final String CURRENT_LONGITUDE = "currant_longtitue";

    public static boolean IS_LOCATION_SERVICE_RUNNING = false;
}
