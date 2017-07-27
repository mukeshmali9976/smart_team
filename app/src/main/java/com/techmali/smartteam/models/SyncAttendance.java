package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 7/22/2017.
 */

public class SyncAttendance implements Serializable {

    private String local_attendance_id;
    private String created_by;
    private String company_id;
    private String attandance_type;
    private String created_on;
    private String status_id;
    private String end_date;
    private String local_user_id;
    private String updated_by;
    private String updated_on;
    private String user_id;
    private String attendance_id;
    private String start_date;

    public String getLocal_attendance_id() {
        return local_attendance_id;
    }

    public void setLocal_attendance_id(String local_attendance_id) {
        this.local_attendance_id = local_attendance_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getAttandance_type() {
        return attandance_type;
    }

    public void setAttandance_type(String attandance_type) {
        this.attandance_type = attandance_type;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getLocal_user_id() {
        return local_user_id;
    }

    public void setLocal_user_id(String local_user_id) {
        this.local_user_id = local_user_id;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
