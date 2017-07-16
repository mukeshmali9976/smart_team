package com.techmali.smartteam.request;


import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class RequestBuilder extends PARAMS {
    private static final String TAG = RequestBuilder.class.getSimpleName();


    // For Login data
    public static final String METHOD_LOGIN = "user/login";

    // for login detail
    public static final String METHOD_LOGIN_DETAIL = "user/logindetail";

    // For DB sync
    public static final String METHOD_SYNC_DATA = "sync/sync";


    public static HashMap<String, String> blankRequest() {

        JSONObject jObjReq = new JSONObject();

        HashMap<String, String> parameters = new HashMap<>();
        Log.i(TAG, jObjReq.toString());
        return parameters;
    }

    public static HashMap<String, String> performLogin(String email, String password) {

        JSONObject jObjReq = new JSONObject();
        HashMap<String, String> parameters = new HashMap<>();
        try {
            jObjReq.put(PARAMS.TAG_EMAIL, email);
            jObjReq.put(PARAMS.TAG_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameters.put(TAG_PARAMS, jObjReq.toString());
        Log.i(TAG, jObjReq.toString());
        return parameters;
    }


}
