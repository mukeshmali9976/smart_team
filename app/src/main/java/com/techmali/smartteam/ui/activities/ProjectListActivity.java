package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.domain.adapters.ViewPagerAdapter;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.fragments.ActiveProjectFragment;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;


public class ProjectListActivity extends BaseAppCompatActivity implements View.OnClickListener,
        RequestListener, TabLayout.OnTabSelectedListener {

    public static final String TAG = ProjectListActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private int reqIdProjectList = -1;

    private TabLayout tabLayout;
    private ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(ProjectListActivity.this).getPrefs();

        initActionBar(getString(R.string.title_project));
        initView();
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.viewpager);
        setupViewpager();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    private void setupViewpager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(getString(R.string.tab_active), new ActiveProjectFragment());
        adapter.addFragment(getString(R.string.tab_message), new ActiveProjectFragment());
        pager.setAdapter(adapter);
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
                startActivity(new Intent(ProjectListActivity.this, CreateProjectActivity.class));
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


    @Override
    public void onSuccess(int id, String response) {
    }

    @Override
    public void onError(int id, String message) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                addFragment(new ActiveProjectFragment(), R.id.llContainer);
                break;
            case 1:
                addFragment(new ActiveProjectFragment(), R.id.llContainer);
                break;
            default:
                addFragment(new ActiveProjectFragment(), R.id.llContainer);
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
}
