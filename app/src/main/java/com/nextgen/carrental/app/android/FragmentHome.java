package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.bo.TripsResponse;
import com.nextgen.carrental.app.model.CarClassEnum;
import com.nextgen.carrental.app.service.RestClient;

import java.util.List;


/**
 * Home - User Home after login
 * @author Prithwish
 */

public class FragmentHome extends Fragment {
    private static final String TAG = FragmentHome.class.getName();
    private View homeView;

    public static String TRIPS_URL = "http://18.188.102.146:8002/trips/upcoming/{sessionId}?username={username}";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.homeView = inflater.inflate(R.layout.fragment_home, container, false);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().show();
        }
        return homeView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        final ImageView carClassImage = homeView.findViewById(R.id.car_image_home);
//        carClassImage.setImageResource(R.drawable.ct_compact);
//        carClassImage.refreshDrawableState();
        MainActivity activity = (MainActivity)getActivity();
        BaseResponse response = null;
        try{
            response = RestClient.INSTANCE.getRequest(
                    TRIPS_URL, BaseResponse.class, activity.sessionId,activity.userId);
        }catch(Exception e){
        }
        List<TripsResponse> trips = (List<TripsResponse>)response.getResponse();

        populateView(trips);

        FloatingActionButton fab = (FloatingActionButton) homeView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), VoiceChatActivity.class));
            }
        });
    }

    private void populateView(List<TripsResponse> trips) {

        for(TripsResponse t : trips){
            final CarClassEnum carClass = CarClassEnum.find(t.getCarType());
            if (carClass == null) {
                //carClassImage.setImageResource();
                carClassDesc.setText("");
            } else {
                carClassImage.setImageResource(carClass.getImgId());
                carClassDesc.setText(carClass.getDesc());
            }
        }
    }

}
