package com.techmali.smartteam.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.techmali.smartteam.R;


public class MyProgressDialog extends Dialog {

    /**
     * Instantiates a new My progress dialog.
     *
     * @param context the context
     */
    public MyProgressDialog(Context context) {

        super(context, R.style.Theme_CustomDialog);
        init();
    }

    // initializeFragmentManager progress dialog
    private void init() {

        try {
            if (super.isShowing()) {
                super.dismiss();
            }
            super.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View mProgressView = getLayoutInflater().inflate(R.layout.dialog_progressbar, null, false);
            // super.addContentView(new ProgressBar(getContext()), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            super.addContentView(mProgressView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            super.setCancelable(false);
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // dismiss progress dialog
    public synchronized void dismiss() {

        try {
            if (this.isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
