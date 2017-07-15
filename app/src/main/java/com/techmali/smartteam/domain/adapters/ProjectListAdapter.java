package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.techmali.smartteam.R;
import com.techmali.smartteam.models.ProjectListModel;
import com.techmali.smartteam.ui.views.CircleImageView;

import java.util.List;

/**
 * Created by mukesh mali on 5/27/2017.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.MyViewHolder> {

    private Context context;
    private List<ProjectListModel> modelList;
    private OnInnerViewsClickListener mListener;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ProjectListAdapter(Context context, List<ProjectListModel> modelList, OnInnerViewsClickListener mListener) {
        this.context = context;
        this.modelList = modelList;
        this.mListener = mListener;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true).cacheInMemory(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new CircleBitmapDisplayer()).build();
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_project_list, parent, false);
        return new MyViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //     imageLoader.displayImage(currentData.getJob_thumb().toString().trim(), holder.ivJobThumb, options);
        holder.llRowProjectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llRowProjectList;
        public CircleImageView ivProject;
        public TextView tvProjectName,tvStratDate,tvEndDate,tvDescription;
        public MyViewHolder(View itemView) {
            super(itemView);

            llRowProjectList = (LinearLayout) itemView.findViewById(R.id.llRowProjectList);
            ivProject = (CircleImageView) itemView.findViewById(R.id.ivProject);

            tvProjectName = (TextView) itemView.findViewById(R.id.tvProjectName);
            tvStratDate = (TextView) itemView.findViewById(R.id.tvStratDate);
            tvEndDate = (TextView) itemView.findViewById(R.id.tvEndDate);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);

        }
    }

    public interface OnInnerViewsClickListener {
        //void onAssignmentClick(View view, int position);

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}