package com.techmali.smartteam.multipleimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.techmali.smartteam.R;
import com.techmali.smartteam.imageview.PhotoView;
import com.techmali.smartteam.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<GalleryItem> mImagesList;
    private Context mContext;
    protected LayoutInflater mLayoutInflater;
    private ImageLoader imageLoader;
    private DisplayImageOptions defaultOptions;
    private int deviceWidth = 0;

    public ImagePagerAdapter(Context mContext, ArrayList<GalleryItem> mImagesList) {
        this.mImagesList = mImagesList;
        this.mContext = mContext;
        imageLoader = ImageLoader.getInstance();
        deviceWidth = Utils.getScreenSize(((ViewImageActivity) mContext)).x;
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.row_zoomview, container, false);
//        ImageViewTouch img = (ImageViewTouch) itemView.findViewById(R.id.img);
        PhotoView img = (PhotoView) itemView.findViewById(R.id.img);

        if (deviceWidth > 0)
            img.setMinimumWidth(deviceWidth);
        if (!Utils.isEmptyString(mImagesList.get(position).filepath)) {
            if (mImagesList.get(position).filepath.contains("http:") || mImagesList.get(position).filepath.contains("https:")) {
                imageLoader.displayImage(mImagesList.get(position).filepath, img, defaultOptions);
            } else {
                imageLoader.displayImage("file://" + mImagesList.get(position).filepath, img, defaultOptions);
            }
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return mImagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
