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

import org.springframework.core.ParameterizedTypeReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
            data.addQueryParam("username", "jsmith");

            ParameterizedTypeReference<BaseResponse<List<BookingData>>> typeRef
                    = new ParameterizedTypeReference<BaseResponse<List<BookingData>>> (){};

            try {
                final BaseResponse<List<BookingData>> response = RestClient.INSTANCE.GET(serviceURL, data, typeRef);

                if (response != null) {
                    if (response.isSuccess()) {
                        final List<BookingData> list = response.getResponse();
                        return list;

                        // FIXME TEMP code till service change
                        /*List<LinkedHashMap<String, Object>> resList = response.getResponse();
                        ArrayList<BookingData> bookingDataList = new ArrayList<>(resList.size());

                        for (LinkedHashMap<String, Object> lhMap  : resList) {
                            BookingData bookingData = new BookingData();
                            bookingData.confNum = (String) lhMap.get("confNum");
                            bookingData.carType = (String) lhMap.get("carType");
                            bookingData.pickupLoc = (String) lhMap.get("pickupLoc");
                            bookingData.returnLoc = (String) lhMap.get("returnLoc");
                            bookingData.pickupDateTime = (Date) lhMap.get("pickupDateTime");
                            bookingData.returnDateTime = (Date) lhMap.get("returnDateTime");
                            bookingDataList.add(bookingData);
                        }
                        return bookingDataList;*/

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
