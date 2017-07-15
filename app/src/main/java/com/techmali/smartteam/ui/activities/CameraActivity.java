package com.techmali.smartteam.ui.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatPermissionActivity;
import com.techmali.smartteam.base.PermissionListener;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;


public class CameraActivity extends BaseAppCompatPermissionActivity implements View.OnClickListener,
        RequestListener, PermissionListener {

    public static final String TAG = CameraActivity.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager = null;

    private DisplayImageOptions defaultOptions;
    private ImageLoader mImageLoader = ImageLoader.getInstance();

    private ImageView ivGallery, ivCamera;
    private Button btnGallery, btnCamera;

    private boolean cameraPermission = false, writeStoragePermission = false, readExternalStorage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(CameraActivity.this).getPrefs();

        initView();
    }

    private void initView() {

        ivGallery = (ImageView) findViewById(R.id.ivGallery);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnCamera = (Button) findViewById(R.id.btnCamera);

        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .cacheInMemory()
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.ic_profile_photo)
                .showImageForEmptyUri(R.drawable.ic_profile_photo)
                .showImageOnFail(R.drawable.ic_profile_photo)
                .displayer(new CircleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        if (isPermissionGranted(this, android.Manifest.permission.CAMERA))
            askForPermissions(new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, this);

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        networkManager.setListener(this);
    }

    @Override
    public void onStop() {
        networkManager.removeListener(this);
        super.onStop();
    }

    @Override
    public void onSuccess(int id, String response) {

    }

    @Override
    public void onError(int id, String message) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnCamera:

                break;

            case R.id.btnGallery:
                break;
        }
    }


    @Override
    public void permissionGranted(String permission) {
        switch (permission) {

            case Manifest.permission.CAMERA:
                cameraPermission = true;
                break;

            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                writeStoragePermission = true;
                break;

            case Manifest.permission.READ_EXTERNAL_STORAGE:
                readExternalStorage = true;
                break;
        }
    }

    @Override
    public void permissionDenied(String permission) {

    }

    @Override
    public void permissionForeverDenied(String permission) {

    }
}
