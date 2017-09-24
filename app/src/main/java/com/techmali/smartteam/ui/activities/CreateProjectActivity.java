package com.techmali.smartteam.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.SimpleMonthAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.MemberSelectionAdapter;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.ui.views.MyProgressDialog;
import com.techmali.smartteam.utils.DateUtils;
import com.techmali.smartteam.utils.DialogUtils;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CreateProjectActivity extends BaseAppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String TAG = CreateProjectActivity.class.getSimpleName();

    public static int TAG_ADD_MEMBER = 444;

    public static final String TAG_IS_FOR_UPDATE = "update";
    public static final String TAG_PROJECT_ID = "project_id";

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    public static final String TAG_START_TIME = "start_time";
    public static final String TAG_END_TIME = "end_time";

    private PendingDataImpl model;

    private EditText etProject, etStartDate, etEndDate, etDescription;
    private TextView tvErrorProject, tvErrorStartDate, tvErrorEndDate, tvErrorDescription;
    private RecyclerView rvMember;

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    private String selectedTime = "", start_date = "", end_date = "", local_project_id = "", project_id = "";
    private boolean isForUpdate = false;
    private ArrayList<SyncUserInfo> prevSelectedUserArrayList = new ArrayList<>();
    private ArrayList<SyncUserInfo> newUserArrayList = new ArrayList<>();

    private Calendar newDate, calendar;
    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        model = new PendingDataImpl(this);
        initView();
    }


    private void initView() {

        if (getIntent() != null) {
            if (getIntent().hasExtra(TAG_IS_FOR_UPDATE)) {
                isForUpdate = getIntent().getBooleanExtra(TAG_IS_FOR_UPDATE, false);
            }
            if (getIntent().hasExtra(TAG_PROJECT_ID)) {
                local_project_id = getIntent().getStringExtra(TAG_PROJECT_ID);
            }
        }

        if (isForUpdate)
            initActionBar(getString(R.string.title_update_project));
        else
            initActionBar(getString(R.string.title_create_project));

        etProject = (EditText) findViewById(R.id.etProject);
        etStartDate = (EditText) findViewById(R.id.etStartDate);
        etEndDate = (EditText) findViewById(R.id.etEndDate);
        etDescription = (EditText) findViewById(R.id.etDescription);

        rvMember = (RecyclerView) findViewById(R.id.rvMember);

        tvErrorProject = (TextView) findViewById(R.id.tvErrorProject);
        tvErrorStartDate = (TextView) findViewById(R.id.tvErrorStartDate);
        tvErrorEndDate = (TextView) findViewById(R.id.tvErrorEndDate);
        tvErrorDescription = (TextView) findViewById(R.id.tvErrorDescription);

        calendar = Calendar.getInstance();
        newDate = Calendar.getInstance();

        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        findViewById(R.id.tvAssignTo).setOnClickListener(this);
        findViewById(R.id.btnSubmit).setOnClickListener(this);

        if (isForUpdate && !Utils.isEmptyString(local_project_id))
            new GetProjectDetail().execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAssignTo:
                Intent intent = new Intent(CreateProjectActivity.this, AddProjectMember.class);
                intent.putParcelableArrayListExtra(AddProjectMember.EXTRA_SELECTED_USERS, prevSelectedUserArrayList);
                startActivityForResult(intent, TAG_ADD_MEMBER);
                break;

            case R.id.btnSubmit:
                if (checkValidation())
                    new CreateProject().execute(etProject.getText().toString(), start_date, end_date, etDescription.getText().toString());
                break;

            case R.id.etStartDate:
                selectedTime = TAG_START_TIME;
                datePickerDialog.setVibrate(false);
                datePickerDialog.setMinDate(new SimpleMonthAdapter.CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
                datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;

            case R.id.etEndDate:
                if (!Utils.isEmptyString(start_date)) {
                    selectedTime = TAG_END_TIME;
                    datePickerDialog.setVibrate(false);
                    datePickerDialog.setMinDate(new SimpleMonthAdapter.CalendarDay(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH)));
                    datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
                    datePickerDialog.setCloseOnSingleTapDay(false);
                    datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                } else {
                    displayError("Select Start Date first.");
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_ADD_MEMBER && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra(AddProjectMember.EXTRA_SELECTED_USERS)) {
                newUserArrayList = data.getParcelableArrayListExtra(AddProjectMember.EXTRA_SELECTED_USERS);

                MemberSelectionAdapter mAdapter = new MemberSelectionAdapter(CreateProjectActivity.this, newUserArrayList, false);
                rvMember.setLayoutManager(new LinearLayoutManager(CreateProjectActivity.this));
                rvMember.setItemAnimator(new DefaultItemAnimator());
                rvMember.setAdapter(mAdapter);

                rvMember.setHasFixedSize(true);
            }
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

        if (Utils.isEmptyString(start_date)) {
            flag = false;
            tvErrorStartDate.setVisibility(View.VISIBLE);
            tvErrorStartDate.setText(getString(R.string.error_enter));
            Utils.focusOnView(etStartDate);
        } else {
            tvErrorStartDate.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(end_date)) {
            flag = false;
            tvErrorEndDate.setVisibility(View.VISIBLE);
            tvErrorEndDate.setText(getString(R.string.error_enter));
            Utils.focusOnView(etEndDate);
        } else {
            tvErrorEndDate.setVisibility(View.GONE);
        }
        return flag;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        newDate.set(year, month, day, 0, 0, 1);

        if (selectedTime.equalsIgnoreCase(TAG_START_TIME)) {
            start_date = DateUtils.convertCurrentToUTC(newDate.getTime(), "yyyy-MM-dd");
            etStartDate.setText(displayFormat.format(newDate.getTime()));
        } else if (selectedTime.equalsIgnoreCase(TAG_END_TIME)) {
            end_date = DateUtils.convertCurrentToUTC(newDate.getTime(), "yyyy-MM-dd");
            etEndDate.setText(displayFormat.format(newDate.getTime()));
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        newDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        newDate.set(Calendar.MINUTE, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm aa", Locale.getDefault());

        if (selectedTime.equalsIgnoreCase(TAG_START_TIME)) {
            start_date = DateUtils.convertCurrentToUTC(newDate.getTime(), "");
            etStartDate.setText(dateFormat.format(newDate.getTime()));
        } else if (selectedTime.equalsIgnoreCase(TAG_END_TIME)) {
            end_date = DateUtils.convertCurrentToUTC(newDate.getTime(), "");
            etEndDate.setText(dateFormat.format(newDate.getTime()));
        }
    }


    private class CreateProject extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return model.createProject(strings[0], strings[1], strings[2], strings[3], prevSelectedUserArrayList, newUserArrayList, isForUpdate, local_project_id, project_id);
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
            dismissProgressDialog();
        }
    }


    private class GetProjectDetail extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            return model.getProjectDetail(local_project_id);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (!Utils.isEmptyString(result)) {
                    Log.e(TAG, result);
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        ArrayList<SyncProject> projectArrayList;
                        projectArrayList = new Gson().fromJson(object.getJSONObject(PARAMS.TAG_RESULT).getString(PARAMS.TAG_PROJECT_DETAIL), new TypeToken<List<SyncProject>>() {
                        }.getType());
                        if (!projectArrayList.isEmpty()) {
                            start_date = projectArrayList.get(0).getStart_date();
                            end_date = projectArrayList.get(0).getEnd_date();
                            project_id = projectArrayList.get(0).getServer_project_id();

                            etProject.setText(projectArrayList.get(0).getTitle());
                            etDescription.setText(projectArrayList.get(0).getDescription());
                            etStartDate.setText(DateUtils.getLocalDateFromUTC(projectArrayList.get(0).getStart_date(), "dd MMM, yyyy", "yyyy-MM-dd"));
                            etEndDate.setText(DateUtils.getLocalDateFromUTC(projectArrayList.get(0).getEnd_date(), "dd MMM, yyyy", "yyyy-MM-dd"));
                        }

                        prevSelectedUserArrayList = new Gson().fromJson(object.getJSONObject(PARAMS.TAG_RESULT).getString(PARAMS.TAG_USER_LIST), new TypeToken<List<SyncUserInfo>>() {
                        }.getType());
                        if (prevSelectedUserArrayList != null && !prevSelectedUserArrayList.isEmpty()) {
                            MemberSelectionAdapter mAdapter = new MemberSelectionAdapter(CreateProjectActivity.this, prevSelectedUserArrayList, false);
                            rvMember.setLayoutManager(new LinearLayoutManager(CreateProjectActivity.this));
                            rvMember.setItemAnimator(new DefaultItemAnimator());
                            rvMember.setAdapter(mAdapter);

                            rvMember.setHasFixedSize(true);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismissProgressDialog();
        }
    }
}
