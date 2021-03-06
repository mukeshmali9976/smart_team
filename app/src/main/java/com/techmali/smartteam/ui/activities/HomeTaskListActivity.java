package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.HomeTaskListAdapter;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncTask;
import com.techmali.smartteam.models.TaskModel;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeTaskListActivity extends BaseAppCompatActivity {

    public static final String TAG = HomeTaskListActivity.class.getSimpleName();

    private RecyclerView rvTaskList;

    private HomeTaskListAdapter mTaskListAdapter;
    private PendingDataImpl pendingData;

    List<TaskModel> mTaskList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        pendingData = new PendingDataImpl(this);

        initActionBar(getString(R.string.tab_task));
        initView();
    }

    private void initView() {
        rvTaskList = (RecyclerView) findViewById(R.id.rvTaskList);
        rvTaskList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new getProjectList().execute();
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
                startActivity(new Intent(HomeTaskListActivity.this, CreateTaskActivity.class));
                Utils.hideKeyboard(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class getProjectList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return pendingData.getTaskList();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (!Utils.isEmptyString(result)) {
                    Log.e(TAG, result);
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        mTaskList.clear();
                        mTaskList = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<TaskModel>>() {
                        }.getType());

                        List<String> projectList = new ArrayList<>();
                        for (int i = 0; i < mTaskList.size(); i++) {
                            boolean isAdded = false;
                            for (int j = 0; j < projectList.size(); j++) {
                                if (projectList.get(j).equalsIgnoreCase(mTaskList.get(i).getProject_name())) {
                                    isAdded = true;
                                    break;
                                }
                            }
                            if (!isAdded)
                                projectList.add(mTaskList.get(i).getProject_name());
                        }
                        mTaskListAdapter = new HomeTaskListAdapter(HomeTaskListActivity.this, mTaskList, projectList);
                        rvTaskList.setAdapter(mTaskListAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
