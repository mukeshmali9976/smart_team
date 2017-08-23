package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.domain.adapters.MyTimeSheetAdapter;
import com.techmali.smartteam.horizontalcalendar.HorizontalCalendar;
import com.techmali.smartteam.horizontalcalendar.HorizontalCalendarListener;
import com.techmali.smartteam.models.TimeSheetModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyTimeSheetActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String TAG = MyTimeSheetActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private MyTimeSheetAdapter mAdapter;
    private RecyclerView rvMyTimeSheet;
    private TextView tvMonthName;
    private HorizontalCalendar horizontalCalendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timesheet);

        initActionBar("My TimeSheet");

        prefManager = CryptoManager.getInstance(MyTimeSheetActivity.this).getPrefs();

        initView();
    }


    private void initView() {

        tvMonthName = (TextView) findViewById(R.id.tvMonthName);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .showDayName(true)
                .showMonthName(true)
                .defaultSelectedDate(Calendar.getInstance().getTime())
                .build();

        rvMyTimeSheet = (RecyclerView) findViewById(R.id.rvMyTimeSheetList);

        List<TimeSheetModel> listModels = new ArrayList<>();
        TimeSheetModel timeSheetModel;
        for (int i = 0; i < 10; i++) {
            timeSheetModel = new TimeSheetModel();
            listModels.add(timeSheetModel);
        }

        mAdapter = new MyTimeSheetAdapter(MyTimeSheetActivity.this, listModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMyTimeSheet.setLayoutManager(mLayoutManager);
        rvMyTimeSheet.setItemAnimator(new DefaultItemAnimator());
        rvMyTimeSheet.setAdapter(mAdapter);

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
                tvMonthName.setText(dateFormat.format(date).toUpperCase());
            }
        });
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
                startActivity(new Intent(this, AddMyTimeSheetActivity.class));
                Utils.hideKeyboard(this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
