package com.techmali.smartteam.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.techmali.smartteam.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    /**
     * to check internet connection is available or not
     *
     * @param context activity context
     * @return internet is available or not
     */
    public static boolean isInternetAvailable(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // test for connection
            if (cm.getActiveNetworkInfo() != null
                    && cm.getActiveNetworkInfo().isAvailable()
                    && cm.getActiveNetworkInfo().isConnected()) {
                return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static PackageInfo getSoftwareVersion(Context mContext) {
        PackageInfo pi;
        try {
            pi = mContext.getPackageManager().getPackageInfo(mContext.getApplicationContext().getPackageName(), 0);
            return pi;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Is empty string boolean.
     * String validation method
     *
     * @param object the object
     * @return the boolean
     */
    public static boolean isEmptyString(String object) {
        return !(object != null && !object.isEmpty() && !object.equalsIgnoreCase("null") && object.trim().length() > 0 && !object.equalsIgnoreCase("(null)"));
    }

    /**
     * Px to dp int.
     *
     * @param mContext the Context
     * @param px       the px that needs to be converted in dp
     * @return the int
     */
    public static int pxToDp(Context mContext, int px) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /**
     * to check email is valid or not
     *
     * @param email email address
     * @return given email is in correct format or not
     */
    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * to check phone number is valid or not
     *
     * @param phone phone number
     * @return phone number is valid or not
     */
    public static boolean isValidPhoneNo(String phone) {
        return (!Utils.isEmptyString(phone) && phone.matches("[+0-9 ()-]+"));
    }

    /**
     * It will return unique filename
     *
     * @param fileExtension extension of the file
     * @return unique filename with extension
     */
    public static String getUniqueFileName(String fileExtension) {
        try {
            if (!Utils.isEmptyString(fileExtension)) {
                StringBuilder sb = new StringBuilder();
                sb.append(getRandomString(8));
                sb.append(new Date().getTime());
                sb.append(fileExtension);
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * It will return random string of given length
     *
     * @param length length of random string
     * @return string
     */
    public static String getRandomString(int length) {
        try {
            String str = "0123456789abcdefghijklmnopqrstuvwxyz";
            SecureRandom rnd = new SecureRandom();
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(str.charAt(rnd.nextInt(str.length())));
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // to hide keyboard

    /**
     * It will hide keyboard
     *
     * @param context activity context
     * @param view    edit view in which we have focus
     */
    public static void hideKeyboard(Context context, View view) {
        try {
            if (context != null && view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides the already popped up keyboard from the screen.
     *
     * @param context activity context
     */
    public static void hideKeyboard(Context context) {
        try {
            if (((Activity) context).getCurrentFocus() != null && ((Activity) context).getCurrentFocus().getWindowToken() != null) {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            Log.e(TAG, "Sigh, cant even hide keyboard " + e.getMessage());
        }
    }


    /**
     * It will return screen width of mobile
     *
     * @param context activity context
     * @return screen width
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * @return Point value from that
     * we can get screen height as "point.X" and width as "point.y"
     */
    public static Point getScreenSize(Activity act) {
        int screenWidth;
        int screenHeight;

        final DisplayMetrics metrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        return new Point(screenWidth, screenHeight);
    }


    public static void callDialog(final Context mContext, String message, final String no) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

//        dialog.setMessage(String.format(mContext.getString(R.string.call_message), no));
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton(mContext.getString(R.string.lbl_call), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                phoneCall(mContext, no);
                /*Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + no));
                startActivity(intent);*/

            }
        });
        dialog.setNegativeButton(mContext.getString(R.string.lbl_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialogred = dialog.create();
        dialogred.show();
        dialogred.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorOrange));
        dialogred.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
    }

    /**
     * For making phone call from app
     *
     * @param number you want to call
     */
    public static void phoneCall(Context context, String number) {

        if (!Utils.isEmptyString(number)) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + number));
            context.startActivity(callIntent);
        }
    }

    /**
     * For sending mail from app using mail clients
     *
     * @param mailId       you want to mail to person
     * @param subject      of the mail
     * @param body         of the mail
     * @param chooserTitle mail client selector title
     */
    public static void composeEmail(Context context, String mailId, String subject, String body, String chooserTitle) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.fromParts("mailto", mailId, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }

    /**
     * For sending mail from app using mail clients
     *
     * @param addresses String array of persons you want to mail
     * @param subject   of the mail
     * @param body      of the mail
     */
    public static void composeEmail(Context context, String[] addresses, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(emailIntent);
        }
    }

    /**
     * For sending mail from app using mail clients
     *
     * @param mailId       you want to mail to person
     * @param subject      of the mail
     * @param body         of the mail
     * @param chooserTitle mail client selector title
     */
    public static void composeEmailWithAttachment(Context context, String mailId, String subject, String body, String chooserTitle, Uri URI) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.fromParts("mailto", mailId, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        if (URI != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
        }
        context.startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }

    /**
     * For sending mail from app using mail clients
     *
     * @param addresses String array of persons you want to mail
     * @param subject   of the mail
     * @param body      of the mail
     */
    public static void composeEmailWithAttachment(Context context, String[] addresses, String subject, String body, Uri URI) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        if (URI != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
        }
        if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(emailIntent);
        }
    }


    /**
     * For sending SNS from app using default SMS services
     *
     * @param message of the SMS
     */
    public static void sendSMS(Context context, String message) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // At least KitKat

            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            sendIntent.putExtra("exit_on_sent", true);
            context.startActivity(sendIntent);

        } else {
            // For early versions, do what worked for you before.

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra(Intent.EXTRA_TEXT, message);
            smsIntent.putExtra("exit_on_sent", true);
            context.startActivity(smsIntent);
        }
    }


    /**
     * For sending SNS from app using default SMS services
     *
     * @param message of the SMS
     */
    public static void sendSMSWithAttachment(Context context, String message, Uri URI) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // At least KitKat

            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("image/png");
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            if (URI != null) {
                sendIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            sendIntent.putExtra("exit_on_sent", true);
            context.startActivity(sendIntent);

        } else {
            // For early versions, do what worked for you before.

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra(Intent.EXTRA_TEXT, message);
            smsIntent.putExtra("exit_on_sent", true);
            if (URI != null) {
                smsIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            context.startActivity(smsIntent);
        }
    }

    /**
     * Opening Whats app and set text to be send
     *
     * @param message text to be send
     */

    public static void shareOnWhatsApp(Context context, String message) {

        try {
            if (Utils.isPackageInstalled(context, "com.whatsapp")) {
                Intent sendIntent = new Intent();
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            } else {
//            Utils.showMessageDialog(context, null, context.getString(R.string.whatsapp_not_installed));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open app in Google Play store
     *
     * @param context     activity context
     * @param packageName package name of the app which needs to be open in play store
     */
    public static void openAppInPlayStore(Context context, String packageName) {

        if (!Utils.isEmptyString(packageName)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent);
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
        }
    }

    /**
     * It will check given package name app is installed or not
     *
     * @param context     activity context
     * @param packageName package name of the app
     * @return app is installed or not
     */
    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * to check normal password is valid or not
     *
     * @param pass email address
     * @return given pass is in correct format or not
     */
    public static boolean isNormalPassword(String pass) {

        String PASSWORD_PATTERN = "(.{6,20})";
        Matcher matcher = Pattern.compile(PASSWORD_PATTERN).matcher(pass);
        return matcher.matches();
    }

    /**
     * to check strong pass is valid or not
     *
     * @param pass email address
     * @return given pass is in correct format or not
     */
    public static boolean isStrongPass(String pass) {

        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        Matcher matcher = Pattern.compile(PASSWORD_PATTERN).matcher(pass);
        return matcher.matches();
    }

    /**
     * to get two digit after double value
     *
     * @param value double value
     * @return double value with two digit after decimal
     */

    public static String formatAmount(double value) {
        if (value != 0) {
            return Constants.CURRENCY_SYMBOL + String.format(Locale.getDefault(), "%.2f", value);
        } else {
            return "";
        }
    }

    public static String compressImage(Context context, String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int maxSize = 1280;
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        int scaleFactor = 1;
        int newWidth = actualWidth;
        int newHeight = actualHeight;
        Log.e(TAG, "Actual Height ::" + actualHeight + "  Width :: " + actualWidth);
        if (actualWidth > maxSize || actualHeight > maxSize) {
            if (actualWidth > actualHeight) {
                scaleFactor = actualWidth / maxSize;
                newWidth = 1280;
                newHeight = actualHeight * 1280 / actualWidth;
            } else {
                scaleFactor = actualHeight / maxSize;
                newHeight = 1280;
                newWidth = actualWidth * 1280 / actualHeight;
            }
        }
        actualWidth = newWidth;
        actualHeight = newHeight;
        Log.e(TAG, "After Compression Height ::" + actualHeight + "  Width :: " + actualWidth);

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = scaleFactor;
//        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        int requiredRotation = 0;
        try {
            ExifInterface ei = new ExifInterface(filePath);

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

        Matrix matrix = new Matrix();
        if (requiredRotation != 0) {
            matrix.preRotate(requiredRotation);
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        }

        FileOutputStream out = null;
        String filename = getOutputMediaFileUri(context).getPath();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    /**
     * Gets output media file uri.
     *
     * @param mContext the context
     * @return the output media file uri
     */
    public static Uri getOutputMediaFileUri(Context mContext) {
        File mediaFile;
        String tempFileName = "img_temp_" + System.currentTimeMillis() + ".png";
        File dir = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dir = new File(Environment.getExternalStorageDirectory() + "/ResearchPlus");
            } else {
                dir = new File(mContext.getFilesDir() + "/ResearchPlus");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dir == null) {
            dir = new File(Environment.getDataDirectory() + "/ResearchPlus");
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }
        mediaFile = new File(dir, tempFileName);
        return Uri.fromFile(mediaFile);
    }

    /**
     * Delete temp directory of temp images..
     */
    public static void deleteTempDirectory() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/ResearchPlus");
            if (dir.isDirectory())

            {
                String[] children = dir.list();
                for (String aChildren : children) {
                    boolean b = new File(dir, aChildren).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     *
     * @param listView the list view
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 1;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * Sets grid view height based on children.
     *
     * @param gridView the grid view
     * @param columns  the columns
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        double x = 1;
        if (items > columns) {
            x = ((double) items) / columns;
            rows = (int) Math.ceil(x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    /**
     * The constant BLOCK_SPECIAL_CHARACTER_SET.
     */
    public static String BLOCK_SPECIAL_CHARACTER_SET = "%~#^|$%*!@/()-'\":;,?{}=!&$^';,?×÷<>...{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪:-);-):-(:'(1234567890.";


    /**
     * The constant specialCharFilter. to filter special character in textField
     */
    public static InputFilter specialCharFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && BLOCK_SPECIAL_CHARACTER_SET.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    /**
     * The constant BLOCK_SPECIAL_CHARACTER_SET.
     */
    public static String BLOCK_UNIT_CHARACTER_SET = "%~#^|$%*!@/()'\":;,?{}=!&$^';,?×÷<>...{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪:'";

    /**
     * The constant specialCharFilter. to filter special character in textField
     */
    public static InputFilter unitNumberCharFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && BLOCK_UNIT_CHARACTER_SET.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    /**
     * Focus on view.
     *
     * @param view the view
     */
    public static void focusOnView(View view) {
        view.clearFocus();
        view.requestFocus();
        view.setFocusable(true);
    }


    public static void showMessageDialog(Context context, String message) {

        if (message != null && message.trim().length() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage(message);
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
//
            alertDialog.show();
        }
    }


    public static String generateRandomNo() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        String result = "";
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }
        return result;
    }

    public static String createBlank(String string) {
        if (isEmptyString(string))
            return "";
        else
            return string;
    }

    public static boolean isLocationEnabled(Context context) {
        try {
            return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle((CharSequence) "Location setting");
        alertDialog.setMessage((CharSequence) "Location is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton((CharSequence) "Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.dismiss();
                context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        alertDialog.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    /**
     * Open document in installed one of the applications.
     *
     * @param mContext the m context
     * @param name     the name of the file
     */
    public static void openDocument(Context mContext, String name) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(name);
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            // if there is no extension or there is no definite mimetype, still try to open the file
            intent.setDataAndType(Uri.fromFile(file), "text/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), mimetype);
        }
        // custom message for the intent
        mContext.startActivity(Intent.createChooser(intent, "Choose an Application:"));
    }
}
