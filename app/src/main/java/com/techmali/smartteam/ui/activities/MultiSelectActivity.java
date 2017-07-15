package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.domain.adapters.MultiSelectAdapter;
import com.techmali.smartteam.models.IdValueModel;
import com.techmali.smartteam.ui.views.CustomDialog;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Multi select activity.
 */
public class MultiSelectActivity extends BaseAppCompatActivity implements TextWatcher,
        MultiSelectAdapter.OnInnerViewsClickListener {
    /**
     * The constant TAG.
     */
    public static final String TAG = MultiSelectActivity.class.getSimpleName();
    private CustomDialog mDialog;
    /**
     * The constant EXTRA_ANSWERS_LIST.
     */
    public static final String EXTRA_ANSWERS_LIST = "extra_list";

    public static final String EXTRA_ANSWERS_WITH_QTY_LIST = "extra_qty_list";
    /**
     * The constant EXTRA_POSITION_QUESTION.
     */
    public static final String EXTRA_POSITION_QUESTION = "EXTRA_POSITION_QUESTION";
    /**
     * The constant EXTRA_SELECTED_ID.
     */
    public static final String EXTRA_SELECTED_ID = "EXTRA_SELECTED_ID";
    public static final String EXTRA_SELECTED_VALUES = "EXTRA_SELECTED_VALUES";
    /**
     * The constant EXTRA_IS_MULTI_SELECT.
     */
    public static final String EXTRA_IS_MULTI_SELECT = "extra_is_multi_select"; // 1  -Single options   2 -- multiple
    /**
     * The constant EXTRA_TITLE.
     */
    public static final String EXTRA_TITLE = "extra_title";
    /**
     * The constant EXTRA_SELECTION_LIMIT.
     */
    public static final String EXTRA_SELECTION_LIMIT = "extra_selection_limit";

    public static final String EXTRA_ADD_OTHER = "extra_add_other";
    public static final String EXTRA_OTHER_SELECTED = "extra_other_selected";

    private SharedPreferences prefManager = null;

    private ArrayList<IdValueModel> filterList = new ArrayList<>();
    private MultiSelectAdapter multiSelectAdapter;
    private EditText edtSearchName;
    private String mSelectedId = "", mSelectedValue = "";
    private int mQuestionPosition = 0, isMultipleSelectionOn = 1, selectionLimit = -1, mSelectedCount = 0;
    private boolean isOtherAdd = false;
    private boolean mOtherSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiselection);

        initActionBar("");

        prefManager = CryptoManager.getInstance(MultiSelectActivity.this).getPrefs();

        if (filterList.size() == 0) {
            retrieveIntentData();
        }
        initView();
    }

    /**
     * Retrieve Intent data
     */

    private void retrieveIntentData() {

        Intent i = getIntent();
        initActionBar(i.getStringExtra(EXTRA_TITLE));
        isMultipleSelectionOn = i.getExtras().getInt(EXTRA_IS_MULTI_SELECT, 1);
        mQuestionPosition = i.getIntExtra(EXTRA_POSITION_QUESTION, 0);

        mSelectedId = i.getStringExtra(EXTRA_SELECTED_ID);

        if (getIntent().hasExtra(EXTRA_OTHER_SELECTED))
            mOtherSelected = i.getBooleanExtra(EXTRA_OTHER_SELECTED, false);

        if (getIntent().hasExtra(EXTRA_SELECTION_LIMIT))
            selectionLimit = i.getIntExtra(EXTRA_SELECTION_LIMIT, 1);
        if (getIntent().hasExtra(EXTRA_ADD_OTHER))
            isOtherAdd = i.getBooleanExtra(EXTRA_ADD_OTHER, false);
        filterList = i.getParcelableArrayListExtra(EXTRA_ANSWERS_LIST);
    }

    private void initView() {
        RecyclerView rvLocationFilterList = (RecyclerView) findViewById(R.id.lvNotifications);
        rvLocationFilterList.setLayoutManager(new LinearLayoutManager(this));
        rvLocationFilterList.setMotionEventSplittingEnabled(false);
        mSelectedValue = "";
        edtSearchName = (EditText) findViewById(R.id.edtSearchName);
        edtSearchName.setVisibility(View.VISIBLE);
        edtSearchName.addTextChangedListener(this);


        if (filterList != null && filterList.size() > 0) {
            if (isMultipleSelectionOn == 2 && (!Utils.isEmptyString(mSelectedId))) {
                String[] arr;
                arr = mSelectedId.split(",");
                ArrayList<String> list = new ArrayList<>(Arrays.asList(arr));

                for (int i = 0; i < filterList.size(); i++) {
                    if (list.contains(filterList.get(i).getId())) {
                        filterList.get(i).setChecked(true);
                        mSelectedCount++;
                    }
                }

            }

        }


        multiSelectAdapter = new MultiSelectAdapter(MultiSelectActivity.this, isMultipleSelectionOn, this, selectionLimit, mSelectedCount);
        multiSelectAdapter.setData(filterList);

        rvLocationFilterList.setAdapter(multiSelectAdapter);
        rvLocationFilterList.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMultipleSelectionOn == 2)
            getMenuInflater().inflate(R.menu.menu_crop, menu);//Menu Resource, Menu

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.crop:
                edtSearchName.setText("");
                Intent mIntent = new Intent();
                mIntent.putExtra(EXTRA_ANSWERS_LIST, getSelectedQuestionAnswersModel());
                mIntent.putExtra(EXTRA_POSITION_QUESTION, mQuestionPosition);
                setResult(RESULT_OK, mIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private ArrayList<IdValueModel> getSelectedQuestionAnswersModel() {
        ArrayList<IdValueModel> locationList = new ArrayList<>();
        StringBuilder selectedLocation = new StringBuilder();
        if (multiSelectAdapter != null) {
            for (int i = 0; i < multiSelectAdapter.getItemCount(); i++) {
                if (multiSelectAdapter.getItem(i).isChecked()) {
                    selectedLocation.append(multiSelectAdapter.getItem(i).getValue());
                    selectedLocation.append(",");
                    locationList.add(multiSelectAdapter.getItem(i));
                }
            }
        }

        filterList = locationList;
        String selected = "";
        if (selectedLocation.length() > 0)
            selected = selectedLocation.substring(0, selectedLocation.length() - 2);

        Log.e(TAG, "Location Filter" + selected);

        return filterList;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0) {
            ArrayList<IdValueModel> searchList = new ArrayList<>();
            for (int i = 0; i < multiSelectAdapter.getItemCount(); i++) {
                if (multiSelectAdapter.getItem(i).getValue().toLowerCase().contains(s.toString().toLowerCase())) {
                    searchList.add(multiSelectAdapter.getItem(i));
                }
            }
            multiSelectAdapter.setData(searchList);
            multiSelectAdapter.notifyDataSetChanged();
        } else {
            multiSelectAdapter.setData(filterList);
            multiSelectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(View view, int position) {

        if (position >= 0 && multiSelectAdapter.getData().size() > 0) {

            ArrayList<IdValueModel> idValueModelList = new ArrayList<>();
            IdValueModel idValueModel = new IdValueModel();
            idValueModel.setId(multiSelectAdapter.getData().get(position).getId());
            idValueModel.setValue(multiSelectAdapter.getData().get(position).getValue());

            idValueModelList.add(idValueModel);

            Intent mIntent = new Intent();
            mIntent.putExtra(EXTRA_ANSWERS_LIST, idValueModelList);
            mIntent.putExtra(EXTRA_POSITION_QUESTION, mQuestionPosition);
            setResult(RESULT_OK, mIntent);
            finish();


        }
    }


}
