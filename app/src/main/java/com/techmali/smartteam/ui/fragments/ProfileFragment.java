package com.techmali.smartteam.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BasePermissionFragment;
import com.techmali.smartteam.base.PermissionListener;
import com.techmali.smartteam.network.NetworkManager;
import com.techmali.smartteam.network.RequestListener;
import com.techmali.smartteam.utils.CryptoManager;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.PathUtil;
import com.techmali.smartteam.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends BasePermissionFragment implements RequestListener, View.OnClickListener, PermissionListener {

    public static final String TAG = ProfileFragment.class.getSimpleName();

    private SharedPreferences prefManager = null;
    private NetworkManager networkManager;

    private static final int INTENT_CAMERA = 1, INTENT_GALLERY = 2;
    private static final String TEMP_FILE_NAME = "img_temp.png";

    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private View mRootView;
    private LinearLayout llMain;
    private EditText edtName, edtEmail, edtPhone, edtNewPassword, edtConfirmPassword;
    private ImageView ivProfile;
    private TextView tvErrorName, tvErrorConfirmPassword, tvErrorNewPassword, tvErrorPhone;

    private Uri mFileUri = null;
    private String encodedImage = "", mOutputFilePath;
    private boolean isCamera;
    private int reqIdUpdate = -1, userId, userType;
    private byte[] byteArray;

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

        initActionBar(getResources().getString(R.string.lbl_profile), mRootView);
        getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_profile));
        changeToolBarColor();
        //getActivity().findViewById(R.id.ivHomeLogo).setVisibility(View.VISIBLE);
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
                if (isPermissionGranted(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    takePic();
                } else {
                    askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, ProfileFragment.this);
                }
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

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isPermissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    isCamera = false;
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        getActivity().startActivityForResult(intent, INTENT_GALLERY);
                    } else {
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getActivity().startActivityForResult(intent, INTENT_GALLERY);
                    }
                } else {
                    askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionListener() {
                        @Override
                        public void permissionGranted(String permission) {
                            isCamera = false;
                            Intent intent;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                getActivity().startActivityForResult(intent, INTENT_GALLERY);
                            } else {
                                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                getActivity().startActivityForResult(intent, INTENT_GALLERY);
                            }
                        }

                        @Override
                        public void permissionDenied(String permission) {

                        }

                        @Override
                        public void permissionForeverDenied(String permission) {
                            openSettingsApp(getActivity(), getString(R.string.permission_denied_message));
                        }
                    });

                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isPermissionsGranted(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                    isCamera = true;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getOutputMediaFileUri();
                    if (mFileUri != null)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        getActivity().startActivityForResult(intent, INTENT_CAMERA);
                    }
                } else {
                    askForPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                        @Override
                        public void permissionGranted(String permission) {

                            isCamera = true;
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getOutputMediaFileUri();
                            if (mFileUri != null)
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                getActivity().startActivityForResult(intent, INTENT_CAMERA);
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
                    });
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                ivProfile.setImageResource(R.drawable.ic_profile_photo);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

      /* * Creating file uri to store image/video */

    private void getOutputMediaFileUri() {

        if (isPermissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File mediaFile;
            String tempFileName = "img_temp_" + System.currentTimeMillis() + ".png";
            File dir = null;
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    dir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
                } else {
                    dir = new File(getActivity().getFilesDir() + "/" + getString(R.string.app_name));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dir == null) {
                dir = new File(Environment.getDataDirectory() + "/" + getString(R.string.app_name));
            }

            if (!dir.exists()) {
                dir.mkdirs();
            }
            mediaFile = new File(dir, tempFileName);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mFileUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", mediaFile);
            } else {
                mFileUri = Uri.fromFile(mediaFile);
            }
        } else {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ProfileFragment.this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_GALLERY:
                if (data != null && resultCode == Activity.RESULT_OK) {
                    try {
                        Uri selectedImageUri = data.getData();
                        mOutputFilePath = PathUtil.getPath(getActivity(), selectedImageUri);
                        if (!TextUtils.isEmpty(mOutputFilePath)) {
                            int dot = mOutputFilePath.lastIndexOf(".");
                            String pext = mOutputFilePath.substring(dot + 1);
                            Log.i("Path ext:: ", pext);
                        }
                        Log.e(TAG, "selectedImageUri - > " + selectedImageUri);
                        if (selectedImageUri != null) {
                            new RotationTask().execute(selectedImageUri);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case INTENT_CAMERA:
                if (mFileUri != null) {
                    mOutputFilePath = mFileUri.getPath();
                    int dot = mOutputFilePath.lastIndexOf(".");
                    String pext = mOutputFilePath.substring(dot + 1);
                    Log.i("Path ext:: ", pext);
                    new RotationTask().execute(mFileUri);
                }
                break;
        }
    }

    @Override
    public void permissionGranted(String permission) {

        switch (permission) {
            case Manifest.permission.CAMERA:
                isCamera = true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getOutputMediaFileUri();
                if (mFileUri != null)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivityForResult(intent, INTENT_CAMERA);
                }
                break;

            case Manifest.permission.READ_EXTERNAL_STORAGE:
                takePic();
                break;

            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                File mediaFile = null;
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        mediaFile = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name), TEMP_FILE_NAME);
                    } else {
                        mediaFile = new File(getActivity().getFilesDir() + "/" + getString(R.string.app_name), TEMP_FILE_NAME);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mediaFile == null) {
                    mediaFile = new File(Environment.getDataDirectory() + "/" + getString(R.string.app_name), TEMP_FILE_NAME);
                }

                if (mediaFile.exists()) {
                    try {
                        boolean isDeleted = mediaFile.delete();
                        Log.i(TAG, "Is temp file deleted = " + isDeleted);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mFileUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", mediaFile);
                } else {
                    mFileUri = Uri.fromFile(mediaFile);
                }
                break;
        }
    }

    @Override
    public void permissionDenied(String permission) {
        Log.e(TAG, "Denied : " + permission);
    }

    @Override
    public void permissionForeverDenied(String permission) {
        Log.e(TAG, "Forever Denied : " + permission);
        openSettingsApp(getActivity(), "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]");
    }

    private class RotationTask extends AsyncTask<Uri, Void, String> {
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
            if (requiredRotation == 0) {
                if (isCamera) {
                    mOutputFilePath = params[0].getPath();
                } else {
                    mOutputFilePath = PathUtil.getPath(getActivity(), params[0]);
                }

                Log.e(TAG, "mOutputFilePath - > " + mOutputFilePath);
                return mOutputFilePath;
            } else {
                Matrix matrix = new Matrix();
                matrix.preRotate(requiredRotation);
                Bitmap bitmap;
                try {
                    if (isCamera) {
                        mOutputFilePath = params[0].getPath();
                    } else {
                        mOutputFilePath = PathUtil.getPath(getActivity(), params[0]);
                    }
                    bitmap = decodeSampledBitmapFromPath(mOutputFilePath,
                            (int) getResources().getDimension(R.dimen.profile_image_width),
                            (int) getResources().getDimension(R.dimen.profile_image_width));
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(mOutputFilePath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "mOutputFilePath - > " + mOutputFilePath);
            return mOutputFilePath;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            Log.e(TAG, "path - > " + path);
            if (!TextUtils.isEmpty(path)) {
                mImageLoader.loadImage(Uri.fromFile(new File(path)).toString(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        ivProfile.setImageBitmap(loadedImage);
                        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
                        loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
                        byteArray = byteArrayOS.toByteArray();
                        encodedImage = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
                    }
                });
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
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
