package com.techmali.smartteam.ui.activities;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddMyTimeSheetActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String TAG = AddMyTimeSheetActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private PendingDataImpl pendingData;

    private Spinner spProject, spTask;
    private EditText etDate, etProjects, etTasks, etTime, etDescription;
    private TextView tvErrorDate, tvErrorDescription, tvErrorTime, tvErrorTasks, tvErrorProjects;

    private ArrayList<SyncProject> projectArrayList = new ArrayList<>();
    private ArrayList<SyncTask> taskArrayList = new ArrayList<>();

    private SpinnerProjectAdapter adapter;
    private SpinnerTaskAdapter taskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_timesheet);

        initActionBar(getString(R.string.title_my_time_sheet));

        prefManager = CryptoManager.getInstance(this).getPrefs();
        pendingData = new PendingDataImpl(this);

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

        SyncProject project = new SyncProject();
        project.setTitle("Select Project");
        projectArrayList.add(project);
        adapter = new SpinnerProjectAdapter(this, R.layout.spinner_item, projectArrayList);
        spProject.setAdapter(adapter);

        SyncTask task = new SyncTask();
        task.setTitle("Select Task");
        taskArrayList.add(task);
        taskAdapter = new SpinnerTaskAdapter(this, R.layout.spinner_item, taskArrayList);
        spTask.setAdapter(taskAdapter);

        new GetProjectList().execute();

        spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position > 0){
                    new GetTaskList().execute(projectArrayList.get(position).getLocal_project_id());
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
                if (checkValidation()) {

                }
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
        if (Utils.isEmptyString(etProjects.getText().toString().trim())) {
            flag = false;
            tvErrorProjects.setVisibility(View.VISIBLE);
            tvErrorProjects.setText(getString(R.string.error_enter));
            Utils.focusOnView(etProjects);
        } else {
            tvErrorProjects.setVisibility(View.GONE);
        }
        if (Utils.isEmptyString(etTasks.getText().toString().trim())) {
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

    private class GetProjectList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return pendingData.getProjectList();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (!Utils.isEmptyString(result)) {
                    Log.e(TAG, result);
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        projectArrayList = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<SyncProject>>() {
                        }.getType());

                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetTaskList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            return pendingData.getTaskList(voids[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (!Utils.isEmptyString(result)) {
                    Log.e(TAG, result);
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        taskArrayList = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<SyncTask>>() {
                        }.getType());

                        if (taskAdapter != null)
                            taskAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
