package com.techmali.smartteam.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.techmali.smartteam.R;


public class DialogHelper {

    public static void showMessage(Context context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showTwoButtonDialog(Context context, String title, String message,
                                           String possitiveText, String negetiveText,
                                           DialogInterface.OnClickListener possitiveListener,
                                           DialogInterface.OnClickListener negetiveListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(possitiveText, possitiveListener);
        builder.setNegativeButton(negetiveText, negetiveListener);
        builder.show();
    }

    public static void showOkButtonDialog(Context context, String title, String message,
                                          String okText,
                                          DialogInterface.OnClickListener possitiveListener,
                                          boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(okText, possitiveListener);
        builder.show();
    }

    public static android.app.AlertDialog showDialog(Context context, String title, String message, boolean cancelable, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {

        try {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.setMessage(message);
            builder.setCancelable(cancelable);
            builder.setPositiveButton(positiveButtonText, positiveButtonListener);
            builder.setNegativeButton(negativeButtonText, negativeButtonListener);
            return builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface DialogListener {
        void onCancel();

        void onOk();
    }
}
