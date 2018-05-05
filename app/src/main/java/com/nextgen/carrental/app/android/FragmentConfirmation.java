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

/**
 * View to display confirmation details
 * @author Prithwish
 *
 */

public class FragmentConfirmation extends Fragment implements View.OnClickListener {
    private static final String TAG = FragmentHome.class.getName();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView carClassImage = view.findViewById(R.id.vehicle_image_confirm);
        carClassImage.setImageResource(R.drawable.ct_premium);

        final TextView carClass = view.findViewById(R.id.vehicle_class_confirm);
        carClass.setText("Premium");

        final TextView carClassDesc = view.findViewById(R.id.vehicle_class_desc);
        carClassDesc.setText("Nissan Maxima or similar");

        final View buttonDone = view.findViewById(R.id.button_done_confirm);
        buttonDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int itemId = v.getId();
        if (itemId == R.id.button_done_confirm) {
            //getActivity().onBackPressed();;
            getFragmentManager().beginTransaction()
                    .replace(R.id.vc_content_frame, new FragmentVoiceChat())
                    .commit();
        }

    }
}
