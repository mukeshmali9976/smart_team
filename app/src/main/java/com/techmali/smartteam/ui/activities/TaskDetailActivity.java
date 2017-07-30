package com.techmali.smartteam.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.domain.adapters.TaskListAdapter;
import com.techmali.smartteam.domain.adapters.UserListAdapter;
import com.techmali.smartteam.models.SyncTask;
import com.techmali.smartteam.models.TaskModel;
import com.techmali.smartteam.models.UserModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends BaseAppCompatActivity implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener, TaskListAdapter.OnInnerViewsClickListener {

    public static final String TAG = TaskDetailActivity.class.getSimpleName();
    private SharedPreferences prefManager = null;
    private TaskListAdapter mAdapter;
    private TaskListAdapter taskListAdapter;
    private RecyclerView rvUserList, rvTaskList;

    private TextView tvProjectName, tvStartDate, tvTaskName, tvTotalTime, tvDescription;
    private ImageView ivProject;
    private RadioGroup rgManagePeoject;
    private Dialog dialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        prefManager = CryptoManager.getInstance(TaskDetailActivity.this).getPrefs();

        initActionBar("Project Name");
        initView();
    }

    private void initView() {

        tvTaskName = (TextView) findViewById(R.id.tvTaskName);
        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        rgManagePeoject = (RadioGroup) findViewById(R.id.rgManagePeoject);
        rgManagePeoject.setOnCheckedChangeListener(this);

        ivProject = (ImageView) findViewById(R.id.ivProject);
        rvUserList = (RecyclerView) findViewById(R.id.rvUserList);
        List<SyncTask> listModels = new ArrayList<>();
        SyncTask userModel;

        for (int i = 0; i < 10; i++) {
            userModel = new SyncTask();
            listModels.add(userModel);
        }

        mAdapter = new TaskListAdapter(TaskDetailActivity.this, listModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvUserList.setLayoutManager(mLayoutManager);
        rvUserList.setItemAnimator(new DefaultItemAnimator());
        rvUserList.setAdapter(mAdapter);

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
                customDialog();
                Utils.hideKeyboard(this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:    // Manage User button
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.btn2:   // Add Task Document
                if (dialog != null) {
                    dialog.dismiss();
                   // startActivity(new Intent(TaskDetailActivity.this, CreateTaskActivity.class));
                }
                break;
            case R.id.btn4:    //cancel button
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;

        }

    }

    private void customDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);

        Button btn1 = (Button) dialog.findViewById(R.id.btn1);
        Button btn2 = (Button) dialog.findViewById(R.id.btn2);
        Button btn3 = (Button) dialog.findViewById(R.id.btn3);
        Button btn4 = (Button) dialog.findViewById(R.id.btn4);
        View view3 = (View) dialog.findViewById(R.id.view3);

        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.VISIBLE);

        btn3.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);

        btn1.setText(getResources().getString(R.string.lbl_manage_user));
        btn2.setText(getResources().getString(R.string.lbl_add_document));
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
                break;

        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
