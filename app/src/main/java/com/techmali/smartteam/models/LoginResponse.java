package com.techmali.smartteam.models;

import android.annotation.SuppressLint;

import java.util.ArrayList;

/**
 * Created by mukesh mali on 5/21/2017.
 */

@SuppressLint("ParcelCreator")
public class LoginResponse extends BaseResponse {
    private ArrayList<ProfileModel>  result = new ArrayList<>();

    public ArrayList<ProfileModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<ProfileModel> result) {
        this.result = result;
    }
}
