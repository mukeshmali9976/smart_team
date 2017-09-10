package com.techmali.smartteam.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.mediacompat.BuildConfig;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BasePermissionFragment;
import com.techmali.smartteam.base.PermissionListener;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.ui.activities.MainActivity;
import com.techmali.smartteam.ui.views.imageCrop.CropActivity;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.ImageUtils;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.PathUtil;
import com.techmali.smartteam.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends BasePermissionFragment implements RequestListener, View.OnClickListener {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    private SharedPreferences prefManager = null;
    private NetworkManager networkManager;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private View mRootView;
    private LinearLayout llMain;
    private EditText edtName, edtEmail, edtPhone, edtNewPassword, edtConfirmPassword;
    private ImageView ivProfile;
    private Uri mFileUri = null;
    private DisplayImageOptions defaultOptions;
    private String encodedImage = "", mOutputFilePath = "", pext = "";
    private boolean isCamera;
    private int reqIdUpdate = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_profile, null);
        networkManager = NetworkManager.getInstance();
        prefManager = CryptoManager.getInstance(getActivity()).getPrefs();
        initView();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).initActionBar(getResources().getString(R.string.lbl_profile), mRootView);
        ((MainActivity) getActivity()).setTitle(getActivity().getResources().getString(R.string.lbl_profile));
    }

    @Override
    public void onStart() {
        super.onStart();
        networkManager.setListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        networkManager.removeListener(this);
    }

    private void initView() {

        mImageLoader = ImageLoader.getInstance();
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

        edtName = (EditText) mRootView.findViewById(R.id.edtName);
        edtEmail = (EditText) mRootView.findViewById(R.id.edtEmail);
        edtPhone = (EditText) mRootView.findViewById(R.id.edtPhone);
        edtNewPassword = (EditText) mRootView.findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (EditText) mRootView.findViewById(R.id.edtConfirmPassword);

//        tvErrorName = (TextView) mRootView.findViewById(R.id.tvErrorName);
//        tvErrorPhone = (TextView) mRootView.findViewById(R.id.tvErrorPhone);
//        tvErrorNewPassword = (TextView) mRootView.findViewById(R.id.tvErrorNewPassword);
//        tvErrorConfirmPassword = (TextView) mRootView.findViewById(R.id.tvErrorConfirmPassword);

        ivProfile = (ImageView) mRootView.findViewById(R.id.ivProfile);
        llMain = (LinearLayout) mRootView.findViewById(R.id.llMain);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        mRootView.findViewById(R.id.btnUpdate).setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        llMain.setOnClickListener(this);
        displayData();
    }

    private void displayData() {
//        LoginResponse mResponse = new Gson().fromJson(prefManager.getString(PreferenceKeys.PREF_PROFILE, ""), LoginResponse.class);
//        userId = mResponse.getData().getId();
//        userType = mResponse.getData().getUser_type();
        edtEmail.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfile:
                takePic();
                break;
            case R.id.btnUpdate:

                updateProfile();
                break;
            case R.id.llMain:
                Utils.hideKeyboard(getActivity(), v);
                break;
        }
    }

    @Override
    public void onSuccess(int id, String response) {
        try {
            Log.e(TAG, response);
            if (!TextUtils.isEmpty(response)) {
                if (id == reqIdUpdate) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int id, String message) {
        if (id == reqIdUpdate) {
            Utils.showMessageDialog(getActivity(), message);
        }
    }

    private void updateProfile() {

    }


    private void takePic() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_pickimage);
        dialog.setCancelable(false);
        Button btnGallery = (Button) dialog.findViewById(R.id.btnGallery);
        Button btnCamera = (Button) dialog.findViewById(R.id.btnCamera);
        Button btnDelete = (Button) dialog.findViewById(R.id.btnDelete);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        View viewDivider = dialog.findViewById(R.id.viewDivider);

        btnDelete.setVisibility(View.GONE);
        viewDivider.setVisibility(View.GONE);


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isCamera = true;
                // for multiple permissions for camera and storage
                if (isPermissionsGranted(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        try {
                            mFileUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",
                                    createImageFile());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                        startActivityForResult(intent, Constants.INTENT_CAMERA);

                    }

                } else {
                    // for multiple permission for camera and storage
//                    askForPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, this, "camera");
                    askForPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                        @Override
                        public void permissionGranted(String permission, String tag) {
                            android.util.Log.e(TAG, "Granted : " + permission);
                            switch (permission) {
                                case Manifest.permission.CAMERA:
                                    break;
                                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                    if (tag.equals("camera") && isPermissionsGranted(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {

                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {

                                            try {
                                                mFileUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",
                                                        createImageFile());

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                                            startActivityForResult(intent, Constants.INTENT_CAMERA);
                                        }
                                    }
                                    break;

                            }


                        }

                        @Override
                        public void permissionDenied(String permission) {
                            android.util.Log.e(TAG, "Denied : " + permission);
                            switch (permission) {
                                case Manifest.permission.CAMERA:
                                    openSettingsApp(getActivity(), getString(R.string.permission_denied_message));
                                    break;
                                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                    openSettingsApp(getActivity(), getString(R.string.permission_denied_message));
                                    break;
                            }

                        }

                        @Override
                        public void permissionForeverDenied(String permission) {
                            android.util.Log.e(TAG, "Forever Denied : " + permission);
                            switch (permission) {
                                case Manifest.permission.CAMERA:
                                    openSettingsApp(getActivity(), getString(R.string.permission_denied_message));
                                    break;
                                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                    openSettingsApp(getActivity(), getString(R.string.permission_denied_message));
                                    break;
                            }
                        }
                    }, "camera");
                }

            }
        });


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isCamera = false;
                // for storage permissions
                if (isPermissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, Constants.INTENT_GALLERY);
                    } else {
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, Constants.INTENT_GALLERY);
                    }
                } else {
                    // ask for storage permission
                    askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                        @Override
                        public void permissionGranted(String permission, String tag) {
                            android.util.Log.e(TAG, "Granted : " + permission);
                            if (tag.equals("gallery")) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, Constants.INTENT_GALLERY);
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, Constants.INTENT_GALLERY);
                                }
                            }
                        }

                        @Override
                        public void permissionDenied(String permission) {
                            android.util.Log.e(TAG, "Denied : " + permission);
                        }

                        @Override
                        public void permissionForeverDenied(String permission) {
                            android.util.Log.e(TAG, "Forever Denied : " + permission);
                            openSettingsApp(getActivity(), getString(R.string.permission_denied_message));
                        }
                    }, "gallery");
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "img_temp_" + System.currentTimeMillis() + ".png";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mOutputFilePath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.INTENT_CROP:
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        android.util.Log.e(TAG, "selectedImageUri:: " + selectedImageUri);
                        mOutputFilePath = PathUtil.getPath(getActivity(), selectedImageUri);
                        new CompressionTask().execute();


                        mOutputFilePath = ImageUtils.getPathFromUri(getActivity(), selectedImageUri);
                        int dot = mOutputFilePath.lastIndexOf(".");
                        pext = mOutputFilePath.substring(dot + 1).toLowerCase();
                        // mOutputFilePath = ImageUtils.compressImage(getActivity(), mOutputFilePath, false);
                        Intent mIntent = new Intent(getActivity(), CropActivity.class);

                        mIntent.setData(Uri.parse(mOutputFilePath));
                        getActivity().startActivityForResult(mIntent, Constants.INTENT_CROP);
                    }
                    break;
                case Constants.INTENT_GALLERY:
                    if (data != null) {
                        try {
                            Uri selectedImageUri = data.getData();
                            mOutputFilePath = ImageUtils.getPathFromUri(getActivity(), selectedImageUri);
                            int dot = mOutputFilePath.lastIndexOf(".");
                            pext = mOutputFilePath.substring(dot + 1).toLowerCase();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constants.INTENT_CAMERA:
                    try {
                        if (mFileUri != null) {
                            int dot = mOutputFilePath.lastIndexOf(".");
                            pext = mOutputFilePath.substring(dot + 1).toLowerCase();
                            // new RotationTask().execute();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (mFileUri != null) {
                            int dot = mOutputFilePath.lastIndexOf(".");
                            pext = mOutputFilePath.substring(dot + 1).toLowerCase();
                            //mOutputFilePath = ImageUtils.compressImage(getActivity(), mOutputFilePath, false);
                            Intent mIntent = new Intent(getActivity(), CropActivity.class);
                            mIntent.setData(Uri.parse(mOutputFilePath));
                            getActivity().startActivityForResult(mIntent, Constants.INTENT_CROP);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    private class CompressionTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return mOutputFilePath = Utils.compressImage(getActivity(), mOutputFilePath);
            } catch (Exception e) {
                e.printStackTrace();

                File mediaFile;
                String tempFileName = "img_temp_" + System.currentTimeMillis() + ".png";
                File dir = null;
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        dir = new File(Environment.getExternalStorageDirectory() + "/iPlusImage");
                    } else {
                        dir = new File(getActivity().getFilesDir() + "/iPlusImage");
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
                    Uri imageUri = Uri.parse(mOutputFilePath);
                    File file = new File(imageUri.getPath());
                    try {
                        InputStream ims = new FileInputStream(file);
                        bitmap = BitmapFactory.decodeStream(ims, null, options);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
//                    bitmap = BitmapFactory.decodeFile(mOutputFilePath, options);
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
            android.util.Log.e(TAG, "selectedImageUri mOutputFilePath:: " + mOutputFilePath);
            int dot = mOutputFilePath.lastIndexOf(".");
            pext = mOutputFilePath.substring(dot + 1).toLowerCase();

            ivProfile.setImageBitmap(BitmapFactory.decodeFile(mOutputFilePath));
            mImageLoader.displayImage("file://" + mOutputFilePath, ivProfile, defaultOptions);
        }
    }

    /**
     * The Asynchronous task will rotate the selected image if needed to provide proper view of image.
     */


//    private boolean checkValidation() {
//        boolean flag = true;
//
//        if (Utils.isEmptyString(edtName.getText().toString().trim())) {
//            flag = false;
//            tvErrorName.setVisibility(View.VISIBLE);
//            tvErrorName.setText(getString(R.string.error_enter));
//            Utils.focusOnView(edtName);
//        } else {
//            tvErrorName.setVisibility(View.GONE);
//        }
//
//        if (Utils.isEmptyString(edtPhone.getText().toString().trim())) {
//            flag = false;
//            tvErrorPhone.setVisibility(View.VISIBLE);
//            tvErrorPhone.setText(getString(R.string.error_enter));
//            Utils.focusOnView(edtPhone);
//        } else {
//            tvErrorPhone.setVisibility(View.GONE);
//        }
//
//        if (Utils.isEmptyString(edtNewPassword.getText().toString())) {
//            flag = false;
//            tvErrorNewPassword.setVisibility(View.VISIBLE);
//            tvErrorNewPassword.setText(R.string.error_enter_new_password);
//            edtNewPassword.setSelected(true);
//        } else {
//            tvErrorNewPassword.setVisibility(View.GONE);
//            if (edtNewPassword.getText().length() < 5 || edtNewPassword.getText().length() > 20) {
//                flag = false;
//                tvErrorNewPassword.setVisibility(View.VISIBLE);
//                tvErrorNewPassword.setText(R.string.error_password_length);
//                edtNewPassword.setSelected(true);
//            } else {
//                tvErrorNewPassword.setVisibility(View.GONE);
//            }
//        }
//        if (Utils.isEmptyString(edtConfirmPassword.getText().toString())) {
//            flag = false;
//            tvErrorConfirmPassword.setVisibility(View.VISIBLE);
//            tvErrorConfirmPassword.setText(R.string.error_enter_confirm_password);
//            edtConfirmPassword.setSelected(true);
//            if (!Utils.isEmptyString((edtNewPassword.getText().toString())))
//                edtNewPassword.setSelected(false);
//        } else {
//            tvErrorConfirmPassword.setVisibility(View.GONE);
//            edtConfirmPassword.setSelected(false);
//            if (!Utils.isEmptyString(edtNewPassword.getText().toString()) && (edtNewPassword.getText().length() < 5 || edtNewPassword.getText().length() > 20)) {
//                flag = false;
//                tvErrorNewPassword.setVisibility(View.VISIBLE);
//                tvErrorNewPassword.setText(R.string.error_password_length);
//                edtNewPassword.setSelected(true);
//            } else {
//                tvErrorNewPassword.setVisibility(View.GONE);
//                if (!TextUtils.equals(edtNewPassword.getText().toString().trim(), edtConfirmPassword.getText().toString().trim())) {
//                    flag = false;
//                    tvErrorConfirmPassword.setVisibility(View.VISIBLE);
//                    tvErrorConfirmPassword.setText(R.string.error_password_not_match);
//                    edtNewPassword.setSelected(true);
//                    edtConfirmPassword.setSelected(true);
//                } else {
//                    tvErrorConfirmPassword.setVisibility(View.GONE);
//                }
//
//                tvErrorNewPassword.setVisibility(View.GONE);
//
//            }
//
//        }
//
//        return flag;
//    }


}
