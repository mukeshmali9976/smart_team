package com.techmali.smartteam.models;

/**
 * Created by Mali on 6/4/2017.
 */

public class TaskModel {

    private String local_task_id;
    private String server_task_id;
    private String local_project_id;
    private String server_project_id;
    private String status_id;
    private String task_name;
    private String project_name;
    private String thumb;

    public String getLocal_task_id() {
        return local_task_id;
    }

    public void setLocal_task_id(String local_task_id) {
        this.local_task_id = local_task_id;
    }

    public String getServer_task_id() {
        return server_task_id;
    }

    public void setServer_task_id(String server_task_id) {
        this.server_task_id = server_task_id;
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

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
