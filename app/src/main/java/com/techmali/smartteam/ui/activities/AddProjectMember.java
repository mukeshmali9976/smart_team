package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.DbParams;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.MemberSelectionAdapter;
import com.techmali.smartteam.domain.adapters.UserListAdapter;
import com.techmali.smartteam.domain.listeners.RecyclerItemClickListener;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav on 7/31/2017.
 */

public class AddProjectMember extends BaseAppCompatActivity {

    public static final String TAG = AddProjectMember.class.getSimpleName();

    public static final String EXTRA_SELECTED_USERS = "selected_users";
    public static final String EXTRA_PROJECT_ID = "project_id";

    private PendingDataImpl model;
    private MemberSelectionAdapter mAdapter;

    private TextView tvNoData;
    private RecyclerView rvMember;

    private String project_id = "";

    private ArrayList<SyncUserInfo> selectedArrayList = new ArrayList<>();
    private ArrayList<SyncUserInfo> userArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        model = new PendingDataImpl(this);
        initView();
    }

    private void initView() {

        initActionBar(getString(R.string.title_add_member));

        tvNoData = (TextView) findViewById(R.id.tvNoData);
        rvMember = (RecyclerView) findViewById(R.id.rvMember);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_SELECTED_USERS))
            selectedArrayList = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED_USERS);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_PROJECT_ID))
            project_id = getIntent().getStringExtra(EXTRA_PROJECT_ID);

        new GetMemberList().execute();

        rvMember.addOnItemTouchListener(new RecyclerItemClickListener(this, rvMember, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mAdapter != null) {
                    mAdapter.itemSelected(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_menu_add).setVisible(false);
        MenuItem actionDone = menu.findItem(R.id.action_done);
        actionDone.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            Intent intent = new Intent();
            if (userArrayList != null && !userArrayList.isEmpty()) {
                for (int i = 0; i < userArrayList.size(); i++) {
                    if (!userArrayList.get(i).isSelected()) {
                        userArrayList.remove(i);
                        i--;
                    }
                }
                intent.putExtra(EXTRA_SELECTED_USERS, userArrayList);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetMemberList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return model.getUserList(project_id);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!Utils.isEmptyString(result)) {
                Log.e(TAG, result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        userArrayList = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<SyncUserInfo>>() {
                        }.getType());

                        if (userArrayList != null && !userArrayList.isEmpty()) {

                            if (selectedArrayList != null && !selectedArrayList.isEmpty()) {
                                for (int i = 0; i < userArrayList.size(); i++) {
                                    for (int j = 0; j < selectedArrayList.size(); j++) {
                                        if (userArrayList.get(i).getLocal_user_id().equalsIgnoreCase(selectedArrayList.get(j).getLocal_user_id()))
                                            userArrayList.get(i).setSelected(true);
                                    }
                                }
                            }
                            mAdapter = new MemberSelectionAdapter(AddProjectMember.this, userArrayList, true);
                            rvMember.setLayoutManager(new LinearLayoutManager(AddProjectMember.this));
                            rvMember.setItemAnimator(new DefaultItemAnimator());
                            rvMember.setAdapter(mAdapter);

                            rvMember.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                        } else {
                            rvMember.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
