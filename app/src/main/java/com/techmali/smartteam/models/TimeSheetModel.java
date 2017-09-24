package com.techmali.smartteam.models;

/**
 * Created by Mali on 6/24/2017.
 */

public class TimeSheetModel  {

    private String note;
    private String timesheet_date;
    private String total_time;
    private String task_name;
    private String project_name;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}
