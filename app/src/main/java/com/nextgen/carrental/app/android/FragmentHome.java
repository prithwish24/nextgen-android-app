package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.TripsListAdapter;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.constants.GlobalConstants;
import com.nextgen.carrental.app.model.BookingData;
import com.nextgen.carrental.app.service.RestClient;
import com.nextgen.carrental.app.service.RestException;
import com.nextgen.carrental.app.service.RestParameter;
import com.nextgen.carrental.app.util.SessionManager;
import com.nextgen.carrental.app.util.Utils;

import org.springframework.core.ParameterizedTypeReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Home - User Home after login
 * @author Prithwish
 */

public class FragmentHome extends Fragment {
    private static final String TAG = FragmentHome.class.getName();
    private View homeView;

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
        final ListView listView = homeView.findViewById(R.id.show_trips_list_view);
        TripsListAdapter adapter = new TripsListAdapter(getActivity(), new ArrayList<BookingData>());
        listView.setAdapter(adapter);
        FloatingActionButton fab = homeView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), VoiceChatActivity.class));
            }
        });
        new GetMyTrips(getActivity().getApplicationContext(),view,adapter).execute();
    }

    private class GetMyTrips extends AsyncTask<Void, Void, List<BookingData>> {
        private WeakReference<Context> contextRef;
        private WeakReference<View> viewRef;
        private TripsListAdapter adapter;

        GetMyTrips(Context context, View view, TripsListAdapter adapter) {
            this.contextRef = new WeakReference<>(context);
            this.viewRef = new WeakReference<>(view);
            this.adapter = adapter;
        }
        @Override
        protected List<BookingData> doInBackground(Void... requests) {
            final SessionManager sessionManager = new SessionManager(contextRef.get());
            final String sessionID = sessionManager.getLoggedInSessionID();
            final String username  = sessionManager.getLoggedInUserID();

            try {
                final String serviceURL = Utils.getServiceURL(contextRef.get(),
                        GlobalConstants.Services.UPCOMING_RESERVATION);

                RestParameter data = new RestParameter();
                data.addQueryParam("sessionId", sessionID);
                data.addQueryParam("username", username);

                ParameterizedTypeReference<BaseResponse<List<BookingData>>> typeRef
                        = new ParameterizedTypeReference<BaseResponse<List<BookingData>>>() {};

                final BaseResponse<List<BookingData>> response = RestClient.INSTANCE.GET(serviceURL, data, typeRef);

                if (response != null) {
                    if (response.isSuccess())
                        return response.getResponse();

                    Log.e(TAG, response.getError().toString());
                }

            } catch (RestException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<BookingData> bookingDataList) {
            if (bookingDataList == null || bookingDataList.size() == 0) {
                final View viewMessage = viewRef.get().findViewById(R.id.no_trip_list_view);
                viewMessage.setVisibility(View.VISIBLE);

                final View viewList = viewRef.get().findViewById(R.id.show_trips_list_view);
                viewList.setVisibility(View.GONE);

            } else {
                adapter.setItemList(bookingDataList);
                adapter.notifyDataSetChanged();
            }
        }
    }

}
