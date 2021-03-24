package com.kmcompany.toolgetcookieface.custom;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressLoadingDialog {
    private ProgressDialog progressDialog;

    public ProgressLoadingDialog(Activity activity) {
        progressDialog = new ProgressDialog(activity);
    }

    public void showDialog() {
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dissDialog() {
        progressDialog.dismiss();
    }
}
