package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.techmali.smartteam.R;
import com.techmali.smartteam.models.UserModel;

import java.util.List;

/**
 * Created by mukesh mali on 5/31/2017.
 */

public class UserListAdapter extends RecyclerSwipeAdapter<UserListAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> userList;
    private onSwipeClickLisener mListener;

    public UserListAdapter(Context context, List<UserModel> userList, onSwipeClickLisener mListener) {
        this.context = context;
        this.userList = userList;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_user_list, parent, false);
        return new ViewHolder(itemLayout);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null)
                    mListener.onSwipeClick(v, holder.getAdapterPosition());

                mItemManger.closeAllItems();
                mItemManger.removeShownLayouts(holder.swipeLayout);

            }
        });

        holder.llRowSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null)
                    mListener.onSwipeClick(v, holder.getAdapterPosition());

                mItemManger.closeAllItems();
                mItemManger.removeShownLayouts(holder.swipeLayout);
            }
        });
        mItemManger.bindView(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SwipeLayout swipeLayout;
        private LinearLayout llRowSwipe;
        private TextView btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            llRowSwipe = (LinearLayout) itemView.findViewById(R.id.llRowSwipe);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            btnDelete = (TextView) itemView.findViewById(R.id.btnDelete);
        }

    }

    public interface onSwipeClickLisener {
        public void onSwipeClick(View v, int position);
    }
}
