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
import com.techmali.smartteam.ui.views.SublimeDatePickerFragment;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.DateUtils;
import com.techmali.smartteam.utils.Utils;

import java.util.Calendar;

public class AddMyAttendanceActivity extends BaseAppCompatActivity implements View.OnClickListener,
        RequestListener {

    public static final String TAG = AddMyAttendanceActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private EditText etDate, etCheckInTime, etDescription, etCheckOutTime;
    private TextView tvErrorDate, tvErrorCheckInTime, tvErrorDescription, tvErrorCheckOutTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_attendance);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(AddMyAttendanceActivity.this).getPrefs();

        initActionBar(getString(R.string.title_add_attendance));
        initView();
    }

    private void initView() {
//
        etCheckInTime = (EditText) findViewById(R.id.etCheckInTime);
        etCheckOutTime = (EditText) findViewById(R.id.etCheckOutTime);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etDate = (EditText) findViewById(R.id.etDate);

        tvErrorDate = (TextView) findViewById(R.id.tvErrorDate);
        tvErrorCheckInTime = (TextView) findViewById(R.id.tvErrorCheckInTime);
        tvErrorCheckOutTime = (TextView) findViewById(R.id.tvErrorCheckOutTime);
        tvErrorDescription = (TextView) findViewById(R.id.tvErrorDescription);

        etDate.setOnClickListener(this);
        findViewById(R.id.btnSubmit).setOnClickListener(this);
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
            case R.id.btnSubmit:
                if (checkValidation()) {

                }
                break;
            case R.id.etDate:
                openCalendar();
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

                                break;
                        }

                    }
                });

        SublimeDatePickerFragment newFragment = datepicker;
        newFragment.setStyle(SublimeDatePickerFragment.STYLE_NO_TITLE, 0);
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
        if (Utils.isEmptyString(etCheckInTime.getText().toString().trim())) {
            flag = false;
            tvErrorCheckInTime.setVisibility(View.VISIBLE);
            tvErrorCheckInTime.setText(getString(R.string.error_enter));
            Utils.focusOnView(etCheckInTime);
        } else {
            tvErrorCheckInTime.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(etCheckOutTime.getText().toString().trim())) {
            flag = false;
            tvErrorCheckOutTime.setVisibility(View.VISIBLE);
            tvErrorCheckOutTime.setText(getString(R.string.error_enter));
            Utils.focusOnView(etCheckOutTime);
        } else {
            tvErrorCheckOutTime.setVisibility(View.GONE);
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
