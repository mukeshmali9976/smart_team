package com.techmali.smartteam.multipleimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.techmali.smartteam.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * The Multiple image adapter for selection of multiple images from custom gallery.
 *
 * @author Vijay Desai
 */
public class MultipleImageAdapter extends BaseAdapter {

    private LayoutInflater mInfalter;
    private ArrayList<GalleryItem> data = new ArrayList<>();
    /**
     * The Image loader.
     */
    ImageLoader imageLoader;
    /**
     * The Image limit.
     */
    int imageLimit = Integer.MAX_VALUE, /**
     * The Counter.
     */
    counter = 0;
    private boolean isActionMultiplePick;

    /**
     * Instantiates a new Multiple image adapter.
     *
     * @param c           the c
     * @param imageLoader the image loader
     */
    public MultipleImageAdapter(Context c, ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        mInfalter = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public GalleryItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Sets multiple pick.
     *
     * @param isMultiplePick the is multiple pick
     */
    public void setMultiplePick(boolean isMultiplePick) {
        this.isActionMultiplePick = isMultiplePick;
    }

    /**
     * Sets image selection limit.
     *
     * @param limit the limit
     */
    public void setImageSelectionLimit(int limit) {
        this.imageLimit = limit;
    }


    /**
     * Gets selected.
     *
     * @return the selected
     */
    public ArrayList<GalleryItem> getSelected() {
        ArrayList<GalleryItem> dataT = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSelected) {
                dataT.add(data.get(i));
            }
        }

        return dataT;
    }

    /**
     * Gets path.
     *
     * @param pos the pos
     * @return the path
     */
    public String getPath(int pos) {
        return data.get(pos).filepath;
    }

    /**
     * Add all.
     *
     * @param files the files
     */
    public void addAll(ArrayList<GalleryItem> files) {
        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    /**
     * Change selection.
     *
     * @param v        the v
     * @param position the position
     */
    public void changeSelection(View v, int position) {
        if (data.get(position).isSelected) {
            data.get(position).isSelected = false;
            if (counter > 0) {
                counter--;
            }
        } else {
            if (counter < imageLimit) {
                counter++;
                data.get(position).isSelected = true;
            }
        }
        ((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data.get(position).isSelected);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = mInfalter.inflate(R.layout.gallery_item, null);
            holder = new ViewHolder();
            holder.imgQueue = (ImageView) convertView.findViewById(R.id.imgQueue);

            holder.imgQueueMultiSelected = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected);
            if (isActionMultiplePick) {
                holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
            } else {
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgQueue.setTag(position);
        try {

            imageLoader.displayImage("file://" + data.get(position).filepath,
                    holder.imgQueue, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.imgQueue.setImageResource(R.drawable.no_media);
                            super.onLoadingStarted(imageUri, view);
                        }
                    });
            if (isActionMultiplePick) {
                holder.imgQueueMultiSelected.setSelected(data.get(position).isSelected);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    /**
     * The type View holder.
     */
    public class ViewHolder {
        /**
         * The Img queue.
         */
        ImageView imgQueue;
        /**
         * The Img queue multi selected.
         */
        ImageView imgQueueMultiSelected;
    }

    /**
     * Clear cache.
     */
    public void clearCache() {
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
    }

    /**
     * Clear.
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
