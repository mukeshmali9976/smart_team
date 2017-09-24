package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.MemberSelectionAdapter;
import com.techmali.smartteam.domain.adapters.MyTimeSheetAdapter;
import com.techmali.smartteam.horizontalcalendar.HorizontalCalendar;
import com.techmali.smartteam.horizontalcalendar.HorizontalCalendarListener;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.models.TimeSheetModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.DateUtils;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyTimeSheetActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String TAG = MyTimeSheetActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private PendingDataImpl model;

    private MyTimeSheetAdapter mAdapter;
    private RecyclerView rvMyTimeSheet;
    private TextView tvMonthName;
    private HorizontalCalendar horizontalCalendar;

    private List<TimeSheetModel> listModels = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timesheet);

        initActionBar("My TimeSheet");
        prefManager = CryptoManager.getInstance(this).getPrefs();
        model = new PendingDataImpl(this);

        initView();
    }

    private void initView() {

        tvMonthName = (TextView) findViewById(R.id.tvMonthName);
        rvMyTimeSheet = (RecyclerView) findViewById(R.id.rvMyTimeSheetList);

        new GetTimesheetList().execute();

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

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
                tvMonthName.setText(dateFormat.format(date).toUpperCase());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String selected = format.format(date);
                List<TimeSheetModel> model = new ArrayList<>();

                for(int i=0; i<listModels.size(); i++){
                    if(listModels.get(i).getTimesheet_date().equalsIgnoreCase(selected)){
                        model.add(listModels.get(i));
                    }
                }

                mAdapter = new MyTimeSheetAdapter(MyTimeSheetActivity.this, model, false);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rvMyTimeSheet.setLayoutManager(mLayoutManager);
                rvMyTimeSheet.setItemAnimator(new DefaultItemAnimator());
                rvMyTimeSheet.setAdapter(mAdapter);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    private class GetTimesheetList extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return model.getTimesheet();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!Utils.isEmptyString(result)) {
                Log.e(TAG, result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {
                        listModels = new Gson().fromJson(object.getString(PARAMS.TAG_RESULT), new TypeToken<List<TimeSheetModel>>() {
                        }.getType());

                        if (listModels != null && !listModels.isEmpty()) {

                            mAdapter = new MyTimeSheetAdapter(MyTimeSheetActivity.this, listModels, false);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rvMyTimeSheet.setLayoutManager(mLayoutManager);
                            rvMyTimeSheet.setItemAnimator(new DefaultItemAnimator());
                            rvMyTimeSheet.setAdapter(mAdapter);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
