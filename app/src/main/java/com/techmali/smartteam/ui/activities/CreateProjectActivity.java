package com.techmali.smartteam.ui.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.views.MyProgressDialog;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.DialogUtils;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;


public class CreateProjectActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String TAG = CreateProjectActivity.class.getSimpleName();

    private PendingDataImpl model;

    private EditText etProject, etStartDate, etEndDate, etDescription, etAssignTo;
    private TextView tvErrorProject, tvErrorStartDate, tvErrorEndDate, tvErrorDescription, tvErrorAssignTo;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        model = new PendingDataImpl(this);

        initActionBar(getString(R.string.title_create_project));
        initView();
    }

    private void initView() {

        etProject = (EditText) findViewById(R.id.etProject);
        etStartDate = (EditText) findViewById(R.id.etStartDate);
        etEndDate = (EditText) findViewById(R.id.etEndDate);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etAssignTo = (EditText) findViewById(R.id.etAssignTo);

        tvErrorProject = (TextView) findViewById(R.id.tvErrorProject);
        tvErrorStartDate = (TextView) findViewById(R.id.tvErrorStartDate);
        tvErrorEndDate = (TextView) findViewById(R.id.tvErrorEndDate);
        tvErrorDescription = (TextView) findViewById(R.id.tvErrorDescription);
        tvErrorAssignTo = (TextView) findViewById(R.id.tvErrorAssignTo);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        etAssignTo.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etAssignTo:
                break;

            case R.id.btnSubmit:
                if (checkValidation())
                    new CreateProject().execute(etProject.getText().toString(), "2017-07-27 10:15:20", "2017-07-28 10:15:20", etDescription.getText().toString(), "");
                break;
        }
    }

    private boolean checkValidation() {
        boolean flag = true;
        if (Utils.isEmptyString(etProject.getText().toString().trim())) {
            flag = false;
            tvErrorProject.setVisibility(View.VISIBLE);
            tvErrorProject.setText(getString(R.string.error_enter));
            Utils.focusOnView(etProject);
        } else {
            tvErrorProject.setVisibility(View.GONE);
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

    private class CreateProject extends AsyncTask<String, Void, Boolean> {

        MyProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new MyProgressDialog(CreateProjectActivity.this);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return model.createProject(strings[0], strings[1], strings[2], strings[3], strings[4]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                DialogUtils.showDialog(CreateProjectActivity.this, "", "Project created successfully......", getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
            } else {
                displayError("There might be some issue, please try later.");
            }
            dialog.dismiss();
        }
    }
}
