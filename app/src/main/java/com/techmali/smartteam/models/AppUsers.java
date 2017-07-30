package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 7/31/2017.
 */

public class AppUsers implements Serializable {

    String local_user_id;
    String user_id;
    String user_name;

    boolean selected;

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
