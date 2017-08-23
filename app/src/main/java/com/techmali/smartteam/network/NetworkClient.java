
package com.techmali.smartteam.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.techmali.smartteam.R;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.ui.views.MyProgressDialog;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NetworkClient extends AsyncTask<Void, Void, String> {

    private static final String TAG = NetworkClient.class.getSimpleName();

    private RequestListener listener;
    private int requestId;
    private HashMap<String, String> reqParams;
    private HashMap<String, String> reqHeader;
    private File file;
    private String fileName;
    private RequestMethod reqMethod;
    private String url;
    private Context context;
    private boolean isProgressVisible = false;
    private MyProgressDialog progressDialog;
    private SharedPreferences prefManager = null;
    private PackageInfo pInfo;
    private boolean isError = false;

    public NetworkClient(Context context, int requestId, RequestListener listener,
                         String url, HashMap<String, String> reqParams, RequestMethod reqMethod,
                         boolean isProgressVisible) {

        this.listener = listener;
        this.requestId = requestId;
        this.reqParams = reqParams;
        this.reqMethod = reqMethod;
        this.url = url;
        this.context = context;
        this.isProgressVisible = isProgressVisible;
        prefManager = CryptoManager.getInstance(context).getPrefs();
        try {
            pInfo = Utils.getSoftwareVersion(this.context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NetworkClient(Context context, int requestId, RequestListener listener,
                         String url, HashMap<String, String> reqParams, File file, String fileName, RequestMethod reqMethod,
                         boolean isProgressVisible) {

        this.listener = listener;
        this.requestId = requestId;
        this.reqParams = reqParams;
        this.file = file;
        this.fileName = fileName;
        this.reqMethod = reqMethod;
        this.url = url;
        this.context = context;
        this.isProgressVisible = isProgressVisible;
        prefManager = CryptoManager.getInstance(context).getPrefs();
        try {
            pInfo = Utils.getSoftwareVersion(this.context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public NetworkClient(Context context, int requestId, RequestListener listener,
                         String url, RequestMethod reqMethod,
                         boolean isProgressVisible) {

        this.listener = listener;
        this.requestId = requestId;
        this.reqMethod = reqMethod;
        this.url = url;
        this.context = context;
        this.isProgressVisible = isProgressVisible;
        prefManager = CryptoManager.getInstance(context).getPrefs();
        try {
            pInfo = Utils.getSoftwareVersion(this.context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isProgressVisible) {
            showProgressDialog();
        }
    }

    @Override
    protected String doInBackground(Void... params) {

        try {

            if (Utils.isInternetAvailable(context)) {

                reqHeader = new HashMap<>();
                if (!Utils.isEmptyString(prefManager.getString(PARAMS.KEY_HEADER_TOKEN, ""))) {
                    reqHeader.put(PARAMS.TAG_HEADER_TOKEN, prefManager.getString(PARAMS.KEY_HEADER_TOKEN, ""));
                    reqHeader.put(PARAMS.TAG_APP_VERSION, pInfo.versionName);
                }

                Log.d(TAG, "Request : " + url);
                Log.i(TAG, "==========================================================================================\n");

                Log.i(TAG, "==========================================================================================");

                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                clientBuilder.connectTimeout(60, TimeUnit.SECONDS);
                clientBuilder.writeTimeout(60, TimeUnit.SECONDS);
                clientBuilder.readTimeout(60, TimeUnit.SECONDS);
                OkHttpClient client = clientBuilder.build();

                if (reqMethod == RequestMethod.GET) {

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .header(PARAMS.TAG_HEADER_TOKEN, prefManager.getString(PARAMS.KEY_HEADER_TOKEN, ""))
                            .addHeader(PARAMS.TAG_APP_VERSION, pInfo.versionName)
                            .addHeader(PARAMS.TAG_DEVICE_TYPE,Constants.DEVICE_TYPE)
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    int statusCode = response.code();

                    String message = response.message();
                    String res = response.body().string();

                    Log.e(TAG, "URL => " + url);
                    Log.e(TAG, "header_token => " + prefManager.getString(PARAMS.KEY_HEADER_TOKEN, ""));
                    Log.e(TAG, "Params => " + reqParams);
                    Log.e(TAG, "Response => " + res);

                    JSONObject jObj = new JSONObject();
                    jObj.put("statusCode", 1);
                    jObj.put("response", message);
                    return jObj.toString();

                } else if (reqMethod == RequestMethod.POST) {

                    if (Utils.isEmptyString(reqParams.toString())) {
                        isError = true;
                        return "Can't perform POST request with EMPTY parameters";
                    }

                    String mRequestParams = (!Utils.isEmptyString(reqParams.get(PARAMS.TAG_PARAMS)) ? reqParams.get(PARAMS.TAG_PARAMS) : "");
                    RequestBody body = new FormBody.Builder()
                            .add(PARAMS.TAG_PARAMS, mRequestParams)
                            .build();

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .header(PARAMS.TAG_HEADER_TOKEN, prefManager.getString(PARAMS.KEY_HEADER_TOKEN, ""))
                            .addHeader(PARAMS.TAG_DEVICE_TYPE, Constants.DEVICE_TYPE)
                            .addHeader(PARAMS.TAG_DEVICE_TOKEN, prefManager.getString(Constants.REGISTRATION_TOKEN, ""))
                            .url(url)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    int statusCode = response.code();

                    String message = response.message();
                    String res = response.body().string();

                    Log.e(TAG, "URL => " + url);
                    Log.e(TAG, "header_token => " + prefManager.getString(PARAMS.KEY_HEADER_TOKEN, ""));
                    Log.e(TAG, "Params => " + reqParams);
                    Log.e(TAG, "Response => " + res);

                    JSONObject jObj = new JSONObject();
                    jObj.put("statusCode", 1);
                    jObj.put("response", res);
                    return jObj.toString();

                } else if (reqMethod == RequestMethod.MULTIPART) {

                    MediaType MEDIA_TYPE = fileName.endsWith("png") ?
                            MediaType.parse("image/png") : MediaType.parse("image/jpeg");

                    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
                    multipartBuilder.setType(MultipartBody.FORM);
                    //TODO: change data part key as per API key
                    multipartBuilder.addFormDataPart("file", fileName, RequestBody.create(MEDIA_TYPE, file));
                    //  multipartBuilder.addFormDataPart(PARAMS.TAG_PARAMS, reqParams);
                    RequestBody body = multipartBuilder.build();

                    Request.Builder reqBuilder = new Request.Builder();
                    reqBuilder.url(url);
                    reqBuilder.post(body);
                    Request request = reqBuilder.build();
                    Response response = client.newCall(request).execute();
                    int statusCode = response.code();
                    String message = response.message();
                    String res = response.body().string();

                    Log.e(TAG, "URL => " + url);
                    Log.e(TAG, "header_token => " + prefManager.getString(PARAMS.KEY_HEADER_TOKEN, ""));
                    Log.e(TAG, "Params => " + reqParams);
                    Log.e(TAG, "Response => " + res);

                    JSONObject jObj = new JSONObject();
                    jObj.put("statusCode", 1);
                    jObj.put("response", res);
                    return jObj.toString();
                }

            } else {

                JSONObject jObj = new JSONObject();
                jObj.put("statusCode", 0);
                jObj.put("response", context.getString(R.string.lbl_no_internet));
                return jObj.toString();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            JSONObject jObj = new JSONObject();
            try {
                jObj.put("statusCode", 0);
                jObj.put("response", e.toString());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return jObj.toString();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject jObj = new JSONObject(result);
            if (jObj.getInt("statusCode") == 1) {
                listener.onSuccess(requestId, jObj.getString("response"));
            } else {
                listener.onError(requestId, jObj.getString("response"));
            }
        } catch (Exception e) {
            listener.onError(requestId, result);
        } finally {
            dismissProgressDialog();
        }
    }

    // show progress dialog
    private void showProgressDialog() {
        progressDialog = new MyProgressDialog(context);
    }

    //  dismiss progress dialog
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
