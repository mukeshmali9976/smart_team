package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 9/23/2017.
 */

public class SyncTimesheet implements Serializable{

    private String timesheet_id;
    private String local_timesheet_id;
    private String timesheet_date;
    private String total_time;
    private String company_id;
    private String local_project_id;
    private String server_project_id;
    private String server_task_id;
    private String local_task_id;
    private String created_by;
    private String created_on;
    private String status_id;
    private String note;
    private String local_user_id;
    private String user_id;
    private String updated_by;
    private String updated_on;

    public String getTimesheet_id() {
        return timesheet_id;
    }

    public void setTimesheet_id(String timesheet_id) {
        this.timesheet_id = timesheet_id;
    }

    public String getLocal_timesheet_id() {
        return local_timesheet_id;
    }

    public void setLocal_timesheet_id(String local_timesheet_id) {
        this.local_timesheet_id = local_timesheet_id;
    }

    public String getTimesheet_date() {
        return timesheet_date;
    }

    public void setTimesheet_date(String timesheet_date) {
        this.timesheet_date = timesheet_date;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getLocal_project_id() {
        return local_project_id;
    }

    public void setLocal_project_id(String local_project_id) {
        this.local_project_id = local_project_id;
    }

    public String getServer_project_id() {
        return server_project_id;
    }

    public void setServer_project_id(String server_project_id) {
        this.server_project_id = server_project_id;
    }

    public String getServer_task_id() {
        return server_task_id;
    }

    public void setServer_task_id(String server_task_id) {
        this.server_task_id = server_task_id;
    }

    public String getLocal_task_id() {
        return local_task_id;
    }

    public void setLocal_task_id(String local_task_id) {
        this.local_task_id = local_task_id;
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

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocal_user_id() {
        return local_user_id;
    }

    public void setLocal_user_id(String local_user_id) {
        this.local_user_id = local_user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
