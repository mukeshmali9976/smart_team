package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav-Pc on 7/22/2017.
 */

public class SyncSecurityMenu implements Serializable {

    private String created_by;
    private String icon;
    private String status;
    private String menu_display_name;
    private String security_menu_id;
    private String menu_name;
    private String url_parameter;
    private String url;
    private String link_options;
    private String is_always_true;
    private String updated;
    private String created;
    private String menu_label;
    private String updated_by;
    private String sort_order;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMenu_display_name() {
        return menu_display_name;
    }

    public void setMenu_display_name(String menu_display_name) {
        this.menu_display_name = menu_display_name;
    }

    public String getSecurity_menu_id() {
        return security_menu_id;
    }

    public void setSecurity_menu_id(String security_menu_id) {
        this.security_menu_id = security_menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getUrl_parameter() {
        return url_parameter;
    }

    public void setUrl_parameter(String url_parameter) {
        this.url_parameter = url_parameter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink_options() {
        return link_options;
    }

    public void setLink_options(String link_options) {
        this.link_options = link_options;
    }

    public String getIs_always_true() {
        return is_always_true;
    }

    public void setIs_always_true(String is_always_true) {
        this.is_always_true = is_always_true;
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

    public String getMenu_label() {
        return menu_label;
    }

    public void setMenu_label(String menu_label) {
        this.menu_label = menu_label;
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
