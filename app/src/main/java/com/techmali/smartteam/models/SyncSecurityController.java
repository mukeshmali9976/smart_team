package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav-Pc on 7/22/2017.
 */

public class SyncSecurityController implements Serializable {

    private String created_by;
    private String display_name;
    private String security_controllers_id;
    private String updated;
    private String created;
    private String status;
    private String name;
    private String controller_name;
    private String updated_by;
    private String sort_order;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getSecurity_controllers_id() {
        return security_controllers_id;
    }

    public void setSecurity_controllers_id(String security_controllers_id) {
        this.security_controllers_id = security_controllers_id;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getController_name() {
        return controller_name;
    }

    public void setController_name(String controller_name) {
        this.controller_name = controller_name;
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
}
