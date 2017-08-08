package com.techmali.smartteam.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gaurav on 7/15/2017.
 */

public class SyncUserInfo implements Parcelable {

    private String company_id;
    private String server_user_id;
    private String role_id;
    private String status_id;
    private String home_address;
    private String first_name;
    private String work_address;
    private String username;
    private String email;
    private String local_user_id;
    private String phone_no;
    private String last_name;
    private String gender;
    private String thumb;

    // For DB entry
    private int is_updated = 0;

    // For Member selection list
    private boolean selected = false;

    protected SyncUserInfo(Parcel in) {
        company_id = in.readString();
        server_user_id = in.readString();
        role_id = in.readString();
        status_id = in.readString();
        home_address = in.readString();
        first_name = in.readString();
        work_address = in.readString();
        username = in.readString();
        email = in.readString();
        local_user_id = in.readString();
        phone_no = in.readString();
        last_name = in.readString();
        gender = in.readString();
        thumb = in.readString();
        is_updated = in.readInt();
        selected = in.readByte() != 0;
    }

    public static final Creator<SyncUserInfo> CREATOR = new Creator<SyncUserInfo>() {
        @Override
        public SyncUserInfo createFromParcel(Parcel in) {
            return new SyncUserInfo(in);
        }

        @Override
        public SyncUserInfo[] newArray(int size) {
            return new SyncUserInfo[size];
        }
    };

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getServer_user_id() {
        return server_user_id;
    }

    public void setServer_user_id(String server_user_id) {
        this.server_user_id = server_user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getWork_address() {
        return work_address;
    }

    public void setWork_address(String work_address) {
        this.work_address = work_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocal_user_id() {
        return local_user_id;
    }

    public void setLocal_user_id(String local_user_id) {
        this.local_user_id = local_user_id;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getIs_updated() {
        return is_updated;
    }

    public void setIs_updated(int is_updated) {
        this.is_updated = is_updated;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(company_id);
        parcel.writeString(server_user_id);
        parcel.writeString(role_id);
        parcel.writeString(status_id);
        parcel.writeString(home_address);
        parcel.writeString(first_name);
        parcel.writeString(work_address);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(local_user_id);
        parcel.writeString(phone_no);
        parcel.writeString(last_name);
        parcel.writeString(gender);
        parcel.writeString(thumb);
        parcel.writeInt(is_updated);
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}