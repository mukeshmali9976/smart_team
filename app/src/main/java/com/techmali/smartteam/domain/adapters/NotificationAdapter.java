package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.techmali.smartteam.R;
import com.techmali.smartteam.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NotificationModel> notificationModelList;
    private OnInnerViewsClickListener mListener;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModelList,
                               OnInnerViewsClickListener mListener) {
        this.context = context;
        this.notificationModelList = notificationModelList;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_notification_list, parent, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.llRowSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llRowSwipe;
        public ViewHolder(View itemView) {
            super(itemView);
            llRowSwipe = (LinearLayout) itemView.findViewById(R.id.llRowSwipe);
        }
    }

    public interface OnInnerViewsClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

}