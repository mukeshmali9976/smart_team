package com.techmali.smartteam.multipleimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techmali.smartteam.R;

import java.util.ArrayList;

/**
 * The Document adapter to load documents in list.
 *
 * @author Vijay Desai
 */
public class DocumentAdapter extends BaseAdapter {

    /**
     * The M context.
     */
    public Context mContext;
    private LayoutInflater mInfalter;
    private ArrayList<GalleryItem> data = new ArrayList<>();

    private boolean isActionMultiplePick;

    /**
     * Instantiates a new Document adapter.
     *
     * @param c the c
     */
    public DocumentAdapter(Context c) {
        this.mContext = c;
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
        } else {
            data.get(position).isSelected = true;
        }
        ((ViewHolder) v.getTag()).imgDocQueueMultiSelected.setSelected(data.get(position).isSelected);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = mInfalter.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.imgDocQueue = (ImageView) convertView.findViewById(R.id.imgDocQueue);
            holder.lnrQueue = (LinearLayout) convertView.findViewById(R.id.lnrQueue);
            holder.tvDocName = (TextView) convertView.findViewById(R.id.tvDocName);
            holder.imgDocQueueMultiSelected = (ImageView) convertView.findViewById(R.id.imgDocQueueMultiSelected);

            holder.tvDocName.setText(data.get(position).docName);
            if (isActionMultiplePick) {
                holder.imgDocQueueMultiSelected.setVisibility(View.VISIBLE);
            } else {
                holder.imgDocQueueMultiSelected.setVisibility(View.GONE);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lnrQueue.setTag(position);
        try {
            if (isActionMultiplePick) {
                holder.imgDocQueueMultiSelected.setSelected(data.get(position).isSelected);
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
         * The Img doc queue.
         */
        ImageView imgDocQueue, /**
         * The Img doc queue multi selected.
         */
        imgDocQueueMultiSelected;
        /**
         * The Tv doc name.
         */
        TextView tvDocName;
        /**
         * The Lnr queue.
         */
        LinearLayout lnrQueue;
    }
}
