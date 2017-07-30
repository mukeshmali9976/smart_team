package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.techmali.smartteam.R;
import com.techmali.smartteam.models.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerSwipeAdapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NotificationModel> notificationModelList;
    private onSwipeClickLisener mListener;

    public NotificationAdapter(Context context, onSwipeClickLisener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_notification_list, parent, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NotificationModel row_object = getItem(position);

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//        if (!Utils.isEmptyString(row_object.getIs_read()) && row_object.getIs_read().equals(String.valueOf(Constants.MESSAGE_READ))) {
//
//            holder.ll_itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
//
//        } else {
//
//            holder.ll_itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccentLight));
//        }

        //   holder.tvNotificationDateTime.setText(row_object.getCreated());

        // holder.tvNotificationTitle.setText(row_object.getSubject());

//        if (!Utils.isEmptyString(row_object.getMessage())) {
//            holder.tvNotificationDescription.setText(row_object.getMessage());
//            holder.tvNotificationDescription.setVisibility(View.VISIBLE);
//        } else
//            holder.tvNotificationDescription.setVisibility(View.GONE);

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

    public NotificationModel getItem(int position) {
        return notificationModelList.get(position);
    }

    public void setData(ArrayList<NotificationModel> mNotificationList) {
        this.notificationModelList = mNotificationList;
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llRowSwipe;
        public TextView btnDelete;
        private SwipeLayout swipeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            llRowSwipe = (LinearLayout) itemView.findViewById(R.id.llRowSwipe);
            btnDelete = (TextView) itemView.findViewById(R.id.btnDelete);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
        }
    }


    public interface onSwipeClickLisener {
        public void onSwipeClick(View v, int position);
    }


}