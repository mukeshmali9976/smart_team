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
import com.techmali.smartteam.domain.adapters.HomeUserListAdapter;
import com.techmali.smartteam.models.TaskModel;
import com.techmali.smartteam.models.UserData;
import com.techmali.smartteam.models.UserModel;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeUserListActivity extends BaseAppCompatActivity {

    public static final String TAG = HomeUserListActivity.class.getSimpleName();

    private RecyclerView rvTaskList;

    private HomeUserListAdapter mTaskListAdapter;
    private PendingDataImpl pendingData;

    List<UserModel> mTaskList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        pendingData = new PendingDataImpl(this);

        initActionBar("Users");
        initView();
    }

    private void initView() {
        rvTaskList = (RecyclerView) findViewById(R.id.rvTaskList);
        rvTaskList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetUserList().execute();
    }

    private class GetUserList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return pendingData.getUserList();
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
                        mTaskList = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<UserModel>>() {
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
                        mTaskListAdapter = new HomeUserListAdapter(HomeUserListActivity.this, mTaskList, projectList);
                        rvTaskList.setAdapter(mTaskListAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
