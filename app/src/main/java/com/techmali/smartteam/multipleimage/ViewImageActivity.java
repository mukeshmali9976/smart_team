package com.techmali.smartteam.multipleimage;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.utils.Utils;
import com.techmali.smartteam.ui.views.HackyViewPager;


import java.util.ArrayList;

/**
 * The View image activity to show images in full screen.
 *
 * @author Vijay Desai
 */
public class ViewImageActivity extends BaseAppCompatActivity implements View.OnClickListener {

    /**
     * The constant EXTRA_VIEWIMAGE_ARRAY.
     */
    public static String EXTRA_VIEWIMAGE_ARRAY = "extra_viewimage_array";
    /**
     * The constant EXTRA_VIEWIMAGE_POSITION.
     */
    public static String EXTRA_VIEWIMAGE_POSITION = "extra_viewimage_position";
    /**
     * The constant FROM.
     */
    public static String FROM = "from";
    /**
     * The M view pager.
     */
    public HackyViewPager mViewPager;
    private int mPosition;
    private static SelectedImageListener mListener;
    private String mFrom;

    private ArrayList<GalleryItem> mImagesList;
    private boolean isEditMode;
    private RelativeLayout ll_pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewimage);
        retrieveArguments(getIntent().getExtras());
        initView();
    }

    /**
     * Retrieve arguments to get input from previous screen..
     *
     * @param bundle the extras of previous screen's intent.
     */
    private void retrieveArguments(Bundle bundle) {
        if (bundle != null) {
            mFrom = getIntent().getStringExtra(FROM);

            if (getIntent().hasExtra(EXTRA_VIEWIMAGE_ARRAY)) {
                mImagesList = bundle.getParcelableArrayList(EXTRA_VIEWIMAGE_ARRAY);
                isEditMode = false;
            } else {
                mImagesList = GalleryView.mImagesList;
                isEditMode = true;
            }
            mPosition = bundle.getInt(EXTRA_VIEWIMAGE_POSITION);
        }
    }

    /**
     * Initialize all views of current screen.
     */
    private void initView() {
        ll_pager = (RelativeLayout) findViewById(R.id.ll_pager);
        mViewPager = new HackyViewPager(this);

        ll_pager.addView(mViewPager);

//        mViewPager = (HackyViewPager) findViewById(R.id.imagePager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(this, mImagesList);

        mViewPager.setAdapter(mAdapter);

        mViewPager.setCurrentItem(mPosition, true);
        mPosition = mViewPager.getCurrentItem();

        if (isEditMode || (!Utils.isEmptyString(mFrom) && mFrom.equalsIgnoreCase("UPDATE"))) {
            findViewById(R.id.ivDelete).setVisibility(View.VISIBLE);
            findViewById(R.id.ivDelete).setOnClickListener(this);
        } else
            findViewById(R.id.ivDelete).setVisibility(View.GONE);

        findViewById(R.id.ivClose).setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDelete:
                if (mListener != null) {
                    mListener.onDelete(mPosition);
                    finish();
                }
                break;
            case R.id.ivClose:
                finish();
                break;
        }
    }

    /**
     * Sets on image listener.
     *
     * @param listener the listener
     */
    public static void setOnImageListener(SelectedImageListener listener) {
        mListener = listener;
    }
}
