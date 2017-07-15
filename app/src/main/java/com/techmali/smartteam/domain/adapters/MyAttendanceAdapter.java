package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techmali.smartteam.R;
import com.techmali.smartteam.models.AttendanceModel;

import java.util.List;


public class MyAttendanceAdapter extends RecyclerView.Adapter<MyAttendanceAdapter.ViewHolder> {

    private Context context;
    private List<AttendanceModel> myAttendanceList;

    public MyAttendanceAdapter(Context context, List<AttendanceModel> myAttendanceList) {
        this.context = context;
        this.myAttendanceList = myAttendanceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_my_attendance_list, parent, false);
        return new ViewHolder(itemLayout);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return myAttendanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
