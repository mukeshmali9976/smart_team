package com.techmali.smartteam.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.domain.adapters.ViewImagesAdapter;
import com.techmali.smartteam.models.GetMediaResponse;
import com.techmali.smartteam.multipleimage.GalleryItem;
import com.techmali.smartteam.multipleimage.GalleryView;
import com.techmali.smartteam.multipleimage.HorizontalListView;
import com.techmali.smartteam.multipleimage.ViewImageActivity;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Hello on 09-Sep-17.
 */

public class ImageSelectionActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener {

    private GalleryView mGalleryView;
    private HorizontalListView lvImages;
    private TextView tvListSize;
    private int[] mImageData = {R.drawable.a_1, R.drawable.a_2, R.drawable.a_3, R.drawable.a_4};
    private ArrayList<GalleryItem> mImageList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);
        initActionBar("");
        initViews();
    }

    /**
     * Initialize all views of current screen.
     */
    private void initViews() {
        mGalleryView = (GalleryView) findViewById(R.id.galleryView);
        lvImages = (HorizontalListView) findViewById(R.id.lvImages);
        tvListSize = (TextView) findViewById(R.id.tvTotalSize);
        mGalleryView.setVisibility(View.GONE);
        mGalleryView.setViewEnable(false);

        String StarThumbImage = "";
        for (int i = 0; i < mImageData.length; i++) {
            GalleryItem item = new GalleryItem();
            item.setFilepath("drawable://" + mImageData[i]);
            mImageList.add(item);
        }

        if (mImageList.size() > 0) {
            for (int i = 0; i < mImageList.size(); i++) {
                if (mImageList.get(i).getIsthumb() == 2) {
                    StarThumbImage = mImageList.get(i).getAttachment_id();
                    mImageList.get(i).isFromServer = true;
                    mGalleryView.setThumbImage(mImageList.get(i));
                }
            }
            Log.e(TAG, "mStringList length: " + mImageList.size());
            mGalleryView.getSelectedImages().clear();
            mGalleryView.setAdapter(mImageList, true);
            mGalleryView.setStarThumbId(StarThumbImage);
            mGalleryView.setViewEnable(true);
            if (mGalleryView.isViewEnable()) {
                mGalleryView.setVisibility(View.VISIBLE);
                findViewById(R.id.llImageList).setVisibility(View.GONE);
            } else {
                mGalleryView.setVisibility(View.GONE);
                findViewById(R.id.llImageList).setVisibility(View.VISIBLE);
                ViewImagesAdapter mAdapter = new ViewImagesAdapter(this, mGalleryView.getSelectedImages());
                lvImages.setAdapter(mAdapter);
                if (mGalleryView.getSelectedImages().size() > 0) {
                    tvListSize.setText(getResources().getString(R.string.total_photos) + " : " + mGalleryView.getSelectedImages().size());
                }
                lvImages.setOnItemClickListener(this);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mGalleryView.getSelectedImages().get(position).file_type != null && !mGalleryView.getSelectedImages().get(position).file_type.equalsIgnoreCase("1") && !mGalleryView.getSelectedImages().get(position).file_type.equalsIgnoreCase("1")) {
            if (mGalleryView.getSelectedImages().get(position).filepath.contains("http:") || mGalleryView.getSelectedImages().get(position).filepath.contains("https:")) {
                Intent videoIntent = new Intent(ImageSelectionActivity.this, VideoDisplayActivity.class);
                videoIntent.putExtra(VideoDisplayActivity.EXTRA_ISDOCUMENT, true);
                videoIntent.putExtra(VideoDisplayActivity.EXTRA_VIDEODISPLAY_URL, mGalleryView.getSelectedImages().get(position).getFilepath());
                startActivity(videoIntent);
            } else {
                Utils.openDocument(ImageSelectionActivity.this, mGalleryView.getSelectedImages().get(position).filepath);
            }
        } else {
            Intent intent = new Intent(this, ViewImageActivity.class);
            intent.putParcelableArrayListExtra(ViewImageActivity.EXTRA_VIEWIMAGE_ARRAY, GalleryView.mImagesList);
            intent.putExtra(ViewImageActivity.EXTRA_VIEWIMAGE_POSITION, position);

            intent.putExtra(ViewImageActivity.FROM, "UPDATE");
            startActivity(intent);
        }

    }

}
