package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav-Pc on 7/22/2017.
 */

public class SyncSecurityMenuControllerAction implements Serializable {

    private String created_by;
    private String action_display_name;
    private String menu_id;
    private String updated;
    private String created;
    private String status;
    private String action_name;
    private String updated_by;
    private String security_menu_controllers_action_id;
    private String sort_order;
    private String menu_controllers_link_id;
    private String controllers_id;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getAction_display_name() {
        return action_display_name;
    }

    public void setAction_display_name(String action_display_name) {
        this.action_display_name = action_display_name;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
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

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getSecurity_menu_controllers_action_id() {
        return security_menu_controllers_action_id;
    }

    public void setSecurity_menu_controllers_action_id(String security_menu_controllers_action_id) {
        this.security_menu_controllers_action_id = security_menu_controllers_action_id;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getMenu_controllers_link_id() {
        return menu_controllers_link_id;
    }

    public void setMenu_controllers_link_id(String menu_controllers_link_id) {
        this.menu_controllers_link_id = menu_controllers_link_id;
    }

    public String getControllers_id() {
        return controllers_id;
    }

    public void setControllers_id(String controllers_id) {
        this.controllers_id = controllers_id;
    }
}
