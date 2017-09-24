package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.techmali.smartteam.R;
import com.techmali.smartteam.models.UserModel;
import com.techmali.smartteam.utils.Utils;

import java.util.List;


public class HomeUserListAdapter extends RecyclerView.Adapter<HomeUserListAdapter.ViewHolder> {


    private Context context;
    private List<UserModel> userModelList;
    private List<String> modelList;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public HomeUserListAdapter(Context context, List<UserModel> userModelList, List<String> modelList) {
        this.context = context;
        this.userModelList = userModelList;
        this.modelList = modelList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayout = layoutInflater.inflate(R.layout.row_header, parent, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvProjectName.setText(modelList.get(position));
        holder.llTaskList.removeAllViews();
        for (int i = 0; i < userModelList.size(); i++) {
            if (userModelList.get(i).getProject_name().equalsIgnoreCase(modelList.get(position)))
                initTaskList(holder.llTaskList, i);
        }
    }

    private void initTaskList(LinearLayout llTaskList, int pos) {
        View mView = layoutInflater.inflate(R.layout.row_user_list, llTaskList, false);
        TextView tvMemberName = (TextView) mView.findViewById(R.id.tvMemberName);
        TextView tvMemberContact = (TextView) mView.findViewById(R.id.tvMemberContact);
        ImageView ivProfile = (ImageView) mView.findViewById(R.id.ivProfile);

        tvMemberName.setText(userModelList.get(pos).getFirst_name() + " " + userModelList.get(pos).getLast_name());
        tvMemberContact.setText("Contact: " + userModelList.get(pos).getPhone_no());

        if (!Utils.isEmptyString(userModelList.get(pos).getThumb()))
            imageLoader.displayImage(userModelList.get(pos).getThumb(), ivProfile, options);

        llTaskList.addView(mView);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
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