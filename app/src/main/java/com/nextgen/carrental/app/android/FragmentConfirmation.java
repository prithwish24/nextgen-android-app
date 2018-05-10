package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.model.BookingData;
import com.nextgen.carrental.app.util.Utils;

/**
 * View to display confirmation details
 * @author Prithwish
 *
 */

public class FragmentConfirmation extends Fragment {
    public static final String TAG = FragmentHome.class.getName();
    View view;
    private BookingData bookingData = new BookingData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView carClassImage = view.findViewById(R.id.vehicle_image_confirm);
        carClassImage.setImageResource(R.drawable.ct_premium);

//        final TextView carClass = view.findViewById(R.id.confirm_vehicle_class);
//        carClass.setText("Premium");

        final TextView carClassDesc = view.findViewById(R.id.confirm_vehicle_class_desc);
        carClassDesc.setText("Nissan Maxima or similar");

        final View buttonDone = view.findViewById(R.id.button_done_confirm);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vc_content_frame, new FragmentVoiceChat())
                        .commit();
            }
        });

        /*Reservation res = ((VoiceChatActivity)getActivity()).getReservation();
        bindConfirmationData(res);
        if(!StringUtils.isEmpty(res.getNumber())){
            view.findViewById(R.id.confirm_number).setVisibility(View.VISIBLE);
            ((TextView)(view.findViewById(R.id.confirm_number))).setText("CONFIRMATION #: "+res.getNumber());
        }*/

        if (TextUtils.equals(bookingData.step, "review")) {
            String tmpStr = null;

            view.findViewById(R.id.button_done_confirm).setVisibility(View.INVISIBLE);

            ((TextView) view.findViewById(R.id.confirm_number)).setText("Please review your booking information..");
            ((TextView) view.findViewById(R.id.confirm_vehicle_class)).setText(bookingData.carType);
            ((TextView) view.findViewById(R.id.confirm_pickup_location)).setText(bookingData.pickupLoc);
            ((TextView) view.findViewById(R.id.confirm_return_location)).setText(bookingData.returnLoc);

            tmpStr = Utils.fmtTime(bookingData.pickupDateTime, Utils.LONG_DATE_TIME);
            ((TextView) view.findViewById(R.id.confirm_pickup_time)).setText(tmpStr);

            tmpStr = Utils.fmtTime(bookingData.returnDateTime, Utils.LONG_DATE_TIME);
            ((TextView) view.findViewById(R.id.confirm_return_time)).setText(tmpStr);

            for (String str : bookingData.additionalEquip) {
                ((TextView) view.findViewById(R.id.confirm_additional_equip)).setText(str + "\n");
            }

        } else if (TextUtils.equals(bookingData.step, "confirmed")) {
            view.findViewById(R.id.button_done_confirm).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.aiButtonContainer).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.aiButtonContainer_topBorder).setVisibility(View.INVISIBLE);

            ((TextView) view.findViewById(R.id.confirm_number)).setText("CONFIRMATION # " + bookingData.confNum);
        }
    }

    public void bindConfirmationData(final BookingData bookingData) {
        this.bookingData = bookingData;
    }

    /*public void populateBookingData(final Reservation res) {
        bookingData.carType = res.getCarType();
        bookingData.pickupLoc = res.getPickUpPoint();
        bookingData.pickupDateTime = Utils.fmtTime(new Date().getTime(), Utils.LONG_DATE_TIME);
        bookingData.returnLoc = res.getDropOffPoint();
        bookingData.returnDateTime = Utils.fmtTime(new Date().getTime(), Utils.LONG_DATE_TIME);
    }*/

    /*private void bindConfirmationData(final Reservation res) {
        ((TextView) view.findViewById(R.id.confirm_pickup_location)).setText(res.getPickUpPoint());
        ((TextView) view.findViewById(R.id.confirm_pickup_time)).setText(res.getPickUpTime());
        ((TextView) view.findViewById(R.id.confirm_return_location)).setText(res.getDropOffPoint());
        ((TextView) view.findViewById(R.id.confirm_return_time)).setText(res.getDropOffTime());
        ((TextView) view.findViewById(R.id.confirm_vehicle_class)).setText(res.getCarType());
    }*/


}
