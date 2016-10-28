package com.wfc.app.test3.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by wangfengchen on 2016/10/28.
 */

public class DialogUtils {

    public static ProgressDialog createProgressDialog(@NonNull Context context, @NonNull String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static void showProgressDialog(@NonNull ProgressDialog pd, String msg) {
        if(msg!=null) {
            pd.setMessage(msg);
        }
        pd.show();
    }

    public static void dismissProgressDialog(@NonNull ProgressDialog pd) {
        pd.dismiss();
    }

}
