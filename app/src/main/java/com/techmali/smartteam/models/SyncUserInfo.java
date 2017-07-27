package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 7/15/2017.
 */

public class SyncUserInfo implements Serializable {

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

    private int is_updated = 0;

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
}