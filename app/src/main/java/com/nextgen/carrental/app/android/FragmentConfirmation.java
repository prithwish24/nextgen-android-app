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
import com.nextgen.carrental.app.model.Reservation;

/**
 * View to display confirmation details
 * @author Prithwish
 *
 */

public class FragmentConfirmation extends Fragment {
    private static final String TAG = FragmentHome.class.getName();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.confirm_number).setVisibility(View.INVISIBLE);

        final ImageView carClassImage = view.findViewById(R.id.vehicle_image_confirm);
        carClassImage.setImageResource(R.drawable.ct_premium);

        final TextView carClass = view.findViewById(R.id.confirm_vehicle_class);
        carClass.setText("Premium");

        final TextView carClassDesc = view.findViewById(R.id.vehicle_class_desc);
        carClassDesc.setText("Nissan Maxima or similar");

        final View buttonDone = view.findViewById(R.id.button_done_confirm);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.vc_content_frame, new FragmentVoiceChat())
                        .commit();
            }
        });
    }

    public void bindConfirmationData(final Reservation res) {
        ((TextView) view.findViewById(R.id.confirm_pickup_location)).setText(res.getPickUpPoint());
        ((TextView) view.findViewById(R.id.confirm_pickup_time)).setText(res.getPickUpTime());
        ((TextView) view.findViewById(R.id.confirm_return_location)).setText(res.getDropOffPoint());
        ((TextView) view.findViewById(R.id.confirm_return_time)).setText(res.getDropOffTime());
        ((TextView) view.findViewById(R.id.confirm_vehicle_class)).setText(res.getCarType());
    }

}
