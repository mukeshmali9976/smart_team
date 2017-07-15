package com.techmali.smartteam.domain.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.models.IdValueModel;
import com.techmali.smartteam.ui.views.CustomDialog;
import com.techmali.smartteam.utils.CryptoManager;

import java.util.List;


/**
 * The type Multi select adapter.
 */
public class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.RowViewHolder> {


    private List<IdValueModel> locationFilterList;
    private Context mContext;
    private int isMultipleSelectionOn, selectionLimit;
    private OnInnerViewsClickListener mListener;
    private CustomDialog mDialog;
    private int mSelectedCount = 0;
    private CustomDialog mCustomDialog;
    private boolean isLanguage = false;
    private View mView;
    private SharedPreferences prefManager = null;
    private String mSelectedString = "";

    /**
     * Instantiates a new Multi select adapter.
     *
     * @param activity              the activity
     * @param isMultipleSelectionOn the is multiple selection on
     * @param mListener             the m listener
     * @param selectionLimit        the selection limit
     * @param selectedCount         the selected count
     */
    public MultiSelectAdapter(Context activity, int isMultipleSelectionOn,
                              OnInnerViewsClickListener mListener, int selectionLimit, int selectedCount) {
        mContext = activity;
        this.isMultipleSelectionOn = isMultipleSelectionOn;
        this.selectionLimit = selectionLimit;
        this.mSelectedCount = selectedCount;
        this.mListener = mListener;
        prefManager = CryptoManager.getInstance(activity).getPrefs();
    }



    public void setData(List<IdValueModel> locationFilterList) {
        this.locationFilterList = locationFilterList;
    }

    public List<IdValueModel> getData() {
        return locationFilterList;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (isMultipleSelectionOn == 2) {
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_text_checkbox, parent, false);
            return new RowViewHolder(mView);
        } else {
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_textview, parent, false);
            return new RowViewHolder(mView);
        }
    }

    @Override
    public void onBindViewHolder(final RowViewHolder holder, final int position) {

        IdValueModel row_object = getItem(position);
        if (isMultipleSelectionOn == 2) {
            holder.setIsRecyclable(false);
            holder.tvItem.setText(row_object.getValue());
            holder.tvItem.setSelected(row_object.isChecked());
            holder.tvItem.setActivated(row_object.isChecked());
            holder.tvItem.setTag(position);


            holder.tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    holder.tvItem.setSelected(!holder.tvItem.isSelected());
                    holder.tvItem.setActivated(!holder.tvItem.isActivated());
                    for (int i = 0; i < locationFilterList.size(); i++) {
                        if (position == i) {
                            if (holder.tvItem.isSelected() || holder.tvItem.isActivated()) {
                                mSelectedCount++;
                                locationFilterList.get(i).setChecked(true);
                            } else {
                                if (mSelectedCount > -1) {
                                    mSelectedCount--;
                                    locationFilterList.get(i).setChecked(false);
                                }
                            }
                        }
                    }
                }
            });

        } else {
            holder.tvSimple.setText(row_object.getValue());
            holder.tvSimple.setTag(position);
            holder.tvSimple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(view, (int) view.getTag());
                }

            });
        }
    }


    public IdValueModel getItem(int pos) {
        return locationFilterList.get(pos);
    }

    @Override
    public int getItemCount() {
        return locationFilterList.size();
    }

    class RowViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSimple, tvItem;
        LinearLayout llParent;

        RowViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvSimple = (TextView) itemView.findViewById(R.id.tvSimple);
            llParent = (LinearLayout) itemView.findViewById(R.id.llParent);
        }
    }


    public interface OnInnerViewsClickListener {

        void onItemClick(View view, int position);
    }


}
