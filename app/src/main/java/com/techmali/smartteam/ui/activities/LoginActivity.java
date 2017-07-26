package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.network.RequestMethod;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.request.RequestBuilder;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.DialogUtils;
import com.techmali.smartteam.utils.Utils;
import org.json.JSONObject;

public class LoginActivity extends BaseAppCompatActivity implements View.OnClickListener,
        RequestListener {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private int reqIdLogin = -1;

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private EditText etPhone, etPassword;
    private TextView tvErrorPhone, tvErrorPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(LoginActivity.this).getPrefs();

        initView();
    }

    private void initView() {

        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvErrorPhone = (TextView) findViewById(R.id.tvErrorPhone);
        tvErrorPassword = (TextView) findViewById(R.id.tvErrorPassword);

        etPhone.setText("deep.dran10@gmail.com");
        etPassword.setText("111111111");

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnForgotPassword).setOnClickListener(this);
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

    private void performLogin() {
        networkManager.isProgressBarVisible(true);
        reqIdLogin = networkManager.addRequest(RequestBuilder.performLogin(etPhone.getText().toString().trim(), etPassword.getText().toString().trim()), LoginActivity.this, RequestMethod.POST, RequestBuilder.METHOD_LOGIN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                //performLogin();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.btnForgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }

    @Override
    public void onSuccess(int id, String response) {
        try {
            if (!Utils.isEmptyString(response)) {
                if (id == reqIdLogin) {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        prefManager.edit().putString(PARAMS.TAG_HEADER_TOKEN, object.getJSONArray(PARAMS.TAG_RESULT).getJSONObject(0).getString(PARAMS.TAG_HEADER_TOKEN)).apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finish();
                    }else{
                        DialogUtils.showDialog(LoginActivity.this,"","",getString(R.string.ok));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int id, String message) {
        DialogUtils.showDialog(LoginActivity.this,"",message,getString(R.string.ok));
    }
}
