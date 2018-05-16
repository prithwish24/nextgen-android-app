package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

public class FragmentConfirmation extends Fragment {
    public static final String TAG = FragmentHome.class.getName();
    View view;
    private BookingData bookingData = new BookingData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View buttonDone = view.findViewById(R.id.button_done_confirm);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final Fragment fragmentVC = getFragmentManager().findFragmentByTag(FragmentVoiceChat.TAG);
                getFragmentManager().beginTransaction()
                        .replace(R.id.vc_content_frame, fragmentVC)
                        //.addToBackStack(null)
                        .commit();*/
                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class), savedInstanceState);
            }
        });

        if (TextUtils.equals(bookingData.step, "review")) {
            String tmpStr = null;

            view.findViewById(R.id.button_done_confirm).setVisibility(View.INVISIBLE);

            final ImageView carClassImage = view.findViewById(R.id.vehicle_image_confirm);
            final TextView carClassDesc = view.findViewById(R.id.confirm_vehicle_class_desc);

            final CarClassEnum carClass = CarClassEnum.find(bookingData.carType);
            if (carClass == null) {
                //carClassImage.setImageResource();
                carClassDesc.setText("");
            } else {
                carClassImage.setImageResource(carClass.getImgId());
                carClassDesc.setText(carClass.getDesc());
            }

            ((TextView) view.findViewById(R.id.confirm_number)).setText(R.string.review_screen_user_message);
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

        }
    }

    public void updateConfirmationNumber(@NonNull final String confNumber) {
        if (!TextUtils.isEmpty(confNumber)) {
            bookingData.confNum = confNumber;
            ((TextView) view.findViewById(R.id.confirm_number)).setText("CONFIRMATION # " + bookingData.confNum);
            ((TextView) view.findViewById(R.id.confirm_number)).setTextColor(Color.RED);

            view.findViewById(R.id.button_done_confirm).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.aiButtonContainer).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.aiButtonContainer_topBorder).setVisibility(View.INVISIBLE);
        }
    }

    public void bindConfirmationData(@NonNull final BookingData bookingData) {
        this.bookingData = bookingData;
    }

}
