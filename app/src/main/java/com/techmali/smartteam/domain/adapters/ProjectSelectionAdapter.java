package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.techmali.smartteam.R;
import com.techmali.smartteam.models.SyncProject;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.Utils;

import java.util.List;

/**
 * Created by mukesh mali on 5/27/2017.
 */

public class ProjectSelectionAdapter extends RecyclerView.Adapter<ProjectSelectionAdapter.MyViewHolder> {

    private Context context;
    private List<SyncProject> modelList;
    private OnInnerViewsClickListener mListener;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ProjectSelectionAdapter(Context context, List<SyncProject> modelList, OnInnerViewsClickListener mListener) {
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

    public void refreshAdpter(List<SyncProject> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_select_project_list, parent, false);
        return new MyViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        SyncProject project = modelList.get(position);

        if (!Utils.isEmptyString(project.getThumb()))
            imageLoader.displayImage(project.getThumb(), holder.ivProject, options);

        holder.tvProjectName.setText(project.getTitle());

        if (project.isSelected() || Constants.TAG_SELECTED_PROJECT_ID.equalsIgnoreCase(project.getLocal_project_id())) {
            modelList.get(position).setSelected(true);
            holder.llMain.setEnabled(false);
            holder.cbSelect.setEnabled(false);
        } else {
            modelList.get(position).setSelected(false);
            holder.llMain.setEnabled(true);
            holder.cbSelect.setEnabled(true);
        }
        holder.cbSelect.setChecked(modelList.get(position).isSelected());

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(view, mListener, position);
            }
        });

        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setSelected(compoundButton, mListener, position);
            }
        });
    }

    private void setSelected(View view, OnInnerViewsClickListener mListener, int pos) {
        if (!modelList.get(pos).isSelected()) {
            Constants.TAG_SELECTED_PROJECT_ID = modelList.get(pos).getLocal_project_id();
            for (int i = 0; i < modelList.size(); i++) {
                if (i != pos)
                    modelList.get(i).setSelected(false);
            }
            notifyDataSetChanged();
            mListener.onItemClick(view, pos);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMain;
        ImageView ivProject;
        TextView tvProjectName;
        CheckBox cbSelect;

        MyViewHolder(View itemView) {
            super(itemView);

            llMain = (LinearLayout) itemView.findViewById(R.id.llMain);
            ivProject = (ImageView) itemView.findViewById(R.id.ivProject);
            tvProjectName = (TextView) itemView.findViewById(R.id.tvProjectName);
            cbSelect = (CheckBox) itemView.findViewById(R.id.cbSelect);
        }
    }

    public interface OnInnerViewsClickListener {
        void onItemClick(View view, int position);
    }
}
