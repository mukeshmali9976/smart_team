package com.techmali.smartteam.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mukesh Mali on 5/21/2017.
 */

@SuppressLint("ParcelCreator")
public class BaseResponse implements Parcelable {

   int status;
   String message = "";
   String header_token = "";
   String notification_count;
   String app_version;
   String device_type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeader_token() {
        return header_token;
    }

    public void setHeader_token(String header_token) {
        this.header_token = header_token;
    }

    public String getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(String notification_count) {
        this.notification_count = notification_count;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }


    protected BaseResponse(Parcel in) {
        status = in.readInt();
        message = in.readString();
        header_token = in.readString();
        notification_count = in.readString();
        app_version = in.readString();
        device_type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(status);
        parcel.writeString(message);
        parcel.writeString(header_token);
        parcel.writeString(notification_count);
        parcel.writeString(app_version);
        parcel.writeString(device_type);
    }
    public BaseResponse() {
    }

}
