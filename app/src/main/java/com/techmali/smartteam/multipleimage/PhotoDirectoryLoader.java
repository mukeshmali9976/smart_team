package com.techmali.smartteam.multipleimage;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * The Customised image loader for Photo directory loader.
 *
 * @author Vijay Desai
 */
public class PhotoDirectoryLoader extends CursorLoader {

    /**
     * The Image projection.
     */
    final String[] IMAGE_PROJECTION = {
            Media._ID,
            Media.DATA,
            Media.BUCKET_ID,
            Media.BUCKET_DISPLAY_NAME,
            Media.DATE_ADDED
    };

    /**
     * Instantiates a new Photo directory loader.
     *
     * @param context the context
     * @param showDoc the show doc
     */
    public PhotoDirectoryLoader(Context context, boolean showDoc) {
        super(context);

        setProjection(IMAGE_PROJECTION);
        setUri(Media.EXTERNAL_CONTENT_URI);
        setSortOrder(Media.DATE_ADDED + " DESC");

        setSelection(MIME_TYPE + "=? or " + MIME_TYPE + "=? " + (showDoc ? ("or " + MIME_TYPE + "=?") : ""));
        String[] selectionArgs;
        if (showDoc) {
            selectionArgs = new String[]{"image/jpeg", "image/png", "image/gif"};
        } else {
            selectionArgs = new String[]{"image/jpeg", "image/png"};
        }

        setSelectionArgs(selectionArgs);
    }

    /*if (aListFile.getName().contains(".docx")
            || aListFile.getName().contains(".doc")
            || aListFile.getName().contains(".pdf")
            || aListFile.getName().contains(".txt")
            || aListFile.getName().contains(".xlsx")
            || aListFile.getName().contains(".xltx")
            || aListFile.getName().contains(".xls")
            || aListFile.getName().contains(".ppt")
            || aListFile.getName().contains(".pptx")
                            *//*|| aListFile.getName().contains(".rar")
                                || aListFile.getName().contains(".zip")*//*) {

            GalleryItem item = new GalleryItem();
            item.docName = aListFile.getName();
            item.filepath = aListFile.getPath();
            item.isFromServer = false;
            mDocList.add(item);
            Log.e("", "pdf file: " + aListFile);
            Log.e("", "docList size: " + mDocList.size());
        }*/
    private PhotoDirectoryLoader(Context context, Uri uri, String[] projection, String selection,
                                 String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }


}
