package com.techmali.smartteam.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;


public class CreateProjectActivity extends BaseAppCompatActivity implements View.OnClickListener,
        RequestListener {

    public static final String TAG = CreateProjectActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private EditText etProject, etStartDate, etEndDate, etDescription, etAssignTo;
    private TextView tvErrorProrject, tvErrorStartDate, tvErrorEndDate, tvErrorDescription, tvErrorAssignTo;

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


        etProject = (EditText) findViewById(R.id.etProject);
        etStartDate = (EditText) findViewById(R.id.etStartDate);
        etEndDate = (EditText) findViewById(R.id.etEndDate);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etAssignTo = (EditText) findViewById(R.id.etAssignTo);

        tvErrorProrject = (TextView) findViewById(R.id.tvErrorProrject);
        tvErrorStartDate = (TextView) findViewById(R.id.tvErrorStartDate);
        tvErrorEndDate = (TextView) findViewById(R.id.tvErrorEndDate);
        tvErrorProrject = (TextView) findViewById(R.id.tvErrorProrject);
        tvErrorDescription = (TextView) findViewById(R.id.tvErrorDescription);
        tvErrorAssignTo = (TextView) findViewById(R.id.tvErrorAssignTo);


        etAssignTo.setOnClickListener(this);
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

            case R.id.etAssignTo:

                break;
        }
    }

    private boolean checkValidation() {
        boolean flag = true;
        if (Utils.isEmptyString(etProject.getText().toString().trim())) {
            flag = false;
            tvErrorProrject.setVisibility(View.VISIBLE);
            tvErrorProrject.setText(getString(R.string.error_enter));
            Utils.focusOnView(etProject);
        } else {
            tvErrorProrject.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(etDescription.getText().toString().trim())) {
            flag = false;
            tvErrorDescription.setVisibility(View.VISIBLE);
            tvErrorDescription.setText(getString(R.string.error_enter));
            Utils.focusOnView(etDescription);
        } else {
            tvErrorDescription.setVisibility(View.GONE);
        }
        return flag;
    }
}
