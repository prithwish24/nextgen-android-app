package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.ReservationListAdapter;
import com.nextgen.carrental.app.model.Reservation;

import java.util.ArrayList;


/**
 * Show list of reservations for this user
 * @author Prithwish
 */

public class FragmentShowReservation extends Fragment {
    View showReservationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.showReservationView = inflater.inflate(R.layout.fragment_show_reservation, container, false);
        return showReservationView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Reservation> list = new ArrayList<Reservation>(3) {
            {
                add(new Reservation("5154353", "UPCOMING", "Compact", "St. Louis Int Airport", "St. Louis Int Airport", "Apr 24, 2018 - 9:00 PM", "Apr 26, 2018 - 9:00 AM"));
                add(new Reservation("8765456", "COMPLETED","Standard", "Denver Int Airport", "Denver Int Airport", "Apr 18, 2018 - 8:00 PM", "Apr 22, 2018 - 8:00 AM"));
                add(new Reservation("2456787", "COMPLETED","Premium", "Chicago Int Airport", "Chicago Int Airport", "Feb 5, 2018 - 12:00 PM", "Feb 14, 2018 - 12:00 PM"));
            }
        };

        final ListView listView = showReservationView.findViewById(R.id.show_reservation_list_view);
        ReservationListAdapter adapter = new ReservationListAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }
}
