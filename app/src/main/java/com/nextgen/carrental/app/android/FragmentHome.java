package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.ReservationListAdapter;
import com.nextgen.carrental.app.adapter.TripsListAdapter;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.bo.TripsResponse;
import com.nextgen.carrental.app.constants.GlobalConstants;
import com.nextgen.carrental.app.model.CarClassEnum;
import com.nextgen.carrental.app.service.RestClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;


/**
 * Home - User Home after login
 * @author Prithwish
 */

public class FragmentHome extends Fragment {
    private static final String TAG = FragmentHome.class.getName();
    private View homeView;

    MainActivity activity = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.homeView = inflater.inflate(R.layout.fragment_home, container, false);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().show();
        }
        activity = (MainActivity)getActivity();
        return homeView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetMyTrips().execute();
        FloatingActionButton fab = homeView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), VoiceChatActivity.class));
            }
        });
    }

    private class GetMyTrips extends AsyncTask<AIRequest, Void, BaseResponse<TripsResponse[]>> {
        @Override
        protected BaseResponse<TripsResponse[]> doInBackground(AIRequest... requests) {
            BaseResponse<TripsResponse[]> response = null;
            try {
                response = RestClient.INSTANCE.getRequest(
                       GlobalConstants.URL_LIST_TRIPS, TripsResponse[].class, activity.sessionId,activity.userId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(BaseResponse<TripsResponse[]> response) {
            TripsResponse[] trips = response.getResponse();

            final ListView listView = homeView.findViewById(R.id.show_trips_list_view);
            TripsListAdapter adapter = new TripsListAdapter(getActivity(), Arrays.asList(trips));
            listView.setAdapter(adapter);
        }
    }

}
