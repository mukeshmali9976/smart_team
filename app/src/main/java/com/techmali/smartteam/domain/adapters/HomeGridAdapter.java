package com.techmali.smartteam.domain.adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.ui.fragments.HomeMenuFragment;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Utils;

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.ViewHolder> {

    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private ViewHolder holder;
    private SharedPreferences prefManager = null;
    private onItemClickListener mListener;
    private boolean viewOthers = false;
    private String[] mListResult = {"DOCUMENT", "TASK", "MY TIMESHEET", "EXPENSE", "USERS"};

    public HomeGridAdapter(Activity mContext, onItemClickListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        prefManager = CryptoManager.getInstance(mContext).getPrefs();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTabName;
        private ImageView ivTabIcon;
        private LinearLayout rowItem;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTabName = (TextView) itemView.findViewById(R.id.tvTabName);
            ivTabIcon = (ImageView) itemView.findViewById(R.id.ivTabIcon);
            rowItem = (LinearLayout) itemView.findViewById(R.id.rowItem);

            rowItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onRowItemClick(getAdapterPosition());
                }
            });
        }
    }

    public Object getItem(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mLayoutInflater.inflate(R.layout.row_home_grid_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        setMenus(mListResult[position], holder);

        int height = Utils.getScreenSize(mContext).y;
        LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) holder.rowItem.getLayoutParams();

        if (mListResult.length == 2) {
            llParams.height = (height - MainActivity.toolbar_height - HomeMenuFragment.viewpager_height - MainActivity.tab_height - HomeMenuFragment.height);
        }
// else if (mListResult.length <= 4) {
//            llParams.height = (height - HomeMenuFragment.toolbar_height - HomeMenuFragment.viewpager_height - HomeMenuFragment.height) / 2;
//        }
        else {
            llParams.height = (height - MainActivity.toolbar_height - HomeMenuFragment.viewpager_height - MainActivity.tab_height - HomeMenuFragment.height) / 2;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mListResult.length;
    }

    private int setMenus(String name, ViewHolder holder) {
        int resourceId = R.drawable.ic_home;
        String moduleName = name;

        holder.ivTabIcon.setImageResource(resourceId);
        holder.ivTabIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.blackRow), PorterDuff.Mode.SRC_ATOP);
        holder.tvTabName.setText(moduleName);
        return resourceId;
    }

    public interface onItemClickListener {

        public void onRowItemClick(int position);
    }


}


