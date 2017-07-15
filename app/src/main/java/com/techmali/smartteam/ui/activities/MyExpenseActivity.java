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
import android.widget.Toast;


import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.domain.adapters.MyExpenseAdapter;
import com.techmali.smartteam.horizontalcalendar.HorizontalCalendar;
import com.techmali.smartteam.horizontalcalendar.HorizontalCalendarListener;
import com.techmali.smartteam.models.ExpenseModel;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyExpenseActivity extends BaseAppCompatActivity implements View.OnClickListener ,
        RequestListener {

    public static final String TAG = MyExpenseActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;
    private HorizontalCalendar horizontalCalendar;

    private MyExpenseAdapter mAdapter;
    private RecyclerView rvMyExpense;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expense);
        initActionBar("My Expense");

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(MyExpenseActivity.this).getPrefs();

        initView();
        calenderView();
    }

    private void initView() {
        List<ExpenseModel> listModels = new ArrayList<>();
        ExpenseModel expenseModel;

        for (int i = 0; i < 10; i++) {
            expenseModel = new ExpenseModel();
            listModels.add(expenseModel);
        }

        rvMyExpense = (RecyclerView) findViewById(R.id.rvMyExpense);

        mAdapter = new MyExpenseAdapter(MyExpenseActivity.this, listModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMyExpense.setLayoutManager(mLayoutManager);
        rvMyExpense.setItemAnimator(new DefaultItemAnimator());
        rvMyExpense.setAdapter(mAdapter);

    }

    private void calenderView() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);


        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -3);

        final Calendar defaultDate = Calendar.getInstance();

        defaultDate.add(Calendar.MONTH, 0);
        defaultDate.add(Calendar.DAY_OF_WEEK, 0);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(3)
                .dayNameFormat("EE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .showDayName(true)
                .showMonthName(true)
                .defaultSelectedDate(defaultDate.getTime())
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.TRANSPARENT)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Toast.makeText(MyExpenseActivity.this, DateFormat.getDateInstance().format(date) + " is selected!", Toast.LENGTH_SHORT).show();
            }

        });
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
                startActivity(new Intent(this,AddMyExpenseActivity.class));
                Utils.hideKeyboard(this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
    @Override
    public void onSuccess(int id, String response) {

    }

    @Override
    public void onError(int id, String message) {

    }




}
