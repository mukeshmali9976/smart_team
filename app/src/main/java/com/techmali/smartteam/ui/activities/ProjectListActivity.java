package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.domain.adapters.ViewPagerAdapter;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.fragments.ActiveProjectFragment;
import com.techmali.smartteam.ui.fragments.MessageProjectFragment;
import com.techmali.smartteam.ui.views.NonSwipeableViewPager;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;


public class ProjectListActivity extends BaseAppCompatActivity {

    public static final String TAG = ProjectListActivity.class.getSimpleName();

    private TabLayout tabLayout;
    private NonSwipeableViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        initActionBar(getString(R.string.title_project));
        initView();
    }

    private void initView() {
        pager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        setupViewpager();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    private void setupViewpager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(getString(R.string.tab_active), new ActiveProjectFragment());
        adapter.addFragment(getString(R.string.tab_message), new MessageProjectFragment());
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
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
}
