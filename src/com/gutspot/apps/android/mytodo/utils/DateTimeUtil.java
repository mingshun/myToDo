package com.gutspot.apps.android.mytodo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public static long millisecondOf(int year, int month, int day, int hour, int minute) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd mm:ss", Locale.getDefault());
            Date date = format.parse("" + year + "-" + (month + 1) + "-" + day + " " + hour + ":" + minute);
            return date.getTime();
        } catch (ParseException e) {
            android.util.Log.e("DateTimeUtil#millisecondOf", e.getStackTrace().toString());
            throw new RuntimeException(e);
        }
    }

    public static long millisecondOf(int year, int month, int day) {
        return millisecondOf(year, month, day, 0, 0);
    }

}
