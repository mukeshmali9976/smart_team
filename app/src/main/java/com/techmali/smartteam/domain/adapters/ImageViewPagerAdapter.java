package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.techmali.smartteam.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techmali.smartteam.models.GalleryItem;

import java.util.ArrayList;


public class ImageViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    private LayoutInflater inflater;
    ArrayList<GalleryItem> mHomeScreenImage;
    private ImageLoader mImageLoader;

    private int[] mImageData = {R.drawable.a_1, R.drawable.a_2, R.drawable.a_3, R.drawable.a_4};

    private DisplayImageOptions mDisplayImageOptions;
    private boolean isHomePager;


    /**
     * Instantiates a new Image gallery view pager adapter.
     *
     * @param mActivity the activity
     */
    public ImageViewPagerAdapter(Context mActivity, ArrayList<GalleryItem> mHomeScreenImage, boolean isHomePager) {
        mContext = mActivity;
        this.isHomePager = isHomePager;
        this.mHomeScreenImage = mHomeScreenImage;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = ImageLoader.getInstance();
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.gray_circle)
                .showImageForEmptyUri(R.drawable.gray_circle)
                .showImageOnFail(R.drawable.gray_circle)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        return mImageData.length;
    }


    /**
     * The type View holder.
     */
    private class ViewHolder {

        private ImageView imageView;

        ViewHolder(View itemView) {
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View mItemView = inflater.inflate(R.layout.row_view_pager, container, false);

        ViewHolder holder = new ViewHolder(mItemView);

        //mImageLoader.displayImage("", holder.imageView, mDisplayImageOptions);
        holder.imageView.setImageResource(mImageData[position]);
        //mImageLoader.displayImage(mHomeScreenImage.get(position).getFilepath(), holder.imageView, mDisplayImageOptions);


        /*if (isHomePager) {
            holder.imageView.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.margin_230);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent viewImagesIntent = new Intent(mContext, ViewImageActivity.class);
                    viewImagesIntent.putParcelableArrayListExtra(ViewImageActivity.EXTRA_VIEWIMAGE_ARRAY, mHomeScreenImage);
                    viewImagesIntent.putExtra(ViewImageActivity.EXTRA_VIEWIMAGE_POSITION, position);
                    mContext.startActivity(viewImagesIntent);
                }
            });
        } else {
            holder.imageView.getLayoutParams().height = ViewPager.LayoutParams.MATCH_PARENT;
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }*/


        // Add viewpager_item.xml to ViewPager
        container.addView(mItemView);

        return mItemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((ImageView) object);

    }
}
