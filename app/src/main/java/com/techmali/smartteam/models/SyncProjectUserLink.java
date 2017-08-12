package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 7/22/2017.
 */

public class SyncProjectUserLink implements Serializable {

    private String created_by;
    private String local_project_id;
    private String local_project_user_link_id;
    private String created_on;
    private String local_user_id;
    private String server_project_id;
    private String updated_by;
    private String updated_on;
    private String user_id;
    private String server_project_user_link_id;
    private String project_user_link_id;
    private String project_id;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLocal_project_id() {
        return local_project_id;
    }

    public void setLocal_project_id(String local_project_id) {
        this.local_project_id = local_project_id;
    }

    public String getLocal_project_user_link_id() {
        return local_project_user_link_id;
    }

    public void setLocal_project_user_link_id(String local_project_user_link_id) {
        this.local_project_user_link_id = local_project_user_link_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getLocal_user_id() {
        return local_user_id;
    }

    public void setLocal_user_id(String local_user_id) {
        this.local_user_id = local_user_id;
    }

    public String getServer_Project_id() {
        return server_project_id;
    }

    public void setServer_Project_id(String server_project_id) {
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getServer_project_user_link_id() {
        return server_project_user_link_id;
    }

    public void setServer_project_user_link_id(String server_project_user_link_id) {
        this.server_project_user_link_id = server_project_user_link_id;
    }

    public String getServer_project_id() {
        return server_project_id;
    }

    public void setServer_project_id(String server_project_id) {
        this.server_project_id = server_project_id;
    }

    public String getProject_user_link_id() {
        return project_user_link_id;
    }

    public void setProject_user_link_id(String project_user_link_id) {
        this.project_user_link_id = project_user_link_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
}
