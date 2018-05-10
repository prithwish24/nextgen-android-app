package com.nextgen.carrental.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nextgen.carrental.app.model.BookingData;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Transform AI data JSON into model
 *
 * @author Prithwish
 */

public class AIResponseTransformer {
    public static final String TAG = AIResponseTransformer.class.getName();

    public BookingData transform(final Map<String, JsonElement> parameters) {
        final BookingData bookingData = new BookingData();
        //bookingData.confNum     = parameters.get("confnumber").getAsString();
        bookingData.step = parameters.get("step").getAsString();
        bookingData.pickupLoc = parameters.get("pickuplocation").getAsString();
        bookingData.returnLoc = parameters.get("pickuplocation").getAsString();
        bookingData.carType = parameters.get("cartype").getAsString();
        bookingData.pickupDateTime = parseDate(parameters.get("pickupdate").getAsString(), parameters.get("pickuptime").getAsString());

        bookingData.returnDateTime = calculateReturnDateTime(bookingData.pickupDateTime, parameters.get("duration"));

        return bookingData;
    }

    private Date parseDate(String dateStr, String timeStr) {
        try {
            final Date date = Utils.fmtDateTime(dateStr, Utils.RESP_DATE_FMT);
            final Date time = Utils.fmtDateTime(timeStr, Utils.RESP_TIME_FMT);

            Calendar calendarA = Calendar.getInstance();
            calendarA.setTime(date);
            Calendar calendarB = Calendar.getInstance();
            calendarB.setTime(time);

            calendarA.set(Calendar.HOUR_OF_DAY, calendarB.get(Calendar.HOUR_OF_DAY));
            calendarA.set(Calendar.MINUTE, calendarB.get(Calendar.MINUTE));
            calendarA.set(Calendar.SECOND, calendarB.get(Calendar.SECOND));

            return calendarA.getTime();

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return null;
    }

    private Date calculateReturnDateTime(Date date, JsonElement jsonElement) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if (jsonElement.isJsonObject()) {
                final JsonObject durationObj = jsonElement.getAsJsonObject();
                int value = durationObj.get("amount").getAsInt();
                String unit = durationObj.get("unit").getAsString();

                if (TextUtils.equals(unit, "day")) {
                    calendar.add(Calendar.DAY_OF_MONTH, value);

                } else if (TextUtils.equals(unit, "week")) {
                    calendar.add(Calendar.WEEK_OF_MONTH, value);

                } else if (TextUtils.equals(unit, "month")) {
                    calendar.add(Calendar.MONTH, value);

                } else {
                    // Did not understand the duration so setting default 2 days
                    calendar.add(Calendar.DAY_OF_MONTH, 2);
                }
            }

            return calendar.getTime();

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return null;
    }

}
