package com.techmali.smartteam.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;


import com.techmali.smartteam.utils.Constants;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class NetworkManager implements RequestListener {

    public static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager instance = null;
    private Set<RequestListener> arrRequestListeners = null;
    private int requestId;
    public boolean isProgressVisible = false;

    private NetworkManager() {
        arrRequestListeners = new HashSet<>();
        arrRequestListeners = Collections.synchronizedSet(arrRequestListeners);
    }

    public static NetworkManager getInstance() {
        if (instance == null)
            instance = new NetworkManager();
        return instance;
    }

    public synchronized int addRequest(final HashMap<String, String> params, Context context, RequestMethod reqMethod, String apiMethod) {

        try {

            String url = Constants.SERVER_URL + apiMethod;
            requestId = UniqueNumberUtils.getInstance().getUniqueId();

            NetworkClient networkClient = new NetworkClient(context, requestId, this, url, params, reqMethod, isProgressVisible);
            networkClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            onError(requestId, e.toString() + e.getMessage());
        }

        return requestId;
    }



    public synchronized int addMultipartRequest(final HashMap<String,String> params, File file, String fileName, Context context, RequestMethod reqMethod, String apiMethod) {

        try {

            String url = Constants.SERVER_URL + apiMethod;
            requestId = UniqueNumberUtils.getInstance().getUniqueId();

            NetworkClient networkClient = new NetworkClient(context, requestId, this, url, params, file, fileName, reqMethod, isProgressVisible);
            networkClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            onError(requestId, e.toString() + e.getMessage());
        }

        return requestId;
    }

    public void isProgressBarVisible(boolean isProgressVisible) {
        this.isProgressVisible = isProgressVisible;
    }

    public void setListener(RequestListener listener) {
        try {
            if (listener != null && !arrRequestListeners.contains(listener)) {
                arrRequestListeners.add(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int id, String response) {

        //Log.d(NetworkManager.class.getSimpleName(), "[onSuccess] arrRequestListeners=" + arrRequestListeners.size());
        if (arrRequestListeners != null && arrRequestListeners.size() > 0) {
            for (RequestListener listener : arrRequestListeners) {
                if (listener != null)
                    listener.onSuccess(id, response);
            }
        }
    }

    @Override
    public void onError(int id, String message) {
        try {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.d(NetworkManager.class.getSimpleName(), "[onError] arrRequestListeners=" + arrRequestListeners.size());
        if (arrRequestListeners != null && arrRequestListeners.size() > 0) {
            for (final RequestListener listener : arrRequestListeners) {
                if (listener != null) {
                    listener.onError(id, message);
                }
            }
        }
    }

    public void removeListener(RequestListener listener) {
        try {
            arrRequestListeners.remove(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
