package com.techmali.smartteam.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.techmali.smartteam.domain.adapters.SpinnerProjectAdapter;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.ui.views.MyProgressDialog;
import com.techmali.smartteam.utils.CryptoManager;
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


public class CreateTaskActivity extends BaseAppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String TAG = CreateTaskActivity.class.getSimpleName();

    public static final String TAG_LOCAL_PROJECT_ID = "local_project_id";
    public static final String TAG_SERVER_PROJECT_ID = "server_project_id";

    public static int TAG_ADD_MEMBER = 444;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    public static final String TAG_START_TIME = "start_time";
    public static final String TAG_END_TIME = "end_time";

    private SharedPreferences prefManager = null;
    private PendingDataImpl model;

    private Spinner spProject;
    private EditText etTaskTitle, etStartDate, etEndDate, etDescription;
    private TextView tvErrorTaskTitle, tvErrorStartDate, tvErrorEndDate, tvErrorDescription, tvErrorProject;
    private RecyclerView rvMember;

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    private String selectedTime = "", start_date = "", end_date = "", local_project_id = "", server_project_id = "";
    private ArrayList<SyncUserInfo> prevSelectedUserArrayList = new ArrayList<>();
    private ArrayList<SyncUserInfo> newUserArrayList = new ArrayList<>();
    private ArrayList<SyncProject> projectArrayList = new ArrayList<>();

    private Calendar newDate, calendar;
    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        prefManager = CryptoManager.getInstance(CreateTaskActivity.this).getPrefs();
        model = new PendingDataImpl(this);
        initView();
    }


    private void initView() {

        initActionBar(getString(R.string.title_create_task));

        if (getIntent() != null && getIntent().hasExtra(TAG_LOCAL_PROJECT_ID))
            local_project_id = getIntent().getStringExtra(TAG_LOCAL_PROJECT_ID);

        if (getIntent() != null && getIntent().hasExtra(TAG_SERVER_PROJECT_ID))
            server_project_id = getIntent().getStringExtra(TAG_SERVER_PROJECT_ID);

        rvMember = (RecyclerView) findViewById(R.id.rvMember);

        etTaskTitle = (EditText) findViewById(R.id.etTaskTitle);
        etStartDate = (EditText) findViewById(R.id.etStartDate);
        etEndDate = (EditText) findViewById(R.id.etEndDate);
        etDescription = (EditText) findViewById(R.id.etDescription);

        tvErrorTaskTitle = (TextView) findViewById(R.id.tvErrorTaskTitle);
        tvErrorStartDate = (TextView) findViewById(R.id.tvErrorStartDate);
        tvErrorEndDate = (TextView) findViewById(R.id.tvErrorEndDate);
        tvErrorDescription = (TextView) findViewById(R.id.tvErrorDescription);
        tvErrorProject = (TextView) findViewById(R.id.tvErrorProject);
        spProject = (Spinner) findViewById(R.id.spProjects);

        calendar = Calendar.getInstance();
        newDate = Calendar.getInstance();

        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        findViewById(R.id.btnSubmit).setOnClickListener(this);
        findViewById(R.id.tvAssignTo).setOnClickListener(this);

        new GetProjectList().execute();

        spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    local_project_id = projectArrayList.get(i).getLocal_project_id();
                    server_project_id = projectArrayList.get(i).getServer_project_id();
                } else {
                    local_project_id = "";
                    server_project_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAssignTo:
                Intent intent = new Intent(CreateTaskActivity.this, AddProjectMember.class);
                intent.putParcelableArrayListExtra(AddProjectMember.EXTRA_SELECTED_USERS, prevSelectedUserArrayList);
                intent.putExtra(AddProjectMember.EXTRA_PROJECT_ID, local_project_id);
                startActivityForResult(intent, TAG_ADD_MEMBER);
                break;

            case R.id.btnSubmit:
                if (checkValidation())
                    new CreateTask().execute(etTaskTitle.getText().toString(), start_date, end_date, etDescription.getText().toString());
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

                MemberSelectionAdapter mAdapter = new MemberSelectionAdapter(CreateTaskActivity.this, newUserArrayList, false);
                rvMember.setLayoutManager(new LinearLayoutManager(CreateTaskActivity.this));
                rvMember.setItemAnimator(new DefaultItemAnimator());
                rvMember.setAdapter(mAdapter);

                rvMember.setHasFixedSize(true);
            }
        }
    }

    private boolean checkValidation() {
        boolean flag = true;
        if (spProject.getSelectedItemPosition() == 0) {
            flag = false;
            tvErrorProject.setVisibility(View.VISIBLE);
            tvErrorProject.setText(getString(R.string.error_enter));
        } else {
            tvErrorProject.setVisibility(View.GONE);
        }

        if (Utils.isEmptyString(etTaskTitle.getText().toString().trim())) {
            flag = false;
            tvErrorTaskTitle.setVisibility(View.VISIBLE);
            tvErrorTaskTitle.setText(getString(R.string.error_enter));
            Utils.focusOnView(etTaskTitle);
        } else {
            tvErrorTaskTitle.setVisibility(View.GONE);
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
            start_date = DateUtils.convertCurrentToUTC(newDate.getTime(), "");
            etStartDate.setText(displayFormat.format(newDate.getTime()));
        } else if (selectedTime.equalsIgnoreCase(TAG_END_TIME)) {
            end_date = DateUtils.convertCurrentToUTC(newDate.getTime(), "");
            etEndDate.setText(displayFormat.format(newDate.getTime()));
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
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

    private class CreateTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return model.createTask(strings[0], strings[1], strings[2], strings[3], prevSelectedUserArrayList, newUserArrayList,
                    false, local_project_id, server_project_id, "", "");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                DialogUtils.showDialog(CreateTaskActivity.this, "", "Task added successfully......", getString(R.string.ok), new DialogInterface.OnClickListener() {
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

    private class GetProjectList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return model.getProjectList();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                projectArrayList.clear();
                SyncProject project = new SyncProject();
                project.setTitle("Select Project");
                projectArrayList.add(project);

                if (!Utils.isEmptyString(result)) {
                    Log.e(TAG, result);
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        ArrayList<SyncProject> syncProjects = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<SyncProject>>() {
                        }.getType());
                        projectArrayList.addAll(syncProjects);
                    }
                }

                SpinnerProjectAdapter adapter = new SpinnerProjectAdapter(CreateTaskActivity.this, R.layout.spinner_item, projectArrayList);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                spProject.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
