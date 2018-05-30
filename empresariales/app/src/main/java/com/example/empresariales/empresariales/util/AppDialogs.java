package com.example.empresariales.empresariales.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

/**
 * Created by moebius on 5/12/15.
 */
public class AppDialogs {

    public static ProgressDialog createLoading(Context context) {
        return ProgressDialog.show(context, "", "Espere un momento...", true);
    }

    public static Dialog createDialog(Context context, final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }

    public static Dialog createDialog(Context context, final String message, final Callbacks callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callback.onSuccess(null);
            }
        });
        return builder.create();
    }

    public static Dialog createErrorDialog(Context context) {
        return createDialog(context, "There's an issue!, please try it again.");
    }

    public static Dialog createActionDialog(Context context, String title, String message, final Callbacks callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callback.onSuccess(null);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onFail(null);
                    }
                });
        return builder.create();
    }

    public static void createSpinner(Context context, String title, final List<String> types, final Callbacks callback) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle(title);

        String[] types__ = new String[types.size()];
        types__ = types.toArray(types__);

        b.setItems(types__, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callback.onSuccess(String.valueOf(which));
            }

        });
        b.show();
    }
}
