package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.ReservationListAdapter;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.constants.GlobalConstants;
import com.nextgen.carrental.app.model.BookingData;
import com.nextgen.carrental.app.model.User;
import com.nextgen.carrental.app.service.RestClient;
import com.nextgen.carrental.app.service.RestException;
import com.nextgen.carrental.app.service.RestParameter;
import com.nextgen.carrental.app.util.SessionManager;
import com.nextgen.carrental.app.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Show list of reservations for this user
 * @author Prithwish
 */

public class FragmentShowReservation extends Fragment {
    public static final String TAG = FragmentShowReservation.class.getName();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_show_reservation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*ArrayList<Reservation> list = new ArrayList<Reservation>(3) {
            {
                add(new Reservation("5154353", "UPCOMING", "Compact", "St. Louis Int Airport", "St. Louis Int Airport", "Apr 24, 2018 - 9:00 PM", "Apr 26, 2018 - 9:00 AM"));
                add(new Reservation("8765456", "COMPLETED","Standard", "Denver Int Airport", "Denver Int Airport", "Apr 18, 2018 - 8:00 PM", "Apr 22, 2018 - 8:00 AM"));
                add(new Reservation("2456787", "COMPLETED","Premium", "Chicago Int Airport", "Chicago Int Airport", "Feb 5, 2018 - 12:00 PM", "Feb 14, 2018 - 12:00 PM"));
            }
        };*/

        final ListView listView = this.view.findViewById(R.id.show_reservation_list_view);
        final ReservationListAdapter listAdapter = new ReservationListAdapter(
                getActivity().getApplicationContext(), new ArrayList<BookingData>());
        listView.setAdapter(listAdapter);

        final User user = new SessionManager(getActivity().getApplicationContext()).getLoggedInUser();
        new ShowAllReservationTask(getActivity().getApplicationContext(), view, listAdapter).execute(user);
    }


    private static class ShowAllReservationTask extends AsyncTask<User, Void, List<BookingData>> {
        private WeakReference<Context> contextRef;
        private WeakReference<View> viewRef;
        private ReservationListAdapter adapter;

        ShowAllReservationTask(Context context, View view, ReservationListAdapter adapter) {
            this.contextRef = new WeakReference<>(context);
            this.viewRef = new WeakReference<>(view);
            this.adapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<BookingData> doInBackground(User... params) {
            String serviceURL = Utils.getServiceURL(contextRef.get(),
                    GlobalConstants.Services.ALL_RESERVATION);

            RestParameter data = new RestParameter();
            //data.addQueryParam("username", params[0].getUserId());
            data.addQueryParam("username", "jsmith");   // FIXME TEMP CODE

            try {
                final BaseResponse<List> response = RestClient.INSTANCE.getRequest(serviceURL, List.class, data);
                if (response != null) {
                    if (response.isSuccess()) {
                        //return response.getResponse();

                        // FIXME TEMP code till service change
                        List<LinkedHashMap<String, String>> resList = response.getResponse();
                        LinkedHashMap<String, String> lhMap = resList.get(0);

                        ArrayList<BookingData> bookingDataList = new ArrayList<>(resList.size());
                        BookingData bookingData = new BookingData();
                        bookingData.confNum = lhMap.get("id");
                        bookingData.carType = lhMap.get("carType");
                        bookingData.pickupLoc = lhMap.get("pickupPoint");
                        bookingData.returnLoc = lhMap.get("dropPoint");
                        bookingData.pickupDateTime = Utils.fmtDateTime(lhMap.get("pickupDateTime"), Utils.LONG_DATE_TIME);
                        bookingData.returnDateTime = Utils.fmtDateTime(lhMap.get("dropoffDateTime"), Utils.LONG_DATE_TIME);
                        bookingDataList.add(bookingData);

                        return bookingDataList;

                    } else {
                        Log.e(TAG, response.getError().toString());
                    }
                }
            } catch (RestException re) {
                Log.e(TAG, re.getMessage(), re);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<BookingData> bookingDataList) {
            super.onPostExecute(bookingDataList);
            if (bookingDataList == null || bookingDataList.size() == 0) {

                final View viewMessage = viewRef.get().findViewById(R.id.no_reservation_message);
                viewMessage.setVisibility(View.VISIBLE);

                final View viewList = viewRef.get().findViewById(R.id.show_reservation_list_view);
                viewList.setVisibility(View.GONE);

            } else {
                adapter.setItemList(bookingDataList);
                adapter.notifyDataSetChanged();
            }
        }
    }

}
