package com.techmali.smartteam.multipleimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.utils.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * The Image adapter to display images in horizontal list.
 *
 * @author Niharika Rana
 */
public class ImageAdapter extends BaseAdapter {
    /**
     * The Selected index.
     */
    int selectedIndex = 0;
    /**
     * The M listener.
     */
//    private ImageView viewSelected;
    onImageClickInterface mListener;
    private LayoutInflater mInfalter;
    private ArrayList<GalleryItem> mImagesList;
    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean isEnabled = true, isThumbEnabled = true;

    /**
     * Instantiates a new Image adapter.
     *
     * @param c           the c
     * @param mImagesList the m images list
     * @param mListener   the m listener
     */
    public ImageAdapter(Context c, ArrayList<GalleryItem> mImagesList, onImageClickInterface mListener) {
        this.mContext = c;
        this.mImagesList = mImagesList;
        this.mListener = mListener;
        mInfalter = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mImagesList.size();
    }

    @Override
    public GalleryItem getItem(int position) {
        return mImagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Sets selected index.
     *
     * @param index the index
     */
    public void setSelectedIndex(int index) {
        Log.d("Display -> ", "arr size : " + mImagesList.size() + "selectedIndex : " + selectedIndex);
        for (GalleryItem item : mImagesList) {
            item.isThumbImageSelected = false;
        }
//        if (mImagesList.size() > selectedIndex)
//            mImagesList.get(selectedIndex).isThumbImageSelected = false;
        selectedIndex = index;
        mImagesList.get(selectedIndex).isThumbImageSelected = true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = mInfalter.inflate(R.layout.row_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
            holder.ivSelected = (ImageView) convertView.findViewById(R.id.ivSelected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivSelected.setTag(position);
        holder.ivDelete.setTag(position);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                if (mListener != null) {
                    mListener.onImageDelete(pos);
                }
            }
        });

        if (mImagesList.get(position).filepath.contains("http:") || mImagesList.get(position).filepath.contains("https:")) {
            if (mImagesList.get(position).ext.equalsIgnoreCase("docx")
                    || mImagesList.get(position).ext.equalsIgnoreCase("doc")
                    || mImagesList.get(position).ext.equalsIgnoreCase("pdf")
                    || mImagesList.get(position).ext.equalsIgnoreCase("txt")
                    || mImagesList.get(position).ext.equalsIgnoreCase("xlsx")
                    || mImagesList.get(position).ext.equalsIgnoreCase("xltx")
                    || mImagesList.get(position).ext.equalsIgnoreCase("xls")
                    || mImagesList.get(position).ext.equalsIgnoreCase("ppt")
                    || mImagesList.get(position).ext.equalsIgnoreCase("pptx")) {
                holder.img.setImageResource(R.drawable.no_media);
                holder.ivSelected.setVisibility(View.GONE);
            } else {
                holder.ivSelected.setVisibility(View.VISIBLE);
                imageLoader.displayImage(mImagesList.get(position).filepath, holder.img);
            }

        } else {
            if (mImagesList.get(position).ext.equalsIgnoreCase("docx")
                    || mImagesList.get(position).ext.equalsIgnoreCase("doc")
                    || mImagesList.get(position).ext.equalsIgnoreCase("pdf")
                    || mImagesList.get(position).ext.equalsIgnoreCase("txt")
                    || mImagesList.get(position).ext.equalsIgnoreCase("xlsx")
                    || mImagesList.get(position).ext.equalsIgnoreCase("xltx")
                    || mImagesList.get(position).ext.equalsIgnoreCase("xls")
                    || mImagesList.get(position).ext.equalsIgnoreCase("ppt")
                    || mImagesList.get(position).ext.equalsIgnoreCase("pptx")) {
                holder.img.setImageResource(R.drawable.active_dot);
                holder.ivSelected.setVisibility(View.GONE);
            } else {
                imageLoader.displayImage("file://" + mImagesList.get(position).filepath, holder.img);
                holder.ivSelected.setVisibility(View.VISIBLE);
            }
        }
        Log.e("", "from server: " + mImagesList.get(position).getIsthumb());

        if (mImagesList.get(position).isFromServer) {
            if (mImagesList.get(position).getIsthumb() == 2 || mImagesList.get(position).isVideo()) {
                holder.ivSelected.setImageResource(R.drawable.circle_orange);
            } else {
                holder.ivSelected.setImageResource(R.drawable.circle_orange);
            }
        } else {
            if (selectedIndex == position || mImagesList.get(position).isVideo()) {
                holder.ivSelected.setImageResource(R.drawable.circle_orange);
            } else {
                holder.ivSelected.setImageResource(R.drawable.circle_orange);
            }
        }

        holder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                if (mListener != null) {
                    mListener.onStarClick(pos);
                }
            }
        });
        holder.img.setEnabled(isEnabled);
        holder.ivDelete.setEnabled(isEnabled);
        holder.ivSelected.setEnabled(isEnabled);

        if (!isThumbEnabled) {
            holder.ivSelected.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Sets list view enabled.
     *
     * @param enabled the enabled
     */
    public void setListViewEnabled(boolean enabled) {
        this.isEnabled = enabled;
        notifyDataSetChanged();
    }

    /**
     * Sets thumb enabled.
     *
     * @param enabled the enabled
     */
    public void setThumbEnabled(boolean enabled) {
        this.isThumbEnabled = enabled;
        notifyDataSetChanged();
    }

    /**
     * The interface On image click interface.
     */
    public interface onImageClickInterface {
        /**
         * On star click.
         *
         * @param pos the pos
         */
        void onStarClick(int pos);

        /**
         * On image delete.
         *
         * @param pos the pos
         */
        void onImageDelete(int pos);
    }

    /**
     * The type View holder.
     */
    public class ViewHolder {
        /**
         * The Img.
         */
        ImageView img, /**
         * The Iv delete.
         */
        ivDelete, /**
         * The Iv selected.
         */
        ivSelected;
    }

}
