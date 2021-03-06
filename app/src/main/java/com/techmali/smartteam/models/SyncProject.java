package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 7/22/2017.
 */

public class SyncProject implements Serializable {

    public String created_by;
    public String company_id;
    public String local_project_id;
    public String created_on;
    public String status_id;
    public String is_delete;
    public String title;
    public String end_date;
    public String description;
    public String server_project_id;
    public String project_id;
    public String updated_by;
    public String updated_on;
    public String thumb;
    public String start_date;

    public boolean selected;

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

    public String getLocal_project_id() {
        return local_project_id;
    }

    public void setLocal_project_id(String local_project_id) {
        this.local_project_id = local_project_id;
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

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServer_project_id() {
        return server_project_id;
    }

    public void setServer_project_id(String server_project_id) {
        this.server_project_id = server_project_id;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
