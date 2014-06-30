package com.gutspot.apps.android.mytodo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

public final class AlertUtil {
    public static void show(Context context, String message, String yesLabel,
            DialogInterface.OnClickListener yesListener, String noLabel, DialogInterface.OnClickListener noListener) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("提示");
        dialog.setMessage(message);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, yesLabel, yesListener);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, noLabel, noListener);
        } else {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, noLabel, noListener);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, yesLabel, yesListener);
        }

        dialog.show();
    }
}
