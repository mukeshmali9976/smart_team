package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 7/22/2017.
 */

public class SyncTaskUserLink implements Serializable {

    private String created_by;
    private String server_task_user_link_id;
    private String local_task_user_link_id;
    private String created_on;
    private String local_user_id;
    private String server_task_id;
    private String updated_by;
    private String updated_on;
    private String user_id;
    private String local_task_id;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getServer_task_user_link_id() {
        return server_task_user_link_id;
    }

    public void setServer_task_user_link_id(String server_task_user_link_id) {
        this.server_task_user_link_id = server_task_user_link_id;
    }

    public String getLocal_task_user_link_id() {
        return local_task_user_link_id;
    }

    public void setLocal_task_user_link_id(String local_task_user_link_id) {
        this.local_task_user_link_id = local_task_user_link_id;
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

    public String getServer_task_id() {
        return server_task_id;
    }

    public void setServer_task_id(String server_task_id) {
        this.server_task_id = server_task_id;
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

    public String getLocal_task_id() {
        return local_task_id;
    }

    public void setLocal_task_id(String local_task_id) {
        this.local_task_id = local_task_id;
    }
}
