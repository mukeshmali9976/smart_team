package com.techmali.smartteam.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.techmali.smartteam.R;
import com.techmali.smartteam.imageview.PhotoView;
import com.techmali.smartteam.multipleimage.GalleryItem;
import com.techmali.smartteam.utils.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Anil Prajapati on 26 Apr, 2017.
 */

public class MultipleImageView extends FrameLayout implements View.OnClickListener {
    private LinearLayout llImageList;
    private PhotoView imgSelected;
    private ArrayList<String> mImageList;
    private ArrayList<String> deletedImages;
    private OnItemClickListener mListener;
    private ImageLoader imageLoader;
    private Context context;
    private boolean isViewEnabled = true;
    private View mView;
    private ArrayList<GalleryItem> mModelList;
    private ImageView imvVideo;

    public MultipleImageView(Context context) {
        super(context);
        this.context = context;
    }

    public MultipleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MultipleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public MultipleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    private void initViews() {
        deletedImages = new ArrayList<>();
        imageLoader = ImageLoader.getInstance();
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.layout_multiple_image, this);
            llImageList = (LinearLayout) mView.findViewById(R.id.llImageList);
            imgSelected = (PhotoView) mView.findViewById(R.id.imgSelected);
            imvVideo = (ImageView) mView.findViewById(R.id.imvVideo);
        }
        setImage();
    }

    private void setImage() {

        llImageList.setVisibility(VISIBLE);
        imgSelected.setVisibility(VISIBLE);
        llImageList.removeAllViews();
        llImageList.setEnabled(isViewEnabled);
        llImageList.setPadding(4, 4, 4, 4);
        if (mImageList != null && mImageList.size() > 1)
            for (int i = 0; i < mImageList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.row_new_galley_item, llImageList, false);
                ImageView imvVideo = (ImageView) view.findViewById(R.id.imvVideo);
                ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
                ImageView ivSelected = (ImageView) view.findViewById(R.id.ivSelected);
                ImageView imageView = (ImageView) view.findViewById(R.id.img);

                if (mModelList.get(i).filepath.contains("http:") || mModelList.get(i).filepath.contains("https:")) {
                    if (mModelList.get(i).ext.equalsIgnoreCase("docx")
                            || mModelList.get(i).ext.equalsIgnoreCase("doc")
                            || mModelList.get(i).ext.equalsIgnoreCase("pdf")
                            || mModelList.get(i).ext.equalsIgnoreCase("txt")
                            || mModelList.get(i).ext.equalsIgnoreCase("xlsx")
                            || mModelList.get(i).ext.equalsIgnoreCase("xltx")
                            || mModelList.get(i).ext.equalsIgnoreCase("xls")
                            || mModelList.get(i).ext.equalsIgnoreCase("ppt")
                            || mModelList.get(i).ext.equalsIgnoreCase("pptx")) {
                        imageView.setImageResource(R.drawable.ic_doc);
                    } else {
                        imageLoader.displayImage(mImageList.get(i), imageView);
                    }
                } else {
                    if (mModelList.get(i).ext.equalsIgnoreCase("docx")
                            || mModelList.get(i).ext.equalsIgnoreCase("doc")
                            || mModelList.get(i).ext.equalsIgnoreCase("pdf")
                            || mModelList.get(i).ext.equalsIgnoreCase("txt")
                            || mModelList.get(i).ext.equalsIgnoreCase("xlsx")
                            || mModelList.get(i).ext.equalsIgnoreCase("xltx")
                            || mModelList.get(i).ext.equalsIgnoreCase("xls")
                            || mModelList.get(i).ext.equalsIgnoreCase("ppt")
                            || mModelList.get(i).ext.equalsIgnoreCase("pptx")) {
                        imageView.setImageResource(R.drawable.ic_doc);
                    } else {
                        imageLoader.displayImage("file://" + mImageList.get(i), imageView);
                    }
                }

                ivDelete.setTag(i);
                imageView.setTag(i);
                ivSelected.setEnabled(isViewEnabled);
                ivDelete.setEnabled(isViewEnabled);
                imageView.setEnabled(isViewEnabled);
                if (!isViewEnabled) {
                    ivDelete.setVisibility(GONE);
                    ivSelected.setVisibility(GONE);
                }
                if (mModelList.get(i).file_type.equalsIgnoreCase(Constants.FILE_TYPE_VIDEO + "")) {
                    imvVideo.setVisibility(VISIBLE);
                } else {
                    imvVideo.setVisibility(GONE);
                }
                imageView.setOnClickListener(this);
                ivDelete.setOnClickListener(this);
                llImageList.addView(view);
            }
        if (mImageList != null && mImageList.size() > 0) {
            if (mImageList.get(0).contains("http:") || mImageList.get(0).contains("https:")) {
                imageLoader.displayImage(mImageList.get(0), imgSelected);
            } else {
                imageLoader.displayImage("file://" + mImageList.get(0), imgSelected);
            }
            if (mModelList.get(0).file_type.equalsIgnoreCase(Constants.FILE_TYPE_VIDEO + "")) {
                imvVideo.setVisibility(VISIBLE);
            } else {
                imvVideo.setVisibility(GONE);
            }
            if (mImageList.size() == 1) {
                llImageList.setVisibility(GONE);
            }
        }
    }

    public void setViewEnable(boolean enabled) {
        isViewEnabled = enabled;
        setImage();
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public boolean isEnabled() {
        return isViewEnabled;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setImageStringList(ArrayList<String> list, boolean enabled) {
        mImageList = list;
        isViewEnabled = enabled;
        initViews();
    }

    public void setImageList(ArrayList<GalleryItem> list) {
        if (mImageList == null) {
            mImageList = new ArrayList<>();
        } else {
            mImageList.clear();
        }
        mModelList = new ArrayList<>();
        mModelList.addAll(list);

        for (int i = 0; i < list.size(); i++) {
            mImageList.add(list.get(i).getFilepath());
        }
        initViews();
    }

    /**
     * Gets selected images.
     *
     * @return the selected images
     */
    public ArrayList<GalleryItem> getSelectedImages() {
        return mModelList;
    }


    /**
     * Gets deleted images id.
     *
     * @return the deleted images id
     */
    public ArrayList<String> getDeletedImagesId() {
        return deletedImages;
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        switch (view.getId()) {
            case R.id.img:
                if (mImageList.get(position).contains("http:") || mImageList.get(position).contains("https:")) {
                    imageLoader.displayImage(mImageList.get(position), imgSelected);
                } else {
                    imageLoader.displayImage("file://" + mImageList.get(position), imgSelected);
                }

                if (mModelList.get(position).file_type.equalsIgnoreCase(Constants.FILE_TYPE_VIDEO + "")) {
                    imvVideo.setVisibility(VISIBLE);
                } else {
                    imvVideo.setVisibility(GONE);
                }
                break;
            case R.id.ivDelete:
                if (mModelList != null && mModelList.get(position).isFromServer) {
                    deletedImages.add(mModelList.get(position).attachment_id);
                }
                mImageList.remove(position);
                if (mModelList != null && mModelList.size() >= position)
                    mModelList.remove(position);
                setImage();
                break;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
