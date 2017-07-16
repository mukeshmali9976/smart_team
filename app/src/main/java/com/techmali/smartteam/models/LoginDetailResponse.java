package com.techmali.smartteam.models;

import java.util.ArrayList;

/**
 * Created by Mali on 7/6/2017.
 */

public class LoginDetailResponse {

    private ArrayList<ProfileModel> user_data = new ArrayList<>();

    public ArrayList<ProfileModel> getUser_data() {
        return user_data;
    }
    public void setUser_data(ArrayList<ProfileModel> user_data) {
        this.user_data = user_data;
    }
}
