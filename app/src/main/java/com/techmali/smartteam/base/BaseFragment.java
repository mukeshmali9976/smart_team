package com.techmali.smartteam.base;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


import com.techmali.smartteam.R;
import com.techmali.smartteam.ui.views.MyProgressDialog;
import com.techmali.smartteam.utils.TypefaceUtils;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;


public class BaseFragment extends Fragment {

    private MyProgressDialog progressDialog = null;
    private AlertDialog alertDialog = null;
    private TextView mActionBarTitle = null;
    private static ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // display progress dialog
    protected void showProgressDialog() {
        try {
            if (getActivity() != null) {
                dismissProgressDialog();
                progressDialog = new MyProgressDialog(getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // dismiss progress dialog if it is visible
    protected void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // dismiss progress dialog on UI thread if it is visible
    protected void dismissProgressDialogOnUIThread() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void displayError(String message) {

        if (message != null) {

            if (message.contains(SocketTimeoutException.class.getName()) || message.contains(UnknownHostException.class.getName()) || message.contains(NetworkErrorException.class.getName()) || message.contains(SSLException.class.getName())
                    || message.contains(SSLPeerUnverifiedException.class.getName()) || message.contains(JSONException.class.getName()) || message.contains(org.apache.http.conn.ConnectTimeoutException.class.getName())) {
                message = getString(R.string.lbl_no_internet);
            }

            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.lbl_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            alertDialog = builder.show();
        }
    }

    public static void changeToolBarColor(){
        actionBar.setBackgroundDrawable(null);
    }

    public void initActionBar(String title, View view) {

        try {
            // initialize toolbar
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            // initialize actionbar
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_menu);
                mActionBarTitle = new TextView(getActivity());
                ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0, 0, 15, 0);
                params.gravity = Gravity.CENTER;
                mActionBarTitle.setGravity(Gravity.CENTER);
                mActionBarTitle.setMaxEms(15);
                mActionBarTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
//        mActionBarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_actionbar_title));
                TypefaceUtils.getInstance(getActivity()).applyTypeface(mActionBarTitle, TypefaceUtils.SEMI_BOLD);
                actionBar.setCustomView(mActionBarTitle, params);
//                mActionBarTitle.setTypeface(Typeface.DEFAULT_BOLD);
                mActionBarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_18));

                if (!TextUtils.isEmpty(title)) {
                    setTitle(title);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
    public void setTitle(int titleId) {

        if (mActionBarTitle != null) {
            mActionBarTitle.setText(titleId);
        }
//        super.setTitle(titleId);
    }

//    @Override
    public void setTitle(CharSequence title) {

        if (mActionBarTitle != null) {
            mActionBarTitle.setText(title);
        }
//        super.setTitle(title);
    }
}
