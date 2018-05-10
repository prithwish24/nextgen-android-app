package com.nextgen.carrental.app.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Single utility class for all common operations
 * @author Prithwish
 */

public class Utils {
    public static final String TAG = Utils.class.getName();
    public static final String SHORT_TIME_AMPM = "h:mm a";
    public static final String LONG_DATE_TIME = "EEE MMM d, yyyy - h:mm a";
    public static final String RESP_DATE_FMT = "yyyy-MM-dd";
    public static final String RESP_TIME_FMT = "hh:mm:ss";


    public static String fmtTime(Date value) {
        return fmtTime(value, SHORT_TIME_AMPM);
    }

    public static String fmtTime(Date value, String fmt) {
        if (value == null) return "";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return sdf.format(value);
        } catch (Exception pe) {
            Log.w(TAG, pe.getMessage(), pe);
        }
        return "";
    }

    public static Date fmtDateTime(String value, String originFmt) {
        final SimpleDateFormat sdf = new SimpleDateFormat(originFmt);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            Log.w(TAG, e.getMessage(), e);
        }
        return null;
    }
}
