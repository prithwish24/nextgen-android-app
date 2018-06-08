package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
 * View to display confirmation details
 * @author Prithwish
 *
 */

public class FragmentReview extends Fragment {
    public static final String TAG = FragmentReview.class.getName();
    private View view;
    private BookingData bookingData = new BookingData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_review, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*final View buttonDone = view.findViewById(R.id.button_re);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*final Fragment fragmentVC = getFragmentManager().findFragmentByTag(FragmentVoiceChat.TAG);
                getFragmentManager().beginTransaction()
                        .replace(R.id.vc_content_frame, fragmentVC)
                        //.addToBackStack(null)
                        .commit();*//*
                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class), savedInstanceState);
            }
        });*/

        if (bookingData != null) {
            String tmpStr;

            //view.findViewById(R.id.button_review_confirm).setVisibility(View.INVISIBLE);

            final ImageView carClassImage = view.findViewById(R.id.review_vehicle_image);
            final TextView carClassDesc = view.findViewById(R.id.review_vehicle_class_desc);

            final CarClassEnum carClass = CarClassEnum.find(bookingData.carType);
            if (carClass == null) {
                carClassDesc.setText("");
                ((TextView) view.findViewById(R.id.review_vehicle_class)).setText(bookingData.carType);

            } else {
                carClassImage.setImageResource(carClass.getImgId());
                carClassDesc.setText(carClass.getDesc());
                ((TextView) view.findViewById(R.id.review_vehicle_class)).setText(carClass.name());
                //tmpStr = carClass.name() + " - " + carClass.getDesc();
                //((TextView) view.findViewById(R.id.review_price_vehicle_text)).setText(tmpStr);
            }

            ((TextView) view.findViewById(R.id.review_message)).setText(R.string.review_screen_user_message);

            ((TextView) view.findViewById(R.id.review_pickup_location)).setText(bookingData.pickupLoc);
            ((TextView) view.findViewById(R.id.review_return_location)).setText(bookingData.returnLoc);

            tmpStr = Utils.fmtTime(bookingData.pickupDateTime, Utils.LONG_DATE_TIME);
            ((TextView) view.findViewById(R.id.review_pickup_time)).setText(tmpStr);

            tmpStr = Utils.fmtTime(bookingData.returnDateTime, Utils.LONG_DATE_TIME);
            ((TextView) view.findViewById(R.id.review_return_time)).setText(tmpStr);

            for (String str : bookingData.additionalEquip) {
                ((TextView) view.findViewById(R.id.review_additional_equip)).setText(str + "\n");
            }

        }
    }

    /*public void updateConfirmationNumber(@NonNull final String confNumber) {
        if (!TextUtils.isEmpty(confNumber)) {
            bookingData.confNum = confNumber;
            final TextView confirmNumberView = view.findViewById(R.id.review_message);
            confirmNumberView.setText("CONFIRMATION # " + bookingData.confNum);
            confirmNumberView.setTextColor(Color.RED);

            //view.findViewById(R.id.button_review_confirm).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.aiButtonContainer).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.aiButtonContainer_topBorder).setVisibility(View.INVISIBLE);
        }
    }*/

    public void bindConfirmationData(@NonNull final BookingData bookingData) {
        this.bookingData = bookingData;
    }

}
