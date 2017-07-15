package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.Task;
import com.techmali.smartteam.R;
import com.techmali.smartteam.models.TaskModel;

import java.util.List;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {


    private Context context;
    private List<TaskModel> taskModelList;
    private OnInnerViewsClickListener mListener;

    public TaskListAdapter(Context context, List<TaskModel> taskModelList, OnInnerViewsClickListener mListener) {
        this.context = context;
        this.taskModelList = taskModelList;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_task_list, parent, false);
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
        return taskModelList.size();
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