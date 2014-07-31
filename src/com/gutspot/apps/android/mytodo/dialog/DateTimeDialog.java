package com.gutspot.apps.android.mytodo.dialog;

import java.util.Calendar;
import java.util.Date;
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
import com.gutspot.apps.android.mytodo.dao.NoticeDAO;
import com.gutspot.apps.android.mytodo.model.Notice;
import com.gutspot.apps.android.mytodo.utils.DateTimeUtil;

public class DateTimeDialog extends DialogFragment implements OnDateChangedListener, OnValueChangeListener {
    private Context context;

    private long toDoId;

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
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                int hour = hourPicker.getValue();
                int minute = minutePicker.getValue();

                Notice notice = new Notice();
                notice.setToDoId(toDoId);
                notice.setTime(new Date(DateTimeUtil.millisecondOf(year, month, day, hour, minute)));
                NoticeDAO noticeDAO = new NoticeDAO(context);
                noticeDAO.create(notice);
            }
        };
        String noLabel = "取消";
        DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getDefault());
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        datePicker.init(year, month, day, this);
        datePicker.setMinDate(DateTimeUtil.millisecondOf(year, month, day));

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

    public void setToDoId(long toDoId) {
        this.toDoId = toDoId;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getDefault());
        long nowMillisecond = DateTimeUtil.millisecondOf(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        long selectMillisecond = DateTimeUtil.millisecondOf(year, monthOfYear, dayOfMonth);

        android.util.Log.d("onDateChanged", "" + nowMillisecond + ", " + selectMillisecond);

        if (selectMillisecond > nowMillisecond) {
            hourPicker.setMinValue(0);
            minutePicker.setMinValue(0);
        } else {
            hourPicker.setMinValue(now.get(Calendar.HOUR_OF_DAY));
            hourPicker.setWrapSelectorWheel(false);
            minutePicker.setMinValue(now.get(Calendar.MINUTE));
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
