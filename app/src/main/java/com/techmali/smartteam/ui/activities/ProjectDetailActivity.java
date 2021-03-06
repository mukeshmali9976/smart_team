package com.techmali.smartteam.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.TaskListAdapter;
import com.techmali.smartteam.domain.adapters.UserListAdapter;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncTask;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.utils.DateUtils;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends BaseAppCompatActivity implements
        View.OnClickListener, UserListAdapter.onSwipeClickListener,
        RadioGroup.OnCheckedChangeListener, TaskListAdapter.OnInnerViewsClickListener {

    public static final String TAG = ProjectDetailActivity.class.getSimpleName();

    public static final String TAG_PROJECT_ID = "project_id";

    private PendingDataImpl model;

    private UserListAdapter mAdapter;
    private TaskListAdapter taskListAdapter;
    private RecyclerView rvUserList, rvTaskList;

    private TextView tvProjectName, tvStartDate, tvEndDate, tvTotalTaskCount, tvTotalTime, tvDescription, tvComingSoon;
    private ImageView ivProject;
    private RadioGroup rgManagePeoject;
    private Dialog dialog = null;

    private String local_project_id = "", server_project_id = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        model = new PendingDataImpl(this);

        initActionBar("Project Detail");
        initView();
    }

    private void initView() {

        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvTotalTaskCount = (TextView) findViewById(R.id.tvTaskCount);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvComingSoon = (TextView) findViewById(R.id.tvComingSoon);

        rgManagePeoject = (RadioGroup) findViewById(R.id.rgManagePeoject);
        rgManagePeoject.setOnCheckedChangeListener(this);

        ivProject = (ImageView) findViewById(R.id.ivProject);

        rvUserList = (RecyclerView) findViewById(R.id.rvUserList);
        rvTaskList = (RecyclerView) findViewById(R.id.rvTaskList);

        if (getIntent() != null && getIntent().hasExtra(TAG_PROJECT_ID)) {
            local_project_id = getIntent().getStringExtra(TAG_PROJECT_ID);
            new GetProjectDetail().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        menu.findItem(R.id.action_menu_add).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_add:
                // startActivityForResult(new Intent(this, NotificationActivity.class), Constants.INTENT_NOTIFICATION);

                customDialog();
                Utils.hideKeyboard(this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:  // Manage User button
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.btn2: // Add Task button
                if (dialog != null) {
                    dialog.dismiss();
                    Intent intent = new Intent(ProjectDetailActivity.this, CreateTaskActivity.class);
                    intent.putExtra(CreateTaskActivity.TAG_LOCAL_PROJECT_ID, local_project_id);
                    intent.putExtra(CreateTaskActivity.TAG_SERVER_PROJECT_ID, server_project_id);
                    startActivity(intent);
                }
                break;
            case R.id.btn3: // Add Document button
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.btn4: //cancel button
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;

        }
    }

    @Override
    public void onSwipeClick(View v, int position) {

    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private void customDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);

        Button btn1 = (Button) dialog.findViewById(R.id.btn1);
        Button btn2 = (Button) dialog.findViewById(R.id.btn2);
        Button btn3 = (Button) dialog.findViewById(R.id.btn3);
        Button btn4 = (Button) dialog.findViewById(R.id.btn4);

        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.VISIBLE);

        btn1.setText(getResources().getString(R.string.lbl_manage_user));
        btn2.setText(getResources().getString(R.string.lbl_add_task));
        btn3.setText(getResources().getString(R.string.lbl_add_document));
        btn4.setText(getResources().getString(R.string.lbl_cancel));

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rbMembers:
                rvUserList.setVisibility(View.VISIBLE);
                rvTaskList.setVisibility(View.GONE);
                tvComingSoon.setVisibility(View.GONE);
                break;
            case R.id.rbTaskList:
                rvUserList.setVisibility(View.GONE);
                rvTaskList.setVisibility(View.VISIBLE);
                tvComingSoon.setVisibility(View.GONE);
                break;
            case R.id.rbDocuments:
                rvUserList.setVisibility(View.GONE);
                rvTaskList.setVisibility(View.GONE);
                tvComingSoon.setVisibility(View.VISIBLE);
                break;
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
                        if (projectArrayList != null && !projectArrayList.isEmpty()) {
                            server_project_id = projectArrayList.get(0).getServer_project_id();

                            tvProjectName.setText(projectArrayList.get(0).getTitle());
                            tvDescription.setText(projectArrayList.get(0).getDescription());
                            tvStartDate.setText(getString(R.string.lbl_start_date) + " " + DateUtils.getLocalDateFromUTC(projectArrayList.get(0).getStart_date(), "dd MMM, yyyy", "yyyy-MM-dd"));
                            tvEndDate.setText(getString(R.string.lbl_end_date) + " " + DateUtils.getLocalDateFromUTC(projectArrayList.get(0).getEnd_date(), "dd MMM, yyyy", "yyyy-MM-dd"));
                        }

                        ArrayList<SyncUserInfo> projectUserLinkArrayList;
                        projectUserLinkArrayList = new Gson().fromJson(object.getJSONObject(PARAMS.TAG_RESULT).getString(PARAMS.TAG_USER_LIST), new TypeToken<List<SyncUserInfo>>() {
                        }.getType());
                        if (projectUserLinkArrayList != null && !projectUserLinkArrayList.isEmpty()) {
                            mAdapter = new UserListAdapter(ProjectDetailActivity.this, projectUserLinkArrayList, ProjectDetailActivity.this);
                            rvUserList.setLayoutManager(new LinearLayoutManager(ProjectDetailActivity.this));
                            rvUserList.setItemAnimator(new DefaultItemAnimator());
                            rvUserList.setAdapter(mAdapter);
                        }

                        ArrayList<SyncTask> taskList;
                        taskList = new Gson().fromJson(object.getJSONObject(PARAMS.TAG_RESULT).getString(PARAMS.TAG_TASK_LIST), new TypeToken<List<SyncTask>>() {
                        }.getType());
                        if (taskList != null && !taskList.isEmpty()) {
                            taskListAdapter = new TaskListAdapter(ProjectDetailActivity.this, taskList, ProjectDetailActivity.this);
                            rvTaskList.setLayoutManager(new LinearLayoutManager(ProjectDetailActivity.this));
                            rvTaskList.setItemAnimator(new DefaultItemAnimator());
                            rvTaskList.setAdapter(taskListAdapter);

                            tvTotalTaskCount.setText(getString(R.string.lbl_total_task) + " " + taskList.size() + " Task(s)");
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
