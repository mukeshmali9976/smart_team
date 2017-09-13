package com.techmali.smartteam.multipleimage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.techmali.smartteam.BuildConfig;
import com.techmali.smartteam.R;
import com.techmali.smartteam.base.BaseAppCompatActivity;
import com.techmali.smartteam.utils.Constants;
import com.techmali.smartteam.utils.DialogHelper;
import com.techmali.smartteam.utils.Log;
import com.techmali.smartteam.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

/**
 * The Custom gallery activity that loads all the images from phone and display in grid
 * also multiple selection allowed.
 *
 * @author Vijay Desai
 */
public class CustomGalleryActivity extends BaseAppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private MultipleImageAdapter mImageAdapter;
    private DocumentAdapter mDocAdapter;
    private LinearLayout llGalleryViewMain;
    private TextView txtCount;
    private Spinner spnFolders;
    private ArrayList<GalleryItem> mDocList = new ArrayList<>();
    private ArrayList<GalleryItem> mImagesFoldersList = new ArrayList<>();
    private List<PhotoDirectory> directories = new ArrayList<>();
    private String action;
    private ImageLoader imageLoader;
    private static SelectedImageListener mListener;
    private Uri mFileUri = null;
    private String mOutputFilePath;
    /**
     * The constant SELECTION_LIMIT.
     */
    public static String SELECTION_LIMIT = "selectionLimit";
    /**
     * The M image selection limit.
     */
    public int mImageSelectionLimit = Integer.MAX_VALUE;
    private FoldersAdapterNew mFoldersAdapter;

    private final static String EXTRA_DOC = "doc";
    /**
     * The constant EXTRA_SHOW_DOC.
     */
    public final static String EXTRA_SHOW_DOC = "SHOW_DOC";
    /**
     * The constant SHOW_DOC.
     */
    public static boolean SHOW_DOC = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
//        initActionBarWhite(getString(R.string.choosephotos));
//        if (savedInstanceState == null) {
        action = getIntent().getAction();
//            getIntent().setAction(null);
        if (action == null) {
            finish();
        }
        mImageSelectionLimit = getIntent().getIntExtra(SELECTION_LIMIT, 0);
        initImageLoader();
        init();
//        }
    }

    private void initImageLoader() {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
                    CACHE_DIR);

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    getBaseContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .diskCache(new UnlimitedDiskCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        llGalleryViewMain = (LinearLayout) findViewById(R.id.llGalleryViewMain);
        GridView gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, true, true);
        gridGallery.setOnScrollListener(listener);

        ListView lvDoc = (ListView) findViewById(R.id.lvDoc);
        lvDoc.setFastScrollEnabled(true);
        lvDoc.setOnScrollListener(listener);

        txtCount = (TextView) findViewById(R.id.txtCount);
        spnFolders = (Spinner) findViewById(R.id.spnFolders);
        ImageView btnOk = (ImageView) findViewById(R.id.btnOk);
        ImageView btnClose = (ImageView) findViewById(R.id.btnClose);
        btnOk.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        Bundle mediaStoreArgs = new Bundle();

        if (action.equalsIgnoreCase(Action.ACTION_DOC_MULTIPLE_PICK) || action.equalsIgnoreCase(Action.ACTION_DOC_PICK)) {
            SHOW_DOC = false;
            FilePickerBuilder.getInstance()
                    .setMaxCount(1)
                    .setSelectedFiles(new ArrayList<String>()).setActivityTheme(R.style.filePickerTheme)
                    .pickDocument(this);

//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//            intent.setType("*/*");
//            startActivityForResult(intent, Constants.INTENT_DOCUMENT);
        } else {
            SHOW_DOC = true;
            mediaStoreArgs.putBoolean(EXTRA_SHOW_DOC, false);
            MediaStoreHelper.getPhotoDirs(CustomGalleryActivity.this, mediaStoreArgs,
                    new MediaStoreHelper.PhotosResultCallback() {
                        @Override
                        public void onResultCallback(List<PhotoDirectory> dirs) {
                            directories.clear();
                            directories.addAll(dirs);
                            mFoldersAdapter = new FoldersAdapterNew(CustomGalleryActivity.this, directories);
                            spnFolders.setAdapter(mFoldersAdapter);
                            spnFolders.setOnItemSelectedListener(CustomGalleryActivity.this);
                        }
                    });

        }

        if (action.equalsIgnoreCase(Action.ACTION_MULTIPLE_PICK)) {
            lvDoc.setVisibility(View.GONE);
            gridGallery.setVisibility(View.VISIBLE);
            mImageAdapter = new MultipleImageAdapter(getApplicationContext(), imageLoader);
            gridGallery.setAdapter(mImageAdapter);

            gridGallery.setOnItemClickListener(mItemMulClickListener);
            mImageAdapter.setMultiplePick(true);
            mImageAdapter.setImageSelectionLimit(mImageSelectionLimit);

        } else if (action.equalsIgnoreCase(Action.ACTION_PICK)) {
            lvDoc.setVisibility(View.GONE);
            gridGallery.setVisibility(View.VISIBLE);
            mImageAdapter = new MultipleImageAdapter(getApplicationContext(), imageLoader);
            gridGallery.setAdapter(mImageAdapter);

            gridGallery.setOnItemClickListener(mItemSingleClickListener);
            mImageAdapter.setMultiplePick(false);

        } else if (action.equalsIgnoreCase(Action.ACTION_DOC_MULTIPLE_PICK)) {
            lvDoc.setVisibility(View.VISIBLE);
            gridGallery.setVisibility(View.GONE);
            spnFolders.setVisibility(View.GONE);

            mDocAdapter = new DocumentAdapter(getApplicationContext());
            lvDoc.setAdapter(mDocAdapter);
            lvDoc.setOnItemClickListener(mItemMulClickListener);
            mDocAdapter.setMultiplePick(true);

        } else if (action.equalsIgnoreCase(Action.ACTION_DOC_PICK)) {
            lvDoc.setVisibility(View.VISIBLE);
            gridGallery.setVisibility(View.GONE);
            spnFolders.setVisibility(View.GONE);

            mDocAdapter = new DocumentAdapter(getApplicationContext());
            lvDoc.setAdapter(mDocAdapter);
            lvDoc.setOnItemClickListener(mItemSingleClickListener);
            mDocAdapter.setMultiplePick(false);

        } else if (action.equalsIgnoreCase(Action.ACTION_CAPTURE)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //mFileUri = getOutputMediaFileUri();
            try {

                mFileUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mFileUri != null)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, Constants.INTENT_CAMERA);
            }
        }
    }

    /**
     * The M item mul click listener.
     */
    AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            if (action.equalsIgnoreCase(Action.ACTION_DOC_MULTIPLE_PICK)) {
                mDocAdapter.changeSelection(v, position);
                Log.e("", "Doc path :: " + mDocAdapter.getPath(position));
                txtCount.setText(String.valueOf(mDocAdapter.getSelected().size()));
            } else {
                mImageAdapter.changeSelection(v, position);
                Log.e("", "image path :: " + mImageAdapter.getPath(position));
                txtCount.setText(String.valueOf(mImageAdapter.getSelected().size()));
            }
        }
    };

    /**
     * The M item single click listener.
     */
    AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            GalleryItem item;
            if (action.equalsIgnoreCase(Action.ACTION_DOC_PICK)) {
                item = mDocAdapter.getItem(position);
            } else {
                item = mImageAdapter.getItem(position);
            }
            Intent data = new Intent().putExtra("single_path", item.filepath);
            setResult(RESULT_OK, data);
            finish();
        }
    };

    /**
     * Gets images.
     *
     * @param dir the dir
     * @return the images
     */
    public ArrayList<GalleryItem> getImages(File dir) {
        ArrayList<GalleryItem> galleryList = new ArrayList<>();
        try {
            File[] listFile = dir.listFiles();
            if (listFile != null) {
                for (File aListFile : listFile) {
                    if (aListFile.isDirectory()) {
                        getImages(aListFile);
                    } else {
                        if (aListFile.getName().toLowerCase().endsWith(".png")
                                || aListFile.getName().toLowerCase().endsWith(".jpg")) {

                            GalleryItem item = new GalleryItem();
                            item.filepath = aListFile.getPath();
                            item.isFromServer = false;
                            galleryList.add(item);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return galleryList;
    }

    /**
     * Sets on touch listener.
     *
     * @param listener the listener
     */
    public static void setOnTouchListener(SelectedImageListener listener) {
        mListener = listener;
    }

    private Uri getOutputMediaFileUri() {
        File mediaFile;
        String tempFileName = "img_temp_" + System.currentTimeMillis() + ".png";
        File dir = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dir = new File(Environment.getExternalStorageDirectory() + "/iPlusImage");
            } else {
                dir = new File(getFilesDir() + "/iPlusImage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dir == null) {
            dir = new File(Environment.getDataDirectory() + "/iPlusImage");
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }
        mediaFile = new File(dir, tempFileName);
        return Uri.fromFile(mediaFile);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        //String imageFileName = ImageUtils.getUniqueFileName("");
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.INTENT_CAMERA:
                    if (mFileUri != null) {
                        //mOutputFilePath = mFileUri.getPath();
                        if (!Utils.isEmptyString(mOutputFilePath)) {
                            llGalleryViewMain.setVisibility(View.GONE);
                            int dot = mOutputFilePath.lastIndexOf(".");
                            String pext = mOutputFilePath.substring(dot + 1);
                            Log.i("Path ext:: ", pext);
                            new RotationTask().execute(mFileUri);
                        }
                    }
                    break;
                case FilePickerConst.REQUEST_CODE_DOC:
                    if (data != null && !data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).isEmpty()) {
                        try {
                            String filePath = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0);
                            String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
                            if (fileExtension.contains("docx")
                                    || fileExtension.contains("doc")
                                    || fileExtension.contains("pdf")
                                    || fileExtension.contains("txt")
                                    || fileExtension.contains("xlsx")
                                    || fileExtension.contains("xltx")
                                    || fileExtension.contains("xls")
                                    || fileExtension.contains("ppt")
                                    || fileExtension.contains("pptx")) {
                                File file = new File(filePath);
                                GalleryItem item = new GalleryItem();
                                item.docName = file.getName();
                                item.file_type = "3";
                                item.filepath = filePath;
                                item.isFromServer = false;

                                ArrayList<GalleryItem> mSelectedDocument = new ArrayList<>();
                                mSelectedDocument.add(item);

                                mListener.onSuccess(mSelectedDocument);
                                finish();
                            } else {
                                llGalleryViewMain.setVisibility(View.GONE);
                                DialogHelper.showOkButtonDialog(CustomGalleryActivity.this, "", getString(R.string.msg_file_extension), getString(R.string.ok), okClick, false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            llGalleryViewMain.setVisibility(View.GONE);
                            DialogHelper.showOkButtonDialog(CustomGalleryActivity.this, "", getString(R.string.msg_file_extension), getString(R.string.ok), okClick, false);
                        }
                    } else {
                        finish();
                    }

                    /*Uri selectedUri = data.getData();
                    String filePath = PathUtil.getPath(this, selectedUri);
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
//                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    if (fileExtension.contains("docx")
                            || fileExtension.contains("doc")
                            || fileExtension.contains("pdf")
                            || fileExtension.contains("txt")
                            || fileExtension.contains("xlsx")
                            || fileExtension.contains("xltx")
                            || fileExtension.contains("xls")
                            || fileExtension.contains("ppt")
                            || fileExtension.contains("pptx")
                            || fileExtension.contains("png")
                            || fileExtension.contains("jpg")
                            || fileExtension.contains("jpeg")) {
                        if (mListener != null) {
                            try {
                                File file = new File(filePath);

                                GalleryItem item = new GalleryItem();
                                item.docName = file.getName();
                                item.file_type = "3";
                                item.filepath = filePath;
                                item.isFromServer = false;

                                ArrayList<GalleryItem> mSelectedDocument = new ArrayList<>();
                                mSelectedDocument.add(item);

                                mListener.onSuccess(mSelectedDocument);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        llGalleryViewMain.setVisibility(View.GONE);
                        DialogHelper.showOkButtonDialog(CustomGalleryActivity.this, "", getString(R.string.msg_file_extension), getString(R.string.ok), okClick, false);
                    }*/
                    break;
            }
        } else {
            finish();
        }
    }

    /**
     * The Ok click.
     */
    DialogInterface.OnClickListener okClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            finish();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                ArrayList<GalleryItem> selected;
                if (action.equalsIgnoreCase(Action.ACTION_DOC_MULTIPLE_PICK)) {
                    selected = mDocAdapter.getSelected();
                    if (mListener != null) {
                        mListener.onSuccess(selected);
                        finish();
                    }
                } else {
                    new CompressionTask().execute();
                }

                break;
            case R.id.btnClose:
                finish();
                break;
        }
    }

    /**
     * The Asynchronous task that will compress the selected image to minimize size and for fast upload.
     */
    private class CompressionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < mImageAdapter.getSelected().size(); i++) {

                try {
                    mImageAdapter.getSelected().get(i).setFilepath(Utils.compressImage(CustomGalleryActivity.this, mImageAdapter.getSelected().get(i).getFilepath()));
                    ;
                } catch (Exception e) {
                    String scaledImagePath = getOutputMediaFileUri(i).getPath();

                    FileOutputStream out = null;
                    Bitmap bitmap = null;
                    Bitmap mutableBitmap = null;
                    try {
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        bitmap = BitmapFactory.decodeFile(mImageAdapter.getSelected().get(i).getFilepath(), options);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);
                        out = new FileOutputStream(scaledImagePath);
                        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                        // write the compressed bitmap at the destination specified by filename.
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

                        mImageAdapter.getSelected().get(i).setFilepath(scaledImagePath);
                    } catch (FileNotFoundException ee) {
                        ee.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException eeee) {
                            eeee.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();
            selected = mImageAdapter.getSelected();
            if (mListener != null) {
                mListener.onSuccess(selected);
                finish();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mImageAdapter != null)
            mImageAdapter.addAll(getImages(directories.get(position).getParentPath()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * The Selected.
     */
    ArrayList<GalleryItem> selected = new ArrayList<>();

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
            showProgressDialog();
            /*ExifInterface ei;
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
            }*/
        }

        @Override
        protected String doInBackground(Uri... params) {

            return mOutputFilePath = Utils.compressImage(CustomGalleryActivity.this, mOutputFilePath);

            /*if (requiredRotation == 0) {
                mOutputFilePath = params[0].getPath();

                Log.e(TAG, "mOutputFilePath - > " + mOutputFilePath);
                return mOutputFilePath;
            } else {
                Matrix matrix = new Matrix();
                matrix.preRotate(requiredRotation);

                Bitmap bitmap;
                try {
                    mOutputFilePath = params[0].getPath();
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    bitmap = BitmapFactory.decodeFile(mOutputFilePath, options);
//                    bitmap = decodeSampledBitmapFromPath(mOutputFilePath,
//                            (int) getResources().getDimension(R.dimen.profile_image_width),
//                            (int) getResources().getDimension(R.dimen.profile_image_width));

                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(mOutputFilePath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
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
            return mOutputFilePath;*/
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            dismissProgressDialog();
            Log.e(TAG, "path - > " + path);
            if (!Utils.isEmptyString(path)) {
                GalleryItem item = new GalleryItem();
                item.filepath = path;
                selected.add(item);

//                String[] allPath = new String[selected.size()];
//                for (int i = 0; i < allPath.length; i++) {
//                    allPath[i] = selected.get(i).mImagePath;
//                }
                if (mListener != null) {
                    mListener.onSuccess(selected);
                    finish();
                }
            }
        }
    }

    /**
     * Gets output media file uri.
     *
     * @param number the number
     * @return the output media file uri
     */
    public Uri getOutputMediaFileUri(int number) {
        File mediaFile;
        String tempFileName = "img_temp_" + number + "_" + System.currentTimeMillis() + ".png";
        File dir = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dir = new File(Environment.getExternalStorageDirectory() + "/iPlusImage");
            } else {
                dir = new File(getFilesDir() + "/iPlusImage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dir == null) {
            dir = new File(Environment.getDataDirectory() + "/iPlusImage");
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }
        mediaFile = new File(dir, tempFileName);
        return Uri.fromFile(mediaFile);
    }

}
