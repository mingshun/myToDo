package com.gutspot.apps.android.mytodo.dialog;

import java.util.Calendar;
import java.util.TimeZone;

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
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.gutspot.apps.android.mytodo.R;

public class DateTimeDialog extends DialogFragment implements OnDateChangedListener, OnValueChangeListener {
    private static final long MILLISECOND_OF_A_DAY = 24 * 60 * 60 * 1000;

    private Context context;

    private DatePicker datePicker;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;

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

        datePicker = (DatePicker) layout.findViewById(R.id.picker_date);
        hourPicker = (NumberPicker) layout.findViewById(R.id.picker_hour);
        minutePicker = (NumberPicker) layout.findViewById(R.id.picker_minute);

        initPickers();

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

    private void initPickers() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        long now = calendar.getTimeInMillis();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        datePicker.init(year, month, day, this);
        datePicker.setMinDate(now / MILLISECOND_OF_A_DAY * MILLISECOND_OF_A_DAY);

        hourPicker.setMinValue(hour);
        hourPicker.setMaxValue(23);
        hourPicker.setValue(hour);
        hourPicker.setWrapSelectorWheel(false);
        hourPicker.setOnValueChangedListener(this);

        minutePicker.setMinValue(minute);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(minute);
        minutePicker.setWrapSelectorWheel(false);
        minutePicker.setOnValueChangedListener(this);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeZone(TimeZone.getDefault());
        long nowTime = nowCalendar.getTimeInMillis();
        long nowMillisecondOfDay = nowTime / MILLISECOND_OF_A_DAY * MILLISECOND_OF_A_DAY;

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeZone(TimeZone.getDefault());
        currentCalendar.set(year, monthOfYear, dayOfMonth);
        long currentTime = currentCalendar.getTimeInMillis();
        long currentMillisecondOfDay = currentTime / MILLISECOND_OF_A_DAY * MILLISECOND_OF_A_DAY;

        android.util.Log.d("onDateChanged", "" + nowMillisecondOfDay + ", " + currentMillisecondOfDay);

        if (currentMillisecondOfDay > nowMillisecondOfDay) {
            hourPicker.setMinValue(0);
            hourPicker.setWrapSelectorWheel(true);

            minutePicker.setMinValue(0);
            minutePicker.setWrapSelectorWheel(true);
        } else {
            hourPicker.setMinValue(nowCalendar.get(Calendar.HOUR_OF_DAY));
            hourPicker.setWrapSelectorWheel(false);

            minutePicker.setMinValue(nowCalendar.get(Calendar.MINUTE));
            minutePicker.setWrapSelectorWheel(false);
        }

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker != hourPicker) {
            return;
        }

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeZone(TimeZone.getDefault());
        int hour = nowCalendar.get(Calendar.HOUR_OF_DAY);
        if (newVal > hour) {
            minutePicker.setMinValue(0);
            minutePicker.setWrapSelectorWheel(true);
        } else {
            minutePicker.setMinValue(nowCalendar.get(Calendar.MINUTE));
            minutePicker.setWrapSelectorWheel(false);
        }
    }
}
