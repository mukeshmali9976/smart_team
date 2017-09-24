package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.models.TimeSheetModel;

import java.util.List;


/**
 * Created by mukesh mali on 5/31/2017.
 */

public class MyTimeSheetAdapter extends RecyclerView.Adapter<MyTimeSheetAdapter.ViewHolder> {

    private Context context;
    private List<TimeSheetModel> myTimeSheetList;
    private boolean showDate;

    public MyTimeSheetAdapter(Context context, List<TimeSheetModel> myTimeSheetList, boolean showDate) {
        this.context = context;
        this.showDate = showDate;
        this.myTimeSheetList = myTimeSheetList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_time_sheet, parent, false);
        return new ViewHolder(itemLayout);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvTaskName.setText(myTimeSheetList.get(position).getTask_name() + " (" + myTimeSheetList.get(position).getProject_name() + ")");
        holder.tvTime.setText("Time: " + myTimeSheetList.get(position).getTotal_time());
        holder.tvDescription.setText("Description: " + myTimeSheetList.get(position).getNote());
        holder.tvDate.setText("Date: " + myTimeSheetList.get(position).getTimesheet_date());
        holder.tvDate.setVisibility(showDate ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return myTimeSheetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTaskName, tvTime, tvDescription, tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }
}
