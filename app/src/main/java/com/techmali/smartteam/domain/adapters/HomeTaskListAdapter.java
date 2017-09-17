package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.models.SyncTask;

import java.util.List;


public class HomeTaskListAdapter extends RecyclerView.Adapter<HomeTaskListAdapter.ViewHolder> {


    private Context context;
    private List<SyncProject> taskModelList;
    private LayoutInflater layoutInflater;

    public HomeTaskListAdapter(Context context, List<SyncProject> taskModelList) {
        this.context = context;
        this.taskModelList = taskModelList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayout = layoutInflater.inflate(R.layout.row_header, parent, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvProjectName.setText(taskModelList.get(position).getTitle());
        holder.llTaskList.removeAllViews();
        for (int i = 0; i < 5; i++) {
            initTaskList(holder.llTaskList, i);
        }
    }

    private void initTaskList(LinearLayout llTaskList, int pos) {
        View mTaskView = layoutInflater.inflate(R.layout.row_task_list, llTaskList, false);
        TextView tvTaskName = (TextView) mTaskView.findViewById(R.id.tvTaskName);

        tvTaskName.setText("Task " + pos);
        llTaskList.addView(mTaskView);
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName;
        LinearLayout llTaskList;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProjectName = (TextView) itemView.findViewById(R.id.tvProjectName);
            llTaskList = (LinearLayout) itemView.findViewById(R.id.llTaskList);
        }
    }


}