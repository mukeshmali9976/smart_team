package com.techmali.smartteam.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.techmali.smartteam.models.TaskModel;
import com.techmali.smartteam.models.UserModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.fragments.ProfileFragment;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;


import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends BaseAppCompatActivity implements
        View.OnClickListener, RequestListener, UserListAdapter.onSwipeClickLisener, RadioGroup.OnCheckedChangeListener {

    public static final String TAG = ProjectDetailActivity.class.getSimpleName();
    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;
    private int reqIdProjectDetail = -1;
    private UserListAdapter mAdapter;
    private TaskListAdapter taskListAdapter;
    private RecyclerView rvUserList, rvTaskList;

    private TextView tvProjectName, tvStartDate, tvEndDate, tvTotalTaskCount, tvTotalTime, tvDescription;
    private ImageView ivProject;
    private RadioGroup rgManagePeoject;
    private Dialog dialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(ProjectDetailActivity.this).getPrefs();

        initActionBar("Project Name");
        initView();
    }

    private void initView() {

        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvTotalTaskCount = (TextView) findViewById(R.id.tvTaskCount);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        rgManagePeoject = (RadioGroup) findViewById(R.id.rgManagePeoject);
        rgManagePeoject.setOnCheckedChangeListener(this);

        ivProject = (ImageView) findViewById(R.id.ivProject);

        rvUserList = (RecyclerView) findViewById(R.id.rvUserList);
        rvTaskList = (RecyclerView) findViewById(R.id.rvTaskList);

        List<UserModel> listModels = new ArrayList<>();
        UserModel userModel;

        for (int i = 0; i < 10; i++) {
            userModel = new UserModel();
            listModels.add(userModel);
        }


        mAdapter = new UserListAdapter(ProjectDetailActivity.this, listModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvUserList.setLayoutManager(mLayoutManager);
        rvUserList.setItemAnimator(new DefaultItemAnimator());
        rvUserList.setAdapter(mAdapter);

        List<TaskModel> listModel = new ArrayList<>();
        TaskModel taskModel;

        for (int i = 0; i < 10; i++) {
            taskModel = new TaskModel();
            listModel.add(taskModel);
        }

        taskListAdapter = new TaskListAdapter(ProjectDetailActivity.this, listModel);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvTaskList.setLayoutManager(mLayoutManager);
        rvTaskList.setItemAnimator(new DefaultItemAnimator());
        rvTaskList.setAdapter(taskListAdapter);

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
                    startActivity(new Intent(ProjectDetailActivity.this, CreateTaskActivity.class));
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
    public void onSuccess(int id, String response) {

    }

    @Override
    public void onError(int id, String message) {

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
                break;
            case R.id.rbTaskList:
                rvUserList.setVisibility(View.GONE);
                rvTaskList.setVisibility(View.VISIBLE);
        }
    }
}