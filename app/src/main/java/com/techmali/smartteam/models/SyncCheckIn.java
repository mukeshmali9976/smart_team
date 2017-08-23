package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 7/22/2017.
 */

public class SyncCheckIn implements Serializable {

    private String local_checkin_id;
    private String server_checkin_id;
    private String company_id;
    private String user_id;
    private String local_user_id;
    private String title;
    private String description;
    private String start_date;
    private String end_date;
    private String checkin_type;
    private String status_id;
    private String created_by;
    private String created_on;
    private String updated_by;
    private String updated_on;

    public String getLocal_checkin_id() {
        return local_checkin_id;
    }

    public void setLocal_checkin_id(String local_checkin_id) {
        this.local_checkin_id = local_checkin_id;
    }

    public String getServer_checkin_id() {
        return server_checkin_id;
    }

    public void setServer_checkin_id(String server_checkin_id) {
        this.server_checkin_id = server_checkin_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLocal_user_id() {
        return local_user_id;
    }

    public void setLocal_user_id(String local_user_id) {
        this.local_user_id = local_user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCheckin_type() {
        return checkin_type;
    }

    public void setCheckin_type(String checkin_type) {
        this.checkin_type = checkin_type;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }
}
