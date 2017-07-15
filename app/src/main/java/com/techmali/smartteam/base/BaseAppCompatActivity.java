package com.techmali.smartteam.base;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;


import com.techmali.smartteam.R;
import com.techmali.smartteam.ui.views.MyProgressDialog;
import com.techmali.smartteam.utils.TypefaceUtils;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;


public class BaseAppCompatActivity extends AppCompatActivity {

    public static final String TAG = BaseAppCompatActivity.class.getSimpleName();
    private MyProgressDialog progressDialog = null;
    private TextView mActionBarTitle;
    private AlertDialog alertDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initActionBar(String title) {

        try {
            // initialize toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // initialize actionbar
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_back_white_normal);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                mActionBarTitle = new TextView(this);
                ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0, 0, 15, 0);
                params.gravity = Gravity.CENTER;
                mActionBarTitle.setGravity(Gravity.CENTER);
                mActionBarTitle.setMaxEms(15);
                mActionBarTitle.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mActionBarTitle.setSingleLine(true);
//        mActionBarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_actionbar_title));
                TypefaceUtils.getInstance(this).applyTypeface(mActionBarTitle, TypefaceUtils.SEMI_BOLD);
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

    @Override
    public void setTitle(int titleId) {

        if (mActionBarTitle != null) {
            mActionBarTitle.setText(titleId);
        }
        super.setTitle(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {

        if (mActionBarTitle != null) {
            mActionBarTitle.setText(title);
        }
        super.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle click of back button in actionbar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = new MyProgressDialog(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void displayError(String message) {

        if (message != null) {

            if (message.contains(SocketTimeoutException.class.getName()) || message.contains(UnknownHostException.class.getName()) || message.contains(NetworkErrorException.class.getName()) || message.contains(SSLException.class.getName())
                    || message.contains(SSLPeerUnverifiedException.class.getName()) || message.contains(JSONException.class.getName()) || message.contains(org.apache.http.conn.ConnectTimeoutException.class.getName())) {
                message = getString(R.string.lbl_no);
            }

            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
}
