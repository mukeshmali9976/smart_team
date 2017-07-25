package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav-Pc on 7/22/2017.
 */

public class SyncRole implements Serializable {

    private String created_by;
    private String role_id;
    private String status;
    private String created_on;
    private String role_group;
    private String name;
    private String updated_on;
    private String updated_by;
    private String sort_order;
    private String is_custom;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getRole_group() {
        return role_group;
    }

    public void setRole_group(String role_group) {
        this.role_group = role_group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getIs_custom() {
        return is_custom;
    }

    public void setIs_custom(String is_custom) {
        this.is_custom = is_custom;
    }
}
