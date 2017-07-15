package com.techmali.smartteam.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class ImageUtils {

    private static String TAG = ImageUtils.class.getSimpleName();

    /**
     * It will return bitmap from file with given max size
     *
     * @param file    file object of image
     * @param maxSize resulting bitmap will have this max size (height or width)
     * @return bitmap object with max size
     */
    public static Bitmap getBitmapFromFile(File file, int maxSize) {

        Bitmap bitmap = null;
        int imageHeight;
        int imageWidth;
        FileInputStream fis = null;

        try {

            if (file != null && file.exists()) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                fis = new FileInputStream(file);
                BitmapFactory.decodeStream(fis, null, options);
                imageHeight = options.outHeight;
                imageWidth = options.outWidth;

                int scaleFactor = 1;
                int newWidth = imageWidth;
                int newHeight = imageHeight;

                if (imageHeight > maxSize || imageHeight > maxSize) {

                    if (imageWidth > imageHeight) {
                        scaleFactor = imageHeight / maxSize;
                        newWidth = maxSize;
                        newHeight = imageHeight * maxSize / imageHeight;
                    } else {
                        scaleFactor = imageHeight / maxSize;
                        newWidth = imageHeight * maxSize / imageHeight;
                        newHeight = maxSize;
                    }
                }

                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;

                fis = new FileInputStream(file);

                bitmap = BitmapFactory.decodeStream(fis, null, options);
                if (bitmap != null) {
                    if (imageWidth != newWidth || imageHeight != newHeight) {
                        try {
                            float scaleWidth = ((float) newWidth) / bitmap.getWidth();
                            float scaleHeight = ((float) newHeight) / bitmap.getHeight();
                            Matrix matrix = new Matrix();
                            matrix.postScale(scaleWidth, scaleHeight);
                            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                            bitmap.recycle();
                            return resizedBitmap;
                        } catch (OutOfMemoryError | Exception e) {
                            e.printStackTrace();
                        }
                    }

                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                    if (exif != null) {
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int rotationInDegrees = exifToDegrees(exifOrientation);
                        if (rotationInDegrees != 0) {
                            bitmap = rotateBitmap(bitmap, rotationInDegrees);
                        }
                    }
                }
            }
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * It will give image rotation degree from exif parameter
     *
     * @param exifOrientation exif orientation value
     * @return degree to rotate the image
     */
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /**
     * It will rotate bitmap to given degree
     *
     * @param bitmap  bitmap to rotate
     * @param degrees degree to apply rotation in bitmap
     * @return bitmap with rotation
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * It will save image on sdcard with given name
     *
     * @param context            activity context
     * @param bitmap             bitmap which needs to be save in sdcard
     * @param fileName           name of the file
     * @param compressPercentage the % of compression you want to apply while saving image
     * @return path of saved bitmap
     */
    public static String saveBitmapToExternalStorage(final Context context, final Bitmap bitmap, final String fileName, int compressPercentage) {
        try {

            File dir = null;

            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    dir = new File(Environment.getExternalStorageDirectory() + "/" + Constants.TEMP_DIRECTORY_NAME + "/images");
                } else {
                    dir = new File(context.getFilesDir() + "/" + Constants.TEMP_DIRECTORY_NAME + "/images");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dir == null) {
                dir = new File(Environment.getDataDirectory() + "/" + Constants.TEMP_DIRECTORY_NAME + "/images");
            }

            if (!dir.exists()) {
                dir.mkdirs();
            }

            // File location to save image
            File mediaFile = new File(dir, fileName);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mediaFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, compressPercentage, fos);
                fos.flush();

                //Media scanner need to scan for the image saved
//                Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri fileContentUri = Uri.fromFile(mediaFile);
//                mediaScannerIntent.setData(fileContentUri);
//                context.sendBroadcast(mediaScannerIntent);

                return mediaFile.getAbsolutePath();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception while trying to save file to internal storage: " + mediaFile + " " + e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception while trying to flush the output stream" + e);
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception wile trying to close file output stream." + e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * It will delete file from given path
     *
     * @param filePath path of file which needs to be delete
     */
    public void deleteFile(String filePath) {

        try {

            if (!Utils.isEmptyString(filePath)) {
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * It will remove all files from given directory path
     *
     * @param directoryPath path of directory from which all images will be deleted
     */
    public static void deleteAllFiles(String directoryPath) {

        try {

            File dir = new File(directoryPath);

            if (dir.exists()) {
                for (File child : dir.listFiles()) {
                    child.delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders
     *
     * @param context The activity context
     * @param uri     The Uri to query
     * @return path of image file
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(final Context context, final Uri uri) {

        try {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                if (isExternalStorageDocument(uri)) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {
                        Pattern DIR_SEPORATOR = Pattern.compile("/");
                        Set<String> rv = new HashSet<>();
                        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                        if (Utils.isEmptyString(rawEmulatedStorageTarget)) {
                            if (Utils.isEmptyString(rawExternalStorage)) {
                                rv.add("/storage/sdcard0");
                            } else {
                                rv.add(rawExternalStorage);
                            }
                        } else {
                            String rawUserId;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                rawUserId = "";
                            } else {
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                String[] folders = DIR_SEPORATOR.split(path);
                                String lastFolder = folders[folders.length - 1];
                                boolean isDigit = false;
                                try {
                                    Integer.valueOf(lastFolder);
                                    isDigit = true;
                                } catch (NumberFormatException ignored) {
                                }
                                rawUserId = isDigit ? lastFolder : "";
                            }
                            if (Utils.isEmptyString(rawUserId)) {
                                rv.add(rawEmulatedStorageTarget);
                            } else {
                                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                            }
                        }
                        if (!Utils.isEmptyString(rawSecondaryStoragesStr)) {
                            String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                            Collections.addAll(rv, rawSecondaryStorages);
                        }
                        String[] temp = rv.toArray(new String[rv.size()]);
                        for (int i = 0; i < temp.length; i++) {
                            File tempf = new File(temp[i] + "/" + split[1]);
                            if (tempf.exists()) {
                                return temp[i] + "/" + split[1];
                            }
                        }
                    }
                } else if (isDownloadsDocument(uri)) {

                    String id = DocumentsContract.getDocumentId(uri);
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);

                } else if (isMediaDocument(uri)) {

                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection, selectionArgs);

                } else {

                    return saveImageFromUri(context, uri, 100);
                }

            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                } else if (isGoogleDriveUri(uri)) {
                    String name = getGDriveDataColumn(context, uri, null, null);
                    if (name != null) {
                        int dot = name.lastIndexOf(".");
                        String ext = name.substring(dot + 1).toLowerCase();
                        if (ext.toLowerCase().equalsIgnoreCase("jpeg") || ext.toLowerCase().equalsIgnoreCase("jpg") || ext.toLowerCase().equalsIgnoreCase("png")) {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
                                String fileName = "Img_" + (new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH)).format(new Date()) + ".png";
                                return saveBitmapToExternalStorage(context, bitmap, fileName, 100);//
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return getDataColumn(context, uri, null, null);
                //return saveImageFromUri(context, uri, 100);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            } else {
                return saveImageFromUri(context, uri, 100);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get bitmap from Uri and save it to sdcard
     *
     * @param context            activity context
     * @param uri                uri of the image file
     * @param compressPercentage the % of compression you want to apply while saving image
     * @return path of saved image
     */

    private static String saveImageFromUri(Context context, Uri uri, int compressPercentage) {

        int imageHeight;
        int imageWidth;

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            InputStream fis = context.getContentResolver().openInputStream(uri);
            try {
                BitmapFactory.decodeStream(fis, null, o);
                imageHeight = o.outHeight;
                imageWidth = o.outWidth;
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }

            int scaleFactor = 1;
            int newWidth = imageWidth;
            int newHeight = imageHeight;

            if (imageWidth > 1400 || imageHeight > 1400) {

                if (imageWidth > imageHeight) {
                    scaleFactor = imageWidth / 1400;
                    newWidth = 1280;
                    newHeight = imageHeight * 1280 / imageWidth;
                } else {
                    scaleFactor = imageHeight / 1400;
                    newHeight = 1280;
                    newWidth = imageWidth * 1280 / imageHeight;
                }
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scaleFactor;
            fis = context.getContentResolver().openInputStream(uri);

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(fis, null, o2);

                if (imageWidth != newWidth || imageHeight != newHeight) {
                    try {
                        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
                        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleHeight);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                    } catch (OutOfMemoryError | Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    ExifInterface exif = new ExifInterface(uri.getPath());
                    if (exif != null) {
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int rotationInDegrees = exifToDegrees(exifOrientation);
                        if (rotationInDegrees != 0) {
                            bitmap = rotateBitmap(bitmap, rotationInDegrees);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(new Date());
                String fileName = "Img_" + timeStamp + ".png";
                return saveBitmapToExternalStorage(context, bitmap, fileName, compressPercentage);

            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * It will remove meta data from image file
     *
     * @param filePath path of image file
     */

    public static void removeImageMetaData(String filePath) {

        try {

            if (!Utils.isEmptyString(filePath)) {

                ExifInterface exif = new ExifInterface(filePath);

                String[] exifTag = new String[]{ExifInterface.TAG_APERTURE, ExifInterface.TAG_DATETIME, ExifInterface.TAG_FLASH, ExifInterface.TAG_FOCAL_LENGTH, ExifInterface.TAG_EXPOSURE_TIME, ExifInterface.TAG_GPS_ALTITUDE, ExifInterface.TAG_GPS_ALTITUDE_REF, ExifInterface.TAG_GPS_LATITUDE, ExifInterface.TAG_GPS_LATITUDE_REF, ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.TAG_GPS_LONGITUDE_REF, ExifInterface.TAG_GPS_DATESTAMP, ExifInterface.TAG_GPS_TIMESTAMP, ExifInterface.TAG_GPS_PROCESSING_METHOD, ExifInterface.TAG_ISO, ExifInterface.TAG_MAKE, ExifInterface.TAG_MODEL};

                for (String anExifTag : exifTag) {
                    try {
                        exif.setAttribute(anExifTag, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, "0/0,0/0,0/0");
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "0/0,0/0,0/0");
                    exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, "0");
                    exif.setAttribute(ExifInterface.TAG_ISO, "0");
                    exif.setAttribute(ExifInterface.TAG_FLASH, "0");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    exif.saveAttributes();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        radius = 100;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getGDriveDataColumn(Context context, Uri uri, String selection,
                                             String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_display_name";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;

    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Docs Photo.
     */
    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }
}