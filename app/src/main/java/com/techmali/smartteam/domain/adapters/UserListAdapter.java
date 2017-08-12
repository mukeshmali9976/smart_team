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

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.techmali.smartteam.R;
import com.techmali.smartteam.models.SyncUserInfo;
import com.techmali.smartteam.utils.Utils;

import java.util.List;

/**
 * Created by mukesh mali on 5/31/2017.
 */

public class UserListAdapter extends RecyclerSwipeAdapter<UserListAdapter.ViewHolder> {

    private Context context;
    private List<SyncUserInfo> userList;
    private onSwipeClickListener mListener;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public UserListAdapter(Context context, List<SyncUserInfo> userList, onSwipeClickListener mListener) {
        this.context = context;
        this.userList = userList;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_user_list, parent, false);
        return new ViewHolder(itemLayout);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.tvMemberName.setText(userList.get(position).getFirst_name() + " " + userList.get(position).getLast_name());

        if (!Utils.isEmptyString(userList.get(position).getThumb()))
            imageLoader.displayImage(userList.get(position).getThumb(), holder.ivProfile, options);

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

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SwipeLayout swipeLayout;
        private LinearLayout llRowSwipe;
        private TextView btnDelete, tvMemberName;
        private ImageView ivProfile;

        public ViewHolder(View itemView) {
            super(itemView);

            llRowSwipe = (LinearLayout) itemView.findViewById(R.id.llRowSwipe);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            btnDelete = (TextView) itemView.findViewById(R.id.btnDelete);
            tvMemberName = (TextView) itemView.findViewById(R.id.tvMemberName);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
        }

    }

    public interface onSwipeClickListener {
        public void onSwipeClick(View v, int position);
    }
}
