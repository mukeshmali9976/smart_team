package com.techmali.smartteam.ui.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;

/**
 * Created by Gaurav on 7/31/2017.
 */

public class AddProjectMember extends BaseAppCompatActivity {

    public static final String TAG = AddProjectMember.class.getSimpleName();

    private PendingDataImpl model;

    private TextView tvNoData;
    private RecyclerView rvMember;

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
    }

    class GetMemberList extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return model.getProjectList();
        }
    }
}
