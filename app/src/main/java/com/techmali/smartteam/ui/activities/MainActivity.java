package com.techmali.smartteam.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.database.PendingDataImpl;
import com.techmali.smartteam.slidingmenu.SlidingActivity;
import com.techmali.smartteam.slidingmenu.SlidingMenu;
import com.techmali.smartteam.ui.fragments.HomeFragment;
import com.techmali.smartteam.ui.fragments.MenuFragment;
import com.techmali.smartteam.utils.Log;

public class MainActivity extends SlidingActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    protected MenuFragment menuFragment;
    private TextView mActionBarTitle = null;
    public static final String EXTRA_FROM = "from";
    private static ActionBar actionBar;
    public static Activity mActivity;

    private PendingDataImpl dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

        mActivity = this;
        dbHelper = new PendingDataImpl(mActivity);

        // set the Behind View
        setBehindContentView(R.layout.layout_menu_drawer_fragment_container);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            menuFragment = new MenuFragment();
            t.replace(R.id.menuFragmentContainer, menuFragment);
            t.commit();
            replaceFragment(new HomeFragment(), "Home");
        } else {
            menuFragment = (MenuFragment) this.getSupportFragmentManager().findFragmentById(R.id.menuFragmentContainer);
        }
//        Collections.synchronizedCollection(mPostList);
//        initActionBar(getString(R.string.app_name));
        SlidingMenu sm = getSlidingMenu();
        sm.setMode(SlidingMenu.LEFT);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setFadeEnabled(true);
        sm.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                menuFragment = new MenuFragment();
                t.replace(R.id.menuFragmentContainer, menuFragment);
                t.commit();
            }
        });

    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                reset((ViewGroup) view);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "Call MainActivity onActivityResult");
        Log.e(TAG, "requestCode" + requestCode);
        Log.e(TAG, "resultCode" + resultCode);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void replaceFragment(Fragment frm, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, frm, tag).addToBackStack(null).commit();
    }

    public void addFragment(Fragment f) {
        this.addFragment(f, true);
    }

    public void addFragment(Fragment f, boolean clearStack) {
        if (clearStack) {
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, f).addToBackStack(null).commit();
    }

    public Fragment getActiveFragment() {

        return getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    public void clearBackStack() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        if (getSlidingMenu().isMenuShowing()) {
            getSlidingMenu().toggle();
        }

        if (getActiveFragment() instanceof HomeFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(R.string.alert);
                    builder.setCancelable(false);
                    builder.setMessage("finish");
                    builder.setPositiveButton(R.string.lbl_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // create alert dialog
                    AlertDialog alertDialog = builder.create();
//                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    // show it
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            getSupportFragmentManager().popBackStack();
            replaceFragment(new HomeFragment(), "Home");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            mActivity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
