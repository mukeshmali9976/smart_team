package com.techmali.smartteam.multipleimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;

import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * The Crop activity to crop image once selected from gallery or captured from camera.
 */
public class CropActivity extends BaseAppCompatActivity {

    /**
     * The constant TAG.
     */
    public static final String TAG = CropActivity.class.getSimpleName();
    /**
     * The constant PARAM_RATIO_X.
     */
    public static final String PARAM_RATIO_X = "ratioX";
    /**
     * The constant PARAM_RATIO_Y.
     */
    public static final String PARAM_RATIO_Y = "ratioY";
    private static SelectedImageListener mListener;
    private ImageCropView imageCropView;
    private Uri uri;
    private String ext, from;
    private int ratioX = 1, ratioY = 1;
    private long mClickedTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initActionBar(getResources().getString(R.string.crop_image));
        imageCropView = (ImageCropView) findViewById(R.id.image);

        ratioX = getIntent().getIntExtra(PARAM_RATIO_X, 1);
        ratioY = getIntent().getIntExtra(PARAM_RATIO_Y, 1);

        Log.e(TAG, "================== crop intent ====================");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Intent i = getIntent();
        if (i != null) {
            uri = i.getData();
            int dot = uri.toString().lastIndexOf(".");
            ext = uri.toString().substring(dot + 1);
        }

        if (!Utils.isEmptyString(uri.toString()))
            try {
                if (uri.toString().contains("http:") || uri.toString().contains("https:")) {
                    Bitmap image = BitmapFactory.decodeStream(new URL(uri.toString()).openConnection().getInputStream());
                    imageCropView.setImageBitmap(image);
                } else {
                    imageCropView.setImageFilePath(uri.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        imageCropView.setAspectRatio(ratioX, ratioY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crop, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.crop:
                if ((System.currentTimeMillis() - mClickedTime) < 1000) {
                    return true;
                }
                mClickedTime = System.currentTimeMillis();
                showProgressDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (!imageCropView.isChangingScale()) {
                                Bitmap b = imageCropView.getCroppedImage();
                                bitmapConvertToFile(b);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissProgressDialog();
                                }
                            });
                        }
                    }
                }).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Bitmap convert to file file.
     *
     * @param bitmap the bitmap
     * @return the file
     */
    public File bitmapConvertToFile(Bitmap bitmap) {

        FileOutputStream fileOutputStream = null;
        File mediaFile = null;

        try {

            String tempFileName = "IMG_" + (new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())).format(Calendar.getInstance().getTime()) + "." + ext;
            File dir = null;

            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    dir = new File(Environment.getExternalStorageDirectory() + "/image_crop");
                } else {
                    dir = new File(getFilesDir() + "/image_crop");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dir == null) {
                dir = new File(Environment.getDataDirectory() + "/image_crop");
            }

            if (!dir.exists()) {
                dir.mkdirs();
            }

            mediaFile = new File(dir, tempFileName);

            fileOutputStream = new FileOutputStream(mediaFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, fileOutputStream);
            MediaScannerConnection.scanFile(this, new String[]{mediaFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {

                }

                @Override
                public void onScanCompleted(final String path, final Uri uri) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                            if (!Utils.isEmptyString(path)) {
                                if (mListener != null) {
                                    mListener.onCrop(path);
                                    finish();
                                } else {
                                    Intent intent = getIntent();
                                    intent.setData(uri);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                }
            });

        }

        return mediaFile;
    }

    /**
     * Sets on crop listener.
     *
     * @param listener the listener
     */
    public static void setOnCropListener(SelectedImageListener listener) {
        mListener = listener;
    }
}
