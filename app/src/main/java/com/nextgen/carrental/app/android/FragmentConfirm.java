package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.model.BookingData;
import com.nextgen.carrental.app.model.CarClassEnum;
import com.nextgen.carrental.app.util.Utils;

/**
 * Confirmation Page
 * @author Prithwish
 *
 */

public class FragmentConfirm extends Fragment {
    public static final String TAG = FragmentConfirm.class.getName();
    private View view;
    private BookingData bookingData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (bookingData != null) {
            ((TextView)view.findViewById(R.id.tv_confNumber)).setText(bookingData.confNum);
            ((TextView)view.findViewById(R.id.tv_pickup_point)).setText(bookingData.pickupLoc);
            ((TextView)view.findViewById(R.id.tv_pickup_date_time)).setText(
                    Utils.fmtTime(bookingData.pickupDateTime, Utils.LONG_DATE_TIME));

            final CarClassEnum carClass = CarClassEnum.find(bookingData.carType);
            if (carClass == null) {
                final String tmp = bookingData.carType + '-' + carClass.getDesc();
                ((TextView) view.findViewById(R.id.tv_car_type)).setText(tmp);

            } else {
                ((ImageView) view.findViewById(R.id.iv_carType_thumbnail)).setImageResource(carClass.getImgId());
                ((TextView) view.findViewById(R.id.tv_car_type)).setText(carClass.name());
            }

            getActivity().findViewById(R.id.aiButtonContainer).setVisibility(View.GONE);
            getActivity().findViewById(R.id.aiButtonContainer_topBorder).setVisibility(View.GONE);

        }

    }

    public void bindConfirmation(final BookingData bookingData) {
        this.bookingData = bookingData;
    }
}
