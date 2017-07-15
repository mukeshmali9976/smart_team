package com.techmali.smartteam.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.fragments.MyAttendanceFragment;
import com.techmali.smartteam.ui.fragments.MyLeavesFragment;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;


public class MyAttendanceActivity extends BaseAppCompatActivity implements View.OnClickListener,
        RequestListener, TabLayout.OnTabSelectedListener {

    public static final String TAG = MyAttendanceActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;
    private TabLayout tabLayout;
    private Dialog dialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance);
        initActionBar("My Attendance");

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(MyAttendanceActivity.this).getPrefs();

        initView();
    }

    private void initView() {

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Attendance"));
        tabLayout.addTab(tabLayout.newTab().setText("Leaves"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this);
        addFragment(new MyAttendanceFragment(), R.id.llContainer);
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
                Utils.hideKeyboard(this);
                customDialog();

                break;
        }
        return super.onOptionsItemSelected(item);
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
            case R.id.btn1: // Add attendance
                if (dialog != null) {
                    dialog.dismiss();
                    startActivity(new Intent(this, AddMyAttendanceActivity.class));
                }

                break;
            case R.id.btn2: // Apply Leaves
                if (dialog != null) {
                    dialog.dismiss();
                    startActivity(new Intent(this, ApplyLeavesActivity.class));
                }
                break;

            case R.id.btn4: // Cancel button
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;


        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                addFragment(new MyAttendanceFragment(), R.id.llContainer);
                break;
            case 1:
                addFragment(new MyLeavesFragment(), R.id.llContainer);
                break;
            default:
                addFragment(new MyAttendanceFragment(), R.id.llContainer);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void addFragment(Fragment fragment, int id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    private void customDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);

        Button btn1 = (Button) dialog.findViewById(R.id.btn1);
        Button btn2 = (Button) dialog.findViewById(R.id.btn2);
        Button btn3 = (Button) dialog.findViewById(R.id.btn3);
        Button btn4 = (Button) dialog.findViewById(R.id.btn4);
        View view3 = (View) dialog.findViewById(R.id.view3);

        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        btn4.setVisibility(View.VISIBLE);

        btn1.setText(getResources().getString(R.string.lbl_add_attendance));
        btn2.setText(getResources().getString(R.string.lbl_apply_leaves));

        btn4.setText(getResources().getString(R.string.lbl_cancel));

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        dialog.show();
    }


}
