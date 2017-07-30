package com.techmali.smartteam.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

public class ApplyLeavesActivity extends BaseAppCompatActivity implements View.OnClickListener ,
        RequestListener {

    public static final String TAG = ApplyLeavesActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private EditText etStartDate,etEndDate,etDescription;
    //private ImageView iv
    private CheckBox chkStartDateYes,chkStartDateNo,chkEndDateYes,chkEndDateNo;

    private TextView tvErrorStartDate,tvErrorEndDate,tvErrorDescription,tvErrorStartIsFullDay,tvErrorEndIsFullDay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leaves);
        initActionBar(getString(R.string.title_apply_leaves));
        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(ApplyLeavesActivity.this).getPrefs();
        initView();
    }
    private void initView() {

        etStartDate = (EditText) findViewById(R.id.etStartDate);
        etEndDate = (EditText) findViewById(R.id.etEndDate);

        chkStartDateYes = (CheckBox) findViewById(R.id.chkStartDateYes);
        chkStartDateNo = (CheckBox) findViewById(R.id.chkStartDateNo);

        chkEndDateYes = (CheckBox) findViewById(R.id.chkEndDateYes);
        chkEndDateNo = (CheckBox) findViewById(R.id.chkEndDateNo);

//        ivStartDateYes = (ImageView) findViewById(R.id.ivStartDateYes);
//        ivStartDateNo = (ImageView) findViewById(R.id.ivStartDateNo);
//
//        ivEndDateYes = (ImageView) findViewById(R.id.ivEndDateYes);
//        ivEndDateNo = (ImageView) findViewById(R.id.ivEndDateNo);

        etDescription = (EditText) findViewById(R.id.etDescription);

        tvErrorStartDate = (TextView) findViewById(R.id.tvErrorStartDate);
        tvErrorEndDate = (TextView) findViewById(R.id.tvErrorEndDate);
        tvErrorDescription = (TextView) findViewById(R.id.tvErrorDescription);
        tvErrorStartIsFullDay = (TextView) findViewById(R.id.tvErrorStartIsFullDay);
        tvErrorEndIsFullDay = (TextView) findViewById(R.id.tvErrorEndIsFullDay);

        findViewById(R.id.btnSubmit).setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
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
            case R.id.etStartDate:

                break;
            case R.id.etEndDate:

                break;

        }
    }

    private boolean checkValidation() {
        boolean flag = true;
        if (Utils.isEmptyString(etStartDate.getText().toString().trim())) {
            flag = false;
            tvErrorStartDate.setVisibility(View.VISIBLE);
            tvErrorStartDate.setText(getString(R.string.error_enter));
            Utils.focusOnView(etStartDate);
        } else {
            tvErrorStartDate.setVisibility(View.GONE);
        }
        if (Utils.isEmptyString(etEndDate.getText().toString().trim())) {
            flag = false;
            tvErrorEndDate.setVisibility(View.VISIBLE);
            tvErrorEndDate.setText(getString(R.string.error_enter));
            Utils.focusOnView(etEndDate);
        } else {
            tvErrorEndDate.setVisibility(View.GONE);
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
