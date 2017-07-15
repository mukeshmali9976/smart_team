package com.techmali.smartteam.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.views.SublimeDatePickerFragment;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.DateUtils;
import com.techmali.smartteam.utils.Utils;

import java.util.Calendar;


public class CreateUserActivity extends BaseAppCompatActivity implements View.OnClickListener,
        RequestListener {

    public static final String TAG = CreateUserActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private EditText etFirstName, etBirthDate, etEmailAddress, etPhone, etLastName;
    private TextView tvErrorEmail, tvErrorBirthDate, tvErrorPhone, tvErrorLastName, tvErrorFirstName;
    private ImageView ivMale, ivFemale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(CreateUserActivity.this).getPrefs();
        initActionBar(getString(R.string.tittle_create_user));
        initView();
    }

    private void initView() {


        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        etBirthDate = (EditText) findViewById(R.id.etBirthDate);

        tvErrorFirstName = (TextView) findViewById(R.id.tvErrorFirstName);
        tvErrorLastName = (TextView) findViewById(R.id.tvErrorLastName);
        tvErrorPhone = (TextView) findViewById(R.id.tvErrorPhone);
        tvErrorEmail = (TextView) findViewById(R.id.tvErrorEmail);
        tvErrorBirthDate = (TextView) findViewById(R.id.tvErrorBirthDate);

        ivMale = (ImageView) findViewById(R.id.ivMale);
        ivFemale = (ImageView) findViewById(R.id.ivFemale);

        etBirthDate.setOnClickListener(this);
        ivMale.setOnClickListener(this);
        ivFemale.setOnClickListener(this);
        findViewById(R.id.btnSubmit).setOnClickListener(this);
        etBirthDate.setFocusable(true);

        etBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    openCalendar();
                }
            }
        });

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
            case R.id.etBirthDate:
                openCalendar();
                break;
            case R.id.btnSubmit:
                if (checkValidation()) {

                }
                break;
            case R.id.ivMale:
                // ivMale.setImageBitmap();
                break;
            case R.id.ivFemale:

                break;

        }
    }

    private void openCalendar() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR) - 18;
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Calendar selectDate = Calendar.getInstance();
        selectDate.set(year, month, day);
        String formattedDate = DateUtils.defaultDateFormatShort.format(selectDate.getTime());
        showDatePicker(1005, formattedDate, true, true);
    }

    private boolean checkValidation() {
        boolean flag = true;
        if (Utils.isEmptyString(etFirstName.getText().toString().trim())) {
            flag = false;
            tvErrorFirstName.setVisibility(View.VISIBLE);
            tvErrorFirstName.setText(getString(R.string.error_first_name));
            Utils.focusOnView(etFirstName);
        } else {
            tvErrorFirstName.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(etLastName.getText().toString().trim())) {
            flag = false;
            tvErrorLastName.setVisibility(View.VISIBLE);
            tvErrorLastName.setText(getString(R.string.error_last_name));
            Utils.focusOnView(etLastName);
        } else {
            tvErrorLastName.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(etEmailAddress.getText().toString().trim())) {
            flag = false;
            tvErrorEmail.setVisibility(View.VISIBLE);
            tvErrorEmail.setText(getString(R.string.error_email_address));
            Utils.focusOnView(etEmailAddress);
        } else if (!Utils.isValidEmail(etEmailAddress.getText().toString().trim())) {
            flag = false;
            tvErrorEmail.setVisibility(View.VISIBLE);
            tvErrorEmail.setText(getString(R.string.error_email_valid));
            Utils.focusOnView(etEmailAddress);
        } else {
            tvErrorEmail.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(etPhone.getText().toString().trim())) {
            flag = false;
            tvErrorPhone.setVisibility(View.VISIBLE);
            tvErrorPhone.setText(getString(R.string.error_phone));
            Utils.focusOnView(etPhone);
        } else if (etPhone.getText().toString().length() < 10) {
            flag = false;
            tvErrorPhone.setVisibility(View.VISIBLE);
            tvErrorPhone.setText(getString(R.string.error_phone));
            Utils.focusOnView(etPhone);
        } else {
            tvErrorPhone.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(etBirthDate.getText().toString().trim())) {
            flag = false;
            tvErrorBirthDate.setVisibility(View.VISIBLE);
            tvErrorBirthDate.setText(getString(R.string.error_birth_date));
        } else {
            tvErrorBirthDate.setVisibility(View.GONE);
        }

        return flag;
    }

    private void showDatePicker(int identifier, String currentDate, boolean isMaxDateRequire, boolean misTodateMaxDate) {

        SublimeDatePickerFragment datepicker = SublimeDatePickerFragment.newInstance(identifier, currentDate, isMaxDateRequire,
                false, misTodateMaxDate, 0, 0, new SublimeDatePickerFragment.Callback() {
                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onDateTimeRecurrenceSet(int identifier, int year, int monthOfYear, int dayOfMonth) {
                        switch (identifier) {
                            case 1005:
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                String selectedDate = DateUtils.sourceFormatShort.format(newDate.getTime());
                                String str = DateUtils.displayDateFormatShort.format(newDate.getTime());
                                etBirthDate.setText(selectedDate);
                                break;
                        }

                    }
                });

        SublimeDatePickerFragment newFragment = datepicker;
        newFragment.setStyle(SublimeDatePickerFragment.STYLE_NO_TITLE, 0);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


}
