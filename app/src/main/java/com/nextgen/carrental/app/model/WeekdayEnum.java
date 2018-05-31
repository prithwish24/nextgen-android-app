package com.nextgen.carrental.app.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by prith on 5/28/2018.
 */

public enum WeekdayEnum {
    Sunday ("Sun"),
    Monday ("Mon"),
    Tuesday ("Tue"),
    Wednesday ("Wed"),
    Thursday ("Thu"),
    Friday ("Fri"),
    Saturday ("Sat");

    private String day;
    WeekdayEnum(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public static WeekdayEnum find(@NonNull String threeLtrDay) {
        for (WeekdayEnum w: values()) {
            if (TextUtils.equals(w.getDay().toLowerCase(), threeLtrDay.toLowerCase()))
                return w;
        }
        return null;
    }
}
