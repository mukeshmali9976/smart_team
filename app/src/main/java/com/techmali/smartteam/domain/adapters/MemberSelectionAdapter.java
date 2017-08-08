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
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.utils.Utils;

import java.util.List;

/**
 * Created by Gaurav on 5/27/2017.
 */

public class MemberSelectionAdapter extends RecyclerView.Adapter<MemberSelectionAdapter.MyViewHolder> {

    private Context context;
    private List<SyncUserInfo> modelList;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private boolean isCbVisible;

    public MemberSelectionAdapter(Context context, List<SyncUserInfo> modelList, boolean isCbVisible) {
        this.context = context;
        this.modelList = modelList;
        this.isCbVisible = isCbVisible;
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
        View itemLayout = layoutInflater.inflate(R.layout.row_add_member, parent, false);
        return new MyViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvMemberName.setText(modelList.get(position).getFirst_name() + " " + modelList.get(position).getLast_name());

        String phone_no = "Contact: ";
        if (!Utils.isEmptyString(modelList.get(position).getPhone_no()))
            phone_no = phone_no + modelList.get(position).getPhone_no();

        holder.tvMemberContact.setText(phone_no);
        holder.cbMember.setChecked(modelList.get(position).isSelected());
        holder.cbMember.setVisibility(isCbVisible ? View.VISIBLE : View.GONE);

        if (!Utils.isEmptyString(modelList.get(position).getThumb()))
            imageLoader.displayImage(modelList.get(position).getThumb(), holder.ivProfile, options);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llUser;
        ImageView ivProfile;
        TextView tvMemberName, tvMemberContact;
        CheckBox cbMember;

        MyViewHolder(View itemView) {
            super(itemView);

            llUser = (LinearLayout) itemView.findViewById(R.id.llUser);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);

            tvMemberName = (TextView) itemView.findViewById(R.id.tvMemberName);
            tvMemberContact = (TextView) itemView.findViewById(R.id.tvMemberContact);
            cbMember = (CheckBox) itemView.findViewById(R.id.cbMember);
        }
    }

    public void itemSelected(final int position) {
        if (modelList != null && !modelList.isEmpty()) {
            boolean check = modelList.get(position).isSelected();
            modelList.get(position).setSelected(!check);
            notifyDataSetChanged();
        }
    }
}
