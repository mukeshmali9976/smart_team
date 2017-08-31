package com.techmali.smartteam.ui.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.SimpleMonthAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.ProjectListAdapter;
import com.techmali.smartteam.domain.adapters.SpinnerProjectAdapter;
import com.techmali.smartteam.domain.adapters.SpinnerTaskAdapter;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncTask;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.ui.fragments.ActiveProjectFragment;
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


public class AddMyTimeSheetActivity extends BaseAppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String TAG = AddMyTimeSheetActivity.class.getSimpleName();

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private SharedPreferences prefManager = null;
    private PendingDataImpl model;

    private Spinner spProject, spTask;
    private EditText etDate, etProjects, etTasks, etTime, etDescription;
    private TextView tvErrorDate, tvErrorDescription, tvErrorTime, tvErrorTasks, tvErrorProjects;

    private ArrayList<SyncProject> projectArrayList = new ArrayList<>();
    private ArrayList<SyncTask> taskArrayList = new ArrayList<>();

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    private SpinnerProjectAdapter adapter;
    private SpinnerTaskAdapter taskAdapter;

    private Calendar newDate, calendar;
    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

    private String start_date = "", project_id = "", task_id = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_timesheet);

        initActionBar(getString(R.string.title_my_time_sheet));

        prefManager = CryptoManager.getInstance(this).getPrefs();
        model = new PendingDataImpl(this);

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

        spProject = (Spinner) findViewById(R.id.spProjects);
        spTask = (Spinner) findViewById(R.id.spTask);

        findViewById(R.id.btnSubmit).setOnClickListener(this);
        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);

        calendar = Calendar.getInstance();
        newDate = Calendar.getInstance();

        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

        SyncTask task = new SyncTask();
        task.setTitle("Select Task");
        taskArrayList.add(task);

        taskAdapter = new SpinnerTaskAdapter(this, R.layout.spinner_item, taskArrayList);
        spTask.setAdapter(taskAdapter);

        new GetProjectList().execute();

        spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    project_id = projectArrayList.get(position).getLocal_project_id();
                    new GetTaskList().execute(projectArrayList.get(position).getLocal_project_id());
                } else {
                    project_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    task_id = taskArrayList.get(position).getLocal_task_id();
                } else {
                    task_id = "";
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
            case R.id.btnSubmit:
                if (checkValidation())
                    new AddTimesheet().execute(project_id, task_id, start_date, etDescription.getText().toString());
                break;
            case R.id.etDate:
                datePickerDialog.setVibrate(false);
                datePickerDialog.setMinDate(new SimpleMonthAdapter.CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
                datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;
            case R.id.etTime:
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(true);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
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
        if (Utils.isEmptyString(project_id)) {
            flag = false;
            tvErrorProjects.setVisibility(View.VISIBLE);
            tvErrorProjects.setText(getString(R.string.error_enter));
            Utils.focusOnView(etProjects);
        } else {
            tvErrorProjects.setVisibility(View.GONE);
        }
        if (Utils.isEmptyString(task_id)) {
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

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        newDate.set(year, month, day, 0, 0, 1);

        start_date = DateUtils.convertCurrentToUTC(newDate.getTime(), "yyyy-MM-dd");
        etDate.setText(displayFormat.format(newDate.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        etTime.setText(hourOfDay + ":" + minute);
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

                adapter = new SpinnerProjectAdapter(AddMyTimeSheetActivity.this, R.layout.spinner_item, projectArrayList);
                spProject.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetTaskList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            return model.getTaskList(voids[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                taskArrayList.clear();
                SyncTask task = new SyncTask();
                task.setTitle("Select Task");
                taskArrayList.add(task);

                if (!Utils.isEmptyString(result)) {
                    Log.e(TAG, result);
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        ArrayList<SyncTask> syncTasks = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<SyncTask>>() {
                        }.getType());
                        taskArrayList.addAll(syncTasks);
                    }
                }

                taskAdapter = new SpinnerTaskAdapter(AddMyTimeSheetActivity.this, R.layout.spinner_item, taskArrayList);
                spTask.setAdapter(taskAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AddTimesheet extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return model.addTimesheet(strings[0], strings[1], strings[2], strings[3], false);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                DialogUtils.showDialog(AddMyTimeSheetActivity.this, "", "Time sheet updated successfully......", getString(R.string.ok), new DialogInterface.OnClickListener() {
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
}
