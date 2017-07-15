package com.techmali.smartteam.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;


public class CreateProjectActivity extends BaseAppCompatActivity implements View.OnClickListener,
        RequestListener {

    public static final String TAG = CreateProjectActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(CreateProjectActivity.this).getPrefs();
        initActionBar(getString(R.string.title_create_project));

        initView();
    }

    private void initView() {


    }

    @Override
    public void onStart() {
        super.onStart();
        networkManager.setListener(this);
    }

    @Override
    public void onStop() {
        networkManager.removeListener(this);
        super.onStop();
    }

    @Override
    public void onSuccess(int id, String response) {

    }

    @Override
    public void onError(int id, String message) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


}
