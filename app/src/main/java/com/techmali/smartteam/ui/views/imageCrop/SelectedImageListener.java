package com.techmali.smartteam.ui.views.imageCrop;



import com.techmali.smartteam.models.GalleryItem;

import java.util.ArrayList;

/**
 * The interface Selected image listener.
 */
public interface SelectedImageListener {
    /**
     * On success.
     *
     * @param allPath the all path
     */
    void onSuccess(ArrayList<GalleryItem> allPath);

    /**
     * On crop.
     *
     * @param path the path
     */
    void onCrop(String path);

    /**
     * On delete.
     *
     * @param pos the pos
     */
    void onDelete(int pos);

    /**
     * On video success.
     *
     * @param path the path
     */
    void onVideoSuccess(ArrayList<GalleryItem> path);
//    void onWrongDocument();
}
