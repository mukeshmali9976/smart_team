package com.techmali.smartteam.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.techmali.smartteam.R;

import com.techmali.smartteam.base.BaseFragment;
import com.techmali.smartteam.domain.adapters.HomeGridAdapter;
import com.techmali.smartteam.domain.adapters.ImageViewPagerAdapter;
import com.techmali.smartteam.domain.listeners.RecyclerItemClickListener;
import com.techmali.smartteam.models.GalleryItem;
import com.techmali.smartteam.models.UserData;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.network.RequestMethod;
import com.techmali.smartteam.request.PARAMS;
import com.techmali.smartteam.request.RequestBuilder;
import com.techmali.smartteam.ui.activities.HomeTaskListActivity;
import com.techmali.smartteam.ui.activities.HomeUserListActivity;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.ui.activities.MyTimeSheetActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.SyncData;
import com.techmali.smartteam.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * The Menu fragment to list out dynamic menus in slide menu.
 *
 * @author Vijay Desai
 */
public class HomeMenuFragment extends BaseFragment implements AdapterView.OnItemClickListener, RequestListener, HomeGridAdapter.onItemClickListener, RecyclerItemClickListener.OnItemClickListener {

    private static final String TAG = HomeMenuFragment.class.getSimpleName();
    public static int viewpager_height;
    public static int height;

    private NetworkManager networkManager = null;
    private SharedPreferences prefManager = null;

    private ViewPager vpEventPager;
    private TabLayout tabDots;
    private RecyclerView rvGridMenu;
    private ImageView ivScroll, ivLastTabIcon;
    private TextView tvLastTabName;
    private LinearLayout llLastRow, lastrowItem;
    private View mRootView;

    private int totalItem = 0;

    private int reqIdLogout = -1, reqIdChangeLanguate = -2, reqIdLoginDetail = -1;


    private HomeGridAdapter mGridAdapter;

    private Handler mHandler = new Handler();
    private boolean isRunning;
    private int LAST_VISIBLE_POSITION;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_home_menu, container, false);

        initView();
        return mRootView;
    }

    private void initView() {

        rvGridMenu = (RecyclerView) mRootView.findViewById(R.id.rvGridMenu);
        tabDots = (TabLayout) mRootView.findViewById(R.id.tabDots);
        vpEventPager = (ViewPager) mRootView.findViewById(R.id.vpEventPager);
        ivScroll = (ImageView) mRootView.findViewById(R.id.ivScroll);
        /*Last Row*/
        llLastRow = (LinearLayout) mRootView.findViewById(R.id.llLastRow);
        tvLastTabName = (TextView) mRootView.findViewById(R.id.tvTabName);
        ivLastTabIcon = (ImageView) mRootView.findViewById(R.id.ivTabIcon);
        lastrowItem = (LinearLayout) mRootView.findViewById(R.id.rowItem);

        ivScroll.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
        // initializeFragmentManager toolbar
        FrameLayout.LayoutParams lpViewpager = (FrameLayout.LayoutParams) vpEventPager.getLayoutParams();
        viewpager_height = lpViewpager.height;
        height = (int) getResources().getDimension(R.dimen.margin_32);

        vpEventPager.setAdapter(new ImageViewPagerAdapter(getActivity(), new ArrayList<GalleryItem>(), true));

        tabDots.setupWithViewPager(vpEventPager);

        // Create a grid layout with two columns
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        rvGridMenu.setLayoutManager(layoutManager);
        mGridAdapter = new HomeGridAdapter(getActivity(), this);
        rvGridMenu.setAdapter(mGridAdapter);

        rvGridMenu.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvGridMenu, this));
        totalItem = mGridAdapter.getItemCount();
        if (totalItem > 4) {
            ivScroll.setVisibility(View.VISIBLE);
        } else {
            ivScroll.setVisibility(View.GONE);
        }

        if (Utils.isInternetAvailable(getActivity())) {
            getLoginDetail();
        }

        ivScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LAST_VISIBLE_POSITION > 0) {
                    rvGridMenu.smoothScrollToPosition(0);
                } else {
                    rvGridMenu.smoothScrollToPosition(totalItem - 1);
                }
            }
        });

        rvGridMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentFirstVisPos = layoutManager.findFirstVisibleItemPosition();
                if (currentFirstVisPos > LAST_VISIBLE_POSITION) {
                    ivScroll.setImageResource(android.R.drawable.arrow_down_float);
                    ivScroll.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                }
                if (currentFirstVisPos < LAST_VISIBLE_POSITION) {
                    ivScroll.setImageResource(android.R.drawable.arrow_up_float);
                    ivScroll.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                }
                LAST_VISIBLE_POSITION = currentFirstVisPos;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).initActionBar(getActivity().getString(R.string.app_name), mRootView);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (vpEventPager.getCurrentItem() + 1 < 4)
                vpEventPager.setCurrentItem(vpEventPager.getCurrentItem() + 1, true);
            else {
                vpEventPager.setCurrentItem(0, true);
            }
            mHandler.postDelayed(mRunnable, 3000);
            isRunning = true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (mHandler != null && mRunnable != null) {
            if (!isRunning) {
                mHandler.postDelayed(mRunnable, 3000);
                isRunning = false;
            }
        }
        if (Utils.isInternetAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), SyncData.class);
            getActivity().startService(intent);
        }
    }

    private void getLoginDetail() {
        networkManager.isProgressBarVisible(true);
        reqIdLoginDetail = networkManager.addRequest(RequestBuilder.blankRequest(), getActivity(), RequestMethod.POST, RequestBuilder.METHOD_LOGIN_DETAIL);
    }

    @Override
    public void onStart() {
        super.onStart();
        networkManager.setListener(this);
    }

    @Override
    public void onStop() {
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
            isRunning = false;
        }
        super.onStop();
        networkManager.removeListener(this);
    }

    @Override
    public void onDestroy() {
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
            isRunning = false;
        }
        super.onDestroy();
    }

    @Override
    public void onSuccess(int id, String response) {
        try {
            if (!Utils.isEmptyString(response)) {
                if (id == reqIdLoginDetail) {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt(PARAMS.TAG_STATUS) == PARAMS.TAG_STATUS_200) {

                        String roleList = object.getJSONObject(PARAMS.TAG_RESULT).getString(PARAMS.TAG_ROLE_LIST);
                        prefManager.edit().putString(PARAMS.KEY_ROLE_LIST, roleList).apply();

                        String userObject = object.getJSONObject(PARAMS.TAG_RESULT).getJSONArray(PARAMS.TAG_USER_DATA).getString(0);
                        UserData data = new Gson().fromJson(userObject, UserData.class);
                        saveLoginDataInPref(data);
                    }
                }else if (id == reqIdLogout || id == reqIdChangeLanguate) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int id, String message) {
        if (id == reqIdLogout || id == reqIdChangeLanguate) {
            displayError(message);
        }
    }

    private void saveLoginDataInPref(UserData data) {

        prefManager.edit().putString(PARAMS.KEY_COMPANY_ID, data.getCompany_id()).apply();
        prefManager.edit().putString(PARAMS.KEY_HEADER_TOKEN, data.getHeader_token()).apply();
        prefManager.edit().putString(PARAMS.KEY_COMPANY_NAME, data.getCompany_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_UNIQUE_CODE, data.getUnique_code()).apply();
        prefManager.edit().putString(PARAMS.KEY_FIRST_NAME, data.getFirst_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_LAST_NAME, data.getLast_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_GENDER, data.getGender()).apply();
        prefManager.edit().putString(PARAMS.KEY_EMAIL, data.getEmail()).apply();
        prefManager.edit().putString(PARAMS.KEY_ROLE_NAME, data.getRole_name()).apply();
        prefManager.edit().putString(PARAMS.KEY_STATUS_ID, data.getStatus_id()).apply();

        prefManager.edit().putBoolean(PARAMS.KEY_IS_LOGGED_IN, true).apply();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onRowItemClick(int position) {

    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.rvGridMenu) {

            switch (position) {
                case 0:
                    break;
                case 1:
                    startActivity(new Intent(getActivity(), HomeTaskListActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(getActivity(), MyTimeSheetActivity.class));
                    break;
                case 3:
                    break;
                case 4:
                    startActivity(new Intent(getActivity(), HomeUserListActivity.class));
                    break;
                case 5:
                    break;
            }
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
