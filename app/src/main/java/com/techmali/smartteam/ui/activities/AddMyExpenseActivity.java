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

public class AddMyExpenseActivity extends BaseAppCompatActivity implements View.OnClickListener ,
        RequestListener {

    public static final String TAG = AddMyExpenseActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private EditText etDate,etExpenseType,etAmount,etDescription;
    private TextView tvErrorDate,tvErrorExpenseType,tvErrorAmount,tvErrorDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_expense);

        initActionBar(getString(R.string.title_add_expense));
        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(AddMyExpenseActivity.this).getPrefs();

        initView();
    }

    private void initView() {

        etDate = (EditText) findViewById(R.id.etDate);
        etExpenseType = (EditText) findViewById(R.id.etExpenseType);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etDescription = (EditText) findViewById(R.id.etDescription);

        tvErrorDate = (TextView) findViewById(R.id.tvErrorDate);
        tvErrorExpenseType = (TextView) findViewById(R.id.tvErrorExpenseType);
        tvErrorAmount = (TextView) findViewById(R.id.tvErrorAmount);
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
                if(checkValidation()){}
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
        if (Utils.isEmptyString(etExpenseType.getText().toString().trim())) {
            flag = false;
            tvErrorExpenseType.setVisibility(View.VISIBLE);
            tvErrorExpenseType.setText(getString(R.string.error_enter));
            Utils.focusOnView(etExpenseType);
        } else {
            tvErrorExpenseType.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(etAmount.getText().toString().trim())) {
            flag = false;
            tvErrorAmount.setVisibility(View.VISIBLE);
            tvErrorAmount.setText(getString(R.string.error_enter));
            Utils.focusOnView(etAmount);
        } else {
            tvErrorAmount.setVisibility(View.GONE);
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
