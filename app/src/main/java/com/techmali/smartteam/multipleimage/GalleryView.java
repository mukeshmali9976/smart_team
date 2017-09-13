package com.techmali.smartteam.multipleimage;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.techmali.smartteam.R;
import com.techmali.smartteam.ui.activities.VideoDisplayActivity;

import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;

/**
 * The customized Gallery view that scrolls horizontally and add dynamic images in selection.
 *
 * @author Vijay Desai
 */
public class GalleryView extends LinearLayout implements SelectedImageListener, AdapterView.OnItemClickListener, View.OnClickListener,
        ImageAdapter.onImageClickInterface {

    private static final String TAG = GalleryView.class.getSimpleName();
    /**
     * The M images list.
     */
    public static ArrayList<GalleryItem> mImagesList;
    /**
     * The Deleted images id.
     */
    public ArrayList<String> DeletedImagesId;
    /**
     * The Star thumb id.
     */
// default star seleceted(from server)
    public String starThumbId;
    /**
     * The Star attachment id.
     */
//
    public String starAttachmentId;
    private HorizontalListView lvImages;
    private ImageView ivThumb;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String text = "";
    private int textColor;
    private TextView tvTitle, tvListsize;
    /**
     * The Frm thumbnail.
     */
    FrameLayout frmThumbnail;
    private boolean isCamera, isGallery, isDoc, isVideo, isThumbnail = true, isEnable;
    private ImageAdapter mAdapter;
    private GalleryItem thumbImage;
    private int mImageLimit = Integer.MAX_VALUE;

    /**
     * Instantiates a new Gallery view.
     *
     * @param context the context
     */
    public GalleryView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.gallery_view, this);
        if (!isInEditMode())
            initView(context, null);
    }

    /**
     * Instantiates a new Gallery view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initView(context, attrs);
    }

    /**
     * Instantiates a new Gallery view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public GalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initView(context, attrs);
    }

    /**
     * Instantiates a new Gallery view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GalleryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode())
            initView(context, attrs);
    }

    /**
     * Initialize all views of current screen.
     *
     * @param context the context of screen.
     * @param attrs   Attribute set of a list of array.
     */
    private void initView(Context context, AttributeSet attrs) {
//        initImageLoader(context);
        DeletedImagesId = new ArrayList<>();
        mImagesList = new ArrayList<>();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GalleryView, 0, 0);
        try {
            // get the text and colors specified using the names in attrs.xml
            text = a.getString(R.styleable.GalleryView_text);
            textColor = a.getInteger(R.styleable.GalleryView_textColor, R.color.blackRow);
            isCamera = a.getBoolean(R.styleable.GalleryView_camera, false);
            isVideo = a.getBoolean(R.styleable.GalleryView_video, false);
            isGallery = a.getBoolean(R.styleable.GalleryView_gallery, false);
            isDoc = a.getBoolean(R.styleable.GalleryView_document, false);
//            isSingleDoc = a.getBoolean(R.styleable.GalleryView_singleDocument, false);
            isThumbnail = a.getBoolean(R.styleable.GalleryView_thumbnail, true);
        } finally {
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.gallery_view, this);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        tvTitle.setText(text);
        frmThumbnail = (FrameLayout) this.findViewById(R.id.frmThumbnail);
        ivThumb = (ImageView) this.findViewById(R.id.ivThumb);
        frmThumbnail.setVisibility(GONE);
        lvImages = (HorizontalListView) this.findViewById(R.id.lvImages);

        CustomGalleryActivity.setOnTouchListener(this);
        VideoActivity.setOnTouchListener(this);
        CropActivity.setOnCropListener(this);
        ViewImageActivity.setOnImageListener(this);

        tvListsize = (TextView) this.findViewById(R.id.tvTotalSize);

        this.findViewById(R.id.ivPhotos).setOnClickListener(this);

        ivThumb.setOnClickListener(this);
        lvImages.setOnItemClickListener(this);
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param leftLabel the left label
     */
    public void setText(String leftLabel) {
        this.text = leftLabel;
        if (tvTitle != null) {
            tvTitle.setText(text);
        }
    }

    /**
     * Sets text color.
     *
     * @param color the color
     */
    public void setTextColor(int color) {
        this.textColor = color;
        tvTitle.setTextColor(textColor);
    }

    /**
     * Sets upload icon.
     *
     * @param icon the icon
     */
    public void setUploadIcon(int icon) {
        ivThumb.setImageResource(icon);
    }

    /**
     * Sets image limit.
     *
     * @param limit the limit
     */
    public void setmImageLimit(int limit) {
        mImageLimit = limit;
    }

    /**
     * Is view enable boolean.
     *
     * @return the boolean
     */
    public boolean isViewEnable() {
        return isEnable;
    }

    /**
     * Sets view enable.
     *
     * @param enabled the enabled
     */
    public void setViewEnable(boolean enabled) {
        findViewById(R.id.ivPhotos).setEnabled(enabled);
        ivThumb.setEnabled(enabled);
        if (mAdapter != null) {
            mAdapter.setListViewEnabled(enabled);
        }
        isEnable = enabled;
    }

    /**
     * Sets is thumbnail.
     *
     * @param enabled the enabled
     */
    public void setIsThumbnail(boolean enabled) {
        this.isThumbnail = enabled;
    }

    /**
     * Sets adapter.
     *
     * @param all_path     the all path
     * @param isFromServer the is from server
     */
    public void setAdapter(ArrayList<GalleryItem> all_path, boolean isFromServer) {
        findViewById(R.id.listLayout).setVisibility(VISIBLE);
        ArrayList<GalleryItem> dataT = new ArrayList<>();

        // To check is it video.
        boolean isNewArray = false;
        if (all_path.get(0).isVideo() && !isFromServer) {
            isNewArray = true;
        } else {
            for (GalleryItem item : mImagesList) {
                if (item.isVideo() && !isFromServer) {
                    isNewArray = true;
                }
            }
        }

        if (isNewArray) {
            DeletedImagesId = new ArrayList<>();
            if (isThumbnail) {
                if (thumbImage != null)
                    DeletedImagesId.add(thumbImage.attachment_id);
            }
            for (GalleryItem iData : mImagesList)
                DeletedImagesId.add(iData.attachment_id);
            mImagesList = new ArrayList<>();
            ivThumb.setImageDrawable(null);
        }

        for (int i = 0; i < all_path.size(); i++) {
            GalleryItem item = new GalleryItem();
            item.filepath = all_path.get(i).filepath;
            item.attachment_id = all_path.get(i).attachment_id;
            item.isFromServer = isFromServer;
            item.isthumb = all_path.get(i).isthumb;
            item.setVideoUrl(all_path.get(i).getVideoUrl());
            item.setIsVideo(all_path.get(i).isVideo());
            item.file_type = all_path.get(i).getFile_type();
            int dot = all_path.get(i).filepath.lastIndexOf(".");
            item.ext = all_path.get(i).filepath.substring(dot + 1);
            if (isFromServer && isThumbnail) {
                if (all_path.get(i).getIsthumb() == 1) {
                    imageLoader.displayImage(all_path.get(i).getThumbpath(), ivThumb);
                    setThumbImage(all_path.get(i));
                    if (all_path.get(i).isVideo()) {
                        dataT.add(item);
                    }
                } else {
                    dataT.add(item);
                }
            } else {
                dataT.add(item);
            }
        }
        mImagesList.addAll(dataT);

        if (mImagesList.size() > 0 && ivThumb.getDrawable() == null && !isFromServer && isThumbnail) {
//
            if (mImagesList.size() > 1) {
                boolean b = false;
                for (int i = 0; i < mImagesList.size(); i++) {
                    if (mImagesList.get(i).getIsthumb() == 2) {
                        mImagesList.get(i).isThumbImageSelected = true;
                        mImagesList.get(i).setIsthumb(2);
                        mImagesList.get(i).thumbpath = mImagesList.get(i).filepath;
                        if (mImagesList.get(i).ext.equalsIgnoreCase("png") || mImagesList.get(i).ext.equalsIgnoreCase("jpg") ||
                                mImagesList.get(i).ext.equalsIgnoreCase("jpeg") || mImagesList.get(i).ext.equalsIgnoreCase("gif")) {
                            setThumbImage(mImagesList.get(i));
                        }
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    mImagesList.get(0).isThumbImageSelected = true;
                    mImagesList.get(0).setIsthumb(2);
                    mImagesList.get(0).thumbpath = mImagesList.get(0).filepath;

                    imageLoader.displayImage("file://" + mImagesList.get(0).filepath, ivThumb);
                    mImagesList.get(0).setVideoUrl(mImagesList.get(0).getVideoUrl());
                    mImagesList.get(0).setIsVideo(mImagesList.get(0).isVideo());
                    if (mImagesList.get(0).ext.equalsIgnoreCase("png") || mImagesList.get(0).ext.equalsIgnoreCase("jpg") ||
                            mImagesList.get(0).ext.equalsIgnoreCase("jpeg") || mImagesList.get(0).ext.equalsIgnoreCase("gif")) {
                        setThumbImage(dataT.get(0));
                    }
                }
            } else {
                mImagesList.get(0).isThumbImageSelected = true;
                mImagesList.get(0).setIsthumb(2);
                mImagesList.get(0).thumbpath = mImagesList.get(0).filepath;

                imageLoader.displayImage("file://" + mImagesList.get(0).filepath, ivThumb);
                mImagesList.get(0).setVideoUrl(mImagesList.get(0).getVideoUrl());
                mImagesList.get(0).setIsVideo(mImagesList.get(0).isVideo());
                if (mImagesList.get(0).ext.equalsIgnoreCase("png") || mImagesList.get(0).ext.equalsIgnoreCase("jpg") ||
                        mImagesList.get(0).ext.equalsIgnoreCase("jpeg") || mImagesList.get(0).ext.equalsIgnoreCase("gif")) {
                    setThumbImage(dataT.get(0));
                }
            }
        }

        mAdapter = new ImageAdapter(getContext(), mImagesList, this);
        lvImages.setAdapter(mAdapter);

        /*for (int i = 0; i < mImagesList.size(); i++) {
            if (mImagesList.get(i).getIsthumb() == 2) {
                setThumbImage(mImagesList.get(i));
                mAdapter.setSelectedIndex(i);
            }
        }*/
        if (mImagesList.get(0).isVideo()) {
            setThumbImage(mImagesList.get(0));
            mAdapter.setSelectedIndex(0);
            mAdapter.notifyDataSetChanged();
        }

        if (!isThumbnail) {
            findViewById(R.id.frmThumbnail).setVisibility(GONE);
            if (mAdapter != null) {
                mAdapter.setThumbEnabled(isThumbnail);
            }
        }
        if (mImagesList.size() > 0) {
            tvListsize.setText("Total Photos" + " : " + mImagesList.size());
        }
    }

    /**
     * Gets selected images.
     *
     * @return the selected images
     */
    public ArrayList<GalleryItem> getSelectedImages() {
        return mImagesList;
    }

    /**
     * Gets deleted images id.
     *
     * @return the deleted images id
     */
    public ArrayList<String> getDeletedImagesId() {
        return DeletedImagesId;
    }

    /**
     * Gets thumb image.
     *
     * @return the thumb image
     */
// get & set ThumbImage
    public GalleryItem getThumbImage() {
        return thumbImage;
    }

    /**
     * Sets thumb image.
     *
     * @param thumbImage the thumb image
     */
    public void setThumbImage(GalleryItem thumbImage) {
        this.thumbImage = thumbImage;
    }

    /**
     * Gets star thumb id.
     *
     * @return the star thumb id
     */
    public String getStarThumbId() {
        return starThumbId;
    }

    /**
     * Sets star thumb id.
     *
     * @param id the id
     */
    public void setStarThumbId(String id) {
        starThumbId = id;
    }

    /**
     * Gets star attachment id.
     *
     * @return the star attachment id
     */
    public String getStarAttachmentId() {
        return starAttachmentId;
    }

    @Override
    public void onSuccess(ArrayList<GalleryItem> allPath) {
        if (allPath.size() > 0) {
            setAdapter(allPath, false);
        }
    }

    @Override
    public void onCrop(String path) {
        getThumbImage().isFromServer = false;
        getThumbImage().thumbpath = path;
        imageLoader.displayImage("file://" + path,
                ivThumb, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        ivThumb.setImageResource(R.drawable.theme_btn_blue);
                        super.onLoadingStarted(imageUri, view);
                    }
                });
    }

    @Override
    public void onDelete(int pos) {
        Log.e(TAG, "deleted Id::-- " + mImagesList.get(pos).attachment_id);
        DeletedImagesId.add(mImagesList.get(pos).attachment_id);
        boolean starDeleted = false;
        if (mImagesList.get(pos).getIsthumb() == 2) {
//            DeletedImagesId.add(mImagesList.get(pos).getAttachment_id());
            starAttachmentId = mImagesList.get(0).getAttachment_id();
            starDeleted = true;
        }

        mImagesList.remove(pos);
        mAdapter.notifyDataSetChanged();

        if (starDeleted && mImagesList.size() > 0 && isThumbnail) {
            mImagesList.get(0).setIsthumb(2);
            starAttachmentId = mImagesList.get(0).getAttachment_id();
            GalleryItem item = new GalleryItem(2, "", mImagesList.get(0).filepath, false);
            setThumbImage(item);
            mAdapter.setSelectedIndex(0);
            if (item.getThumbpath().contains("http:") || item.getThumbpath().contains("https:")) {
                imageLoader.displayImage(item.getThumbpath(), ivThumb);
            } else {
                imageLoader.displayImage("file://" + item.getThumbpath(), ivThumb);
            }
        }
        if (mImagesList.size() <= 0 && isThumbnail) {
            ivThumb.setImageDrawable(null);
            setThumbImage(null);
            findViewById(R.id.listLayout).setVisibility(GONE);
        } else {
            tvListsize.setText("Total Photos" + " : " + mImagesList.size());
        }
    }

    @Override
    public void onVideoSuccess(ArrayList<GalleryItem> path) {
        if (path.size() > 0) {
            setAdapter(path, false);
        }
    }

    @Override
    public void onStarClick(int pos) {
        mAdapter.setSelectedIndex(pos);
        mAdapter.notifyDataSetChanged();

        for (int i = 0; i < mImagesList.size(); i++) {
            if (!mImagesList.get(0).isVideo()) {
                if (i == pos) {
                    mImagesList.get(i).setIsthumb(2);
                    DeletedImagesId.add(getStarThumbId());
                } else {
                    mImagesList.get(i).setIsthumb(0);
                }
            }
        }

        starAttachmentId = mImagesList.get(pos).getAttachment_id();

        GalleryItem item;
        if (mImagesList.get(0).isVideo())
            item = mImagesList.get(0);
        else
            item = new GalleryItem(2, "", mImagesList.get(pos).filepath, false);
//        GalleryItem item = new GalleryItem(2, "", mImagesList.get(pos).filepath, false);
        setThumbImage(item);

        if (mImagesList.get(pos).filepath.contains("http:") || mImagesList.get(pos).filepath.contains("https:")) {
            imageLoader.displayImage(mImagesList.get(pos).filepath, ivThumb);
        } else {
            imageLoader.displayImage("file://" + mImagesList.get(pos).filepath, ivThumb);
        }
    }

    @Override
    public void onImageDelete(int pos) {
        Log.e(TAG, "deleted Id:: " + mImagesList.get(pos).attachment_id);
        DeletedImagesId.add(mImagesList.get(pos).attachment_id);
        boolean starDeleted = false;
        if (mImagesList.get(pos).getIsthumb() == 2) {
            starDeleted = true;
        }

        GalleryItem tempItem = mImagesList.get(pos);
        mImagesList.remove(pos);
        mAdapter.notifyDataSetChanged();

        if (starDeleted && mImagesList.size() > 0 && isThumbnail) {
//            DeletedImagesId.add(getThumbImage().getAttachment_id());
            mImagesList.get(0).setIsthumb(2);
            starAttachmentId = mImagesList.get(0).getAttachment_id();

            GalleryItem item = new GalleryItem(1, "", mImagesList.get(0).filepath, false);
            setThumbImage(item);
            mAdapter.setSelectedIndex(0);
            if (item.getThumbpath().contains("http:") || item.getThumbpath().contains("https:")) {
                imageLoader.displayImage(item.getThumbpath(), ivThumb);
            } else {
                imageLoader.displayImage("file://" + item.getThumbpath(), ivThumb);
            }
        }
        if (mImagesList.size() <= 0 && isThumbnail) {
//            DeletedImagesId.add(getThumbImage().getAttachment_id());
            ivThumb.setImageDrawable(null);
            setThumbImage(null);
            findViewById(R.id.listLayout).setVisibility(GONE);
        } else {
            tvListsize.setText(getResources().getString(R.string.total_photos) + " : " + mImagesList.size());
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        if (mAdapter != null && mAdapter.isEnabled()) {
            if (mImagesList.get(pos).file_type != null && !mImagesList.get(pos).file_type.equalsIgnoreCase("1") && !mImagesList.get(pos).file_type.equalsIgnoreCase("1")) {
                if (mImagesList.get(pos).filepath.contains("http:") || mImagesList.get(pos).filepath.contains("https:")) {
                    Intent videoIntent = new Intent(getContext(), VideoDisplayActivity.class);
                    videoIntent.putExtra(VideoDisplayActivity.EXTRA_ISDOCUMENT, true);
                    videoIntent.putExtra(VideoDisplayActivity.EXTRA_VIDEODISPLAY_URL, mImagesList.get(pos).getFilepath());
                    getContext().startActivity(videoIntent);
                } else {
                    Utils.openDocument(getContext(), mImagesList.get(pos).filepath);
                }
            } else {
                Intent intent = new Intent(getContext(), ViewImageActivity.class);
                intent.putExtra(ViewImageActivity.EXTRA_VIEWIMAGE_POSITION, pos);
                getContext().startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPhotos:
                if (mImagesList.size() < mImageLimit) {
                    openCustomDialog();
                }
                break;
            case R.id.ivThumb:
                Intent intent = new Intent(getContext(), CropActivity.class);
                intent.setData(Uri.parse(getThumbImage().getThumbpath()));
                intent.putExtra(CropActivity.PARAM_RATIO_X, 3);
                intent.putExtra(CropActivity.PARAM_RATIO_Y, 2);
                getContext().startActivity(intent);
                break;
        }
    }

    /**
     * Open custom dialog.
     */
    public void openCustomDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pick);
        if (!isCamera) {
            dialog.findViewById(R.id.btnOpenCamera).setVisibility(GONE);
        }
        if (!isGallery) {
            dialog.findViewById(R.id.btnOpenGallery).setVisibility(GONE);
        }
        if (!isDoc) {
//            if(isSingleDoc){
//                dialog.findViewById(R.id.btnOpenDocument).setVisibility(VISIBLE);
//            }else{
            dialog.findViewById(R.id.btnOpenDocument).setVisibility(GONE);
//            }
        }

        if (!isVideo) {
            dialog.findViewById(R.id.btnOpenVideo).setVisibility(GONE);
        }

        dialog.findViewById(R.id.btnOpenCamera).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(Action.ACTION_CAPTURE);
                getContext().startActivity(i);
            }
        });
        dialog.findViewById(R.id.btnOpenGallery).
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                        i.putExtra(CustomGalleryActivity.SELECTION_LIMIT, mImageLimit - mImagesList.size());
                        getContext().startActivity(i);
                    }
                });
        dialog.findViewById(R.id.btnOpenDocument).
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(Action.ACTION_DOC_MULTIPLE_PICK);
                        getContext().startActivity(i);
                    }
                });
        dialog.findViewById(R.id.btnOpenVideo).
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(getContext(), VideoActivity.class);
                        getContext().startActivity(i);
                    }
                });
        dialog.show();
    }

}
