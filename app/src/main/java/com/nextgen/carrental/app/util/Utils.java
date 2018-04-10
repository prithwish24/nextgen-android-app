package com.nextgen.carrental.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Single utility class for all common operations
 * @author Prithwish
 */

public class Utils {

    public static final String SHORT_TIME_AMPM = "h:mm a";


    public static String fmtTime (long value, String fmt){
        final Date dt = new Date(value);
        final SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(dt);
    }

}
