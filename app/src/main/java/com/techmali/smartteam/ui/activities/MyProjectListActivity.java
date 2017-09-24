package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.ProjectListAdapter;
import com.techmali.smartteam.domain.adapters.ProjectSelectionAdapter;
import com.techmali.smartteam.domain.adapters.ViewPagerAdapter;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.ui.fragments.ActiveProjectFragment;
import com.techmali.smartteam.ui.fragments.MessageProjectFragment;
import com.techmali.smartteam.ui.views.NonSwipeableViewPager;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyProjectListActivity extends BaseAppCompatActivity implements ProjectSelectionAdapter.OnInnerViewsClickListener {

    public static final String TAG = MyProjectListActivity.class.getSimpleName();

    private PendingDataImpl pendingData;
    private ProjectSelectionAdapter mAdapter;

    private RecyclerView rvActiveProjectList;
    private TextView tvNoData;

    private ArrayList<SyncProject> projectArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_project);

        pendingData = new PendingDataImpl(this);
        initView();
    }

    private void initView() {

        initActionBar("Select Project");

        tvNoData = (TextView) findViewById(R.id.tvNoData);
        rvActiveProjectList = (RecyclerView) findViewById(R.id.rvActiveProjectList);

        new GetProjectList().execute();
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
                startActivity(new Intent(MyProjectListActivity.this, CreateProjectActivity.class));
                Utils.hideKeyboard(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.fix, R.anim.from_top);
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
                        projectArrayList.clear();
                        projectArrayList = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<SyncProject>>() {
                        }.getType());

                        mAdapter = new ProjectSelectionAdapter(MyProjectListActivity.this, projectArrayList, MyProjectListActivity.this);
                        rvActiveProjectList.setLayoutManager(new LinearLayoutManager(MyProjectListActivity.this));
                        rvActiveProjectList.setItemAnimator(new DefaultItemAnimator());
                        rvActiveProjectList.setAdapter(mAdapter);
                    }
                }

                tvNoData.setVisibility(projectArrayList.size() > 0 ? View.GONE : View.VISIBLE);
                rvActiveProjectList.setVisibility(projectArrayList.size() > 0 ? View.VISIBLE : View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
