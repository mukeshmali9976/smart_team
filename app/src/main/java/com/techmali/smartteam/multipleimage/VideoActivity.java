package com.techmali.smartteam.multipleimage;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.DialogHelper;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.PathUtil;
import com.techmali.smartteam.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Video activity.
 * To add video in wall user can add it from this screen,
 * User needs to add url of video here and select thumb image from gallery for that video.
 *
 * @author Niharika Rana
 */
public class VideoActivity extends BaseAppCompatActivity {

    private ImageView ivThumb;
    private EditText etVideoUrl;
    private Uri mFileUri = null;
    private String mOutputFilePath = "", pext = "";
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private static SelectedImageListener mListener;
    private ArrayList<GalleryItem> selected = new ArrayList<>();
    private boolean isCamera;
    private long mClickedTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initActionBar(getString(R.string.add_video_url));
        initView();
    }


    /**
     * Initialize all views of current screen.
     */
    private void initView() {
        ivThumb = (ImageView) findViewById(R.id.ivThumbImage);
        etVideoUrl = (EditText) findViewById(R.id.etVideoUrl);
        ivThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.hideKeyboard(VideoActivity.this);
                onBackPressed();
                break;
            case R.id.add:
                if ((System.currentTimeMillis() - mClickedTime) < 1000) {
                    return true;
                }
                mClickedTime = System.currentTimeMillis();
                Utils.hideKeyboard(VideoActivity.this);
                if (checkValidation()) {
                    if (mListener != null && !Utils.isEmptyString(etVideoUrl.getText().toString().trim())) {
                        GalleryItem galleryItem = new GalleryItem();
                        galleryItem.filepath = mOutputFilePath;
                        galleryItem.ext = pext;
                        galleryItem.setVideoUrl(etVideoUrl.getText().toString().trim());
                        galleryItem.setIsVideo(true);
                        selected.add(galleryItem);
                        mListener.onVideoSuccess(selected);
                        finish();
                    }
                }
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * perform validation on each needful field of the screen.
     */
    private boolean checkValidation() {
        StringBuilder error = new StringBuilder();
        boolean flag = true;

        if (Utils.isEmptyString(mOutputFilePath)) {
            flag = false;
            error.append("\n");
            error.append(getString(R.string.error_video_thumb));
            etVideoUrl.setSelected(true);
        } else if (Utils.isEmptyString(etVideoUrl.getText().toString().trim())) {
            flag = false;
            error.append("\n");
            error.append(getString(R.string.blank_video_url));
            etVideoUrl.setSelected(true);
        } else {
            if (!URLUtil.isValidUrl(etVideoUrl.getText().toString().trim())) {
                flag = false;
                error.append("\n");
                error.append(getString(R.string.error_video_url));
                etVideoUrl.setSelected(true);
            } else {
                etVideoUrl.setSelected(false);
            }
        }

        if (!Utils.isEmptyString(error.toString()))
            DialogHelper.showMessage(VideoActivity.this, "", error.toString().replaceFirst("\n", ""));

        return flag;
    }

    /**
     * Open custom dialog.
     */
    public void openCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pick);
        dialog.findViewById(R.id.btnOpenDocument).setVisibility(View.GONE);
        dialog.findViewById(R.id.btnOpenVideo).setVisibility(View.GONE);

        dialog.findViewById(R.id.btnOpenCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isCamera = true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mFileUri = Utils.getOutputMediaFileUri(VideoActivity.this);
                if (mFileUri != null)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, Constants.INTENT_CAMERA);
                }
            }
        });
        dialog.findViewById(R.id.btnOpenGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isCamera = false;
                Intent intent;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, Constants.INTENT_GALLERY);
                } else {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Constants.INTENT_GALLERY);
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.INTENT_CROP:
                if (data != null) {
                    new CompressionTask().execute();
                }
                break;
            case Constants.INTENT_GALLERY:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImageUri = data.getData();
                        mOutputFilePath = PathUtil.getPath(this, selectedImageUri);
                        int dot = mOutputFilePath.lastIndexOf(".");
                        pext = mOutputFilePath.substring(dot + 1).toLowerCase();
                        new RotationTask().execute(selectedImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.INTENT_CAMERA:
                if (resultCode == RESULT_OK) {
                    try {
                        if (mFileUri != null) {
                            mOutputFilePath = mFileUri.getPath();
                            int dot = mOutputFilePath.lastIndexOf(".");
                            pext = mOutputFilePath.substring(dot + 1).toLowerCase();
                            new RotationTask().execute(mFileUri);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * The Asynchronous task will rotate the selected image if needed to provide proper view of image.
     */
    private class RotationTask extends AsyncTask<Uri, Void, String> {
        /**
         * The Required rotation.
         */
        int requiredRotation = 0;

        @Override
        protected void onPreExecute() {
            ExifInterface ei;
            try {
                ei = new ExifInterface(mOutputFilePath);

                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_NORMAL:
                        requiredRotation = 0;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        requiredRotation = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        requiredRotation = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        requiredRotation = 270;
                        break;
                    case ExifInterface.ORIENTATION_UNDEFINED:
                        requiredRotation = 0;
                        break;
                    default:
                        requiredRotation = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
                requiredRotation = 0;
            }
        }

        @Override
        protected String doInBackground(Uri... params) {
            if (isCamera) {
                mOutputFilePath = params[0].getPath();
            } else {
                mOutputFilePath = PathUtil.getPath(VideoActivity.this, params[0]);
            }

            Matrix matrix = null;
            if (requiredRotation == 0) {
                matrix = new Matrix();
                Log.e(TAG, "mOutputFilePath - > " + mOutputFilePath);
            } else {
                matrix = new Matrix();
                matrix.preRotate(requiredRotation);
            }
            String scaledImagePath = Utils.getOutputMediaFileUri(VideoActivity.this).getPath();
            FileOutputStream out = null;
            Bitmap bitmap = null;
            Bitmap mutableBitmap = null;
            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                bitmap = BitmapFactory.decodeFile(mOutputFilePath, options);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                out = new FileOutputStream(scaledImagePath);
                mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                // write the compressed bitmap at the destination specified by filename.
                mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                mOutputFilePath = scaledImagePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "mOutputFilePath - > " + mOutputFilePath);
            return mOutputFilePath;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            mOutputFilePath = path;
            Log.e(TAG, "mOutputFilePath - > " + mOutputFilePath);
            Intent intent = new Intent(VideoActivity.this, CropActivity.class);
            CropActivity.setOnCropListener(null);
            intent.setData(Uri.parse(mOutputFilePath));
            intent.putExtra(CropActivity.PARAM_RATIO_X, 1);
            intent.putExtra(CropActivity.PARAM_RATIO_Y, 1);
            intent.putExtra("EXT", pext);
            startActivityForResult(intent, Constants.INTENT_CROP);
        }
    }

    /**
     * The Asynchronous task that will compress the selected image to minimize size and for fast upload.
     */
    private class CompressionTask extends AsyncTask<String, Void, String> {
        /**
         * The Required rotation.
         */
        int requiredRotation = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                return mOutputFilePath = Utils.compressImage(VideoActivity.this, mOutputFilePath);
            } catch (Exception e) {
                e.printStackTrace();
                File mediaFile;
                String tempFileName = "img_temp_" + System.currentTimeMillis() + ".png";
                File dir = null;
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        dir = new File(Environment.getExternalStorageDirectory() + "/iPlusImage");
                    } else {
                        dir = new File(getFilesDir() + "/iPlusImage");
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

                if (dir == null) {
                    dir = new File(Environment.getDataDirectory() + "/iPlusImage");
                }

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                mediaFile = new File(dir, tempFileName);

                FileOutputStream out = null;
                Bitmap bitmap = null;
                Bitmap mutableBitmap = null;
                try {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    bitmap = BitmapFactory.decodeFile(mOutputFilePath, options);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);
                    out = new FileOutputStream(mediaFile.getAbsolutePath());
                    mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                    // write the compressed bitmap at the destination specified by filename.
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

                } catch (FileNotFoundException eee) {
                    eee.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException eeee) {
                        eeee.printStackTrace();
                    }
                }
                return mediaFile.getAbsolutePath();
            }

        }

        @Override
        protected void onPostExecute(String mString) {
            super.onPostExecute(mString);
            dismissProgressDialog();
            mOutputFilePath = mString;
            Log.e(TAG, "selectedImageUri mOutputFilePath:: " + mOutputFilePath);
            int dot = mOutputFilePath.lastIndexOf(".");
            pext = mOutputFilePath.substring(dot + 1).toLowerCase();
            if (!Utils.isEmptyString(mOutputFilePath)) {
                mImageLoader.displayImage("file://" + mOutputFilePath, ivThumb);
            }
        }
    }

    /**
     * Sets on touch listener.
     *
     * @param listener the listener
     */
    public static void setOnTouchListener(SelectedImageListener listener) {
        mListener = listener;
    }

}
