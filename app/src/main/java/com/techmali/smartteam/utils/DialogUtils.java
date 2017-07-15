package com.techmali.smartteam.utils;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.techmali.smartteam.R;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

public class DialogUtils {

    /**
     * @param context    activity context
     * @param title      dialog title, if null or blank then it will not show
     * @param message    dialog message
     * @param buttonText label of the button of dialog
     * @return object of alert dialog
     */
    public static AlertDialog showDialog(Context context, String title, String message, String buttonText) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!Utils.isEmptyString(title)) {
                builder.setTitle(title);
            }
            builder.setMessage(message);
            builder.setCancelable(true);

            builder.setPositiveButton(buttonText, null);
            return builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context    activity context
     * @param title      dialog title, if null or blank then it will not show
     * @param message    dialog message
     * @param buttonText label of the button of dialog
     * @param listener   button click listener
     * @return object of alert dialog
     */
    public static AlertDialog showDialog(Context context, String title, String message, String buttonText, DialogInterface.OnClickListener listener) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!Utils.isEmptyString(title)) {
                builder.setTitle(title);
            }
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(buttonText, listener);
            return builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context    activity context
     * @param title      dialog title, if null or blank then it will not show
     * @param message    dialog message
     * @param cancelable dialog is cancellable or not
     * @param buttonText label of the button of dialog
     * @param listener   button click listener
     * @return object of alert dialog
     */
    public static AlertDialog showDialog(Context context, String title, String message, boolean cancelable, String buttonText, DialogInterface.OnClickListener listener) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!Utils.isEmptyString(title)) {
                builder.setTitle(title);
            }
            builder.setMessage(message);
            builder.setCancelable(cancelable);
            builder.setPositiveButton(buttonText, listener);
            return builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context                activity context
     * @param title                  dialog title, if null or blank then it will not show
     * @param message                dialog message
     * @param positiveButtonText     label of the positive button of dialog
     * @param negativeButtonText     label of the negative button of dialog
     * @param positiveButtonListener positive button click listener
     * @param negativeButtonListener negative button click listener
     * @return object of alert dialog
     */
    public static AlertDialog showDialog(Context context, String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!Utils.isEmptyString(title)) {
                builder.setTitle(title);
            }
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(positiveButtonText, positiveButtonListener);
            builder.setNegativeButton(negativeButtonText, negativeButtonListener);
            return builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context                activity context
     * @param title                  dialog title, if null or blank then it will not show
     * @param message                dialog message
     * @param cancelable             dialog is cancellable or not
     * @param positiveButtonText     label of the positive button of dialog
     * @param negativeButtonText     label of the negative button of dialog
     * @param positiveButtonListener positive button click listener
     * @param negativeButtonListener negative button click listener
     * @return object of alert dialog
     */

    public static AlertDialog showDialog(Context context, String title, String message, boolean cancelable, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!Utils.isEmptyString(title)) {
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

    /**
     * Display error in alert dialog. If message contains exception of network error then it will display internet not available message.
     *
     * @param message Error message or exception
     */
    public static void displayError(Context context, String message) {

        if (!Utils.isEmptyString(message)) {

            if (message.contains(SocketTimeoutException.class.getName()) || message.contains(UnknownHostException.class.getName()) || message.contains(NetworkErrorException.class.getName()) || message.contains(SSLException.class.getName())
                    || message.contains(SSLPeerUnverifiedException.class.getName()) || message.contains(JSONException.class.getName()) || message.contains(org.apache.http.conn.ConnectTimeoutException.class.getName()) || message.contains(ConnectException.class.getName())) {
                message = context.getString(R.string.alert_no_internet);
            }
            DialogUtils.showDialog(context, null, message, context.getString(R.string.lbl_ok));
        }
    }

    public static void showConfirmationDialog(Context context, String title,
                                              String message, boolean cancelable,
                                              String buttonYesText, String buttonNoText, int isNoButtonVisible,
                                              final View.OnClickListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(cancelable);
        dialog.setContentView(R.layout.custom_cancel_dailog);

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage1);

        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        ivClose.setVisibility(View.GONE);
        btnNo.setVisibility(isNoButtonVisible);

        tvTitle.setText(title);
        tvMessage.setText(message);
        btnNo.setText(buttonNoText);
        btnYes.setText(buttonYesText);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.onClick(view);

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.onClick(view);
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.onClick(view);
            }
        });
        dialog.show();
    }





    public interface OnListItemClickListener {
        void onItemClick(int position, View view, String text);

        void onDoneClick(ArrayList<String> list, View view);
    }


}
