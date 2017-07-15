package com.techmali.smartteam.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.domain.adapters.AttendanceAdapter;
import com.techmali.smartteam.models.AttendanceModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;

import java.util.ArrayList;
import java.util.List;


public class TimeSheetActivity extends BaseAppCompatActivity implements View.OnClickListener,
        RequestListener {

    public static final String TAG = TimeSheetActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private AttendanceAdapter mAdapter;
    private RecyclerView rvAttendanceList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(TimeSheetActivity.this).getPrefs();
        initActionBar(getResources().getString(R.string.title_attendance));
        initView();
    }

    private void initView() {

        List<AttendanceModel> listModels = new ArrayList<>();
        AttendanceModel attendanceModel;

        for (int i = 0; i < 10; i++) {
            attendanceModel = new AttendanceModel();
            listModels.add(attendanceModel);
        }
        rvAttendanceList = (RecyclerView) findViewById(R.id.rvAttendance);

        mAdapter = new AttendanceAdapter(TimeSheetActivity.this, listModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAttendanceList.setLayoutManager(mLayoutManager);
        rvAttendanceList.setItemAnimator(new DefaultItemAnimator());
        rvAttendanceList.setAdapter(mAdapter);
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
    public void onSuccess(int id, String response) {

    }

    @Override
    public void onError(int id, String message) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


}
