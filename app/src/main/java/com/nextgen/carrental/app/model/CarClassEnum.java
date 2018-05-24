package com.nextgen.carrental.app.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.nextgen.carrental.app.R;

/**
 * Static car class data
 *
 * @author Prithwish
 */

public enum CarClassEnum {
    Economy("Mitsubishi Mirage or similar", R.raw.economy),
    Compact("Nissan Versa or similar", R.raw.compact),
    Intermediate("Hyundai Elantra or similar", R.raw.intermediate),
    Standard("Kia Soul or similar", R.raw.standard),
    FullSize("Toyota Camry or similar", R.raw.fullsize),
    Premium("Nissan Maxima or similar", R.raw.premium),
    Luxury("Cadillac XTS or similar", R.raw.luxury);

    String desc;
    int imgId;

    CarClassEnum(String desc, int imgId) {
        this.desc = desc;
        this.imgId = imgId;
    }

    public static CarClassEnum find(@NonNull final String type) {
        for (CarClassEnum cc : CarClassEnum.values()) {
            if (TextUtils.equals(cc.name().toLowerCase(), type)) {
                return cc;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public int getImgId() {
        return imgId;
    }
}
