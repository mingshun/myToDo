package com.gutspot.apps.android.mytodo.dialog;

import com.gutspot.apps.android.mytodo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DateTimeDialog extends DialogFragment {
    private Context context;

    public static DateTimeDialog newInstance(Context context) {
        DateTimeDialog dialog = new DateTimeDialog();
        dialog.context = context;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = (Activity) context;
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_date_time,
                (ViewGroup) activity.findViewById(R.id.dialog_date_time));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("设置提醒时间");
        builder.setView(layout);

        String yesLabel = "确认";
        DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        };
        String noLabel = "取消";
        DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        };

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder.setPositiveButton(yesLabel, yesListener);
            builder.setNegativeButton(noLabel, noListener);
        } else {
            builder.setNegativeButton(yesLabel, yesListener);
            builder.setPositiveButton(noLabel, noListener);
        }

        return builder.create();
    }
}
