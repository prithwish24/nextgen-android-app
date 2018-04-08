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

public class ShowReservationFragment extends Fragment {
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
                add(new Reservation("5154353", "Midsize", "St. Louis Int Airport", "St. Louis Int Airport", "9:00 am", "9:00 am"));
                add(new Reservation("8765456", "Standard", "Denver Int Airport", "Denver Int Airport", "8:00 am", "8:00 am"));
                add(new Reservation("2456787", "Premium", "Chicago Int Airport", "Chicago Int Airport", "12:00 noon", "12:00 noon"));
            }
        };

        final ListView listView = showReservationView.findViewById(R.id.show_reservation_list_view);
        ReservationListAdapter adapter = new ReservationListAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }
}
