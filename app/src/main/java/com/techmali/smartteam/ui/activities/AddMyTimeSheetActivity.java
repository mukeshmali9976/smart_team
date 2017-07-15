package com.techmali.smartteam.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;


public class AddMyTimeSheetActivity extends BaseAppCompatActivity implements View.OnClickListener ,
        RequestListener {

    public static final String TAG = AddMyTimeSheetActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private EditText etDate,etProjects,etTasks,etTime,etDescription;
    private TextView tvErrorDate,tvErrorDescription,tvErrorTime,tvErrorTasks,tvErrorProjects;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_timesheet);

        initActionBar(getString(R.string.title_my_time_sheet));
        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(AddMyTimeSheetActivity.this).getPrefs();

        initView();
    }

    private void initView() {

        etDate = (EditText) findViewById(R.id.etDate);
        etProjects = (EditText) findViewById(R.id.etProjects);
        etTasks = (EditText) findViewById(R.id.etTasks);
        etTime = (EditText) findViewById(R.id.etTime);
        etDescription = (EditText) findViewById(R.id.etDescription);

        tvErrorDate = (TextView) findViewById(R.id.tvErrorDate);
        tvErrorProjects = (TextView) findViewById(R.id.tvErrorProjects);
        tvErrorTasks = (TextView) findViewById(R.id.tvErrorTasks);
        tvErrorTime = (TextView) findViewById(R.id.tvErrorTime);
        tvErrorDescription = (TextView) findViewById(R.id.tvErrorDescription);

        findViewById(R.id.btnSubmit).setOnClickListener(this);
        etDate.setOnClickListener(this);
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
        switch (view.getId()){
            case R.id.btnSubmit:
                if(checkValidation()){

                }
                break;

        }
    }

    private boolean checkValidation() {
        boolean flag = true;
        if (Utils.isEmptyString(etDate.getText().toString().trim())) {
            flag = false;
            tvErrorDate.setVisibility(View.VISIBLE);
            tvErrorDate.setText(getString(R.string.error_enter));
            Utils.focusOnView(etDate);
        } else {
            tvErrorDate.setVisibility(View.GONE);
        }
        if (Utils.isEmptyString(etProjects.getText().toString().trim())) {
            flag = false;
            tvErrorProjects.setVisibility(View.VISIBLE);
            tvErrorProjects.setText(getString(R.string.error_enter));
            Utils.focusOnView(etProjects);
        } else {
            tvErrorProjects.setVisibility(View.GONE);
        }
        if (Utils.isEmptyString(etTasks.getText().toString().trim())) {
            flag = false;
            tvErrorTasks.setVisibility(View.VISIBLE);
            tvErrorTasks.setText(getString(R.string.error_enter));
            Utils.focusOnView(etTasks);
        } else {
            tvErrorTasks.setVisibility(View.GONE);
        }
        if (Utils.isEmptyString(etTime.getText().toString().trim())) {
            flag = false;
            tvErrorTime.setVisibility(View.VISIBLE);
            tvErrorTime.setText(getString(R.string.error_enter));
            Utils.focusOnView(etTime);
        } else {
            tvErrorTime.setVisibility(View.GONE);
        }
        if (Utils.isEmptyString(etDescription.getText().toString().trim())) {
            flag = false;
            tvErrorDescription.setVisibility(View.VISIBLE);
            tvErrorDescription.setText(getString(R.string.error_enter));
            Utils.focusOnView(etTime);
        } else {
            tvErrorDescription.setVisibility(View.GONE);
        }


        return flag;
    }


}
