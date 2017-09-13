package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.multipleimage.GalleryItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class ViewImagesAdapter extends BaseAdapter {

    private LayoutInflater mInfalter;
    private ArrayList<GalleryItem> mImagesList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions defaultOptions;

    public ViewImagesAdapter(Context c, ArrayList<GalleryItem> mImagesList) {
        this.mImagesList = mImagesList;
        mInfalter = LayoutInflater.from(c);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.above_shadow)
                .showImageForEmptyUri(R.drawable.above_shadow)
                .showImageOnFail(R.drawable.above_shadow)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        return mImagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInfalter.inflate(R.layout.row_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.findViewById(R.id.ivDelete).setVisibility(View.GONE);
            convertView.findViewById(R.id.ivSelected).setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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
                holder.img.setImageResource(R.drawable.ic_doc);
            } else {
                imageLoader.displayImage(mImagesList.get(position).filepath, holder.img, defaultOptions);
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
                holder.img.setImageResource(R.drawable.ic_doc);
            } else {
                imageLoader.displayImage("file://" + mImagesList.get(position).filepath, holder.img, defaultOptions);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView img;
    }
}
