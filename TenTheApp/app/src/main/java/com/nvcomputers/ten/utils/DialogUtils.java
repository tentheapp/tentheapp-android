package com.nvcomputers.ten.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;


public class DialogUtils {
    /**
     *
     * @param context
     * @return
     */
    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       //todo progressDialog.setMessage(context.getString(R.string.please_wait));
        return progressDialog;
    }

    /**
     *
     * @param context
     * @param titleId - @StringRes
     * @param messageId -  @StringRes
     * @param view
     * @param positiveClickListener
     * @param negativeClickListener
     * @return
     */
    public static Dialog createDialog(Context context,
                                      int titleId, int messageId, View view,
                                      DialogInterface.OnClickListener positiveClickListener,
                                      DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setView(view);
      // todo  builder.setPositiveButton(R.string.dlg_ok, positiveClickListener);
      //todo  builder.setNegativeButton(R.string.dlg_cancel, negativeClickListener);

        return builder.create();
    }
}