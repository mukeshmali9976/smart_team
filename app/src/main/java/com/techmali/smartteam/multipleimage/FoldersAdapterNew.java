package com.techmali.smartteam.multipleimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techmali.smartteam.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The Folders adapter to load and show in list.
 *
 * @author Vijay Desai
 */
public class FoldersAdapterNew extends BaseAdapter {

    /**
     * The M context.
     */
    public Context mContext;
    private LayoutInflater mInfalter;
    private ArrayList<GalleryItem> data = new ArrayList<>();
    private List<PhotoDirectory> directories = new ArrayList<>();

    /**
     * Instantiates a new Folders adapter new.
     *
     * @param c           the c
     * @param directories the directories
     */
    public FoldersAdapterNew(Context c, List<PhotoDirectory> directories) {
        this.mContext = c;
        this.directories = directories;
        mInfalter = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return directories.size();
    }

    @Override
    public PhotoDirectory getItem(int position) {
        return directories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInfalter.inflate(R.layout.row_folderpopup, null);
            holder = new ViewHolder();
            holder.tvFolderName = (TextView) convertView.findViewById(R.id.tvFolderName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvFolderName.setText(directories.get(position).getName());

        return convertView;
    }

    /**
     * The type View holder.
     */
    public class ViewHolder {
        /**
         * The Tv folder name.
         */
        TextView tvFolderName;
    }
}
